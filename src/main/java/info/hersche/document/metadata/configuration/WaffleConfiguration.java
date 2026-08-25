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
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import waffle.servlet.spi.BasicSecurityFilterProvider;
import waffle.servlet.spi.NegotiateSecurityFilterProvider;
import waffle.servlet.spi.SecurityFilterProvider;
import waffle.servlet.spi.SecurityFilterProviderCollection;
import waffle.spring.NegotiateSecurityFilter;
import waffle.spring.NegotiateSecurityFilterEntryPoint;
import waffle.windows.auth.PrincipalFormat;
import waffle.windows.auth.impl.WindowsAuthProviderImpl;

/**
 * @author herscju
 */
@Configuration
public class WaffleConfiguration
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(WaffleConfiguration.class);

	/**
	 * Default constructor
	 */
	public WaffleConfiguration()
	{
		// Nothing to do here

		LOGGER.info("Class '{}' instantiated successfully.", WaffleConfiguration.class.getSimpleName());
	}

	/**
	 * @param windowsAuthProvider
	 * @return
	 */
	@Bean
	public BasicSecurityFilterProvider basicSecurityFilterProvider(WindowsAuthProviderImpl windowsAuthProvider)
	{
		return new BasicSecurityFilterProvider(windowsAuthProvider);
	}

	/**
	 * @return
	 */
	@Bean
	public CustomAuthenticationProvider customAuthenticationProvider()
	{
		return new CustomAuthenticationProvider();
	}

	/**
	 * @param securityFilterProviderCollection
	 * @return
	 */
	@Bean
	public NegotiateSecurityFilterEntryPoint negotiateSecurityFilterEntryPoint(SecurityFilterProviderCollection securityFilterProviderCollection)
	{
		NegotiateSecurityFilterEntryPoint filterEntryPoint = new NegotiateSecurityFilterEntryPoint();
		filterEntryPoint.setProvider(securityFilterProviderCollection);

		return filterEntryPoint;
	}

	/**
	 * @param windowsAuthProvider
	 * @return
	 */
	@Bean
	public NegotiateSecurityFilterProvider negotiateSecurityFilterProvider(WindowsAuthProviderImpl windowsAuthProvider)
	{
		return new NegotiateSecurityFilterProvider(windowsAuthProvider);
	}

	/**
	 * @param securityFilterProviderCollection
	 * @return
	 */
	@Bean
	public NegotiateSecurityFilter waffleNegotiateSecurityFilter(SecurityFilterProviderCollection securityFilterProviderCollection)
	{
		NegotiateSecurityFilter filter = new NegotiateSecurityFilter();
		filter.setProvider(securityFilterProviderCollection);
		filter.setAllowGuestLogin(false);
		filter.setImpersonate(true);
		filter.setPrincipalFormatEnum(PrincipalFormat.FQN);
		filter.setRoleFormatEnum(PrincipalFormat.BOTH);

		return filter;
	}

	/**
	 * This is required for Spring Boot so it does not register the same filter twice
	 * 
	 * @param waffleNegotiateSecurityFilter
	 * @return
	 */
	@Bean
	public FilterRegistrationBean<NegotiateSecurityFilter> waffleNegotiateSecurityFilterRegistration(NegotiateSecurityFilter waffleNegotiateSecurityFilter)
	{
		FilterRegistrationBean<NegotiateSecurityFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(waffleNegotiateSecurityFilter);
		registrationBean.setEnabled(true);

		return registrationBean;
	}

	/**
	 * @param negotiateSecurityFilterProvider
	 * @param basicSecurityFilterProvider
	 * @return
	 */
	@Bean
	public SecurityFilterProviderCollection waffleSecurityFilterProviderCollection(NegotiateSecurityFilterProvider negotiateSecurityFilterProvider,
			BasicSecurityFilterProvider basicSecurityFilterProvider)
	{
		SecurityFilterProvider[] providers = { negotiateSecurityFilterProvider, basicSecurityFilterProvider };

		return new SecurityFilterProviderCollection(providers);
	}

	/**
	 * @return
	 */
	@Bean
	public WindowsAuthProviderImpl waffleWindowsAuthProvider()
	{
		return new WindowsAuthProviderImpl();
	}

}
