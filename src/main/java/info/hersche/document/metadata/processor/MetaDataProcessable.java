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
package info.hersche.document.metadata.processor;

import info.hersche.document.metadata.configuration.HostsProperties;
import info.hersche.document.metadata.configuration.TlsEncryptionProperties;
import info.hersche.document.metadata.model.Action;
import info.hersche.document.metadata.storage.MultiMedia;

/**
 * @author Jürg Hersche (herscju)
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
