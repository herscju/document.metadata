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

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import io.netty.handler.codec.http.HttpMethod;
import waffle.spring.NegotiateSecurityFilter;
import waffle.spring.NegotiateSecurityFilterEntryPoint;
import waffle.spring.WindowsAuthenticationProvider;

/**
 * @author herscju
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfiguration.class);
	private static final String ROOT = "/";
	private static final String LOGOUT = "/logout.html";
	private static final String[] INDEX = { "/index", "/index.html" };
	private static final String[] ERROR = { "/error", "/error-403", "/error-404", "/error-500" };
	private static final String[] LOGIN = { "/login" };
	private static final String[] SOCKET = { //
			SecurityConfiguration.updatePathToDirectory(WebSocketServerConfiguration.CHAT), //
			SecurityConfiguration.updatePathToDirectory(WebSocketServerConfiguration.PUSH) //
	};

	private static final String[] RESOURCES = { "favicon.ico", "/css/**", "/img/**", "/js/**", "/vendor/**" };

	private static final String[] API_V1 = { "/api/v1/**" };
	private static final String[] OPENAPI = { "/openapi.html", "/swagger-ui/**", "/v3/**" };
	private static final String[] DETAILS = { "/details/**" };
	private static final String[] ABOUT = { "/about/**", "/about.html" };
	private static final String[] BLOG = { "/blog/**" };
	private static final String[] CONTACT = { "/contact/**", "/contact.html" };
	private static final String[] DEVELOPMENT = { "/development/**" };
	private static final String[] PROJECTS = { "/projects/**" };
	private static final String[] SERVICES = { "/services/**" };

	private static final String[] PUBLIC_PATTERN = SecurityConfiguration.addAll(String.class, //
			SecurityConfiguration.addAll(String.class, LOGOUT), //
			INDEX, //
			ERROR, //
			LOGIN, //
			RESOURCES, //
			API_V1);

	private static final String[] RESTRICTED_PATTERN = SecurityConfiguration.addAll(String.class, //
			DETAILS, //
			SERVICES, //
			ABOUT, //
			PROJECTS, //
			BLOG, //
			DEVELOPMENT, //
			CONTACT, //
			SOCKET, //
			OPENAPI);
	/**
	 * 
	 */
	public static final String ANONYMUS = "Anonymus";

	/**
	 * 
	 */
	public static final String USER = "USER";
	public static final String DOMAIN_USER = "HERSCHE\\DOMAIN USERS";

	/**
	 * 
	 */
	public static final String[] USER_ROLE = { USER };
	public static final String[] DOMAIN_USER_ROLE = { DOMAIN_USER };

	/**
	 * https://www.simplexacode.ch/en/blog/2018/08/the-problem-with-creating-generic-arrays/
	 * 
	 * @param clazz
	 * @param arrays
	 * @return
	 */
	@SafeVarargs
	@SuppressWarnings("unchecked")
	private static <T> T[] addAll(Class<?> clazz, T... entries)
	{
		List<T> items = new LinkedList<>();
		for (T entry : entries)
		{
			items.add(entry);
		}

		T[] result = (T[]) Array.newInstance(clazz, items.size());
		System.arraycopy(items.stream().toArray(), 0, result, 0, result.length);

		return result;
	}

	/**
	 * https://www.simplexacode.ch/en/blog/2018/08/the-problem-with-creating-generic-arrays/
	 * 
	 * @param clazz
	 * @param arrays
	 * @return
	 */
	@SafeVarargs
	@SuppressWarnings("unchecked")
	private static <T> T[] addAll(Class<?> clazz, T[]... arrays)
	{
		List<T> items = new LinkedList<>();
		for (T[] array : arrays)
		{
			items.addAll(Stream.of(array).collect(Collectors.toList()));
		}

		T[] result = (T[]) Array.newInstance(clazz, items.size());
		System.arraycopy(items.stream().toArray(), 0, result, 0, result.length);

		return result;
	}

	/**
	 * @return
	 */
	private static String updatePathToDirectory(String path)
	{
		return new StringBuilder(path).append("/**").toString();
	}

	/**
	 * 
	 */
	private WindowsAuthenticationProvider authenticationProvider;
	private NegotiateSecurityFilter negotiateSecurityFilter;
	private NegotiateSecurityFilterEntryPoint authenticationEntryPoint;

	/**
	 * Constructor
	 * 
	 * Autowire constructor injects bean auto-configured by Starter.
	 *
	 * @param authenticationProvider
	 *            the provider
	 * @param negotiateFilter
	 *            the filter
	 * @param authenticationEntryPoint
	 *            the entry point
	 */
	public SecurityConfiguration(final WindowsAuthenticationProvider authenticationProvider, final NegotiateSecurityFilter negotiateSecurityFilter,
			final NegotiateSecurityFilterEntryPoint authenticationEntryPoint)
	{
		this.authenticationProvider = authenticationProvider;
		this.negotiateSecurityFilter = negotiateSecurityFilter;
		this.authenticationEntryPoint = authenticationEntryPoint;

		LOGGER.info("Class '{}' instantiated successfully.", SecurityConfiguration.class.getSimpleName());
	}

	/**
	 * See 'https://stackoverflow.com/a/45058887/6487382'
	 * 
	 * @return
	 */
	@Bean
	public HttpFirewall allowUrlEncodedSlashHttpFirewall()
	{
		DefaultHttpFirewall firewall = new DefaultHttpFirewall();
		firewall.setAllowUrlEncodedSlash(true);

		return firewall;
	}

	/**
	 * https://stackoverflow.com/a/74840094
	 * 
	 * @param http
	 * @return
	 * @throws Exception
	 */
	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception
	{
		AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class).authenticationProvider(this.authenticationProvider);
		builder.authenticationProvider(this.authenticationProvider);
		AuthenticationManager manager = builder.build();

		return manager;
	}

	/**
	 * See 'https://www.baeldung.com/spring-security-httpsecurity-vs-websecurity'
	 * 
	 * @return
	 */
	@Bean
	public WebSecurityCustomizer fireWall()
	{
		return (web) -> web.httpFirewall(this.allowUrlEncodedSlashHttpFirewall());
	}

	/**
	 * @return
	 */
	@Bean
	public CorsConfigurationSource corsConfigurationSource()
	{
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedHeaders(List.of(CorsConfiguration.ALL));
		configuration.setAllowedOrigins(List.of(CorsConfiguration.ALL));
		configuration.setAllowedMethods(Arrays.asList( //
				HttpMethod.GET.name(), //
				HttpMethod.POST.name(), //
				HttpMethod.DELETE.name(), //
				HttpMethod.PUT.name(), //
				HttpMethod.HEAD.name(), //
				HttpMethod.POST.name(), //
				HttpMethod.OPTIONS.name()));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);

		return source;
	}

	/**
	 * Implements negotiate authentication for path /api/v3/ (done by Waffle).
	 * Paths to OpenAPI and Actuator are also protected here by Waffle.
	 * Path /api/v1/ is permitted to everybody (public).
	 * 
	 * See 'https://stackoverflow.com/a/61253719/6487382'
	 * 
	 * @param http
	 * @param authenticationManager
	 * @return
	 * @throws Exception
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception
	{
		LOGGER.debug("Resources '{}' are accessible public.", Arrays.asList(PUBLIC_PATTERN));
		LOGGER.debug("Resources '{}' are accessible with role.", Arrays.asList(RESTRICTED_PATTERN));
		LOGGER.debug("Provider available '{}'.", this.authenticationProvider);
		LOGGER.debug("Security filter '{}'.", this.negotiateSecurityFilter);
		LOGGER.debug("Entry point '{}'.", this.authenticationEntryPoint);

		http.csrf(csrf -> csrf.disable()) //
				.cors(cors -> cors.configurationSource(this.corsConfigurationSource())) //
				.headers(headers -> headers.frameOptions(options -> options.sameOrigin())) //
				.headers(headers -> headers.httpStrictTransportSecurity(Customizer.withDefaults()).disable()) //
				.httpBasic(basic -> basic.authenticationEntryPoint(this.authenticationEntryPoint)) //
				.authenticationManager(authenticationManager) //
				.authenticationProvider(this.authenticationProvider) //
				.addFilterBefore(this.negotiateSecurityFilter, BasicAuthenticationFilter.class) //
				.exceptionHandling(handling -> handling.accessDeniedPage("/error-403").authenticationEntryPoint(this.authenticationEntryPoint)) //
				.formLogin(login -> login.disable()) //
				.logout(logout -> logout.logoutRequestMatcher(PathPatternRequestMatcher.withDefaults().matcher(LOGOUT)) //
						.logoutSuccessUrl(ROOT) //
						.invalidateHttpSession(true) //
						.deleteCookies("JSESSIONID") //
						.permitAll()) //
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //

				.authorizeHttpRequests(auth -> auth.requestMatchers(PathPatternRequestMatcher.withDefaults().matcher(ROOT)).permitAll()) //
				.authorizeHttpRequests(auth -> auth.requestMatchers(PUBLIC_PATTERN).permitAll()) //
				// .authorizeHttpRequests(auth -> auth.requestMatchers(RESTRICTED_PATTERN).hasAnyRole(USER)) //
				// .authorizeHttpRequests(auth -> auth.requestMatchers(RESTRICTED_PATTERN).hasAnyRole(DOMAIN_USER)) //
				// .authorizeHttpRequests(auth -> auth.requestMatchers(RESTRICTED_PATTERN).hasAnyRole(USER_ROLE)) //
				// .authorizeHttpRequests(auth -> auth.requestMatchers(RESTRICTED_PATTERN).hasAnyRole(DOMAIN_USER_ROLE)) //
				.authorizeHttpRequests(auth -> auth.requestMatchers(RESTRICTED_PATTERN).permitAll()) //
				.authorizeHttpRequests(auth -> auth.anyRequest().authenticated());

		return http.build();
	}
}
