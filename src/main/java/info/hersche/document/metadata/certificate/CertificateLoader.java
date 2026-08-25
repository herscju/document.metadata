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
package info.hersche.document.metadata.certificate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

import info.hersche.document.metadata.client.StompClient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author herscju
 * 
 *         See
 *         'https://techgig365.wordpress.com/2020/07/13/download-certificates-from-url-java-example/',
 *         'https://tty4.dev/development/spring/spring-stomp-client/' and
 *         'https://github.com/davidmoten/ssl-certificate-downloader/blob/master/src/main/java/SslCertificateDownloader.java'
 */
@Getter
@Setter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true, builderMethodName = "toBuilder")
public class CertificateLoader implements CertificateLoadable {
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CertificateLoader.class);
	private static final String SEPARATOR_DETAIL = "\t--------------------------------------------------------";
	private static final String SEPARATOR_BEGIN = "=== begin ======================================================";
	private static final String SEPARATOR_END = "=== end ========================================================";
	private static final String TLS = "TLS";
	private static final String DEFAULT_PASSPHRASE = "changeit";
	private static final char[] HEXDIGITS = "0123456789abcdef".toCharArray();

	/**
	 * @param bytes
	 * @return
	 */
	private static String toHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder(bytes.length * 3);
		for (int b : bytes) {
			b &= 0xff;
			sb.append(HEXDIGITS[b >> 4]);
			sb.append(HEXDIGITS[b & 15]);
			sb.append(' ');
		}

		return sb.toString();
	}

	/**
	 * 
	 */
	private String host;
	private Integer port;
	private StompClient client;
	private ResourceLoader resourceLoader;
	private String keyStoreLocation;
	private String keyStorePassword;

	private StompSessionHandler sessionHandler;

	/**
	 * Default constructor
	 */
	public CertificateLoader() {
		// Nothing to do here
	}

	/**
	 * 
	 */
	@Override
	public boolean loadCertificate() {
		boolean isSuccess = false;

		try {
			this.client.sendMessage(SEPARATOR_BEGIN);

			SSLContext context = SSLContext.getInstance(TLS);
			TrustManagerFactory managerFactory = TrustManagerFactory
					.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			KeyStore keyStore = this.loadKeyStore();
			managerFactory.init(keyStore);
			X509TrustManager defaultTrustManager = (X509TrustManager) managerFactory.getTrustManagers()[0];
			SaveTrustManager trustManager = new SaveTrustManager(defaultTrustManager);
			context.init(null, new TrustManager[] { trustManager }, new SecureRandom());
			SSLSocketFactory factory = context.getSocketFactory();

			this.sendMessage(String.format("Opening connection to '%s:%d'.", this.host, this.port));

			// Initiate socket
			SSLSocket socket = (SSLSocket) factory.createSocket(this.host, this.port.intValue());
			socket.setSoTimeout(10000);

			try {
				this.sendMessage("Initiating SSL/TLS handshake...");

				socket.startHandshake();
				socket.close();

				this.sendMessage("Certificate is already trusted.");
			} catch (SSLException e) {
				this.sendErrorMessage("SSLException: An error has been occurred. Try again!", e);
			}

			X509Certificate[] chain = trustManager.getChain();
			if (chain == null) {
				this.sendErrorMessage("Could not obtain server certificate chain.", null);
			} else {
				this.sendMessage(String.format("Server sent '%d' certificate(s).", chain.length));

				MessageDigest sha1 = MessageDigest.getInstance("SHA1");
				MessageDigest md5 = MessageDigest.getInstance("MD5");
				for (int i = 0; i < chain.length; i++) {
					X509Certificate certificate = chain[i];
					String alias = keyStore.getCertificateAlias(certificate);

					int j = i + 1;
					this.sendMessage(String.format("\t%02d: Subject  %s", j, certificate.getSubjectX500Principal()));
					this.sendMessage(String.format("\t%02d: Issuer   %s", j, certificate.getIssuerX500Principal()));
					this.sendMessage(String.format("\t%02d: Alias    %s", j, alias));
					this.sendMessage(String.format("\t%02d: Serial#  %s", j, certificate.getSerialNumber()));
					this.sendMessage(String.format("\t%02d: Version  %s", j, certificate.getVersion()));
					this.sendMessage(String.format("\t%02d: SigAlg   %s", j, certificate.getSigAlgName()));
					this.sendMessage(String.format("\t%02d: From/To  %s ... %s", j, certificate.getNotBefore(),
							certificate.getNotAfter()));

					sha1.update(certificate.getEncoded());
					this.sendMessage(
							String.format("\t%02d: SHA1     %s", j, CertificateLoader.toHexString(sha1.digest())));

					md5.update(certificate.getEncoded());
					this.sendMessage(
							String.format("\t%02d: MD5      %s", j, CertificateLoader.toHexString(md5.digest())));
					this.client.sendMessage(SEPARATOR_DETAIL);
				}

				isSuccess = (chain.length > 0);
			}
		} catch (NoSuchAlgorithmException e) {
			this.sendErrorMessage("NoSuchAlgorithmException: An error has been occured.", e);
		} catch (KeyStoreException e) {
			this.sendErrorMessage("KeyStoreException: An error has been occured.", e);
		} catch (CertificateException e) {
			this.sendErrorMessage("CertificateException: An error has been occured.", e);
		} catch (IOException e) {
			this.sendErrorMessage("IOException: An error has been occured.", e);
		} catch (KeyManagementException e) {
			this.sendErrorMessage("KeyManagementException: An error has been occured.", e);
		}

		this.client.sendMessage(SEPARATOR_END);

		return isSuccess;
	}

	/**
	 * @return
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws IOException
	 */
	private KeyStore loadKeyStore()
			throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		char[] passphrase = this.keyStorePassword.toCharArray();

		if (this.keyStoreLocation != null) {
			LOGGER.trace("Get keystore from '{}'.", this.keyStoreLocation);

			// Load key store
			Resource certificate = this.resourceLoader.getResource(this.keyStoreLocation);
			keyStore.load(certificate.getInputStream(), passphrase);

		} else {
			File file = new File("");

			if (!file.isFile()) {
				LOGGER.error("Keystore file '{}' not found.", this.keyStoreLocation);

				File dir = new File(
						System.getProperty("java.home") + File.separatorChar + "lib" + File.separatorChar + "security");
				file = new File(dir, "jssecacerts");

				this.keyStoreLocation = file.getAbsolutePath();

				if (!file.isFile()) {
					LOGGER.trace("Loading default keystore...");
					passphrase = DEFAULT_PASSPHRASE.toCharArray();
					file = new File(dir, "cacerts");

					this.keyStoreLocation = file.getAbsolutePath();
				}
			}

			LOGGER.trace("Get keystore from '{}'.", this.keyStoreLocation);

			try (InputStream is = new FileInputStream(this.keyStoreLocation)) {
				// Load key store
				keyStore.load(is, passphrase);
			}
		}

		return keyStore;
	}

	/**
	 * @param message
	 * @param e
	 */
	private void sendErrorMessage(String message, Throwable e) {
		LOGGER.error(message, (e != null) ? e.getMessage() : "");
		StringBuilder msg = new StringBuilder(message);
		if (e != null) {
			msg.append(" Details: ").append(e.getLocalizedMessage());
		}
		this.client.sendMessage(msg.toString());
	}

	/**
	 * @param message
	 */
	/**
	 * @param message
	 */
	private void sendMessage(String message) {
		LOGGER.trace(message);
		this.client.sendMessage(message);
	}
}
