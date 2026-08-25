/**
 * Copyright (c) 2024 Jürg Hersche (Green@rt)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package info.hersche.document.metadata.image;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.imaging.ImagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drew.imaging.ImageProcessingException;
import com.drew.imaging.mp4.Mp4MetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.DirectoryNames;
import com.drew.metadata.Metadata;
import com.drew.metadata.SectionNames;
import com.drew.metadata.mp4.Mp4Directory;

import info.hersche.document.metadata.factory.AbstractDocumentFactory;
import info.hersche.document.metadata.factory.AbstractMetadataFactory;
import info.hersche.document.metadata.factory.MetadataDirectoryFactory;
import info.hersche.document.metadata.metadata.GeoLocation;
import info.hersche.document.metadata.metadata.MetadataDirectory;
import info.hersche.document.metadata.model.Detail;
import info.hersche.document.metadata.model.OutputImageDefaultMetadataDetails;
import info.hersche.document.metadata.model.OutputImageMetadataMessage;
import info.hersche.document.metadata.model.OutputImageMetadataMessage.OutputImageMetadataMessageBuilder;

/**
 * 
 */
public class Mp4Movie implements Picture
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(Mp4Movie.class);
	private static final int TYPE = BufferedImage.TYPE_3BYTE_BGR;
	private static final float QUALITY = 1f;
	private static final Base64 CODEC = new Base64();

	/**
	 * @return
	 */
	public static Map<Integer, String> getTagNameMap()
	{
		HashMap<Integer, String> map = new HashMap<>();

		return map;
	}

	/**
	 * Uploaded picture as buffered image
	 */
	private BufferedImage bufferedImage;

	/**
	 * Uploaded picture as input stream
	 */
	private InputStream inputStream;

	/**
	 * Picture output format
	 */
	private String outputFormat;

	/**
	 * Map contains all found dates, exclusive 'Profile Date/Time' ('0x0018').
	 */
	private Map<String, LocalDateTime> dates;

	/**
	 * Contains location data
	 */
	private GeoLocation geoLocation;

	/**
	 * List of available tags
	 */
	private Set<String> availableTags = new HashSet<>();

	/**
	 * @param outputFormat
	 * @param bufferedImage
	 * @param inputStream
	 */
	public Mp4Movie(String outputFormat, BufferedImage bufferedImage, InputStream inputStream)
	{
		this.outputFormat = outputFormat;
		this.bufferedImage = bufferedImage;
		this.inputStream = inputStream;
		this.dates = new TreeMap<>();
		this.geoLocation = GeoLocation.toBuilder().build();
	}

	/**
	 *
	 */
	@Override
	public String generateThumbnail(boolean doGenerateThumbnail, Dimension sourceDimension, Dimension targetDimension) throws IOException
	{
		if (!doGenerateThumbnail)
		{
			LOGGER.debug("Do not generate thumbnail.");

			return "";
		}

		LOGGER.debug("Build thumbnail of image '{}' with format '{}'.", this.bufferedImage, this.outputFormat);

		final RenderPolicy policy = RenderPolicy.toBuilder().sourceDimension(sourceDimension).targetDimension(targetDimension).build();
		policy.calculateRatioAndScaleBySourceAndTarget();

		final BufferedImage croppedImage = Converter.cropImage(this.bufferedImage, sourceDimension, targetDimension);

		/**
		 * Set-up converter
		 */
		String format = (!this.outputFormat.equalsIgnoreCase(AbstractDocumentFactory.Extension.MP4.getExtension())) ? this.outputFormat
				: AbstractDocumentFactory.Extension.JPG.getExtension();
		final Converter converter = Converter.toBuilder() //
				.image(croppedImage) //
				.format(format) //
				.quality(QUALITY) //
				.scale(policy.getScaleFactor()) //
				.type(TYPE) //
				.build();

		/**
		 * Generate thumbnail and convert it to a Base64 encoded string, which can be sent to the STOMP client.
		 */
		final BufferedImage thumbnail = converter.generateThumbnail();
		try (final ByteArrayOutputStream baos = new ByteArrayOutputStream())
		{
			ImageIO.write(thumbnail, format, baos);

			return CODEC.encodeToString(baos.toByteArray());
		}
	}

	/**
	 * Convert image to Base64 encoded string
	 * See'https://www.baeldung.com/convert-input-stream-to-string'
	 */
	@Override
	public String generateImage(boolean doGenerateImage, Dimension sourceDimension, Dimension targetDimension)
	{
		if (!doGenerateImage)
		{
			LOGGER.debug("Do not generate image.");

			return "";
		}

		LOGGER.debug("Build '{}' with format '{}'.", this.bufferedImage, this.outputFormat);
		String text = new BufferedReader(new InputStreamReader(this.inputStream, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
		
		return CODEC.encodeToString(text.getBytes());
	}

	/**
	 *
	 */
	@Override
	public LocalDateTime getOriginalDate()
	{
		// Get original date found in metadata
		Optional<String> key = this.dates.keySet().stream().findFirst();
		if (key.isPresent())
		{
			LocalDateTime date = this.dates.get(key.get());
			LOGGER.trace("Current original date is '{}'.", date);
			
			return date;
		}

		return LocalDateTime.now();
	}

	/**
	 *
	 */
	@Override
	public OutputImageMetadataMessage readMetadata() throws ImagingException, IOException, ImageProcessingException
	{
		final OutputImageMetadataMessageBuilder metadataBuilder = OutputImageMetadataMessage.toBuilder();

		if (this.inputStream != null)
		{
			final Metadata metadata = Mp4MetadataReader.readMetadata(this.inputStream);

			int counter = 0;
			metadataBuilder.general(this.readMetadataDirectories(Mp4Directory.class, SectionNames.GENERAL, metadata, counter++));
		}

		metadataBuilder.geoLocation(this.geoLocation);

		return metadataBuilder.build();
	}

	/**
	 * @param clazz
	 * @param sectionName
	 * @param metadata
	 * @return
	 */
	private <T extends Directory> OutputImageDefaultMetadataDetails readMetadataDirectories(Class<T> clazz, SectionNames sectionName, Metadata metadata,
			int counter)
	{
		final String section = new StringBuilder(sectionName.getLabel()).append(" (").append(counter).append(")").toString();
		final Map<String, Collection<Detail>> details = new TreeMap<>();
		final Map<String, LocalDateTime> dates = new TreeMap<>();

		for (Directory directory : metadata.getDirectories())
		{
			String directoryName = directory.getName();
			DirectoryNames directoryType = DirectoryNames.getByName(directoryName);
			MetadataDirectoryFactory factory = AbstractMetadataFactory.getMetadataFactory(directoryType, this.availableTags);
			MetadataDirectory metadataDirectory = factory.getMetadataDirectory();
			Collection<Detail> metaDataDetails = metadataDirectory.getDetails(directory);
			details.put(directory.getName(), metaDataDetails);

			// Collect all types of date
			dates.putAll(metadataDirectory.getDates());
		}

		this.dates.putAll(dates);

		return OutputImageDefaultMetadataDetails.toBuilder().label(sectionName.getLabel()).section(section).details(details).build();
	}

}
