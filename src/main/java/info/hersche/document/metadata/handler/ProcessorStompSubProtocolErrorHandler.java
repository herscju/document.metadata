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
 */
package info.hersche.document.metadata.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

/**
 * 
 */
public class ProcessorStompSubProtocolErrorHandler extends StompSubProtocolErrorHandler
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessorStompSubProtocolErrorHandler.class);

	public ProcessorStompSubProtocolErrorHandler()
	{
		// Nothing to do here

		LOGGER.info("Class '{}' instantiated successfully.", ProcessorStompSubProtocolErrorHandler.class.getSimpleName());
	}

	/**
	 *
	 */
	@Override
	public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable exception)
	{
		Throwable throwable = exception;
		LOGGER.error("An exception has been occured: '{}'.", throwable.getMessage());

		if (throwable instanceof MessageDeliveryException)
		{
			throwable = throwable.getCause();
		}

		return super.handleClientMessageProcessingError(clientMessage, exception);
	}
}
