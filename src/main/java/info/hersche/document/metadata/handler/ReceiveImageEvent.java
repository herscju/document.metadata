/**
 * 
 */
package info.hersche.document.metadata.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.hersche.document.metadata.storage.MultiMedia;
import lombok.Getter;

/**
 * @author herscju
 */
public class ReceiveImageEvent
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ReceiveImageEvent.class);

	/**
	 * 
	 */
	@Getter
	private MultiMedia multiMedia;

	@Getter
	private String destination;

	@Getter
	private String subscription;

	/**
	 * Constructor
	 * 
	 * @param multiMedia
	 */
	public ReceiveImageEvent(MultiMedia multiMedia)
	{
		this(multiMedia, ProcessorStompSessionHandler.DEST_ALL2JPEG, ProcessorStompSessionHandler.SUB_ALL2JPEG);
	}

	/**
	 * Constructor
	 * 
	 * @param multiMedia
	 * @param destination
	 * @param subscription
	 */
	public ReceiveImageEvent(MultiMedia multiMedia, String destination, String subscription)
	{
		this.multiMedia = multiMedia;
		this.destination = destination;
		this.subscription = subscription;

		LOGGER.info("Class '{}' instantiated successfully.", ReceiveImageEvent.class.getSimpleName());
	}

}
