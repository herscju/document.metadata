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

import org.apache.catalina.connector.Connector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Jürg Hersche (herscju)
 *
 */
@Configuration
public class ServerConfiguration
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ServerConfiguration.class);
	private static final String CONSTRAINT = "CONFIDENTIAL";
	private static final String PATTERN = "/*";
	private static final String SCHEME = "http";
	private static final int STD_PORT = 8080;
	private static final int SSL_PORT = 8443;

	private int stdPort = STD_PORT;
	private int sslPort = SSL_PORT;

	/**
	 * Constructor
	 * 
	 * @param stdPort
	 * @param sslPort
	 */
	public ServerConfiguration(@Value("${server.http.port}") int stdPort, @Value("${server.port}") int sslPort)
	{
		this.stdPort = stdPort;
		this.sslPort = sslPort;

		LOGGER.info("Class '{}' instantiated successfully.", ServerConfiguration.class.getSimpleName());
	}


	/**
	 * https://stackoverflow.com/a/49094956
	 * 
	 * @return
	 */
	@Bean
	public ServletWebServerFactory servletContainer(Connector connector)
	{
		TomcatServletWebServerFactory tomcat = new CustomTomcatServletWebServerFactory(PATTERN, CONSTRAINT);
		tomcat.addAdditionalTomcatConnectors(connector);

		return tomcat;
	}


	/**
	 * https://stackoverflow.com/a/49094956
	 * https://stackoverflow.com/a/52648698
	 * 
	 * @return
	 */
	@Bean
	public Connector connector()
	{
		Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
		connector.setScheme(SCHEME);
		connector.setPort(this.stdPort);
		connector.setSecure(false);
		connector.setRedirectPort(this.sslPort);
		connector.setXpoweredBy(true);

		LOGGER.info("HTTP connector: {} (SSL/TLS redirects from port '{}' to '{}').", connector, this.stdPort, this.sslPort);

		return connector;
	}

}
