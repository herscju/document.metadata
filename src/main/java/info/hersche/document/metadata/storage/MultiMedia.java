/**
 * 
 */
package info.hersche.document.metadata.storage;

import java.awt.image.BufferedImage;
import java.nio.file.Path;

import org.apache.commons.imaging.bytesource.ByteSource;

import info.hersche.document.metadata.image.MultiMediaImpl.Type;

/**
 * 
 */
public interface MultiMedia
{
	/**
	 * @return
	 */
	public BufferedImage getBufferedImage();

	/**
	 * @return
	 */
	public byte[] getBytes();

	/**
	 * @return
	 */
	public ByteSource getByteSource();

	/**
	 * @return
	 */
	public String getExtension();

	/**
	 * @return
	 */
	public Type getMediaType();

	/**
	 * @return
	 */
	public String getMimeType();

	/**
	 * @return
	 */
	public String getName();

	/**
	 * @return
	 */
	public Path getPath();

	/**
	 * @param bufferedImage
	 */
	public void setBufferedImage(BufferedImage bufferedImage);

	/**
	 * @param bytes
	 */
	public void setBytes(byte[] bytes);

	/**
	 * @param byteSource
	 */
	public void setByteSource(ByteSource byteSource);

	/**
	 * @param extension
	 */
	public void setExtension(String extension);

	/**
	 * @param type
	 */
	public void setMediaType(Type type);

	/**
	 * @param mimeType
	 */
	public void setMimeType(String mimeType);

	/**
	 * @param name
	 */
	public void setName(String name);

	/**
	 * @param path
	 */
	public void setPath(Path path);
}
