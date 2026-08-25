/**
 * 
 */
package info.hersche.document.metadata.listener;

import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author herscju
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
