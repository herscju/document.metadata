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

import org.apache.commons.imaging.ImagingException;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;

/**
 * @author Jürg Hersche (herscju)
 */
public class ImageMetaData
{
	private ImageMetadata imageMetadata = null;

	/**
	 * Constructor
	 */
	public ImageMetaData()
	{
		//
	}

	/**
	 * Constructor
	 * 
	 * @param jpegImageMetadata
	 */
	public ImageMetaData(ImageMetadata jpegImageMetadata)
	{
		this.imageMetadata = jpegImageMetadata;
	}

	/**
	 * @return
	 * @throws ImagingException
	 */
	public TiffOutputSet getOutputSet() throws ImagingException
	{
		TiffOutputSet outputSet = new TiffOutputSet();

		if (this.imageMetadata != null)
		{
			/**
			 * Note that exif might be null if no Exif metadata is found.
			 */
			final JpegImageMetadata meta = (JpegImageMetadata) this.imageMetadata;
			final TiffImageMetadata exif = meta.getExif();

			if (exif != null)
			{
				/**
				 * TiffImageMetadata class is immutable (read-only). TiffOutputSet class represents the Exif data to write. Usually, we want to update
				 * existing Exif metadata by changing the values of a few fields, or adding a field. In these cases, it is easiest to use getOutputSet() to
				 * start with a "copy" of the fields read from the image.
				 */
				outputSet = exif.getOutputSet();
			}

		}

		return outputSet;
	}
}
