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
package info.hersche.document.metadata.factory;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.imaging.bytesource.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.hersche.document.metadata.factory.AbstractDocumentFactory.Extension;
import info.hersche.document.metadata.image.Converter;
import info.hersche.document.metadata.image.JpegPicture;
import info.hersche.document.metadata.image.Picture;
import info.hersche.document.metadata.processor.JpegMetaDataWriter;
import info.hersche.document.metadata.processor.MetaDataWriteable;

/**
 * @author Jürg Hersche (herscju)
 * @version 18-Jun-2014
 * 
 */
public class JpegPictureFactory implements MediaFactory
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(JpegPictureFactory.class);

	/**
	 * Member
	 */
	private String outputFormat;

	/**
	 * Constructor
	 *
	 * @param outputFormat
	 */
	public JpegPictureFactory(String outputFormat)
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

	/**
	 *
	 */
	@Override
	public Picture getPicture(BufferedImage bufferedImage, InputStream inputStream)
	{
		LOGGER.trace("Get '{}' media.", JpegPicture.class.getSimpleName());

		return new JpegPicture(this.outputFormat, bufferedImage, inputStream);
	}

	/**
	 *
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
			image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);

			Graphics2D g2d = image.createGraphics();

			g2d.setColor(Color.WHITE);
			g2d.fill(new Rectangle2D.Double(1, 1, 1, 1));
			g2d.dispose();
		}

		LOGGER.trace("BufferedImage: {}", image);

		return this.getPicture(image, inputStream);
	}

	/**
	 * @return
	 */
	@Override
	public Map<Integer, String> getTagNameMap()
	{
		return JpegPicture.getTagNameMap();
	}

	/**
	 * @param extension
	 * @param target
	 * @param source
	 * @return MetaDataWriteable
	 */
	@Override
	public MetaDataWriteable getWriter(Extension extension, InputStream source, ByteArrayOutputStream target)
	{
		return new JpegMetaDataWriter(extension, source, target);
	}
}
