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
package info.hersche.document.metadata.image;

import java.awt.image.BufferedImage;
import java.nio.file.Path;

import org.apache.commons.imaging.bytesource.ByteSource;

import info.hersche.document.metadata.storage.MultiMedia;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Jürg Hersche (herscju)
 */
@Getter
@Setter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true, builderMethodName = "toBuilder")
public class MultiMediaImpl implements MultiMedia
{
	/**
	 * 
	 */
	public enum Type {
						DOCUMENT_SCAN("Document Scan"),
						STANDARD_SCAN("Standard Scan"),
						STANDARD_IMAGE("Standard Image"),
						STANDARD_VIDEO("Standard Video"),
						WHATSAPP_IMAGE("WhatsApp Image"),
						WHATSAPP_VIDEO("WhatsApp Video"),
						WHATSAPP_STATUS("WhatsApp Status");

		/**
		 * 
		 */
		@Getter
		private String name;

		/**
		 * @param string
		 */
		private Type(String name)
		{
			this.name = name;
		}
	}

	/**
	 * Image content
	 */
	@ToString.Exclude
	private BufferedImage bufferedImage;

	/**
	 * Original byte array as byte source
	 */
	@ToString.Exclude
	private ByteSource byteSource;

	/**
	 * 
	 */
	@ToString.Exclude
	private byte[] bytes;

	/**
	 * Path to file
	 */
	private Path path;

	/**
	 * Name of file
	 */
	private String name;

	/**
	 * File extension
	 */
	private String extension;

	/**
	 * Corresponding mimetype
	 */
	private String mimeType;

	/**
	 * Matches WhatsApp image syntax
	 */
	@Builder.Default
	private Type mediaType = Type.STANDARD_IMAGE;

	/**
	 * Default constructor
	 */
	public MultiMediaImpl()
	{
		// Nothing to do here
	}
}
