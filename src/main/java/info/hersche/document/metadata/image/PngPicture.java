/**
 * Copyright (c) 2024 - 2026 Jürg Hersche (Green@rt)
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
import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.ImagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.imaging.png.PngChunkType;
import com.drew.metadata.Directory;
import com.drew.metadata.DirectoryNames;
import com.drew.metadata.Metadata;
import com.drew.metadata.SectionNames;
import com.drew.metadata.exif.ExifDirectoryBase;
import com.drew.metadata.exif.GpsDirectory;
import com.drew.metadata.exif.makernotes.NikonPictureControl1Directory;
import com.drew.metadata.exif.makernotes.NikonPictureControl2Directory;
import com.drew.metadata.exif.makernotes.NikonType1MakernoteDirectory;
import com.drew.metadata.exif.makernotes.NikonType2MakernoteDirectory;
import com.drew.metadata.file.FileSystemDirectory;
import com.drew.metadata.heif.HeifDirectory;
import com.drew.metadata.icc.IccDirectory;
import com.drew.metadata.ico.IcoDirectory;
import com.drew.metadata.iptc.IptcDirectory;
import com.drew.metadata.png.PngChromaticitiesDirectory;
import com.drew.metadata.png.PngDirectory;
import com.hillert.image.metadata.service.support.MetadataExtractor;

import info.hersche.document.metadata.factory.AbstractMetadataFactory;
import info.hersche.document.metadata.factory.MetadataDirectoryFactory;
import info.hersche.document.metadata.metadata.GeoLocation;
import info.hersche.document.metadata.metadata.MetadataDirectory;
import info.hersche.document.metadata.model.Detail;
import info.hersche.document.metadata.model.OutputImageDefaultMetadataDetails;
import info.hersche.document.metadata.model.OutputImageDefaultMetadataMessage;
import info.hersche.document.metadata.model.OutputImageDefaultMetadataMessage.OutputImageDefaultMetadataMessageBuilder;
import info.hersche.document.metadata.model.OutputImageMetadataMessage;
import info.hersche.document.metadata.model.OutputImageMetadataMessage.OutputImageMetadataMessageBuilder;
import info.hersche.document.metadata.model.SourceImageFormat;

/**
 * @author Jürg Hersche (herscju)
 */
public class PngPicture implements Picture
{
	/**
	 * 
	 */
	private final static Logger LOGGER = LoggerFactory.getLogger(PngPicture.class);
	private static final int TYPE = BufferedImage.TYPE_4BYTE_ABGR;
	private static final float QUALITY = 1f;
	private static final Base64 CODEC = new Base64();

	/**
	 * @return
	 */
	public static Map<Integer, String> getTagNameMap()
	{
		HashMap<Integer, String> map = new HashMap<>();

		// Add base tag names
		ExifDirectoryBase.addExifTagNames(map);

		// Add specific tag names
		FileSystemDirectory file = new FileSystemDirectory();
		map.putAll(file.getTagNameMap());
		GpsDirectory gps = new GpsDirectory();
		map.putAll(gps.getTagNameMap());
		IccDirectory icc = new IccDirectory();
		map.putAll(icc.getTagNameMap());
		IcoDirectory ico = new IcoDirectory();
		map.putAll(ico.getTagNameMap());
		IptcDirectory iptc = new IptcDirectory();
		map.putAll(iptc.getTagNameMap());
		NikonPictureControl1Directory npc1 = new NikonPictureControl1Directory();
		map.putAll(npc1.getTagNameMap());
		NikonPictureControl2Directory npc2 = new NikonPictureControl2Directory();
		map.putAll(npc2.getTagNameMap());
		NikonType1MakernoteDirectory ntm1 = new NikonType1MakernoteDirectory();
		map.putAll(ntm1.getTagNameMap());
		NikonType2MakernoteDirectory ntm2 = new NikonType2MakernoteDirectory();
		map.putAll(ntm2.getTagNameMap());
		PngDirectory pngDirectory = new PngDirectory(PngChunkType.sRGB);
		map.putAll(pngDirectory.getTagNameMap());
		PngChromaticitiesDirectory pngChromaticitiesDirectory = new PngChromaticitiesDirectory();
		map.putAll(pngChromaticitiesDirectory.getTagNameMap());

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
	 * Default constructor
	 */
	public PngPicture()
	{
		// Nothing to do here
	}

	/**
	 * Constructor
	 * 
	 * @param outputFormat
	 * @param bufferedImage
	 * @param inputStream
	 */
	public PngPicture(String outputFormat, BufferedImage bufferedImage, InputStream inputStream)
	{
		this.outputFormat = outputFormat;
		this.bufferedImage = bufferedImage;
		this.inputStream = inputStream;
		this.dates = new TreeMap<>();
		this.geoLocation = GeoLocation.toBuilder().build();
	}

	/**
	 * Generate thumbnail of image and return it as Base64 encoded string
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
		final Converter converter = Converter.toBuilder() //
				.image(croppedImage) //
				.format(this.outputFormat) //
				.quality(QUALITY) //
				.scale(policy.getScaleFactor()) //
				.type(TYPE) //
				.build();

		/**
		 * Generate thumbnail and convert it to a Base64 encoded string, which can be sent to the STOMP client.
		 */
		final BufferedImage thumbnail = converter.generateThumbnail();
		try (final ByteArrayOutputStream baos = new ByteArrayOutputStream();)
		{
			ImageIO.write(thumbnail, this.outputFormat, baos);

			return CODEC.encodeToString(baos.toByteArray());
		}
	}

	/**
	 * Convert image to Base64 encoded string
	 */
	@Override
	public String generateImage(boolean doGenerateImage, Dimension sourceDimension, Dimension targetDimension) throws IOException
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
	 * Get the original date
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
	 * Read full set of image metadata
	 * 
	 * @throws ImagingException
	 * @throws IOException
	 * @throws ImageProcessingException
	 */
	@Override
	public OutputImageMetadataMessage readMetadata() throws ImagingException, IOException, ImageProcessingException
	{
		final OutputImageMetadataMessageBuilder metadataBuilder = OutputImageMetadataMessage.toBuilder();

		/**
		 * https://mkyong.com/java/how-to-convert-bufferedimage-to-byte-in-java/
		 */
		if (this.bufferedImage != null)
		{
			try (ByteArrayOutputStream baos = new ByteArrayOutputStream();)
			{
				ImageIO.write(this.bufferedImage, this.outputFormat, baos);
				byte[] bytes = baos.toByteArray();

				final ImageInfo info = MetadataExtractor.loadImageInfo(bytes);
				final OutputImageDefaultMetadataMessageBuilder infoMetadataBuilder = OutputImageDefaultMetadataMessage.toBuilder();
				final OutputImageDefaultMetadataMessage infoMetadata = infoMetadataBuilder.bitsPerPixel(info.getBitsPerPixel()) //
						.colorType(info.getColorType()) //
						.comments(info.getComments()) //
						.compressionAlgorithm(info.getCompressionAlgorithm()) //
						.format(new SourceImageFormat(info.getFormat())) //
						.formatDetails(info.getFormatDetails()) //
						.formatName(info.getFormatName()) //
						.height(info.getHeight()) //
						.mimeType(info.getMimeType()) //
						.numberOfImages(info.getNumberOfImages()) //
						.physicalHeightDpi(info.getPhysicalHeightDpi()) //
						.physicalHeightInch(info.getPhysicalHeightInch()) //
						.physicalWidthDpi(info.getPhysicalWidthDpi()) //
						.physicalWidthInch(info.getPhysicalWidthInch()) //
						.width(info.getWidth()) //
						.progressive(info.isProgressive()) //
						.transparent(info.isTransparent()) //
						.usesPalette(info.usesPalette()) //
						.build();
				metadataBuilder.info(infoMetadata);

				Metadata metadata = ImageMetadataReader.readMetadata(this.inputStream, bytes.length);// , FileType.Png);

				int counter = 0;
				metadataBuilder.exifDetails(this.readMetadataDirectories(ExifDirectoryBase.class, SectionNames.EXIF, metadata, counter++)) //
						// .exifSubIfdDetails(this.readMetadataDirectories(ExifSubIFDDirectory.class, SectionNames.EXIF_SUBIFD, metadata, counter++)) //
						.fileSystemDetails(this.readMetadataDirectories(FileSystemDirectory.class, SectionNames.FILE, metadata, counter++)) //
						.heifDetails(this.readMetadataDirectories(HeifDirectory.class, SectionNames.HEIF, metadata, counter++)) //
						.gpsDetails(this.readMetadataDirectories(GpsDirectory.class, SectionNames.GPS, metadata, counter++)) //
						.iccDetails(this.readMetadataDirectories(IccDirectory.class, SectionNames.ICC, metadata, counter++)) //
						.icoDetails(this.readMetadataDirectories(IcoDirectory.class, SectionNames.ICO, metadata, counter++)) //
						.iptcDetails(this.readMetadataDirectories(IptcDirectory.class, SectionNames.IPTC, metadata, counter++)) //
						.nikonPictureControl1Details(this.readMetadataDirectories(NikonPictureControl1Directory.class, SectionNames.NIKON, metadata, counter++)) //
						.nikonPictureControl2Details(this.readMetadataDirectories(NikonPictureControl2Directory.class, SectionNames.NIKON, metadata, counter++)) //
						.nikonType1MakernoteDetails(this.readMetadataDirectories(NikonType1MakernoteDirectory.class, SectionNames.NIKON, metadata, counter++)) //
						.nikonType2MakernoteDetails(this.readMetadataDirectories(NikonType2MakernoteDirectory.class, SectionNames.NIKON, metadata, counter++)) //
						.pngDetails(this.readMetadataDirectories(PngDirectory.class, SectionNames.PNG, metadata, counter++)) //
						.pngChromaticitiesDetails(this.readMetadataDirectories(PngChromaticitiesDirectory.class, SectionNames.PNG, metadata, counter++));
			}
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

		final Collection<T> directories = metadata.getDirectoriesOfType(clazz);
		if (directories.size() > 0)
		{
			for (T directory : directories)
			{
				String directoryName = directory.getName();
				DirectoryNames directoryType = DirectoryNames.getByName(directoryName);
				MetadataDirectoryFactory factory = AbstractMetadataFactory.getMetadataFactory(directoryType, this.availableTags);
				MetadataDirectory metadataDirectory = factory.getMetadataDirectory();
				Collection<Detail> metaDataDetails = metadataDirectory.getDetails(directory);
				details.put(directory.getName(), metaDataDetails);

				dates.putAll(metadataDirectory.getDates());
				if (directoryName.equalsIgnoreCase(SectionNames.GPS.toString()))
				{
					this.geoLocation = metadataDirectory.getLocation();
					this.geoLocation.setConfigured(Boolean.TRUE);
					LOGGER.trace("Set geo location data '{}'.", this.geoLocation);
				}
			}
		}

		this.dates.putAll(dates);

		return OutputImageDefaultMetadataDetails.toBuilder().label(sectionName.getLabel()).section(section).details(details).build();
	}
}
