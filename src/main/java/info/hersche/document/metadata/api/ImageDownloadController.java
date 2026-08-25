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
package info.hersche.document.metadata.api;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import javax.cache.Cache;
import javax.cache.CacheManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import info.hersche.document.metadata.configuration.ApiKeyProperties;
import info.hersche.document.metadata.model.Counter;
import info.hersche.document.metadata.storage.FileSystemDownloadStorageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * @author Jürg Hersche (herscju)
 */
@Configuration
@RestController
@RequestMapping(value = "/api/v1/images")
@PropertySource("classpath:./custom.properties")
@EnableConfigurationProperties(ApiKeyProperties.class)
public class ImageDownloadController extends AbstractController
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageDownloadController.class);

	/**
	 * 
	 */
	private final FileSystemDownloadStorageService storageService;

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
	 * @param storageService
	 */
	public ImageDownloadController(FileSystemDownloadStorageService storageService, CacheManager cacheManager, ApiKeyProperties keys, Counter counter)
	{
		this.storageService = storageService;
		this.cacheManager = cacheManager;
		this.keys = keys;
		this.counter = counter;

		LOGGER.info("Class '{}' instantiated successfully.", ImageDownloadController.class.getSimpleName());
	}

	/**
	 * @param userAgent
	 * @param arg0
	 * @param authentication
	 * @param redirectAttributes
	 * @return
	 */
	@GetMapping(value = "/image/download/all/{name}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_OCTET_STREAM })
	public ResponseEntity<StreamingResponseBody> getHandleAllImageDownloadByName( //
			@RequestHeader(value = "User-Agent") final String userAgent, //
			@PathVariable("name") final String arg0, //
			final HttpServletRequest request, //
			final Authentication authentication, //
			final RedirectAttributes redirectAttributes)
	{
		LOGGER.trace("Got '{}' request.", request.getMethod());

		String name = URLDecoder.decode(arg0, StandardCharsets.UTF_8);
		Cache<String, byte[]> cache = this.cacheManager.getCache(this.cacheName);
		if (!cache.containsKey(name))
		{
			return ResponseEntity.noContent().build();
		}
		Resource resource = new ByteArrayResource(cache.get(name));
		redirectAttributes.addFlashAttribute("message", "You successfully downloaded " + name + "!");
		LOGGER.trace("You successfully downloaded file '{}'.", name);

		String msg = new StringBuilder("Multimedia file '").append(name).append("' successfully downloaded.").toString();

		return this.buildResponseEntity(HttpStatus.OK, resource, URLEncoder.encode(name, StandardCharsets.UTF_8), msg, authentication, userAgent);
	}

	/**
	 * @param userAgent
	 * @param arg0
	 * @param authentication
	 * @param redirectAttributes
	 * @return
	 */
	@GetMapping(value = "/image/download/archive/{path}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_OCTET_STREAM })
	public ResponseEntity<InputStreamResource> getHandleArchiveDownload( //
			@RequestHeader(value = "User-Agent") final String userAgent, //
			@PathVariable("path") final String arg0, //
			final HttpServletRequest request, //
			final Authentication authentication, //
			final RedirectAttributes redirectAttributes)
	{
		LOGGER.trace("Got '{}' request.", request.getMethod());

		String path = URLDecoder.decode(arg0, StandardCharsets.UTF_8);
		String name = UUID.randomUUID().toString();
		Cache<String, byte[]> cache = this.cacheManager.getCache(this.cacheName);

		String msg = new StringBuilder("Archive file '").append(name).append("' successfully downloaded. ('").append(path).append("')").toString();

		return this.buildResponseEntity(HttpStatus.OK, this.counter, cache, name, msg, authentication, userAgent);
	}

	/**
	 * @param userAgent
	 * @param arg0
	 * @param authentication
	 * @param redirectAttributes
	 * @return
	 */
	@GetMapping(value = "/image/download/dng/{name}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_OCTET_STREAM })
	public ResponseEntity<StreamingResponseBody> getHandleDngImageDownloadByName( //
			@RequestHeader(value = "User-Agent") final String userAgent, //
			@PathVariable("name") final String arg0, //
			final HttpServletRequest request, //
			final Authentication authentication, //
			final RedirectAttributes redirectAttributes)
	{
		LOGGER.trace("Got '{}' request.", request.getMethod());

		String name = URLDecoder.decode(arg0, StandardCharsets.UTF_8);
		Resource resource = this.storageService.loadAsResource(name);
		redirectAttributes.addFlashAttribute("message", "You successfully downloaded " + name + "!");
		LOGGER.trace("You successfully downloaded file '{}'.", name);

		String msg = new StringBuilder("Multimedia file '").append(name).append("' successfully downloaded.").toString();

		return this.buildResponseEntity(HttpStatus.OK, resource, URLEncoder.encode(name, StandardCharsets.UTF_8), msg, authentication, userAgent);
	}
}
