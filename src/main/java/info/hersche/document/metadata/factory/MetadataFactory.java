/**
 * 
 */
package info.hersche.document.metadata.factory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import org.apache.commons.imaging.ImagingException;

/**
 * 
 */
public interface MetadataFactory
{

	/**
	 * @param source
	 * @return
	 * @throws IOException 
	 * @throws ImagingException 
	 */
	public ImageMetaData getPictureMetaData(Path source) throws ImagingException, IOException;

	/**
	 * @param source
	 * @param filename
	 * @return
	 * @throws IOException 
	 * @throws ImagingException 
	 */
	public ImageMetaData getPictureMetadata(InputStream source, String filename) throws ImagingException, IOException;

	/**
	 * @param source
	 * @return
	 * @throws IOException 
	 * @throws ImagingException 
	 */
	public ImageMetaData getImageMetaData(InputStream source) throws ImagingException, IOException;
}
