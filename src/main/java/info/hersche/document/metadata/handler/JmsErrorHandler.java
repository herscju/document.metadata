/**
 * 
 */
package info.hersche.document.metadata.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ErrorHandler;

/**
 * 
 */
@Service
public class JmsErrorHandler implements ErrorHandler
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(JmsErrorHandler.class);

	/**
	 * Default constructor
	 */
	public JmsErrorHandler()
	{
		// Nothing to do here
	}

	/**
	 *
	 */
	@Override
	public void handleError(Throwable throwable)
	{
		LOGGER.error("An error has been occured.", throwable);
	}

}
