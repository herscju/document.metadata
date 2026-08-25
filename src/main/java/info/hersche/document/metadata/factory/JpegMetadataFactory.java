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
package info.hersche.document.metadata.factory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.ImagingException;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;

/**
 * @author Jürg Hersche (herscju)
 */
public class JpegMetadataFactory implements MetadataFactory
{
	/**
	 * Default constructor
	 */
	public JpegMetadataFactory()
	{
		// Nothing to do here
	}

	/**
	 *
	 */
	@Override
	public ImageMetaData getImageMetaData(final InputStream source) throws ImagingException, IOException
	{
		return this.getPictureMetadata(source, "ImageName");
	}

	/**
	 *
	 */
	@Override
	public ImageMetaData getPictureMetadata(final InputStream source, final String filename) throws ImagingException, IOException
	{
		return new ImageMetaData((JpegImageMetadata) Imaging.getMetadata(source, filename));
	}

	/**
	 *
	 */
	@Override
	public ImageMetaData getPictureMetaData(final Path source) throws ImagingException, IOException
	{
		return new ImageMetaData((JpegImageMetadata) Imaging.getMetadata(source.toFile()));
	}

}
