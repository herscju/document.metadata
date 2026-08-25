/**
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
