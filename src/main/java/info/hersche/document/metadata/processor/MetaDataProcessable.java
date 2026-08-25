/**
 * 
 */
package info.hersche.document.metadata.processor;

import info.hersche.document.metadata.configuration.HostsProperties;
import info.hersche.document.metadata.configuration.TlsEncryptionProperties;
import info.hersche.document.metadata.model.Action;
import info.hersche.document.metadata.storage.MultiMedia;

/**
 * @author herscju
 */
public interface MetaDataProcessable
{
	/**
	 * 
	 * @return
	 */
	public Boolean getStatus();

	/**
	 * @param multiMedia
	 * @param action
	 * @return
	 */
	public byte[] process(MultiMedia multiMedia, Action action);

	/**
	 * @param destination
	 */
	public void setDestination(String destination);

	/**
	 * @param encryptionProperties
	 */
	public void setEncryptionProperties(TlsEncryptionProperties encryptionProperties);

	/**
	 * @param hostsProperties
	 */
	public void setHostsProperties(HostsProperties hostsProperties);

	/**
	 * @param path
	 */
	public void setPath(String path);

	/**
	 * @param subscription
	 */
	public void setSubscription(String subscription);
}
