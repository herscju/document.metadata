/**
 * 
 */
package info.hersche.document.metadata.certificate;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * @author herscju
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
