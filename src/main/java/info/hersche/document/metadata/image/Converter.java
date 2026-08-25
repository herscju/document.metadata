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
package info.hersche.document.metadata.image;

import java.awt.Dimension;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ImageObserver;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.imaging.bytesource.ByteSource;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.hersche.document.metadata.listener.PictureObserver;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.coobird.thumbnailator.Thumbnails;

/**
 * @author Jürg Hersche (herscju)
 * @version 18-Jun-2014
 */
@Getter
@Setter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true, builderMethodName = "toBuilder")
public class Converter
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(Converter.class);
	private static final Base64 CODEC = new Base64();

	/**
	 * Converts a BufferedImage to RGB colour space
	 * 
	 * @param source
	 * @param newType
	 * @return
	 */
	public static final BufferedImage convertColorspace(BufferedImage source, int newType)
	{
		if (source.getType() == newType)
		{
			return source;
		}

		BufferedImage destination = new BufferedImage(source.getWidth(), source.getHeight(), newType);

		try
		{
			Map<Key, Object> init = new HashMap<>();
			init.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			init.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
			RenderingHints hints = new RenderingHints(init);
			// ColorSpace colorSpace = ColorSpace.getInstance(ColorSpace.CS_sRGB); // Not required
			// ColorConvertOp colorConvertOp = new ColorConvertOp(colorSpace, hints); // Not working
			// ColorConvertOp colorConvertOp = new ColorConvertOp(colorSpace, null); // Not working
			ColorConvertOp colorConvertOp = new ColorConvertOp(hints);
			colorConvertOp.filter(source, destination);
		}
		catch (Exception e)
		{
			LOGGER.error("Exception {} while converting image", e.getMessage());
		}

		return destination;
	}

	/**
	 * @param bufferedImage
	 * @param targetDimension
	 */
	public static BufferedImage cropImage(final BufferedImage bufferedImage, final Dimension sourceDimension, final Dimension targetDimension)
	{
		if (bufferedImage == null)
		{
			return new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		}

		/**
		 * Get longest and smallest side
		 */
		double width = sourceDimension.getWidth();
		double height = sourceDimension.getHeight();
		double max = Math.max(width, height);
		double min = Math.min(width, height);
		double x = 0;
		double y = 0;
		double w = width;
		double h = height;
		if (min == width && max == height)
		{
			y = ((max - min) / 2);
			w = width;
			h = width;
		}
		else if (min == height && max == width)
		{
			x = ((max - min) / 2);
			w = height;
			h = height;
		}
		else
		{
			//
		}

		return Converter.cropImage(bufferedImage, x, y, w, h);
	}

	/**
	 * @param bufferedImage
	 * @param width
	 * @param height
	 * @return
	 */
	public static BufferedImage cropImage(BufferedImage bufferedImage, double x, double y, double w, double h)
	{
		return bufferedImage.getSubimage((int) x, (int) y, (int) w, (int) h);
	}

	/**
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String dateToLocalDateTime(LocalDateTime date, String pattern)
	{
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

		return date.format(formatter);
	}

	/**
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String dateToLocalDateTime(Long date, String pattern)
	{
		final LocalDateTime dateTime = Instant.ofEpochMilli(date.longValue()).atZone(ZoneId.systemDefault()).toLocalDateTime();
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

		return dateTime.format(formatter);
	}

	/**
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String dateToLocalDateTime(String date, String pattern)
	{
		return Converter.dateToLocalDateTime(Long.valueOf(date), pattern);
	}

	/**
	 * Converts a string representation of a hex dump to a byte array
	 * See also: https://stackoverflow.com/a/33552306/6487382
	 * 
	 * @param arg
	 * @param codec
	 * @return
	 */
	public static final byte[] decodeHexString(String arg)
	{
		int len = arg.length();

		if (len % 2 != 0)
		{
			return new byte[len / 2];
		}

		return CODEC.decode(arg);
	}

	/**
	 * https://stackoverflow.com/a/21071761/6487382
	 * 
	 * @param bytes
	 * @return
	 * @throws IOException
	 */
	public static BufferedImage getBufferedImageFromDocument(byte[] bytes) throws IOException
	{
		PDDocument document = Loader.loadPDF(new RandomAccessReadBuffer(bytes));
		PDFRenderer pdfRenderer = new PDFRenderer(document);
		int numberOfPages = document.getNumberOfPages();
		LOGGER.trace("Total pages available '{}'.", numberOfPages);

		// Use less DPI (300) for to save more space in hard disk.
		return pdfRenderer.renderImageWithDPI(0, 300, ImageType.RGB);
	}

	/**
	 * @param bytes
	 * @return
	 * @throws IOException
	 * @throws JCodecException
	 */
	public static BufferedImage getBufferedImageFromMovie(byte[] bytes) throws IOException, JCodecException
	{
		File file = File.createTempFile("VID_", null, null);
		file.deleteOnExit();

		try (FileOutputStream fos = new FileOutputStream(file))
		{
			fos.write(bytes);
		}

		return Converter.getBufferedImageFromMovie(file);
	}

	/**
	 * See 'https://github.com/jcodec/jcodec'
	 * 
	 * @param file
	 * @return
	 * @throws JCodecException
	 * @throws IOException
	 */
	public static BufferedImage getBufferedImageFromMovie(File file) throws IOException, JCodecException
	{
		Picture picture = FrameGrab.getFrameFromFile(file, 0);
		BufferedImage bufferedImage = AWTUtil.toBufferedImage(picture);

		return bufferedImage;
	}

	/**
	 * @param is
	 * @return
	 * @throws IOException
	 * @throws JCodecException
	 */
	public static BufferedImage getBufferedImageFromMovie(InputStream is) throws IOException, JCodecException
	{
		byte[] bytes = new byte[is.available()];
		is.read(bytes);

		return Converter.getBufferedImageFromMovie(bytes);
	}

	/**
	 * @param bytes
	 * @return
	 * @throws IOException
	 */
	public static final BufferedImage getBufferedImageFromPicture(byte[] bytes) throws IOException
	{
		try (InputStream is = Converter.getInputStreamFromByteArray(bytes))
		{
			return Converter.getBufferedImageFromPicture(is);
		}
	}

	/**
	 * @param imageData
	 * @return
	 * @throws IOException
	 */
	public static final BufferedImage getBufferedImageFromPicture(ByteSource byteSource) throws IOException
	{
		try (InputStream is = byteSource.getInputStream())
		{
			return Converter.getBufferedImageFromPicture(is);
		}
	}

	/**
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static final BufferedImage getBufferedImageFromPicture(File file) throws IOException
	{
		return ImageIO.read(file);
	}

	/**
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static final BufferedImage getBufferedImageFromPicture(InputStream is) throws IOException
	{
		return ImageIO.read(is);
	}

	/**
	 * Gets the dimension of a given buffered image
	 * 
	 * @param image
	 * @return
	 */
	public static Dimension getDimensionOfBufferedImage(BufferedImage image)
	{
		if (image == null)
		{
			return new Dimension(1, 1);
		}

		ImageObserver observer = new PictureObserver();
		int height = image.getHeight(observer);
		int width = image.getWidth(observer);

		return new Dimension(width, height);
	}

	/**
	 * Get an input stream from a byte array.
	 * 
	 * @param bytes
	 * @return
	 */
	public static final InputStream getInputStreamFromByteArray(byte[] bytes)
	{
		return new ByteArrayInputStream(bytes);
	}

	/**
	 * @param image
	 * @param extension
	 * @return
	 * @throws IOException
	 */
	public static byte[] toByteArray(BufferedImage image, String extension) throws IOException
	{
		byte[] bytes = {};
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream())
		{
			ImageIO.write(image, extension, baos);
			bytes = baos.toByteArray();
		}

		return bytes;
	}

	/**
	 * The original input image as Buffered Image
	 */
	private BufferedImage image;

	/**
	 * File extension or format of newly rendered picture
	 */
	private String format;

	/**
	 * Output quality of the compression algorithm
	 */
	private float quality;

	/**
	 * Sets the scaling factor of the thumbnail
	 */
	private double scale;

	/**
	 * The image type
	 */
	private int type;

	/**
	 * Default constructor
	 */
	public Converter()
	{
		// Nothing to do here
	}

	/**
	 * Generates a thumbnail with given parameters and returns as buffered image.
	 * 
	 * @return
	 * @throws IOException
	 */
	public BufferedImage generateThumbnail() throws IOException
	{
		return Thumbnails.of(this.image) // Original
				.imageType(this.type) // Image type
				.outputFormat(this.format) // Output format (JPEG, GIF, PNG, etc. ...)
				.scale(this.scale) // Scale factor
				.outputQuality(this.quality) // Output quality
				.asBufferedImage();
	}
}
