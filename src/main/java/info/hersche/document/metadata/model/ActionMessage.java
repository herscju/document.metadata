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

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import info.hersche.document.metadata.configuration.SecurityConfiguration;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Jürg Hersche
 */
@Component
@ToString
@EqualsAndHashCode
@JsonPropertyOrder({ "code", "status", "reason", "user", "message", "payload" })
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true, builderMethodName = "toBuilder")
public class ActionMessage implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7499054100275754723L;

	/**
	 * Current HTTP status code
	 */
	@Getter
	@Setter
	private HttpStatus code;

	/**
	 * Current HTTP status
	 */
	@Getter
	@Setter
	private int status;

	/**
	 * Current status reason
	 */
	@Getter
	@Setter
	private String reason;

	/**
	 * Current user
	 */
	@Getter
	@Setter
	@Builder.Default
	private String user = SecurityConfiguration.ANONYMUS;

	/**
	 * Current status message
	 */
	@Getter
	@Setter
	private String message;

	/**
	 * Current payload
	 */
	@Getter
	@Setter
	@Builder.Default
	private transient Object payload = new Object();

	/**
	 * Default constructor
	 */
	public ActionMessage()
	{
		// Nothing to do here
	}
}
