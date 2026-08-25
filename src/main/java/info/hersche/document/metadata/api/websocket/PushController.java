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
package info.hersche.document.metadata.api.websocket;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import info.hersche.document.metadata.handler.ProcessorStompSessionHandler;
import info.hersche.document.metadata.model.ChatMessage;
import info.hersche.document.metadata.model.OutputMessage;

/**
 * @author herscju
 * 
 *         See 'https://www.baeldung.com/spring-websockets-send-message-to-user'
 */
@Controller
public class PushController
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(PushController.class);
	private static final String DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
	private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DATETIME_PATTERN);
	private static final String NAME = "Europe/Zurich";
	private static final ZoneId ZONE = ZoneId.of(NAME);

	/**
	 * Default constructor
	 */
	public PushController()
	{
		// Nothing to do here

		LOGGER.info("Class '{}' instantiated successfully.", PushController.class.getSimpleName());
	}

	/**
	 * 
	 * @param message
	 * @return
	 * @throws Exception
	 */
	@MessageMapping(ProcessorStompSessionHandler.PATH_CERTIFICATE)
	@SendTo("/topic/certificates")
	public OutputMessage certificates(@Payload ChatMessage message, @Header("simpSessionId") String sessionId)
	{
		LOGGER.trace("User session ID is '{}'.", sessionId);

		final String time = DATETIME_FORMATTER.format(OffsetDateTime.now(ZONE));

		return new OutputMessage(message.getFrom(), message.getText(), message.getData(), message.getUser(), time);
	}

	/**
	 * 
	 * @param message
	 * @return
	 * @throws Exception
	 */
	@MessageMapping(ProcessorStompSessionHandler.PATH_TIMELINE)
	@SendTo("/topic/timeline")
	public OutputMessage create(@Payload ChatMessage message, @Header("simpSessionId") String sessionId)
	{
		LOGGER.trace("User session ID is '{}'.", sessionId);

		final String time = DATETIME_FORMATTER.format(OffsetDateTime.now(ZONE));

		return new OutputMessage(message.getFrom(), message.getText(), message.getData(), message.getUser(), time);
	}

	/**
	 * 
	 * @param message
	 * @return
	 * @throws Exception
	 */
	@MessageMapping(ProcessorStompSessionHandler.PATH_LIGHTBOX)
	@SendTo("/topic/display")
	public OutputMessage display(@Payload ChatMessage message, @Header("simpSessionId") String sessionId)
	{
		LOGGER.trace("User session ID is '{}'.", sessionId);

		final String time = DATETIME_FORMATTER.format(OffsetDateTime.now(ZONE));

		return new OutputMessage(message.getFrom(), message.getText(), message.getData(), message.getUser(), time);
	}

	/**
	 * 
	 * @param message
	 * @return
	 * @throws Exception
	 */
	@MessageMapping(ProcessorStompSessionHandler.PATH_LDAP_OBJ)
	@SendTo("/topic/objects")
	public OutputMessage objects(@Payload ChatMessage message, @Header("simpSessionId") String sessionId)
	{
		LOGGER.trace("User session ID is '{}'.", sessionId);

		final String time = DATETIME_FORMATTER.format(OffsetDateTime.now(ZONE));

		return new OutputMessage(message.getFrom(), message.getText(), message.getData(), message.getUser(), time);
	}

	/**
	 * 
	 * @param message
	 * @return
	 * @throws Exception
	 */
	@MessageMapping(ProcessorStompSessionHandler.PATH_ALL2JPEG)
	@SendTo("/topic/all2jpg")
	public OutputMessage originate(@Payload ChatMessage message, @Header("simpSessionId") String sessionId)
	{
		LOGGER.trace("User session ID is '{}'.", sessionId);

		final String time = DATETIME_FORMATTER.format(OffsetDateTime.now(ZONE));

		return new OutputMessage(message.getFrom(), message.getText(), message.getData(), message.getUser(), time);
	}

	/**
	 * 
	 * @param message
	 * @return
	 * @throws Exception
	 */
	@MessageMapping(ProcessorStompSessionHandler.PATH_DOCUMENT)
	@SendTo("/topic/document")
	public OutputMessage parser(@Payload ChatMessage message, @Header("simpSessionId") String sessionId)
	{
		LOGGER.trace("User session ID is '{}'.", sessionId);

		final String time = DATETIME_FORMATTER.format(OffsetDateTime.now(ZONE));

		return new OutputMessage(message.getFrom(), message.getText(), message.getData(), message.getUser(), time);
	}

	/**
	 * 
	 * @param message
	 * @return
	 * @throws Exception
	 */
	@MessageMapping(ProcessorStompSessionHandler.PATH_DNG2JPEG)
	@SendTo("/topic/dng2jpg")
	public OutputMessage process(@Payload ChatMessage message, @Header("simpSessionId") String sessionId)
	{
		LOGGER.trace("User session ID is '{}'.", sessionId);

		final String time = DATETIME_FORMATTER.format(OffsetDateTime.now(ZONE));

		return new OutputMessage(message.getFrom(), message.getText(), message.getData(), message.getUser(), time);
	}

}
