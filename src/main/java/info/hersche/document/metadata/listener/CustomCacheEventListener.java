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
package info.hersche.document.metadata.listener;

import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jürg Hersche (herscju)
 */
public class CustomCacheEventListener<K, V> implements CacheEventListener<K, V>
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomCacheEventListener.class);

	/**
	 * Default constructor
	 */
	public CustomCacheEventListener()
	{
		// Nothing to do here

		LOGGER.info("Class '{}' instantiated successfully.", CustomCacheEventListener.class.getSimpleName());
	}

	/**
	 *
	 */
	@Override
	public void onEvent(CacheEvent<? extends K, ? extends V> cacheEvent)
	{
		LOGGER.debug("Cache event '{}' with key '{}' fired.", cacheEvent.getType(), cacheEvent.getKey());
	}
}
