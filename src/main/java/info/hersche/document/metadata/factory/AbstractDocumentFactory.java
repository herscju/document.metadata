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

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import info.hersche.document.metadata.util.NetWorkUtil;

/**
 * @author Jürg Hersche (herscju)
 */
public class AbstractDocumentFactory
{
	/**
	 *
	 */
	public enum Extension {
							AAA("$$$"), // Unknown document, indicates search for details
							GIF("gif"), // Graphics Interchange Format
							GP3("3gp"), // Multimedia container format defined by the Third Generation Partnership Project (3GPP)
							JPEG("jpeg"), // Joint Photographic Experts Group format
							JPG("jpg"), // Joint Photographic Experts Group format
							MP4("mp4"), // MP4 digital multimedia container format
							OGV("ogv"), // OGG free, open digital multimedia container format
							PDF("pdf"), // Portable Document Format
							PNG("png"), // Portable Network Graphics
							SVG("svg"), // Scalable Vector Graphics
							SVGZ("svgz"), // Scalable Vector Graphics
							TIF("tif"), // Tagged Image File Format
							TIFF("tiff"), // Tagged Image File Format
							WEBM("webm"), // WebM media file format
							WEBP("webp"); // WebP image file format

		/**
		 * Convert a string into the appropriate ENUM
		 * 
		 * @param format
		 * @return ENUM
		 */
		public static Extension fromFormat(String format)
		{
			if (format == null || format.isEmpty())
			{
				return GIF;
			}

			switch (format.toLowerCase(DEFAULT_LOCALE))
			{
				case "$$$":
					return AAA;

				case "3gp":
					return GP3;

				case "gif":
					return GIF;

				case "jpeg":
				case "jpg":
					return JPG;

				case "mp4":
					return MP4;

				case "ogv":
				case "ogg":
					return OGV;

				case "pdf":
					return PDF;

				case "png":
					return PNG;

				case "svg":
					return SVG;

				case "svgz":
					return SVGZ;

				case "tif":
				case "tiff":
					return TIFF;

				case "webm":
					return WEBM;

				case "webp":
					return WEBP;

				default:
					return GIF;
			}
		}

		/**
		 * Convert a string into the appropriate ENUM
		 * 
		 * @param format
		 * @param extension
		 * @return ENUM
		 */
		public static Extension fromFormat(String format, String extension)
		{
			if (extension != null && !extension.isEmpty())
			{
				return Extension.fromFormat(extension);
			}
			else
			{
				return Extension.fromFormat(format);
			}
		}

		private String fileExtension;

		/**
		 * Private constructor
		 * 
		 * @param fileExtension
		 */
		private Extension(String fileExtension)
		{
			this.fileExtension = fileExtension;
		}

		/**
		 * Get the correct extension
		 * 
		 * @return
		 */
		public String getExtension()
		{
			return this.fileExtension;
		}

		/**
		 * Get the suitable mimetype
		 * 
		 * @return
		 */
		public String getMimetype()
		{
			return NetWorkUtil.detectMimtype("default", this.fileExtension);
		}
	}

	/**
	 * @author Jürg Hersche (herscju)
	 *
	 */
	public enum Label {
						LABEL_DENSITY(100);

		private int value;

		/**
		 * Private constructor
		 * 
		 * @param value
		 */
		private Label(int value)
		{
			this.value = value;
		}

		/**
		 * Get value
		 * 
		 * @return
		 */
		public int getValue()
		{
			return this.value;
		}
	}

	/**
	 * Private member / Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDocumentFactory.class);
	private static final Locale DEFAULT_LOCALE = Locale.UK;
	private static final String XSI_SCHEMALOCATION_VALUE = null;

	/**
	 * Public member
	 */
	public static final Schema SCHEMA = AbstractDocumentFactory.createSchema(XSI_SCHEMALOCATION_VALUE);
	public static final DocumentBuilder BUILDER = AbstractDocumentFactory.createDocumentBuilder();
	public static final Document DOCUMENT = BUILDER.newDocument();
	public static final Transformer TRANSFORMER = AbstractDocumentFactory.createTransformer();

	/**
	 * Private member
	 */
	private static AbstractDocumentFactory instance = new AbstractDocumentFactory();
	private static int counter = 1;

	/**
	 * @param image
	 * @param suffix
	 * @param extension
	 * @return
	 * @throws IOException
	 */
	public static File convertBufferedImageToFile(BufferedImage image, String suffix, String extension) throws IOException
	{
		StringBuffer pathname = new StringBuffer(System.getProperty("java.io.tmpdir"));
		pathname.append("\\");
		pathname.append(counter);
		pathname.append(".");
		pathname.append(AbstractDocumentFactory.class.getSimpleName());
		pathname.append(".");
		pathname.append(suffix);
		pathname.append(".");
		pathname.append(extension);
		File file = new File(pathname.toString());

		ImageIO.write(image, extension, file);
		counter++;

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("BufferedImage ('{}') has been written to '{}'", extension, file.getAbsolutePath());
			LOGGER.debug("Image: {}", image);
		}

		return file;
	}

	/**
	 * @param document
	 * @return
	 */
	public static String convertDocumentToString(Document document)
	{
		String output = "";

		try
		{
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			DOMSource source = new DOMSource(document);
			transformer.transform(source, result);
			output = writer.getBuffer().toString().replaceAll("\n|\r", "");
		}
		catch (TransformerConfigurationException e)
		{
			LOGGER.error("TransformerConfigurationException", e);
		}
		catch (TransformerException e)
		{
			LOGGER.error("TransformerException", e);
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("XML Document: {}", output);
		}

		return output;
	}

	/**
	 * @param baos
	 * @param suffix
	 * @param extension
	 * @return
	 * @throws IOException
	 */
	public static File convertOutputStreamToFile(ByteArrayOutputStream baos, String suffix, String extension) throws IOException
	{
		StringBuffer pathname = new StringBuffer(System.getProperty("java.io.tmpdir"));
		pathname.append("\\");
		pathname.append(counter);
		pathname.append(".");
		pathname.append(AbstractDocumentFactory.class.getSimpleName());
		pathname.append(".");
		pathname.append(suffix);
		pathname.append(".");
		pathname.append(extension);
		File file = new File(pathname.toString());
		try (FileOutputStream fos = new FileOutputStream(file))
		{
			baos.writeTo(fos);
		}
		counter++;

		if (LOGGER.isDebugEnabled())
		{
			String size = (baos != null) ? String.valueOf(baos.size()) : "n/a";
			LOGGER.debug("OutputStream ('{}') of size '{}' has been written to '{}'", extension, size, file.getAbsolutePath());
		}

		return file;
	}

	/**
	 * Get an instance of document builder
	 * 
	 * @return DocumentBuilder
	 */
	public static DocumentBuilder createDocumentBuilder()
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setValidating(false);
		factory.setSchema(SCHEMA);

		try
		{
			return factory.newDocumentBuilder();
		}
		catch (ParserConfigurationException e)
		{
			LOGGER.error("ParserConfigurationException", e);
		}

		return null;
	}

	/**
	 * Get an instance of document schema
	 * 
	 * @return Schema
	 */
	public static Schema createSchema(String schema)
	{
		if (schema != null && !schema.isEmpty())
		{
			// InputStream is = context.getResourceAsStream(schema);
			// Source xsd = new StreamSource(is);
			// SchemaFactory factory =
			// SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			// Schema schema = factory.newSchema(xsd);
			// is.close();
			//
			// return schema;
		}

		return null;
	}

	/**
	 * Get an instance of transformer
	 * 
	 * @return Transformer
	 */
	public static Transformer createTransformer()
	{
		TransformerFactory factory = TransformerFactory.newInstance();
		try
		{
			Transformer transformer = factory.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

			return transformer;
		}
		catch (TransformerConfigurationException e)
		{
			LOGGER.error("TransformerConfigurationException", e);
		}

		return null;
	}

	/**
	 * @return AbstractBinaryFactory
	 */
	public static AbstractDocumentFactory getInstance()
	{
		return AbstractDocumentFactory.instance;
	}

	public static MetadataFactory getMetadataWriterFactory(final Extension extension)
	{
		return null;
	}

	/**
	 * @param extension
	 * @return
	 */
	public static MetadataFactory getMetadataFactory(final Extension extension)
	{
		if (Extension.GIF.equals(extension))
		{
			// return new GifMetadataFactory();
		}

		if (Extension.JPG.equals(extension))
		{
			return new JpegMetadataFactory();
		}

		if (Extension.JPEG.equals(extension))
		{
			return new JpegMetadataFactory();
		}

		if (Extension.MP4.equals(extension))
		{
			return new Mp4MetadataFactory();
		}

		// if (Extension.PDF.equals(extension))
		// {
		// return new PdfMetadataFactory();
		// }

		if (Extension.PNG.equals(extension))
		{
			return new PngMetadataFactory();
		}

		if (Extension.SVG.equals(extension))
		{
			// return new SvgMetadataFactory();
		}

		if (Extension.TIFF.equals(extension))
		{
			return new TiffMetadataFactory();
		}

		if (Extension.WEBP.equals(extension))
		{
			// return new WebMetadataFactory();
		}

		StringBuilder message = new StringBuilder("Document of type '");
		message.append(extension.getExtension().toUpperCase(DEFAULT_LOCALE));
		message.append("' is currently not supported.");

		throw new RuntimeException(message.toString());
	}

	/**
	 * @param format
	 * @return ImageFactory
	 */
	public static MediaFactory getMediaFactory(String format)
	{
		return AbstractDocumentFactory.getMediaFactory(format, format);
	}

	/**
	 * @param format
	 * @param outputFormat
	 * @return
	 */
	public static MediaFactory getMediaFactory(String format, String outputFormat)
	{
		if (Extension.PDF.getExtension().equalsIgnoreCase(format))
		{
			LOGGER.trace("Get '{}' factory.", PdfDocumentFactory.class.getSimpleName());
			return new PdfDocumentFactory(outputFormat);
		}
		else if (Extension.PNG.getExtension().equalsIgnoreCase(format))
		{
			LOGGER.trace("Get '{}' factory.", PngPictureFactory.class.getSimpleName());
			return new PngPictureFactory(outputFormat);
		}
		else if (Extension.JPG.getExtension().equalsIgnoreCase(format))
		{
			LOGGER.trace("Get '{}' factory.", JpegPictureFactory.class.getSimpleName());
			return new JpegPictureFactory(outputFormat);
		}
		else if (Extension.JPEG.getExtension().equalsIgnoreCase(format))
		{
			LOGGER.trace("Get '{}' factory.", JpegPictureFactory.class.getSimpleName());
			return new JpegPictureFactory(outputFormat);
		}
		else if (Extension.GIF.getExtension().equalsIgnoreCase(format))
		{
			LOGGER.trace("Get '{}' factory.", GifPictureFactory.class.getSimpleName());
			return new GifPictureFactory(outputFormat);
		}
		else if (Extension.SVG.getExtension().equalsIgnoreCase(format))
		{
			LOGGER.trace("Get '{}' factory.", SvgPictureFactory.class.getSimpleName());
			return new SvgPictureFactory(outputFormat);
		}
		else if (Extension.WEBP.getExtension().equalsIgnoreCase(format))
		{
			LOGGER.trace("Get '{}' factory.", WebPictureFactory.class.getSimpleName());
			return new WebPictureFactory(outputFormat);
		}
		else if (Extension.MP4.getExtension().equalsIgnoreCase(format))
		{
			LOGGER.trace("Get '{}' factory.", Mp4MovieFactory.class.getSimpleName());
			return new Mp4MovieFactory(outputFormat);
		}

		StringBuilder message = new StringBuilder("Image of type '");
		message.append(format.toUpperCase(DEFAULT_LOCALE));
		message.append("' is currently not supported.");

		throw new RuntimeException(message.toString());
	}

	/**
	 * Private constructor
	 */
	private AbstractDocumentFactory()
	{
		// Nothing to do here
	}

}
