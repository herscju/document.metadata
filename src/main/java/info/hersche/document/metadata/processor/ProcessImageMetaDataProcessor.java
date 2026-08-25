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
 * https://stackoverflow.com/a/60397791
 */
package info.hersche.document.metadata.processor;

import javax.cache.CacheManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import info.hersche.document.metadata.client.StompClient;
import info.hersche.document.metadata.configuration.HostsProperties;
import info.hersche.document.metadata.configuration.TlsEncryptionProperties;
import info.hersche.document.metadata.handler.ProcessorStompSessionHandler;
import info.hersche.document.metadata.model.Action;
import info.hersche.document.metadata.model.Counter;
import info.hersche.document.metadata.storage.MultiMedia;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 */
@Component
@PropertySource("classpath:./custom.properties")
public class ProcessImageMetaDataProcessor implements MetaDataProcessable
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessImageMetaDataProcessor.class);

	/**
	 * 
	 */
	@NotNull
	@Value("${application.ehcache.name}")
	private String cacheName;
	
	/**
	 * 
	 */
	private ObjectMapper mapper;

	/**
	 * 
	 */
	@Setter
	@NotNull
	private CacheManager cacheManager;

	/**
	 * 
	 */
	@Setter
	@NotNull
	private TlsEncryptionProperties encryptionProperties;

	/**
	 * 
	 */
	@Setter
	@NotNull
	private HostsProperties hostsProperties;

	/**
	 * 
	 */
	@Setter
	@NotNull
	private String destination;

	/**
	 * 
	 */
	@Setter
	@NotNull
	private String subscription;

	/**
	 * 
	 */
	@Setter
	@NotNull
	private String path;

	/**
	 * 
	 */
	@Setter
	@NotNull
	private Counter counter;

	/**
	 * 
	 */
	@Getter
	private Boolean status = null;

	/**
	 * Default constructor
	 */
	public ProcessImageMetaDataProcessor(ObjectMapper mapper, CacheManager cacheManager)
	{
		this.mapper = mapper;
		this.cacheManager = cacheManager;
	}

	/**
	 * @return
	 *
	 */
	@Override
	public byte[] process(MultiMedia multiMedia, Action action)
	{
		final StompSessionHandler handler = new ProcessorStompSessionHandler( //
				this.mapper, //
				this.counter, //
				this.cacheName, //
				this.cacheManager, //
				this.destination, //
				this.subscription, //
				action, //
				multiMedia);
		final StompClient client = new StompClient( //
				this.encryptionProperties, //
				this.hostsProperties, //
				this.destination, //
				this.subscription, //
				this.path, //
				multiMedia, //
				handler);

		this.status = Boolean.valueOf(client.connect());

		String message = this.status.booleanValue() ? "Connected successfully to STOMP client." : "Not connected to STOMP client.";
		client.sendMessage(message);

		return new byte[0];
	}

}
