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
package info.hersche.document.metadata.model;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.UUID;

import info.hersche.document.metadata.image.MultiMediaImpl.Type;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author herscju
 */
@Getter
@Setter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true, builderMethodName = "toBuilder")
public class OutputImageDataMessage implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1963730868863615829L;

	/**
	 * Original name
	 */
	@Builder.Default
	private UUID id = UUID.randomUUID();

	/**
	 * Current performed action
	 */
	private Action action;

	/**
	 * Raw content of image
	 */
	private String content;

	/**
	 * Original path
	 */
	private Path path;

	/**
	 * Original date
	 */
	private String date;

	/**
	 * Original name
	 */
	private String name;

	/**
	 * Original extension
	 */
	private String extendsion;

	/**
	 * Original mimetype
	 */
	private String mimetype;
	
	/**
	 * Matches WhatsApp image syntax
	 */
	@Builder.Default
	private Type imageType = Type.STANDARD_IMAGE;
	
	/**
	 * WhatsApp image date and time from name
	 */
	private String downloadDateTime;

	/**
	 * Updated metadata
	 */
	private OutputImageMetadataMessage metadata;

	/**
	 * Default constructor
	 */
	public OutputImageDataMessage()
	{
		// Nothing to do here
	}
}
