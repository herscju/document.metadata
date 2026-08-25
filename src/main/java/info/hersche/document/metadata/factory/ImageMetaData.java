/**
 * 
 */
package info.hersche.document.metadata.factory;

import org.apache.commons.imaging.ImagingException;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;

/**
 * @author herscju
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
