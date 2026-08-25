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
package info.hersche.document.metadata.api;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import javax.cache.CacheManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import info.hersche.document.metadata.certificate.CertificateLoadable;
import info.hersche.document.metadata.certificate.CertificateLoader;
import info.hersche.document.metadata.client.StompClient;
import info.hersche.document.metadata.configuration.ApiKeyProperties;
import info.hersche.document.metadata.configuration.HostsProperties;
import info.hersche.document.metadata.configuration.TlsEncryptionProperties;
import info.hersche.document.metadata.configuration.WebSocketServerConfiguration;
import info.hersche.document.metadata.handler.CertificateStompSessionHandler;
import info.hersche.document.metadata.handler.ProcessorStompSessionHandler;
import info.hersche.document.metadata.image.MultiMediaImpl;
import info.hersche.document.metadata.storage.MultiMedia;
import info.hersche.document.metadata.util.PortUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * @author Jürg Hersche (herscju) 
 */
@Configuration
@RestController
@RequestMapping(value = "/api/v1/certificates")
@PropertySource("classpath:./custom.properties")
@EnableConfigurationProperties(ApiKeyProperties.class)
public class CertificateController extends AbstractController
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CertificateController.class);

	/**
	 * 
	 */
	private final ResourceLoader resourceLoader;

	/**
	 * 
	 */
	private final CacheManager cacheManager;

	/**
	 * 
	 */
	private final TlsEncryptionProperties encryptionProperties;
	
	/**
	 * 
	 */
	private final HostsProperties hostsProperties;

	/**
	 * 
	 */
	private final ApiKeyProperties keys;

	/**
	 * 
	 */
	@NotNull
	@Value("${server.address}")
	private String host;

	@NotNull
	@Value("${server.port}")
	private Integer port;

	@NotNull
	@Value("${application.ehcache.name}")
	private String cacheName;

	@Value("${server.ssl.key-store}")
	private String keyStoreLocation;

	@Value("${server.ssl.key-store-password}")
	private String keyStorePassword;

	/**
	 * Constructor
	 */
	public CertificateController(ResourceLoader resourceLoader, CacheManager cacheManager, TlsEncryptionProperties encryptionProperties,
			HostsProperties hostsProperties, ApiKeyProperties keys)
	{
		this.resourceLoader = resourceLoader;
		this.cacheManager = cacheManager;
		this.encryptionProperties = encryptionProperties;
		this.hostsProperties = hostsProperties;
		this.keys = keys;

		LOGGER.info("Class '{}' instantiated successfully.", ImageController.class.getSimpleName());
	}

	/**
	 * @param userAgent
	 * @param arg0
	 * @param arg1
	 * @param authentication
	 * @param redirectAttributes
	 * @return
	 */
	@PutMapping(value = "/certificate/{host}/{port}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.WILDCARD })
	public ResponseEntity<StreamingResponseBody> putHandleCertificateLoad( //
			@RequestHeader(value = "User-Agent") final String userAgent, //
			@PathVariable("host") final String arg0, //
			@PathVariable("port") final Integer arg1, //
			final HttpServletRequest request, //
			final Authentication authentication, //
			final RedirectAttributes redirectAttributes)
	{
		LOGGER.trace("Got '{}' request.", request.getMethod());

		String host = URLDecoder.decode(arg0, StandardCharsets.UTF_8);
		Integer port = (PortUtil.isValidPort(arg1.intValue())) ? arg1 : Integer.valueOf(443);

		LOGGER.info("Going to get certificates of host:port '{}:{}'.", host, port);

		String url = new StringBuilder("wss://").append(this.host).append(":").append(this.port).append(WebSocketServerConfiguration.CHAT).toString();
		LOGGER.info("Connecting to websocket on host and port '{}'.", url);

		String destination = ProcessorStompSessionHandler.DEST_CERTIFICATE;
		String subscription = ProcessorStompSessionHandler.SUB_CERTIFICATE;
		String path = WebSocketServerConfiguration.CHAT;
		MultiMedia multiMedia = MultiMediaImpl.toBuilder().name(CertificateController.class.getSimpleName()).build();

		final StompSessionHandler handler = new CertificateStompSessionHandler(destination, subscription, multiMedia);
		final StompClient client = new StompClient( //
				this.encryptionProperties, //
				this.hostsProperties, //
				destination, //
				subscription, //
				path, //
				multiMedia, //
				handler);
		final Boolean status = Boolean.valueOf(client.connect());
		LOGGER.info("Websocket client connected successfully? '{}'.", status);

		final CertificateLoadable loader = CertificateLoader.toBuilder() //
				.client(client) //
				.host(host) //
				.port(port) //
				.resourceLoader(this.resourceLoader) //
				.keyStoreLocation(this.keyStoreLocation) //
				.keyStorePassword(this.keyStorePassword) //
				.build();
		boolean isSuccess = loader.loadCertificate();
		LOGGER.info("Loading and processing certificates was {}successful.", (isSuccess) ? "" : "not ");

		return ResponseEntity.noContent().build();
	}

}
