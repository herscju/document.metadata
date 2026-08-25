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
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.imaging.bytesource.ByteSource;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.hersche.document.metadata.factory.AbstractDocumentFactory.Extension;
import info.hersche.document.metadata.image.Picture;
import info.hersche.document.metadata.image.PngPicture;
import info.hersche.document.metadata.processor.MetaDataWriteable;

/**
 * 
 */
public class PdfDocumentFactory implements MediaFactory
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(PdfDocumentFactory.class);
	private static final int DPI = 300;

	/**
	 * Member
	 */
	private String outputFormat;

	/**
	 * @param outputFormat
	 */
	public PdfDocumentFactory(String outputFormat)
	{
		this.outputFormat = outputFormat;
	}

	/**
	 *
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
		LOGGER.trace("Get '{}' media.", PngPicture.class.getSimpleName());

		return new PngPicture("png", bufferedImage, inputStream);
	}

	/**
	 * https://stackoverflow.com/a/21071761/6487382
	 */
	@Override
	public Picture getPicture(InputStream is, InputStream inputStream)
	{
		BufferedImage image = null;

		try
		{
			// https://stackoverflow.com/a/21071761/6487382
			PDDocument document = Loader.loadPDF(new RandomAccessReadBuffer(is));
			PDFRenderer pdfRenderer = new PDFRenderer(document);
			int numberOfPages = document.getNumberOfPages();
			LOGGER.trace("Total pages to be converting '{}'.", numberOfPages);

			// Use less DPI (300) for to save more space in hard disk.
			image = pdfRenderer.renderImageWithDPI(0, DPI, ImageType.RGB);
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
	 *
	 */
	@Override
	public Map<Integer, String> getTagNameMap()
	{
		return new HashMap<>();
	}

	/**
	 *
	 */
	@Override
	public MetaDataWriteable getWriter(Extension extension, InputStream source, ByteArrayOutputStream target)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
