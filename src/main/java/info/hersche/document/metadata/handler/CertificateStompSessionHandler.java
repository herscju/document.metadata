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
package info.hersche.document.metadata.handler;

import java.lang.reflect.Type;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import info.hersche.document.metadata.model.OutputMessage;
import info.hersche.document.metadata.storage.MultiMedia;

/**
 * @author Jürg Hersche (herscju)
 */
public class CertificateStompSessionHandler extends StompSessionHandlerAdapter
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CertificateStompSessionHandler.class);
	private static final String DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";
	private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DATETIME_PATTERN);
	private static final String NAME = "Europe/Zurich";
	private static final ZoneId ZONE = ZoneId.of(NAME);

	private String destination;
	private String subscription;
	private MultiMedia multiMedia;

	/**
	 * Constructor
	 * 
	 * @param subscription
	 * @param destination
	 * @param multiMedia
	 */
	public CertificateStompSessionHandler(String destination, String subscription, MultiMedia multiMedia)
	{
		this.destination = destination;
		this.subscription = subscription;
		this.multiMedia = multiMedia;
	}

	/**
	 *
	 */
	@Override
	public void afterConnected(StompSession session, StompHeaders connectedHeaders)
	{
		LOGGER.trace("New session established: '{}'.", session.getSessionId());

		session.subscribe(this.subscription, this);
		LOGGER.info("Subscribed to '{}'.", this.subscription);

		session.setAutoReceipt(true);
		
		String time = DATETIME_FORMATTER.format(OffsetDateTime.now(ZONE));
		OutputMessage message = OutputMessage.toBuilder() //
				.from(this.multiMedia.getName()) //
				.text("» Going to download available certificates...") //
				.time(time) //
				.build();

		session.send(this.destination, message);
	}

	/**
	 *
	 */
	@Override
	public Type getPayloadType(StompHeaders headers)
	{
		return OutputMessage.class;
	}
}
