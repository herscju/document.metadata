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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;

import org.apache.commons.imaging.ImagingException;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import org.apache.commons.imaging.formats.tiff.constants.TiffDirectoryType;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoAscii;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoBytes;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputDirectory;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.hersche.document.metadata.factory.AbstractDocumentFactory;
import info.hersche.document.metadata.factory.AbstractDocumentFactory.Extension;
import info.hersche.document.metadata.factory.ImageMetaData;
import info.hersche.document.metadata.factory.MetadataFactory;

/**
 * @author Jürg Hersche (herscju)
 */
public class JpegMetaDataWriter implements MetaDataWriteable
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(JpegMetaDataWriter.class);
	private static final ExifRewriter WRITER = new ExifRewriter();

	/**
	 * 
	 */
	private File file;
	private Path sourcePath;
	private Path targetPath;
	private InputStream sourceStream;
	private OutputStream targetStream;
	private TiffOutputSet outputSet;

	/**
	 * @param extension
	 * @param source
	 * @param target
	 * @param string
	 */
	public JpegMetaDataWriter(final Extension extension, final InputStream source, final OutputStream target)
	{
		this.sourceStream = source;
		this.targetStream = target;
		this.outputSet = null;

		LOGGER.info("Class '{}' initialized successfully.", JpegMetaDataWriter.class.getSimpleName());

		try
		{
			MetadataFactory factory = AbstractDocumentFactory.getMetadataFactory(extension);
			ImageMetaData metaData = factory.getImageMetaData(source);
			if (metaData == null)
			{
				throw new NullPointerException("No image metadata available for image type '" + extension + "'.");
			}
			this.outputSet = metaData.getOutputSet();
			source.reset();
		}
		catch (ImagingException e)
		{
			LOGGER.error("Processing image failed!", e);
		}
		catch (IOException e)
		{
			LOGGER.error("Couldn't open inputstream!", e);
		}
	}

	/**
	 * Constructor
	 * 
	 * @param extension
	 * 
	 * @param source
	 * @param target
	 */
	public JpegMetaDataWriter(final Extension extension, final Path source, final Path target)
	{
		this.sourcePath = source;
		this.targetPath = target;
		this.file = new File(this.targetPath.toUri());
		this.outputSet = null;

		LOGGER.info("Class '{}' initialized successfully.", JpegMetaDataWriter.class.getSimpleName());

		try
		{
			MetadataFactory factory = AbstractDocumentFactory.getMetadataFactory(extension);
			ImageMetaData metaData = factory.getPictureMetaData(source);
			if (metaData == null)
			{
				throw new NullPointerException("No image metadata available for image type '" + extension + "'.");
			}
			this.outputSet = metaData.getOutputSet();
		}
		catch (ImagingException e)
		{
			LOGGER.error("Processing image failed!", e);
		}
		catch (IOException e)
		{
			LOGGER.error("Couldn't open file!", e);
		}
	}

	/**
	 * @param type
	 * @param gpsInfo
	 * @param gpsVersionId
	 */
	@Override
	public void prepareExifDirectory(final TiffDirectoryType type, final TagInfoAscii tagInfo, final String value) throws ImagingException
	{
		/**
		 * If file does not contain any exif metadata, we create an empty set of exif metadata. Otherwise, we keep all of the other existing tags.
		 */
		if (this.outputSet == null)
		{
			this.outputSet = new TiffOutputSet();
		}

		/**
		 * Note that you should first remove the field/tag if it already exists in this directory, or you may end up with duplicate tags.
		 * 
		 * Certain fields/tags are expected in certain Exif directories. Others can occur in more than one directory (and often have a different meaning in
		 * different directories).
		 * 
		 * TagInfo constants often contain a description of what directories are associated with a given tag.
		 */
		final TiffOutputDirectory directory = this.outputSet.getOrCreateRootDirectory();

		/**
		 * Before updating, make sure to remove old value if
		 * present (this method will not fail if the tag does not exist).
		 */
		directory.removeField(tagInfo);
		directory.add(tagInfo, value);
		LOGGER.trace("Prepare Exif directory: {} --> {}", tagInfo, value);
	}

	/**
	 * @param type
	 * @param tagInfo
	 * @param value
	 */
	@Override
	public void prepareGpsDirectory(final TiffDirectoryType type, final TagInfoBytes tagInfo, final byte[] value) throws ImagingException
	{
		if (this.outputSet == null)
		{
			this.outputSet = new TiffOutputSet();
		}

		final TiffOutputDirectory directory = this.outputSet.getGpsDirectory();
		directory.removeField(tagInfo);
		directory.add(tagInfo, value);
		LOGGER.trace("Prepare GPS directory: {} --> {}", tagInfo, value);
	}

	/**
	 *
	 */
	@Override
	public void prepareRootDirectory(Mp4OutputSet outputSet)
	{
		// Nothing to do here

		throw new NotImplementedException("Method is not implemented.");
	}

	/**
	 * See 'https://stackoverflow.com/a/66215172/6487382', 'https://stackoverflow.com/a/68942970/6487382', 'https://stackoverflow.com/a/57757312/6487382',
	 * and 'https://github.com/apache/commons-imaging/blob/master/src/test/java/org/apache/commons/imaging/examples/WriteExifMetadataExample.java'
	 * 
	 * @param type
	 * 
	 * @param type
	 * @param tagInfo
	 * @param value
	 * @throws IOException
	 * @throws FileNotFoundException
	 * 
	 */
	@Override
	public void prepareRootDirectory(final TiffDirectoryType type, final TagInfoAscii tagInfo, final String value) throws ImagingException
	{
		/**
		 * If file does not contain any exif metadata, we create an empty set of exif metadata. Otherwise, we keep all of the other existing tags.
		 */
		if (this.outputSet == null)
		{
			this.outputSet = new TiffOutputSet();
		}

		/**
		 * Note that you should first remove the field/tag if it already exists in this directory, or you may end up with duplicate tags.
		 * 
		 * Certain fields/tags are expected in certain Exif directories. Others can occur in more than one directory (and often have a different meaning in
		 * different directories).
		 * 
		 * TagInfo constants often contain a description of what directories are associated with a given tag.
		 */
		final TiffOutputDirectory directory = this.outputSet.getOrCreateRootDirectory();

		/**
		 * Before updating, make sure to remove old value if
		 * present (this method will not fail if the tag does not exist).
		 */
		directory.removeField(tagInfo);
		directory.add(tagInfo, value);
		LOGGER.trace("Prepare root directory: {} --> {}", tagInfo, value);
	}

	/**
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@Override
	public boolean write() // throws FileNotFoundException, IOException
	{
		/**
		 * Update EXIF metadata either from path or stream.
		 */
		try
		{
			if (this.sourcePath != null && this.targetPath != null)
			{
				try (FileOutputStream fos = new FileOutputStream(this.file); OutputStream os = new BufferedOutputStream(fos))
				{
					WRITER.updateExifMetadataLossless(this.sourcePath.toFile(), os, this.outputSet);
				}
			}
			else
			{
				WRITER.updateExifMetadataLossless(this.sourceStream, this.targetStream, this.outputSet);
			}
		}
		catch (FileNotFoundException e)
		{
			LOGGER.error("FileNotFoundException", e);

			return false;
		}
		catch (IOException e)
		{
			LOGGER.error("IOException", e);

			return false;
		}

		return true;
	}

}
