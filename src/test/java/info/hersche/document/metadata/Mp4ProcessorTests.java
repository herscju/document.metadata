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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import com.drew.imaging.ImageProcessingException;

import info.hersche.document.metadata.processor.Mp4Processable;
import info.hersche.document.metadata.processor.Mp4Processor;
import info.hersche.document.metadata.processor.Mp4Writer.BetterByteArrayOutputStream;

/**
 * @author Jürg Hersche (herscju)
 */
@SpringBootTest
@WebAppConfiguration
@TestInstance(Lifecycle.PER_CLASS)
public class Mp4ProcessorTests
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(Mp4ProcessorTests.class);
	private static final Long OFFSET = 33L;
	private static final Long ZERO = 0L;
	private static final Long DIFF = 10L;

	/**
	 * 
	 */
	@Value("C:\\Temp\\LoremIpsum_0.txt")
	private String textInput;

	@Value("C:\\Temp\\VID-20250209-WA0256.mp4")
	private String videoInput;

	/**
	 * 
	 */
	private InputStream input;
	private byte[] bytes;
	private Long sizeBefore;
	private Long sizeAfter;

	private String dateTime;
	private String title;
	private Locale locale;

	/**
	 * Default constructor
	 */
	public Mp4ProcessorTests()
	{
		// Nothing to do here

		LOGGER.info("Class '{}' instantiated successfully.", MovieTests.class.getSimpleName());
	}

	/**
	 * 
	 */
	@AfterAll
	private void after()
	{
		LOGGER.info("=== The end! ===================================================================");
	}

	/**
	 * @throws IOException
	 * @throws ImageProcessingException
	 */
	@BeforeAll
	private void init() throws IOException
	{
		this.input = new FileInputStream(new File(this.textInput));
		this.bytes = this.input.readAllBytes();
		this.sizeBefore = Long.valueOf(this.bytes.length);
		this.sizeAfter = Long.sum(this.sizeBefore.longValue(), DIFF.longValue());

		String result = new String(this.bytes, StandardCharsets.UTF_8);
		LOGGER.info(">>> A >>> '{}'", result);
		LOGGER.info(">>> A >>> '{}'", result.length());

		this.input = new FileInputStream(new File(this.textInput));
	}

	/**
	 * @throws IOException
	 */
	@AfterEach
	private void reset() throws IOException
	{
		this.input = new FileInputStream(new File(this.textInput));
	}

	/**
	 * @throws IOException
	 * 
	 */
	@Test
	public void testDiffLarge() throws IOException
	{
		Long offset = OFFSET;
		Long diff = DIFF;
		BetterByteArrayOutputStream baos = new BetterByteArrayOutputStream();

		Mp4Processable processor = new Mp4Processor(this.input);
		processor.setSizeBefore(this.sizeBefore);
		processor.setSizeAfter(this.sizeAfter);
		processor.setOffset(offset);
		processor.process(diff, baos);

		String result = new String(baos.toByteArray(), StandardCharsets.UTF_8);
		LOGGER.info(">>> C >>> '{}'", result);
		LOGGER.info(">>> C >>> '{}'", result.length());
	}

	/**
	 * @throws IOException
	 * 
	 */
	@Test
	public void testDiffZero() throws IOException
	{
		Long offset = OFFSET;
		Long diff = ZERO;
		BetterByteArrayOutputStream baos = new BetterByteArrayOutputStream();

		Mp4Processable processor = new Mp4Processor(this.input);
		processor.setOffset(offset);
		processor.process(diff, baos);

		String result = new String(baos.toByteArray(), StandardCharsets.UTF_8);
		LOGGER.info(">>> B >>> '{}'", result);
		LOGGER.info(">>> B >>> '{}'", result.length());
	}

}
