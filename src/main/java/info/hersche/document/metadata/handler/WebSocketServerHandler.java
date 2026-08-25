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
package info.hersche.document.metadata.handler;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.SubProtocolCapable;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.HtmlUtils;

/**
 * 
 */
public class WebSocketServerHandler extends TextWebSocketHandler implements SubProtocolCapable
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketServerHandler.class);

	/**
	 * 
	 */
	private final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

	/**
	 * Default constructor
	 */
	public WebSocketServerHandler()
	{
		// Nothing to do here
	}

	/**
	 * @throws Exception
	 *
	 */
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception
	{
		LOGGER.info("Server connection closed: '{}'.", status);
		this.sessions.remove(session);

		// See 'https://stackoverflow.com/a/68814904/6487382'
		super.afterConnectionClosed(session, status);
	}

	/**
	 *
	 */
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception
	{
		LOGGER.info("Server connection opened.");
		this.sessions.add(session);

		TextMessage message = new TextMessage("one-time message from server");
		LOGGER.info("Server sends: '{}'.", message);

		// See 'https://stackoverflow.com/a/29272777/6487382'
		synchronized (session)
		{
			session.sendMessage(message);
		}

		// See 'https://stackoverflow.com/a/68814904/6487382'
		super.afterConnectionEstablished(session);
	}

	/**
	 *
	 */
	@Override
	public List<String> getSubProtocols()
	{
		return Collections.singletonList("subprotocol.demo.websocket");
	}

	/**
	 *
	 */
	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception
	{
		String request = message.getPayload();
		LOGGER.info("Server received: '{}'.", request);

		String response = String.format("Response from server to '%s'", HtmlUtils.htmlEscape(request));
		LOGGER.info("Server sends: '{}'.", response);

		// See 'https://stackoverflow.com/a/29272777/6487382'
		synchronized (session)
		{
			session.sendMessage(new TextMessage(response));
		}
	}

	/**
	 *
	 */
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception)
	{
		LOGGER.info("Server transport error: '{}'.", exception.getMessage());
	}

	/**
	 * @throws IOException
	 */
	@Scheduled(fixedRate = 10000)
	void sendPeriodicMessages() throws IOException
	{
		for (WebSocketSession session : this.sessions)
		{
			if (session.isOpen())
			{
				StringBuilder broadcast = new StringBuilder("Server periodic message ").append(LocalTime.now()).append("");
				LOGGER.info("Server sends: '{}'.", broadcast);

				// See 'https://stackoverflow.com/a/29272777/6487382'
				synchronized (session)
				{
					session.sendMessage(new TextMessage(broadcast));
				}
			}
		}
	}
}
