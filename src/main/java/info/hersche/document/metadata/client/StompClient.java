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
 */
package info.hersche.document.metadata.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.ResourceUtils;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import info.hersche.document.metadata.configuration.HostsProperties;
import info.hersche.document.metadata.configuration.TlsEncryptionProperties;
import info.hersche.document.metadata.model.ChatMessage;
import info.hersche.document.metadata.storage.MultiMedia;

/**
 * @author herscju
 */
public class StompClient
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(StompClient.class);
	private static final String KEYSTORE = "classpath:wildcard.hersche.name.2024.pfx";
	private static final String PASSWORD = "BsXu+WPnc-P$Ge66";
	private static final String PROTOCOL = "wss";
	private static final String TLS = "TLS";
	private static final String URL = "wss://artemis.hersche.name:8443/push";

	/**
	 * 
	 */
	private TlsEncryptionProperties encryptionProperties;
	private HostsProperties hostsProperties;
	private String destination;
	private String subscription;
	private String path;
	private MultiMedia multiMedia;
	private StompSessionHandler handler;

	/**
	 * 
	 */
	private StompSession session;

	/**
	 * Constructor
	 * 
	 * @param cacheManager
	 * 
	 * @param encryptionProperties
	 * @param hostsProperties
	 * @param subscription
	 * @param path
	 * @param multiMedia
	 * @param action
	 * @param handler
	 */
	public StompClient(TlsEncryptionProperties encryptionProperties, //
			HostsProperties hostsProperties, //
			String destination, //
			String subscription, //
			String path, //
			MultiMedia multiMedia, //
			StompSessionHandler handler)
	{
		this.encryptionProperties = encryptionProperties;
		this.hostsProperties = hostsProperties;
		this.destination = destination;
		this.subscription = subscription;
		this.path = path;
		this.multiMedia = multiMedia;
		this.handler = handler;

		LOGGER.info("Class '{}' instantiated successfully.", StompClient.class.getSimpleName());
	}

	/**
	 * @return
	 */
	private String buildUrl()
	{
		return new StringBuilder(PROTOCOL) //
				.append("://") //
				.append(this.hostsProperties.name()) //
				.append(":") //
				.append(this.hostsProperties.port()) //
				.append("/") //
				.append(this.path) //
				.toString();
	}

	/**
	 * 
	 */
	public boolean connect()
	{
		try
		{
			final String location = (this.encryptionProperties != null) ? this.encryptionProperties.keyStore() : KEYSTORE;
			final String password = (this.encryptionProperties != null) ? this.encryptionProperties.keyStorePassword() : PASSWORD;
			final char[] secret = password.toCharArray();

			final File file = ResourceUtils.getFile(location);
			LOGGER.debug("SSL/TLS trust store file is '{}' with password '*****'. Has been loaded from location '{}'.", file, location);

			// Load the trust store
			final KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(new FileInputStream(file), secret);
			final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			trustManagerFactory.init(trustStore);

			// Load the key store
			final KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			keyStore.load(new FileInputStream(file), secret);
			final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			keyManagerFactory.init(keyStore, secret);

			// Get SSL/TLS context
			final SSLContext sslContext = SSLContext.getInstance(TLS);
			sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

			// Configure WebSocket client
			final StandardWebSocketClient webSocketClient = new StandardWebSocketClient();
			webSocketClient.setUserProperties(Collections.singletonMap("org.apache.tomcat.websocket.SSL_CONTEXT", sslContext));

			final WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
			stompClient.setMessageConverter(new MappingJackson2MessageConverter());

			final ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
			taskScheduler.afterPropertiesSet();
			stompClient.setTaskScheduler(taskScheduler);
			stompClient.setDefaultHeartbeat(new long[] { 10000, 10000 });

			final String url = (this.hostsProperties != null && this.path != null) ? this.buildUrl() : URL;
			final CompletableFuture<StompSession> connection = stompClient.connectAsync(url, new WebSocketHttpHeaders(), this.handler);
			this.session = connection.get();
			this.session.setAutoReceipt(true);
			LOGGER.trace("New STOMP session established: '{}'.", this.session.getSessionId());

			return true;
		}
		catch (InterruptedException | ExecutionException | KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException
				| KeyManagementException | UnrecoverableKeyException e)
		{
			LOGGER.error("An error has been occured.", e);

			return false;
		}
	}

	/**
	 * @param message
	 */
	public void sendMessage(String message)
	{
		LOGGER.trace("Send message '{}'.", message);
		ChatMessage chatMessage = new ChatMessage();
		chatMessage.setFrom(this.multiMedia.getName());
		chatMessage.setText(message);

		this.session.send(this.subscription, chatMessage);
		LOGGER.trace("Message sent to websocket server URL '{}' ('{}').", this.subscription, this.destination);
	}

}
