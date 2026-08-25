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
 * https://github.com/eugenp/tutorials/tree/a587d2ec2ea54cb464c6f6e49be738282e582515/spring-websockets
 */
package info.hersche.document.metadata.api;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author herscju
 */
@Controller
public class WebSocketSendToUserController
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketSendToUserController.class);

	/**
	 * JSON marshaling and unmarshaling
	 */
	@Autowired
	private ObjectMapper mapper;

	/**
	 * Default constructor
	 */
	public WebSocketSendToUserController()
	{
		// Nothing to do here

		LOGGER.info("Class '{}' instantiated successfully.", WebSocketSendToUserController.class.getSimpleName());
	}

	/**
	 * @param message
	 * @param principal
	 * @return
	 * @throws Exception
	 */
	@MessageMapping("/message")
	@SendToUser("/queue/reply")
	public String processMessageFromClient(@Payload String message, Principal principal) throws Exception
	{
		return this.mapper.writeValueAsString(message);
	}

	/**
	 * @param exception
	 * @return
	 */
	@MessageExceptionHandler
	@SendToUser("/queue/errors")
	public String handleException(Throwable exception)
	{
		return exception.getMessage();
	}
}
