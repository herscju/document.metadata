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
 * https://jenkov.com/tutorials/java-io/bufferedinputstream.html
 */
package info.hersche.document.metadata;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.imaging.ImagingException;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.web.WebAppConfiguration;

import com.drew.imaging.ImageProcessingException;

import info.hersche.document.metadata.factory.AbstractDocumentFactory;
import info.hersche.document.metadata.factory.MediaFactory;
import info.hersche.document.metadata.image.Converter;
import info.hersche.document.metadata.image.Picture;
import info.hersche.document.metadata.image.RenderPolicy;
import info.hersche.document.metadata.model.OutputImageMetadataMessage;

/**
 * @author Jürg Hersche (herscju)
 */
@SpringBootTest
@WebAppConfiguration
@TestInstance(Lifecycle.PER_CLASS)
public class PictureTests
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(PictureTests.class);
	private static final String DATETIME_PATTERN = "yyyy:MM:dd HH:mm:ss";
	private static final String FILENAME_PATTERN = "yyyyMMdd_HHmmss";
	private static final int TYPE = BufferedImage.TYPE_3BYTE_BGR;
	private static final float QUALITY = 1f;

	/**
	 * Required file resources
	 */
	@Value("classpath:images/20240615_114600.jpeg")
	private Resource landscape_20240615_114600;

	@Value("classpath:images/20240614_103051.jpg")
	private Resource portrait_20240614_103051;

	@Value("classpath:images/20240613_152212.jpg")
	private Resource square_20240613_152212;

	/**
	 * 
	 */
	private List<Resource> files = new ArrayList<>();
	private Map<String, Resource> resources = new LinkedHashMap<>();
	private Map<String, BufferedImage> images = new LinkedHashMap<>();
	private Map<String, Picture> pictures = new LinkedHashMap<>();
	private Map<String, OutputImageMetadataMessage> messages = new LinkedHashMap<>();

	/**
	 * Default constructor
	 */
	public PictureTests()
	{
		// Nothing to do here

		LOGGER.info("Class '{}' instantiated successfully.", PictureTests.class.getSimpleName());
	}

	/**
	 * @param extension
	 * @param bufferedImage
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	private Picture buildPicture(String extension, BufferedImage bufferedImage, InputStream inputStream) throws IOException
	{
		final MediaFactory factory = AbstractDocumentFactory.getMediaFactory(extension);
		final Picture picture = factory.getPicture(bufferedImage, inputStream);

		return picture;
	}

	/**
	 * @param extension
	 * @param extension
	 * @param bufferedImage
	 * @return
	 * @throws IOException
	 * 
	 */
	private Dimension generateThumbnail(String extension, BufferedImage bufferedImage, final Dimension sourceDimension, final Dimension targetDimension)
			throws IOException
	{
		final RenderPolicy policy = RenderPolicy.toBuilder().sourceDimension(sourceDimension).targetDimension(targetDimension).build();
		policy.calculateRatioAndScaleBySourceAndTarget();

		final BufferedImage croppedImage = Converter.cropImage(bufferedImage, sourceDimension, targetDimension);
		final Converter converter = Converter.toBuilder() //
				.image(croppedImage) //
				.format(extension) //
				.quality(QUALITY) //
				.scale(policy.getScaleFactor()) //
				.type(TYPE) //
				.build();
		final BufferedImage thumbnail = converter.generateThumbnail();

		return Converter.getDimensionOfBufferedImage(thumbnail);
	}

	/**
	 * @throws IOException
	 * @throws ImageProcessingException
	 */
	@BeforeAll
	private void init() throws IOException, ImageProcessingException
	{
		this.files.add(this.landscape_20240615_114600);
		this.files.add(this.portrait_20240614_103051);
		this.files.add(this.square_20240613_152212);

		/**
		 * Build linked hash map for all resources with file name as key
		 */
		for (Resource resource : this.files)
		{
			this.resources.put(resource.getFilename(), resource);
		}

		/**
		 * Generate buffered image for all given resources and build linked hash map with file name as key.
		 */
		for (Map.Entry<String, Resource> entry : this.resources.entrySet())
		{
			final String fileName = entry.getKey();
			final Resource resource = entry.getValue();
			try (final InputStream is = resource.getInputStream(); final ByteArrayOutputStream baos = new ByteArrayOutputStream())
			{
				is.transferTo(baos);

				LOGGER.info("Create buffered image of file '{}'.", fileName);
				final BufferedImage bufferedImage = Converter.getBufferedImageFromPicture(new ByteArrayInputStream(baos.toByteArray()));
				this.images.put(fileName, bufferedImage);
			}
		}
	}

	/**
	 * @throws IOException
	 * @throws ImageProcessingException
	 */
	@Test
	public void testGenerateThumbnail() throws IOException, ImageProcessingException
	{
		for (Map.Entry<String, BufferedImage> entry : this.images.entrySet())
		{
			final String extension = FilenameUtils.getExtension(entry.getKey());
			final Dimension sourceDimension = Converter.getDimensionOfBufferedImage(entry.getValue());
			final Dimension targetDimension = new Dimension(Picture.THUMBNAIL_WIDTH, Picture.THUMBNAIL_HEIGHT);
			final Dimension thumbnailDimension = this.generateThumbnail(extension, entry.getValue(), sourceDimension, targetDimension);

			assertEquals(targetDimension, thumbnailDimension);
		}

	}

	/**
	 * @throws IOException
	 * @throws ImageProcessingException
	 * @throws ImagingException
	 * 
	 */
	@Test
	public void testOriginalDate() throws ImagingException, ImageProcessingException, IOException
	{
		for (Map.Entry<String, Resource> entry : this.resources.entrySet())
		{
			final String fileName = entry.getKey();
			final String baseName = FilenameUtils.getBaseName(fileName);
			final String extension = FilenameUtils.getExtension(fileName);
			final Resource resource = entry.getValue();

			try (InputStream is = resource.getInputStream())
			{
				final Picture picture = this.buildPicture(extension, this.images.get(fileName), is);
				final OutputImageMetadataMessage metadata = picture.readMetadata();
				final LocalDateTime dateTime = picture.getOriginalDate();
				final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIME_PATTERN);
				final String originalDate = dateTime.format(formatter);
				final String fileDate = LocalDateTime.parse(baseName, DateTimeFormatter.ofPattern(FILENAME_PATTERN)).format(formatter);

				assertEquals(fileDate, originalDate);

				this.pictures.put(fileName, picture);
				this.messages.put(fileName, metadata);
			}
		}
	}

}
