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
 * https://github.com/eugenp/tutorials/tree/a587d2ec2ea54cb464c6f6e49be738282e582515/spring-websockets
 */
package info.hersche.document.metadata.configuration;

import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.datetime.DateFormatterRegistrar;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.number.NumberFormatAnnotationFormatterFactory;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import info.hersche.document.metadata.handler.WebSocketServerHandler;

/**
 * @author herscju
 */
@Configuration
@EnableWebSocket
public class WebSocketServerConfiguration implements WebSocketConfigurer
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketServerConfiguration.class);
	private static final String DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
	private static final String DATE_PATTERN = "yyyy-MM-dd";

	/**
	 * 
	 */
	public static final String CHAT = "/chat";
	public static final String PUSH = "/push";
	public static final String SOCKET = "/socket.io";

	/**
	 * Default constructor
	 */
	public WebSocketServerConfiguration()
	{
		// Nothing to do here

		LOGGER.info("Class '{}' instantiated successfully.", WebSocketServerConfiguration.class.getSimpleName());
	}

	/**
	 * @return
	 */
	@Bean
	public ServletServerContainerFactoryBean createWebSocketContainer()
	{
		Long idleTimeOut = 1200000L;
		Long timeOut = Long.valueOf(WebSocketSendToUserConfiguration.BUFFER_LIMIT);

		ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
		container.setMaxSessionIdleTimeout(idleTimeOut); // set the timeout to 2min
		container.setAsyncSendTimeout(timeOut);
		container.setMaxTextMessageBufferSize(WebSocketSendToUserConfiguration.BUFFER_LIMIT);
		container.setMaxBinaryMessageBufferSize(WebSocketSendToUserConfiguration.BUFFER_LIMIT);

		return container;
	}

	/**
	 * @param mapper
	 * @return
	 */
	@Bean
	public MappingJackson2MessageConverter mappingJackson2MessageConverter(ObjectMapper mapper,
			Jackson2ObjectMapperBuilderCustomizer customJackson2ObjectMapperBuilderCustomizer)
	{
		MappingJackson2MessageConverter jacksonMessageConverter = new MappingJackson2MessageConverter();
		jacksonMessageConverter.setObjectMapper(mapper);
		jacksonMessageConverter.setSerializedPayloadClass(String.class);
		jacksonMessageConverter.setStrictContentTypeMatch(true);

		return jacksonMessageConverter;
	}

	/**
	 * package com.hubersuhner.pdgw.impt.configuration -> CustomConfiguration
	 * 
	 * https://stackoverflow.com/a/25718495/2157268
	 * https://stackoverflow.com/a/60845316/2157268
	 * https://stackoverflow.com/a/6349488
	 * 
	 * 
	 * https://stackoverflow.com/a/54819859 &
	 * https://stackoverflow.com/a/27952529
	 * 
	 * @return
	 */
	@Bean
	public ObjectMapper objectMapper()
	{
		// Set up and configure object mapper
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
		mapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, true);
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		mapper.configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, true);

		mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
		mapper.configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, true);
		mapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);

		// Register modules
		mapper.registerModule(new JavaTimeModule());

		return mapper;
	}

	/**
	 * https://docs.spring.io/spring-framework/reference/core/validation/format-configuring-formatting-globaldatetimeformat.html
	 * 
	 * @return
	 */
	@Bean
	public FormattingConversionService conversionService()
	{
		// Use the DefaultFormattingConversionService but do not register defaults
		DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService(false);

		// Ensure @NumberFormat is still supported
		conversionService.addFormatterForFieldAnnotation(new NumberFormatAnnotationFormatterFactory());

		// Register JSR-310 date conversion with a specific global format
		DateTimeFormatterRegistrar dateTimeRegistrar = new DateTimeFormatterRegistrar();
		dateTimeRegistrar.setDateFormatter(DateTimeFormatter.ofPattern(DATETIME_PATTERN));
		dateTimeRegistrar.registerFormatters(conversionService);

		// Register date conversion with a specific global format
		DateFormatterRegistrar dateRegistrar = new DateFormatterRegistrar();
		dateRegistrar.setFormatter(new DateFormatter(DATE_PATTERN));
		dateRegistrar.registerFormatters(conversionService);

		return conversionService;
	}

	/**
	 *
	 */
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry)
	{
		registry.addHandler(this.webSocketHandler(), CHAT);
		registry.addHandler(this.webSocketHandler(), PUSH);
		registry.addHandler(this.webSocketHandler(), SOCKET).setAllowedOrigins("*");
	}

	/**
	 * @return
	 */
	@Bean
	public WebSocketHandler webSocketHandler()
	{
		return new WebSocketServerHandler();
	}

}
