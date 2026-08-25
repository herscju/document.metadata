/**
 * 
 */
package info.hersche.document.metadata.factory;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.imaging.bytesource.ByteSource;

import info.hersche.document.metadata.factory.AbstractDocumentFactory.Extension;
import info.hersche.document.metadata.image.Picture;
import info.hersche.document.metadata.processor.MetaDataWriteable;

/**
 * @author Jürg Hersche (herscju)
 * @version 18-Jun-2014
 * 
 */
public interface MediaFactory
{
	/**
	 * @param bufferedImage
	 * @param byteSource
	 * @return Picture
	 * @throws IOException 
	 */
	public Picture getPicture(BufferedImage bufferedImage, ByteSource byteSource) throws IOException;

	/**
	 * @param bufferedImage
	 * @param inputStream
	 * @return Picture
	 */
	public Picture getPicture(BufferedImage bufferedImage, InputStream inputStream);

	/**
	 * @param is
	 * @param inputStream
	 * @return Picture
	 */
	public Picture getPicture(InputStream is, InputStream inputStream);

	/**
	 * @return Map
	 */
	public Map<Integer, String> getTagNameMap();

	/**
	 * @param target 
	 * @param source 
	 * @param extension 
	 * @return MetaDataWriteable
	 */
	public MetaDataWriteable getWriter(Extension extension, InputStream source, ByteArrayOutputStream target);
}
