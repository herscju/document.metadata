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
public class JmsLightBoxProducer
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(JmsLightBoxProducer.class);

	/**
	 * 
	 */
	private JmsTemplate jmsTemplate;

	/**
	 * 
	 */
	@Value("${application.activemq.queue.lightbox.name}")
	private String destination;

	/**
	 * Constructor
	 * 
	 * @param jmsTemplate
	 */
	public JmsLightBoxProducer(JmsTemplate jmsTemplate)
	{
		this.jmsTemplate = jmsTemplate;

		LOGGER.info("Class '{}' instantiated successfully.", JmsLightBoxProducer.class.getSimpleName());
	}

	/**
	 * @param message
	 */
	public void send(FileMessage message)
	{
		this.jmsTemplate.convertAndSend(this.destination, message);

		LOGGER.info("Sent message '{}' to destination '{}'.", message, this.destination);
	}
}
