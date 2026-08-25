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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.ldap.repository.config.EnableLdapRepositories;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

/**
 * @author Jürg Hersche (herscju)
 */
@Configuration
@EnableLdapRepositories
public class LdapConfiguration
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(LdapConfiguration.class);

	/**
	 * 
	 */
	private Environment environment;

	/**
	 * Default constructor
	 */
	public LdapConfiguration(Environment environment)
	{
		this.environment = environment;

		LOGGER.info("Class '{}' instantiated successfully.", LdapConfiguration.class.getSimpleName());
	}

	/**
	 * @return
	 */
	@Bean
	public LdapContextSource contextSource()
	{
		LdapContextSource contextSource = new LdapContextSource();
		contextSource.setUrl(this.environment.getProperty("spring.ldap.urls"));
		contextSource.setBase(this.environment.getRequiredProperty("spring.ldap.base"));
		contextSource.setUserDn(this.environment.getRequiredProperty("spring.ldap.username"));
		contextSource.setPassword(this.environment.getRequiredProperty("spring.ldap.password"));
		contextSource.afterPropertiesSet();

		return contextSource;
	}

	/**
	 * @return
	 */
	@Bean
	public LdapTemplate ldapTemplate(LdapContextSource contextSource)
	{
		return new LdapTemplate(contextSource);
	}
}
