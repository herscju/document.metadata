/**
 * 
 */
package info.hersche.document.metadata.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import com.fasterxml.jackson.databind.ObjectMapper;

import info.hersche.document.metadata.handler.JmsErrorHandler;
import info.hersche.document.metadata.model.FileMessage;

/**
 * @author herscju
 */
@EnableJms
@Configuration
@PropertySource("classpath:./custom.properties")
public class JmsConfiguration
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(JmsConfiguration.class);

	/**
	 * 
	 */
	private ObjectMapper mapper;

	/**
	 * 
	 */
	@Value("${spring.activemq.broker-url}")
	private String brokerUrl;

	/**
	 * Constructor
	 * 
	 * @param mapper
	 */
	public JmsConfiguration(ObjectMapper mapper)
	{
		this.mapper = mapper;

		LOGGER.info("Class '{}' instantiated successfully.", JmsConfiguration.class.getSimpleName());
	}

	/**
	 * Active MQ Embedded Broker configuration
	 * 
	 * @return
	 * @throws Exception
	 */
	@Bean
	public BrokerService broker() throws Exception
	{

		BrokerService broker = new BrokerService();
		broker.setPersistent(false);
		broker.setUseJmx(true);
		broker.addConnector(this.brokerUrl);
		broker.start();

		return broker;
	}

	/**
	 * @return
	 */
	@Bean
	public PooledConnectionFactory connectionFactory()
	{
		PooledConnectionFactory factory = new PooledConnectionFactory(this.brokerUrl);

		return factory;

	}

	/**
	 * Consumer configuration
	 * 
	 * @return
	 */
	@Bean
	public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(JmsErrorHandler errorHandler)
	{
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(this.connectionFactory());
		factory.setMessageConverter(this.messageConverter());
		factory.setErrorHandler(errorHandler);

		return factory;

	}

	/**
	 * Producer configuration
	 * 
	 * @return
	 */
	@Bean
	public JmsTemplate jmsTemplate()
	{
		JmsTemplate template = new JmsTemplate();
		template.setConnectionFactory(this.connectionFactory());
		template.setMessageConverter(this.messageConverter());

		return template;
	}

	/**
	 * Consumer configuration
	 * 
	 * @return
	 */

	/**
	 * Message converter configuration
	 * 
	 * See 'https://www.baeldung.com/spring-jms', 'https://www.blackbox.ai/chat/RD14LG5', 'https://www.pieterjd.be/posts/jms-property-name-must-not-be-null/'
	 * and 'https://ichihedge.wordpress.com/2016/05/23/correctly-configuring-mappingjackson2messageconverter-for-messaging-via-json/'
	 * 
	 * @return
	 */
	@Bean
	public MessageConverter messageConverter()
	{
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter(this.mapper);
		converter.setTargetType(MessageType.BYTES);
		converter.setTypeIdPropertyName(FileMessage.class.getName());

		Map<String, Class<?>> typeIdMappings = new HashMap<>();
		typeIdMappings.put(FileMessage.class.getName(), FileMessage.class);
		converter.setTypeIdMappings(typeIdMappings);

		return converter;
	}

}
