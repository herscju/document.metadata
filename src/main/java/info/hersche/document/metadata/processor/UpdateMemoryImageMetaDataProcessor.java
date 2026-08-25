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
package info.hersche.document.metadata.processor;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import org.apache.commons.imaging.ImagingException;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifDirectoryBase;
import com.drew.metadata.mp4.Mp4Directory;

import info.hersche.document.metadata.configuration.HostsProperties;
import info.hersche.document.metadata.configuration.TlsEncryptionProperties;
import info.hersche.document.metadata.factory.AbstractDocumentFactory;
import info.hersche.document.metadata.factory.AbstractDocumentFactory.Extension;
import info.hersche.document.metadata.factory.MediaFactory;
import info.hersche.document.metadata.image.MultiMediaImpl;
import info.hersche.document.metadata.model.Action;
import info.hersche.document.metadata.model.Counter;
import info.hersche.document.metadata.storage.MultiMedia;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author herscju
 */
@Getter
@Setter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true, builderMethodName = "toBuilder")
public class UpdateMemoryImageMetaDataProcessor extends AbstractMetaDataProcessor implements MetaDataProcessable
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(UpdateMemoryImageMetaDataProcessor.class);

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
	 * 
	 */
	@Getter
	private Counter counter;

	/**
	 * Constructor
	 */
	public UpdateMemoryImageMetaDataProcessor()
	{
		this.status = Boolean.FALSE;

		LOGGER.info("Class '{}' instantiated successfully.", UpdateMemoryImageMetaDataProcessor.class.getSimpleName());
	}

	/**
	 * @param multiMedia
	 * @return
	 */
	private byte[] exceptionHandling(MultiMedia multiMedia)
	{
		try
		{
			int length = (int) multiMedia.getByteSource().size();

			return multiMedia.getByteSource().getByteArray(0, length);
		}
		catch (IOException e)
		{
			LOGGER.error("Couldn't read byte array!", e);
		}

		return new byte[0];
	}

	/**
	 * @return
	 *
	 */
	@Override
	public byte[] process(MultiMedia multiMedia, Action action)
	{
		byte[] bytes = new byte[0];

		try
		{
			// Read the metadata from the image file
			try (final InputStream inputStream = multiMedia.getByteSource().getInputStream())
			{
				final Metadata metadata = ImageMetadataReader.readMetadata(inputStream);

				/**
				 * Log all available directories and tags for deep debugging purposes.
				 */
				if (LOGGER.isTraceEnabled())
				{
					for (Directory directory : metadata.getDirectories())
					{
						Collection<Tag> tags = directory.getTags();
						for (Tag tag : tags)
						{
							LOGGER.trace("'{}': '{}' = '{}' ('{}')", directory.getName(), tag.getTagName(), tag.getDescription(),
									directory.getClass().getName());
						}
					}
				}

				if (metadata.containsDirectoryOfType(ExifDirectoryBase.class) || metadata.containsDirectoryOfType(Mp4Directory.class))
				{
					try (final InputStream source = multiMedia.getByteSource().getInputStream();
							final ByteArrayOutputStream target = new ByteArrayOutputStream())
					{
						final MediaFactory factory = AbstractDocumentFactory.getMediaFactory(multiMedia.getExtension());

						if (metadata.containsDirectoryOfType(ExifDirectoryBase.class))
						{
							final MetaDataWriteable writer = factory.getWriter(Extension.fromFormat(multiMedia.getExtension()), source, target);
							final Collection<ExifDirectoryBase> directories = metadata.getDirectoriesOfType(ExifDirectoryBase.class);
							final List<TagInfo> tags = this.updateMediaMetaData(multiMedia, writer, directories, this.date);
							LOGGER.debug("ExifDirectoryBase: Tags updated '{}'.", tags);

							this.status = Boolean.TRUE;

							bytes = target.toByteArray();
						}

						if (metadata.containsDirectoryOfType(Mp4Directory.class))
						{
							final MetaDataWriteable writer = factory.getWriter(Extension.fromFormat(multiMedia.getExtension()), source, target);
							final Collection<Mp4Directory> directories = metadata.getDirectoriesOfType(Mp4Directory.class);
							final List<TagInfo> tags = this.updateMovieMetaData(multiMedia, writer, directories, this.date);
							LOGGER.debug("Mp4Directory: Tags updated '{}'.", tags);

							this.status = Boolean.TRUE;

							bytes = target.toByteArray();
						}
					}
					LOGGER.debug("'{}' bytes available.", bytes.length);
				}
			}
		}
		catch (ImageProcessingException e)
		{
			LOGGER.error("Couldn't process image!", e);

			return this.exceptionHandling(multiMedia);
		}
		catch (ImagingException e)
		{
			LOGGER.error("Processing image failed!", e);

			return this.exceptionHandling(multiMedia);
		}
		catch (FileNotFoundException e)
		{
			LOGGER.error("Missing file!", e);

			return this.exceptionHandling(multiMedia);
		}
		catch (IOException e)
		{
			LOGGER.error("Couldn't open inputstream!", e);

			return this.exceptionHandling(multiMedia);
		}
		finally
		{
			// Nothing to do here
		}

		if (bytes.length == 0)
		{
			return this.exceptionHandling(multiMedia);
		}

		return bytes;
	}

}
