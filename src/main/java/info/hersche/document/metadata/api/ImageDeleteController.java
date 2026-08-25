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
package info.hersche.document.metadata.api;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import javax.cache.Cache;
import javax.cache.CacheManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import info.hersche.document.metadata.configuration.ApiKeyProperties;
import info.hersche.document.metadata.model.Action;
import info.hersche.document.metadata.model.Counter;
import info.hersche.document.metadata.storage.FileSystemDownloadStorageService;
import info.hersche.document.metadata.storage.FileSystemUploadStorageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * 
 */
/**
 * @author herscju
 */
@Configuration
@RestController
@RequestMapping(value = "/api/v1/images")
@PropertySource("classpath:./custom.properties")
@EnableConfigurationProperties(ApiKeyProperties.class)
public class ImageDeleteController extends AbstractController
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageDeleteController.class);

	/**
	 * 
	 */
	private final FileSystemUploadStorageService uploadStorageService;

	/**
	 * 
	 */
	private FileSystemDownloadStorageService downloadStorageService;

	/**
	 * 
	 */
	private final CacheManager cacheManager;

	/**
	 * 
	 */
	private final ApiKeyProperties keys;

	/**
	 * 
	 */
	private final Counter counter;

	/**
	 * 
	 */
	@NotNull
	@Value("${application.ehcache.name}")
	private String cacheName;

	/**
	 * Constructor
	 * 
	 * @param uploadStorageService
	 */
	public ImageDeleteController(FileSystemUploadStorageService uploadStorageService, FileSystemDownloadStorageService downloadStorageService,
			CacheManager cacheManager, ApiKeyProperties keys, Counter counter)
	{
		this.uploadStorageService = uploadStorageService;
		this.downloadStorageService = downloadStorageService;
		this.cacheManager = cacheManager;
		this.keys = keys;
		this.counter = counter;

		LOGGER.info("Class '{}' instantiated successfully.", ImageDeleteController.class.getSimpleName());
	}

	/**
	 * @param userAgent
	 * @param arg0
	 * @param authentication
	 * @param redirectAttributes
	 * @return
	 */
	@DeleteMapping(value = "/delete/all/{path}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.MEDIA_TYPE_WILDCARD })
	public ResponseEntity<String> deleteHandleAllImageRemoveAll( //
			@RequestHeader(value = "User-Agent") final String userAgent, //
			@PathVariable("path") final String arg0, //
			final HttpServletRequest request, //
			final Authentication authentication, //
			final RedirectAttributes redirectAttributes)
	{
		LOGGER.trace("Got '{}' request.", request.getMethod());

		String path = URLDecoder.decode(arg0, StandardCharsets.UTF_8);
		Action action = Action.fromName(path);
		switch (action)
		{
			case MEMORY:
				Cache<String, byte[]> cache = this.cacheManager.getCache(this.cacheName);
				cache.forEach(entry -> {
					String key = entry.getKey();
					cache.remove(key);
					this.counter.decrement();
					LOGGER.info("Delete entry with key '{}' on path '{}' from cache. Still '{}' entries available.", key, path, this.counter.getCounter());
				});
				break;

			default:
				//
		}
		redirectAttributes.addFlashAttribute("message", "You successfully removed from " + path + "!");
		LOGGER.trace("You successfully removed from path '{}'. Still '{}' entries in cache.", path, this.counter.getCounter().intValue());

		String msg = new StringBuilder("Image cache entry from path '").append(path).append("' successfully removed.").toString();

		return this.buildResponseEntity(HttpStatus.OK, "n/a", msg, authentication, userAgent);
	}

	/**
	 * @param userAgent
	 * @param arg0
	 * @param arg1
	 * @param authentication
	 * @param redirectAttributes
	 * @return
	 */
	@DeleteMapping(value = "/delete/all/{path}/{name}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.MEDIA_TYPE_WILDCARD })
	public ResponseEntity<String> deleteHandleAllImageRemoveByName( //
			@RequestHeader(value = "User-Agent") final String userAgent, //
			@PathVariable("path") final String arg0, //
			@PathVariable("name") final String arg1, //
			final HttpServletRequest request, //
			final Authentication authentication, //
			final RedirectAttributes redirectAttributes)
	{
		LOGGER.trace("Got '{}' request.", request.getMethod());

		String path = URLDecoder.decode(arg0, StandardCharsets.UTF_8);
		String name = URLDecoder.decode(arg1, StandardCharsets.UTF_8);
		Action action = Action.fromName(path);
		switch (action)
		{
			case MEMORY:
				Cache<String, byte[]> cache = this.cacheManager.getCache(this.cacheName);
				if (cache.containsKey(name))
				{
					cache.remove(name);
					this.counter.decrement();
					LOGGER.info("Delete entry with key '{}' on path '{}' from cache. Still '{}' entries available.", name, path, this.counter.getCounter());
				}
				break;

			default:
				//
		}
		redirectAttributes.addFlashAttribute("message", "You successfully removed " + name + "!");
		LOGGER.trace("You successfully removed cache entry '{}'. Still '{}' entries in cache.", name, this.counter.getCounter().intValue());

		String msg = new StringBuilder("Image cache entry '").append(name).append("' successfully removed.").toString();

		return this.buildResponseEntity(HttpStatus.OK, name, msg, authentication, userAgent);
	}

	/**
	 * @param userAgent
	 * @param arg0
	 * @param arg1
	 * @param authentication
	 * @param redirectAttributes
	 * @return
	 */
	@DeleteMapping(value = "/image/delete/dng/{path}/{name}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.MEDIA_TYPE_WILDCARD })
	public ResponseEntity<String> deleteHandleDngImageRemoveByName( //
			@RequestHeader(value = "User-Agent") final String userAgent, //
			@PathVariable("path") final String arg0, //
			@PathVariable("name") final String arg1, //
			final HttpServletRequest request, //
			final Authentication authentication, //
			final RedirectAttributes redirectAttributes)
	{
		LOGGER.trace("Got '{}' request.", request.getMethod());

		String path = URLDecoder.decode(arg0, StandardCharsets.UTF_8);
		String name = URLDecoder.decode(arg1, StandardCharsets.UTF_8);
		Action action = Action.fromName(path);
		switch (action)
		{
			case UPLOAD:
				this.uploadStorageService.delete(name);
				break;

			case DOWNLOAD:
				this.downloadStorageService.delete(name);
				break;

			case MEMORY:
				Cache<String, byte[]> cache = this.cacheManager.getCache(this.cacheName);
				if (cache.containsKey(name))
				{
					cache.remove(name);
					this.counter.decrement();
					LOGGER.info("Delete entry with key '{}' on path '{}' from cache. Still '{}' entries available.", name, path, this.counter.getCounter());
				}
				break;

			default:
				//
		}
		redirectAttributes.addFlashAttribute("message", "You successfully removed " + name + "!");
		LOGGER.trace("You successfully removed file '{}'. Still '{}' entries in cache.", name, this.counter.getCounter().intValue());

		String msg = new StringBuilder("Multimedia file '").append(name).append("' successfully removed.").toString();

		return this.buildResponseEntity(HttpStatus.OK, name, msg, authentication, userAgent);
	}

	/**
	 * @param userAgent
	 * @param arg0
	 * @param authentication
	 * @param redirectAttributes
	 * @return
	 */
	@DeleteMapping(value = "/delete")
	@Consumes({ MediaType.MEDIA_TYPE_WILDCARD })
	@Produces({ MediaType.MEDIA_TYPE_WILDCARD })
	public ResponseEntity<String> deleteHandleImageRemoveAll( //
			@RequestHeader(value = "User-Agent") final String userAgent, //
			final HttpServletRequest request, //
			final Authentication authentication, //
			final RedirectAttributes redirectAttributes)
	{
		LOGGER.trace("Got '{}' request.", request.getMethod());

		this.uploadStorageService.deleteAll();
		this.uploadStorageService.init();
		this.downloadStorageService.deleteAll();
		this.downloadStorageService.init();

		redirectAttributes.addFlashAttribute("message", "You successfully deleted all files!");
		LOGGER.trace("You successfully deleted all files.");

		String msg = "All files successfully deleted.";

		return this.buildResponseEntity(HttpStatus.OK, "", msg, authentication, userAgent);
	}
}
