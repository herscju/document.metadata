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
package info.hersche.document.metadata.processor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

import org.apache.commons.imaging.ImagingException;
import org.apache.commons.imaging.bytesource.ByteSource;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifDirectoryBase;

import info.hersche.document.metadata.configuration.HostsProperties;
import info.hersche.document.metadata.configuration.TlsEncryptionProperties;
import info.hersche.document.metadata.factory.AbstractDocumentFactory.Extension;
import info.hersche.document.metadata.image.MultiMediaImpl;
import info.hersche.document.metadata.model.Action;
import info.hersche.document.metadata.storage.MultiMedia;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Jürg Hersche (herscju)
 */
@Getter
@Setter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true, builderMethodName = "toBuilder")
public class UpdateFileImageMetaDataProcessor extends AbstractMetaDataProcessor implements MetaDataProcessable
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(UpdateFileImageMetaDataProcessor.class);

	/**
	 * 
	 */
	@Setter
	private TlsEncryptionProperties encryptionProperties;

	/**
	 * 
	 */
	@Setter
	private HostsProperties hostsProperties;

	/**
	 * 
	 */
	@Setter
	@NotNull
	private String destination;

	/**
	 * 
	 */
	@Setter
	@NotNull
	private String subscription;

	/**
	 * 
	 */
	@Setter
	@NotNull
	private String path;

	/**
	 * 
	 */
	@NotNull
	private MultiMediaImpl image;

	/**
	 * 
	 */
	private String directory;

	/**
	 * 
	 */
	@NotNull
	private String date;

	/**
	 * 
	 */
	@Getter
	private Boolean status;

	/**
	 * Constructor
	 */
	public UpdateFileImageMetaDataProcessor()
	{
		this.status = Boolean.FALSE;

		LOGGER.info("Class '{}' instantiated successfully.", UpdateFileImageMetaDataProcessor.class.getSimpleName());
	}

	/**
	 * @return
	 *
	 */
	@Override
	public byte[] process(MultiMedia image, Action action)
	{
		byte[] bytes = new byte[0];

		try
		{
			// Read the metadata from the image file
			try (final InputStream inputStream = image.getByteSource().getInputStream())
			{
				final Metadata metadata = ImageMetadataReader.readMetadata(inputStream);

				if (metadata.containsDirectoryOfType(ExifDirectoryBase.class))
				{
					// Get the Exif directories
					final Path source = Paths.get(image.getPath().toUri());
					final Path target = Paths.get(this.directory, image.getName()).toAbsolutePath();
					LOGGER.trace("From source '{}' to target '{}'.", source, target);

					final MetaDataWriteable writer = new JpegMetaDataWriter(Extension.fromFormat(image.getExtension()), source, target);
					final Collection<ExifDirectoryBase> directories = metadata.getDirectoriesOfType(ExifDirectoryBase.class);
					final List<TagInfo> tags = this.updateMediaMetaData(image, writer, directories, this.date);
					LOGGER.debug("Tags updated '{}'.", tags);

					bytes = Files.readAllBytes(target);
					LOGGER.debug("'{}' bytes available.", bytes.length);
				}
			}

			this.status = Boolean.TRUE;
		}
		catch (ImageProcessingException e)
		{
			LOGGER.error("Couldn't process image!", e);
		}
		catch (ImagingException e)
		{
			LOGGER.error("Processing image failed!", e);
		}
		catch (FileNotFoundException e)
		{
			LOGGER.error("Missing file!", e);
		}
		catch (IOException e)
		{
			LOGGER.error("Couldn't open inputstream!", e);
		}
		finally
		{
			// Nothing to do here
		}

		try
		{
			ByteSource source = image.getByteSource();

			bytes = source.getByteArray(0, (int) source.size());
		}
		catch (IOException e)
		{
			LOGGER.error("Couldn't read byte array!", e);
		}

		return bytes;
	}

}
