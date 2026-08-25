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
package info.hersche.document.metadata.certificate;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * @author Jürg Hersche (herscju)
 */
public class SaveTrustManager implements X509TrustManager
{
	private final X509TrustManager manager;
	private X509Certificate[] chain;

	/**
	 * Constructor
	 * 
	 * @param manager
	 */
	public SaveTrustManager(X509TrustManager manager)
	{
		this.manager = manager;
	}

	/**
	 *
	 */
	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException
	{
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException
	{
		this.chain = chain;
		this.manager.checkServerTrusted(chain, authType);
	}

	/**
	 *
	 */
	@Override
	public X509Certificate[] getAcceptedIssuers()
	{
		// This change has been done due to the following resolution advised for Java 1.7+
		// http://infposs.blogspot.kr/2013/06/installcert-and-java-7.html

		return new X509Certificate[0];
	}

	/**
	 * @return
	 */
	public X509Certificate[] getChain()
	{
		return this.chain;
	}
}
