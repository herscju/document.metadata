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
package info.hersche.document.metadata.configuration;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import info.hersche.document.metadata.filter.ExceptionHandlerFilter;
import info.hersche.document.metadata.model.PageText;

/**
 * @author Jürg Hersche (herscju)
 */
@Configuration
public class CustomConfiguration
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomConfiguration.class);
	private static final Charset UTF8 = StandardCharsets.UTF_8;

	/**
	 * 
	 */
	private ObjectMapper mapper;

	/**
	 * Constructor
	 */
	public CustomConfiguration(ObjectMapper mapper)
	{
		this.mapper = mapper;

		LOGGER.info("Class '{}' instantiated successfully.", CustomConfiguration.class.getSimpleName());
	}

	/**
	 * See 'https://stackoverflow.com/a/28214811'
	 * 
	 * @return
	 */
	@Bean
	public FilterRegistrationBean<CharacterEncodingFilter> charEncodingFilterRegistrationBean()
	{
		final CharacterEncodingFilter filter = new CharacterEncodingFilter();
		filter.setForceEncoding(true);
		filter.setEncoding(UTF8.toString());

		final FilterRegistrationBean<CharacterEncodingFilter> registration = new FilterRegistrationBean<>();
		registration.setFilter(filter);

		LOGGER.trace("Filter '{}' configured and registered. ({})", CharacterEncodingFilter.class.getSimpleName(), filter);

		return registration;
	}

	/**
	 * See 'https://stackoverflow.com/a/77000204/6487382'
	 * 
	 * @return
	 */
	@Bean
	public FilterRegistrationBean<ExceptionHandlerFilter> exceptionHandlerFilterRegistrationBean()
	{
		final ExceptionHandlerFilter filter = new ExceptionHandlerFilter(this.mapper);

		final FilterRegistrationBean<ExceptionHandlerFilter> registration = new FilterRegistrationBean<>();
		registration.setFilter(filter);
		registration.setEnabled(false);

		LOGGER.trace("Filter '{}' configured and registered. ({})", ExceptionHandlerFilter.class.getSimpleName(), filter);

		return registration;
	}

	/**
	 * @return
	 */
	@Bean
	public MessageSource messageSource()
	{
		final ResourceBundleMessageSource source = new ResourceBundleMessageSource();
		source.setBasenames("messages");
		source.setCacheSeconds(10); // reload messages every 10 seconds
		source.setDefaultEncoding("UTF-8");
		source.setFallbackToSystemLocale(true);

		return source;
	}

	/**
	 * @return
	 */
	@Bean
	public PageText pageText()
	{
		MessageSource messageSource = this.messageSource();

		return new PageText(messageSource);
	}
}
