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

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import info.hersche.document.metadata.image.MultiMediaImpl.Type;
import info.hersche.document.metadata.util.Detector;

/**
 * @author Jürg Hersche (herscju)
 */
@SpringBootTest
@WebAppConfiguration
@TestInstance(Lifecycle.PER_CLASS)
public class DetectorTests
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DetectorTests.class);
	private static final List<FileType> TYPES = DetectorTests.newList();

	/**
	 * Default constructor
	 */
	public DetectorTests()
	{
		// Nothing to do here

		LOGGER.info("Class '{}' instantiated successfully.", DetectorTests.class.getSimpleName());
	}

	/**
	 * @return
	 */
	private static List<FileType> newList()
	{
		List<FileType> types = new ArrayList<>();
		types.add(new FileType("20240615_113240.jpg", Type.STANDARD_IMAGE));
		types.add(new FileType("20250207_133812.jpg", Type.STANDARD_IMAGE));
		types.add(new FileType("20250208_093305.jpg", Type.STANDARD_IMAGE));
		types.add(new FileType("IMG-20211118-WA0000.jpg", Type.WHATSAPP_IMAGE));
		types.add(new FileType("IMG-20230906-WA0007.jpg", Type.WHATSAPP_IMAGE));
		types.add(new FileType("IMG-20230906-WA0008.jpg", Type.WHATSAPP_IMAGE));
		types.add(new FileType("IMG-20230906-WA0009.jpg", Type.WHATSAPP_IMAGE));
		types.add(new FileType("IMG-20231218-WA0001.jpg", Type.WHATSAPP_IMAGE));
		types.add(new FileType("IMG-20231218-WA0002.jpeg", Type.WHATSAPP_IMAGE));
		types.add(new FileType("IMG-20231218-WA0006.png", Type.WHATSAPP_IMAGE));
		types.add(new FileType("IMG-20240707-WA0000.gif", Type.WHATSAPP_IMAGE));
		types.add(new FileType("IMG-20250331-WA0161.webp", Type.WHATSAPP_IMAGE));
		types.add(new FileType("IMG-20250331-WA0299.jpg", Type.WHATSAPP_IMAGE));
		types.add(new FileType("Samsung SM-G981B öäü_(1)_Изображение_图像.jpeg", Type.STANDARD_IMAGE));
		types.add(new FileType("SCN__B28_00003.jpg", Type.STANDARD_SCAN));
		types.add(new FileType("SCN__C36_00028.jpeg", Type.STANDARD_SCAN));
		types.add(new FileType("VID_20250204_133618.mp4", Type.STANDARD_VIDEO));
		types.add(new FileType("VID-20250209-WA0256.mp4", Type.WHATSAPP_VIDEO));
		types.add(new FileType("WhatsApp Bild 2025-03-05 um 18.43.53_9562d8f6.jpg", Type.WHATSAPP_IMAGE));
		types.add(new FileType("WhatsApp Image 2025-02-09 at 15.18.09(1).jpg", Type.WHATSAPP_IMAGE));
		types.add(new FileType("WhatsApp Image 2025-02-09 at 21.44.53(0).jpeg", Type.WHATSAPP_IMAGE));
		types.add(new FileType("WhatsApp Image 2025-02-20 at 08.38.30.jpeg", Type.WHATSAPP_IMAGE));
		types.add(new FileType("WhatsApp Image 2025-02-23 at 16.38.03.jpeg", Type.WHATSAPP_IMAGE));
		types.add(new FileType("WhatsApp Image 2025-06-18 at 10.14.08(13).jpeg", Type.WHATSAPP_IMAGE));
		types.add(new FileType("WhatsApp Status 02d4d3638a8b4206bbc35cedc8f41d38.mp4", Type.WHATSAPP_STATUS));
		types.add(new FileType("WhatsApp Status 03fc62f59b344d85ab260115eb98f764.jpg", Type.WHATSAPP_STATUS));
		types.add(new FileType("WhatsApp Status 5230a91ec2c94ca68dcb40b2f5ebfc7c.jpeg", Type.WHATSAPP_STATUS));
		types.add(new FileType("WhatsApp Status 3937196c1c2f457ba169ab069fe8a4c4.png", Type.WHATSAPP_STATUS));
		types.add(new FileType("WhatsApp Status ab6c593cc39b49c6bc5911a27189b64a.gif", Type.WHATSAPP_STATUS));
		types.add(new FileType("WhatsApp Status c4b27a2d98ab4fd9a57565904f8eaa1d.webp", Type.WHATSAPP_STATUS));
		types.add(new FileType("WhatsApp Status c4de843f4c404139bfc26c88a7b22bde.webm", Type.WHATSAPP_STATUS));
		types.add(new FileType("WhatsApp Status c6b055254bf448a38f93d636d1fafd6a.jpg", Type.WHATSAPP_STATUS));
		types.add(new FileType("WhatsApp Status fdc5566d1c004deeadb74aa54e84585b.jpeg", Type.WHATSAPP_STATUS));

		return types;
	}

	/**
	 * 
	 */
	@Test
	public void testType()
	{
		int counter = 0;
		for (FileType fileType : TYPES)
		{
			counter++;

			String name = fileType.getName();
			Type type = Detector.toBuilder().name(name).build().detectMultiMediaType();
			LOGGER.info("{}/{}: Working on file '{}' of estimated type '{}'.", counter, TYPES.size(), name, type);

			assertEquals(fileType.getType(), type);
		}
	}

}
