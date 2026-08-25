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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.imaging.bytesource.ByteSource;
import org.jcodec.api.JCodecException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.hersche.document.metadata.factory.AbstractDocumentFactory.Extension;
import info.hersche.document.metadata.image.Converter;
import info.hersche.document.metadata.image.Mp4Movie;
import info.hersche.document.metadata.image.Picture;
import info.hersche.document.metadata.processor.MetaDataWriteable;
import info.hersche.document.metadata.processor.Mp4MetaDataWriter;

/**
 * @author Jürg Hersche (herscju)
 */
public class Mp4MovieFactory implements MediaFactory
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(Mp4MovieFactory.class);

	/**
	 * Member
	 */
	private String outputFormat;

	/**
	 * Constructor
	 * 
	 * @param outputFormat
	 */
	public Mp4MovieFactory(String outputFormat)
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
		LOGGER.trace("Get '{}' media.", Mp4Movie.class.getSimpleName());
		return new Mp4Movie(this.outputFormat, bufferedImage, inputStream);
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
			image = Converter.getBufferedImageFromMovie(is);
		}
		catch (IOException | JCodecException e)
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
	 *
	 */
	@Override
	public Map<Integer, String> getTagNameMap()
	{
		return Mp4Movie.getTagNameMap();
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
		return new Mp4MetaDataWriter(extension, source, target);
	}

}
