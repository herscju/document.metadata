/**
 * 
 */
package info.hersche.document.metadata.factory;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.imaging.bytesource.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.hersche.document.metadata.factory.AbstractDocumentFactory.Extension;
import info.hersche.document.metadata.image.Converter;
import info.hersche.document.metadata.image.Picture;
import info.hersche.document.metadata.image.PngPicture;
import info.hersche.document.metadata.processor.MetaDataWriteable;

/**
 * @author Jürg Hersche (herscju)
 * @version 18-Jun-2014
 */
public class PngPictureFactory implements MediaFactory
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(PngPictureFactory.class);

	/**
	 * Member
	 */
	private String outputFormat;

	/**
	 * Constructor
	 *
	 * @param outputFormat
	 */
	public PngPictureFactory(String outputFormat)
	{
		this.outputFormat = outputFormat;
	}

	/**
	 * @return
	 */
	@Override
	public Picture getPicture(BufferedImage bufferedImage, ByteSource byteSource) throws IOException
	{
		return this.getPicture(bufferedImage, byteSource.getInputStream());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hubersuhner.qrcode.binary.factory.ImageFactory#getImage(java.awt.image.BufferedImage)
	 */
	@Override
	public Picture getPicture(BufferedImage bufferedImage, InputStream inputStream)
	{
		LOGGER.trace("Get '{}' media.", PngPicture.class.getSimpleName());

		return new PngPicture(this.outputFormat, bufferedImage, inputStream);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hubersuhner.epdb.data.document.factory.PictureFactory#getPicture(java.io.InputStream)
	 */
	@Override
	public Picture getPicture(InputStream is, InputStream inputStream)
	{
		BufferedImage image = null;

		try
		{
			image = Converter.getBufferedImageFromPicture(is);
		}
		catch (IOException e)
		{
			LOGGER.error("IOException: {} (InputStream {})", e.getMessage(), is);
			image = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);

			Graphics2D g2d = image.createGraphics();

			g2d.setColor(Color.WHITE);
			g2d.fill(new Rectangle2D.Double(1, 1, 1, 1));
			g2d.dispose();
		}

		LOGGER.trace("BufferedImage: {} (Source: {})", image, is);

		return this.getPicture(image, inputStream);
	}

	/**
	 * @return
	 */
	@Override
	public Map<Integer, String> getTagNameMap()
	{
		return new HashMap<>();
	}

	/**
	 * @param target
	 * @param source
	 * @param extension
	 * @return MetaDataWriteable
	 */
	@Override
	public MetaDataWriteable getWriter(Extension extension, InputStream source, ByteArrayOutputStream target)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
