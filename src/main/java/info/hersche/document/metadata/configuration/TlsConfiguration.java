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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.net.ssl.SSLContext;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.TlsConfig;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.DefaultClientTlsStrategy;
import org.apache.hc.client5.http.ssl.TlsSocketStrategy;
import org.apache.hc.core5.http.ssl.TLS;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.util.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author herscju
 *
 */
@Configuration
public class TlsConfiguration
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(TlsConfiguration.class);
	private static final String NAME = "keystore.";
	private static final String EXTENSION = ".pfx";
	private static final String KEYSTORE = "classpath:wildcard.hersche.name.2024.pfx";
	private static final String PASSWORD = "BsXu+WPnc-P$Ge66";
	private static final long TIMEOUT = 30L;

	/**
	 * 
	 */
	private ResourceLoader resourceLoader;

	@Value("${server.ssl.key-store}")
	private String keyStoreLocation;

	@Value("${server.ssl.key-store-password}")
	private String keyStorePassword;

	/**
	 * Default constructor
	 */
	public TlsConfiguration(ResourceLoader resourceLoader)
	{
		this.resourceLoader = resourceLoader;

		LOGGER.info("Class '{}' instantiated successfully.", TlsConfiguration.class.getSimpleName());
	}

	/**
	 * https://www.baeldung.com/spring-load-resource-as-string
	 * https://www.baeldung.com/spring-boot-https-self-signed-certificate
	 * https://stackoverflow.com/a/43235569
	 * https://github.com/apache/httpcomponents-client/blob/master/httpclient5/src/test/java/org/apache/hc/client5/http/examples/ClientCustomSSL.java
	 * 
	 * Add CAcert to JKS in path 'C:\Program Files\<PathToJava>\lib\security'
	 * https://stackoverflow.com/a/36427118
	 * https://magicmonster.com/kb/prg/java/ssl/pkix_path_building_failed/
	 * 
	 * @return
	 */
	@Bean
	public RestTemplate restTemplate() throws Exception
	{
		String location = (this.keyStoreLocation != null) ? this.keyStoreLocation : KEYSTORE;
		String password = (this.keyStorePassword != null) ? this.keyStorePassword : PASSWORD;

		Resource certificate = this.resourceLoader.getResource(location);
		InputStream input = certificate.getInputStream();
		File file = File.createTempFile(NAME, EXTENSION);
		this.copyInputStreamToFile(input, file);
		LOGGER.info("SSL/TLS trust store file is '{}' with password '*****'. Has been loaded from location '{}'.", file, location);

		SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(file, password.toCharArray()).build();
		TlsSocketStrategy tlsStrategy = new DefaultClientTlsStrategy(sslContext);
		HttpClientConnectionManager cmconnectionManager = PoolingHttpClientConnectionManagerBuilder.create() //
				.setTlsSocketStrategy(tlsStrategy) //
				.setDefaultTlsConfig(TlsConfig.custom() //
						.setHandshakeTimeout(Timeout.ofSeconds(TIMEOUT)) //
						.setSupportedProtocols(TLS.V_1_2, TLS.V_1_3) // Allow TLSv1.2 and TLSv1.3 protocols
						.build())
				.build();
		HttpClient httpClient = HttpClients.custom().setConnectionManager(cmconnectionManager).build();
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);

		return new RestTemplate(factory);
	}

	/**
	 * @param input
	 * @param tempFile
	 */
	private void copyInputStreamToFile(InputStream input, File file)
	{
		try (OutputStream output = new FileOutputStream(file, false))
		{
			input.transferTo(output);
		}
		catch (FileNotFoundException e)
		{
			LOGGER.error("FileNotFoundException", e);
		}
		catch (IOException e)
		{
			LOGGER.error("IOException", e);
		}
	}

}
