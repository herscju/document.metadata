/**
 * 
 */
package info.hersche.document.metadata.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import info.hersche.document.metadata.model.FileMessage;

/**
 * 
 */
@Service
@PropertySource("classpath:./custom.properties")
public class JmsDocumentProducer
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(JmsDocumentProducer.class);

	/**
	 * 
	 */
	private JmsTemplate template;

	/**
	 * 
	 */
	@Value("${application.activemq.queue.document.name}")
	private String destination;

	/**
	 * Constructor
	 * 
	 * @param template
	 */
	public JmsDocumentProducer(JmsTemplate template)
	{
		this.template = template;

		LOGGER.info("Class '{}' instantiated successfully.", JmsDocumentProducer.class.getSimpleName());
	}

	/**
	 * @param message
	 */
	public void send(FileMessage message)
	{
		this.template.convertAndSend(this.destination, message);

		LOGGER.info("Sent message '{}' to destination '{}'.", message, this.destination);
	}
}
