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

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import info.hersche.document.metadata.configuration.SecurityConfiguration;
import info.hersche.document.metadata.image.MultiMediaImpl.Type;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Jürg Hersche (herscju)
 */
@ToString
@EqualsAndHashCode
@JsonPropertyOrder({ "user", "name", "size", "type", "payload" })
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true, builderMethodName = "toBuilder")
public class FileMessage implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2417243679889798649L;

	/**
	 * Current user
	 */
	@Getter
	@Setter
	@Builder.Default
	private String user = SecurityConfiguration.ANONYMUS;

	/**
	 * Current full file name
	 */
	@Getter
	@Setter
	private String name;

	/**
	 * Current file size
	 */
	@Getter
	@Setter
	private Long size;

	/**
	 * Current file type
	 */
	@Getter
	@Setter
	private String type;
	
	/**
	 * Matches WhatsApp image syntax
	 */
	@Getter
	@Setter
	@Builder.Default
	private Type imageType = Type.STANDARD_IMAGE;

	/**
	 * Current payload
	 */
	@Getter
	@Setter
	@Builder.Default
	@ToString.Exclude
	private transient byte[] payload = new byte[0];

	/**
	 * Default constructor
	 */
	public FileMessage()
	{
		// Nothing to do here
	}
}
