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

import java.time.Duration;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import javax.management.MalformedObjectNameException;

import org.ehcache.config.ResourcePools;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheEventListenerConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.event.EventType;
import org.ehcache.expiry.ExpiryPolicy;
import org.ehcache.jsr107.Eh107Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import info.hersche.document.metadata.listener.CustomCacheEventListener;
import jakarta.validation.constraints.NotNull;

/**
 * @author Jürg Hersche (herscju)
 */
@Configuration
@EnableCaching
@PropertySource("classpath:./custom.properties")
public class CacheConfiguration
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CacheConfiguration.class);
	private static final Long TTL = Long.valueOf(86400L);
	private static final Long HEAP = Long.valueOf(3L);
	private static final Long OFFHEAP = Long.valueOf(6L);

	/**
	 * 
	 */
	@NotNull
	@Value("${application.ehcache.name}")
	private String cacheName;

	/**
	 * Default constructor
	 */
	public CacheConfiguration()
	{
		// Nothing to do here

		LOGGER.info("Class '{}' instantiated successfully.", CacheConfiguration.class.getSimpleName());
	}

	/**
	 * https://www.ehcache.org/documentation/3.10/cache-event-listeners.html
	 * 
	 * @return
	 * @throws MalformedObjectNameException
	 */
	@Bean
	public CacheManager ehCacheManager()
	{
		CustomCacheEventListener<String, byte[]> listener = new CustomCacheEventListener<>();
		CacheEventListenerConfigurationBuilder listenerConfiguration = CacheEventListenerConfigurationBuilder
				.newEventListenerConfiguration(listener, EventType.CREATED, EventType.UPDATED, EventType.EXPIRED, EventType.REMOVED, EventType.EVICTED) //
				.ordered() //
				.asynchronous(); //
		CachingProvider provider = Caching.getCachingProvider();
		CacheManager cacheManager = provider.getCacheManager();
		ResourcePools resourcePools = ResourcePoolsBuilder.newResourcePoolsBuilder() //
				.offheap(OFFHEAP, MemoryUnit.GB) //
				.heap(HEAP, MemoryUnit.GB) //
				.build();
		ExpiryPolicy<Object, Object> expiryPolicy = ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(TTL.longValue()));
		CacheConfigurationBuilder<String, byte[]> configuration = CacheConfigurationBuilder
				.newCacheConfigurationBuilder(String.class, byte[].class, resourcePools) //
				.withService(listenerConfiguration) //
				.withExpiry(expiryPolicy);

		Cache<String, byte[]> cache = cacheManager.createCache(this.cacheName, Eh107Configuration.fromEhcacheCacheConfiguration(configuration));
		LOGGER.debug("Cache '{}' created successfully.", cache.getName());

		return cacheManager;
	}

}
