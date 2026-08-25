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
 * https://github.com/eugenp/tutorials/tree/a587d2ec2ea54cb464c6f6e49be738282e582515/spring-websockets
 */
package info.hersche.document.metadata.model;

import info.hersche.document.metadata.api.websocket.MessageType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Jürg Hersche (herscju)
 */
@Getter
@Setter
@ToString
public class ChatMessage
{
	private MessageType type;
	private String from;
	private String text;
	private String user;
	private OutputDataMessage data;

	/**
	 * Default constructor
	 */
	public ChatMessage()
	{
		// Nothing to do here
	}
}
