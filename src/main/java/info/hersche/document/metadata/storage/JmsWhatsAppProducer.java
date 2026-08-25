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
public class JmsWhatsAppProducer
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(JmsWhatsAppProducer.class);

	/**
	 * 
	 */
	private JmsTemplate jmsTemplate;

	/**
	 * 
	 */
	@Value("${application.activemq.queue.whatsapp.name}")
	private String destination;

	/**
	 * Constructor
	 * 
	 * @param jmsTemplate
	 */
	public JmsWhatsAppProducer(JmsTemplate jmsTemplate)
	{
		this.jmsTemplate = jmsTemplate;

		LOGGER.info("Class '{}' instantiated successfully.", JmsWhatsAppProducer.class.getSimpleName());
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
