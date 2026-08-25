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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import org.apache.commons.imaging.ImagingException;
import org.apache.commons.imaging.formats.tiff.constants.TiffDirectoryType;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoAscii;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoBytes;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.hersche.document.metadata.factory.AbstractDocumentFactory.Extension;

/**
 * @author Jürg Hersche (herscju)
 */
public class Mp4MetaDataWriter implements MetaDataWriteable
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(Mp4MetaDataWriter.class);

	/**
	 * 
	 */
	private File file;
	private Mp4WriterExample processor;

	/**
	 * @param extension
	 * @param source
	 * @param target
	 * @param string
	 */
	public Mp4MetaDataWriter(final Extension extension, final InputStream source, final ByteArrayOutputStream target)
	{
		try
		{
			this.file = File.createTempFile("MOVIE", extension.getExtension());
			this.file.deleteOnExit();

			this.processor = new Mp4WriterExample(this.file);
		}
		catch (IOException e)
		{
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
	public Mp4MetaDataWriter(final Extension extension, final Path source, final Path target)
	{
		throw new NotImplementedException("Constructor 'public Mp4MetaDataWriter(Extension extension, Path source, Path target)' is not implemented now.");
	}

	/**
	 * @param type
	 * @param gpsInfo
	 * @param gpsVersionId
	 */
	@Override
	public void prepareExifDirectory(final TiffDirectoryType type, final TagInfoAscii tagInfo, final String value) throws ImagingException
	{
		// Nothing to do here
	}

	/**
	 * @param type
	 * @param gpsInfo
	 * @param gpsVersionId
	 */
	@Override
	public void prepareGpsDirectory(final TiffDirectoryType type, final TagInfoBytes gpsInfo, final byte[] gpsVersionId) throws ImagingException
	{
		// Nothing to do here
	}

	/**
	 *
	 */
	@Override
	public void prepareRootDirectory(Mp4OutputSet outputSet)
	{
		this.processor.prepare(outputSet);
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
		// Nothing to do here

		throw new NotImplementedException("Method is not implemented.");
	}

	/**
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@Override
	public boolean write()// throws FileNotFoundException, IOException
	{
		try
		{
			this.processor.write();
			this.processor.finalize(new FileOutputStream(this.file));
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
