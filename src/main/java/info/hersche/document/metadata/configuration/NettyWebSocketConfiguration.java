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

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.hc.core5.http.ssl.TLS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;

import info.hersche.document.metadata.listener.SocketIOBinaryEventListener;
import info.hersche.document.metadata.listener.SocketIOConnectionListener;
import info.hersche.document.metadata.listener.SocketIODisconnectionListener;
import info.hersche.document.metadata.listener.SocketIOTextEventListener;
import info.hersche.document.metadata.storage.MultiMedia;
import jakarta.annotation.PreDestroy;

/**
 * @author Jürg Hersche (herscju)
 *
 */
@Component
@PropertySource("classpath:./custom.properties")
@org.springframework.context.annotation.Configuration
public class NettyWebSocketConfiguration
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(NettyWebSocketConfiguration.class);
	private static final String HOST = "localhost";
	private static final Integer PORT = Integer.valueOf(8081);
	private static final String KEYSTORE = "classpath:wildcard.hersche.name.2024.pfx";
	private static final String PASSWORD = "BsXu+WPnc-P$Ge66";

	/**
	 * 
	 */
	public static final String PROTOCOL = "https";
	public static final String EVENT_SENDTEXT = "EventSendText";
	public static final String CHANNEL_TEXT = "ChannelText";
	public static final String EVENT_SENDBINARY = "EventSendBinary";
	public static final String CHANNEL_BINARY = "ChannelBinary";
	public static final String ROOM_BINARY = "RoomBinary";

	/**
	 * 
	 */
	private SocketIOServer server;

	/**
	 * 
	 */
	private ResourceLoader resourceLoader;

	@Value("${server.address}")
	private String host = HOST;

	@Value("${server.port}")
	private Integer port = PORT;

	@Value("${server.ssl.trust-store}")
	private String trustStoreLocation;

	@Value("${server.ssl.trust-store-password}")
	private String trustStorePassword;

	@Value("${server.ssl.trust-store-type}")
	private String trustStoreType;

	/**
	 * Default constructor
	 */
	public NettyWebSocketConfiguration(ResourceLoader resourceLoader)
	{
		this.resourceLoader = resourceLoader;

		LOGGER.info("Class '{}' instantiated successfully.", NettyWebSocketConfiguration.class.getSimpleName());
	}

	/**
	 * @return
	 * @throws IOException
	 */
	@Bean
	public Configuration configuration() throws IOException
	{
		this.port = Integer.valueOf(this.port.intValue() + 1);

		if (this.host.equals(HOST))
		{
			try
			{
				InetAddress inetAddress = InetAddress.getLocalHost();
				this.host = inetAddress.getHostAddress();
			}
			catch (UnknownHostException e)
			{
				LOGGER.error("An error has been ouccured.", e);
			}
		}

		String location = (this.trustStoreLocation != null) ? this.trustStoreLocation : KEYSTORE;
		String password = (this.trustStorePassword != null) ? this.trustStorePassword : PASSWORD;

		Resource certificate = this.resourceLoader.getResource(location);

		Configuration configuration = new Configuration();
		configuration.setHostname(this.host);
		configuration.setPort(this.port);
		configuration.setEnableCors(true);
		configuration.setKeyStoreFormat(this.trustStoreType);
		configuration.setKeyStore(certificate.getInputStream());
		configuration.setKeyStorePassword(password);
		LOGGER.info("--------- {}", TLS.V_1_0.getVersion().toString());
		LOGGER.info("--------- {}", TLS.V_1_1.getVersion().toString());
		LOGGER.info("--------- {}", TLS.V_1_2.getVersion().toString());
		LOGGER.info("--------- {}", TLS.V_1_3.getVersion().toString());
//		TLSv1
//		configuration.setSSLProtocol(TLS.V_1_0.getVersion().toString());

		return configuration;
	}

	/**
	 * @param configuration
	 * @return
	 */
	@Bean
	public SocketIOServer socketIOServer(Configuration configuration)
	{
		this.server = new SocketIOServer(configuration);
		this.server.addConnectListener(new SocketIOConnectionListener());
		this.server.addDisconnectListener(new SocketIODisconnectionListener());
		this.server.addEventListener(EVENT_SENDTEXT, String.class, new SocketIOTextEventListener<String>(this.server, CHANNEL_TEXT));
		this.server.addEventListener(EVENT_SENDBINARY, MultiMedia.class, new SocketIOBinaryEventListener<MultiMedia>(this.server, CHANNEL_BINARY));

		LOGGER.trace("Start WebSocket server on '{}:{}'.", this.host, this.port);
		this.server.start();

		return this.server;
	}

	/**
	 * See 'https://medium.com/geekculture/socket-io-with-springboot-application-5a59358f7d48'
	 */
	@PreDestroy
	public void stopSocketIOServer()
	{
		LOGGER.trace("Stop WebSocket server on '{}:{}'.", this.host, this.port);
		this.server.stop();
	}
}
