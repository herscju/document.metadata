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
public class JmsStandardProducer
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(JmsStandardProducer.class);

	/**
	 * 
	 */
	private JmsTemplate jmsTemplate;

	/**
	 * 
	 */
	@Value("${application.activemq.queue.standard.name}")
	private String destination;

	/**
	 * Constructor
	 * 
	 * @param jmsTemplate
	 */
	public JmsStandardProducer(JmsTemplate jmsTemplate)
	{
		this.jmsTemplate = jmsTemplate;

		LOGGER.info("Class '{}' instantiated successfully.", JmsStandardProducer.class.getSimpleName());
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
