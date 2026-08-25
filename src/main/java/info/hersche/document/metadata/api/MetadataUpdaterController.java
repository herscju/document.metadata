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
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import info.hersche.document.metadata.configuration.ApiKeyProperties;
import info.hersche.document.metadata.factory.AbstractDocumentFactory;
import info.hersche.document.metadata.factory.MediaFactory;
import info.hersche.document.metadata.util.NetWorkUtil;
import jakarta.servlet.http.HttpServletRequest;
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
@EnableConfigurationProperties(ApiKeyProperties.class)
public class MetadataUpdaterController extends AbstractController
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(MetadataUpdaterController.class);

	/**
	 * 
	 */
	private final ApiKeyProperties keys;

	/**
	 * Constructor
	 * 
	 * @param keys
	 */
	public MetadataUpdaterController(ApiKeyProperties keys)
	{
		this.keys = keys;
		
		LOGGER.info("Class '{}' instantiated successfully.", MetadataUpdaterController.class.getSimpleName());
	}

	/**
	 * @param userAgent
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 * @param authentication
	 * @param redirectAttributes
	 * @return
	 */
	@PostMapping(value = "/image/metadata/update/{name}/{section}/{directory}/{tag}")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Produces({ MediaType.MEDIA_TYPE_WILDCARD })
	public ResponseEntity<String> postHandleMetadataUpdateByName( //
			@RequestHeader(value = "User-Agent") final String userAgent, //
			@PathVariable("name") final String arg0, //
			@PathVariable("section") final String arg1, //
			@PathVariable("directory") final String arg2, //
			@PathVariable("tag") final String arg3, //
			@RequestBody() final String arg4, //
			final HttpServletRequest request, //
			final Authentication authentication, //
			final RedirectAttributes redirectAttributes)
	{
		LOGGER.trace("Got '{}' request.", request.getMethod());

		return this.prepareResponseEntity(arg0, arg1, arg2, arg3, arg4, authentication, userAgent);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 * @param authentication
	 * @param userAgent
	 * @return
	 */
	private ResponseEntity<String> prepareResponseEntity(String arg0, String arg1, String arg2, String arg3, String arg4, Authentication authentication,
			String userAgent)
	{
		String name = URLDecoder.decode(arg0, StandardCharsets.UTF_8);
		String section = URLDecoder.decode(arg1, StandardCharsets.UTF_8);
		String directory = URLDecoder.decode(arg2, StandardCharsets.UTF_8);
		String tag = URLDecoder.decode(arg3, StandardCharsets.UTF_8);
		String value = URLDecoder.decode(arg4, StandardCharsets.UTF_8);

		String mimeType = NetWorkUtil.detectMimtype(name);
		String extension = NetWorkUtil.getExtension(Paths.get(name));

		// Get list of all available tag names
		MediaFactory factory = AbstractDocumentFactory.getMediaFactory(extension);
		Map<Integer, String> tagNames = factory.getTagNameMap();
		Integer tagId = Integer.decode(tag);

		LOGGER.info("Tag names >>> {}", tagNames);

		if (!tagNames.containsKey(tagId))
		{
			return this.buildResponseEntity(HttpStatus.BAD_REQUEST, "Tag not available!", authentication, userAgent);
		}

		String tagLabel = tagNames.get(tagId);

		LOGGER.info("File name >>> {} --> {} --> {}", name, extension, mimeType);
		LOGGER.info("Section   >>> {}", section);
		LOGGER.info("Directory >>> {}", directory);
		LOGGER.info("Tag       >>> {} --> {} --> {}", tag, tagId, tagLabel);
		LOGGER.info("Value     >>> {}", value);

		String msg = new StringBuilder("Multimedia file '").append(name).append("' successfully updated.").toString();

		return this.buildResponseEntity(HttpStatus.OK, name, msg, authentication, userAgent);
	}

	/**
	 * @param userAgent
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 * @param authentication
	 * @param redirectAttributes
	 * @return
	 */
	@PutMapping(value = "/image/metadata/update/{name}/{section}/{directory}/{tag}/{value}")
	@Consumes({ MediaType.TEXT_PLAIN })
	@Produces({ MediaType.MEDIA_TYPE_WILDCARD })
	public ResponseEntity<String> putHandleMetadataUpdateByName( //
			@RequestHeader(value = "User-Agent") final String userAgent, //
			@PathVariable("name") final String arg0, //
			@PathVariable("section") final String arg1, //
			@PathVariable("directory") final String arg2, //
			@PathVariable("tag") final String arg3, //
			@PathVariable("value") final String arg4, //
			final HttpServletRequest request, //
			final Authentication authentication, //
			final RedirectAttributes redirectAttributes)
	{
		LOGGER.trace("Got '{}' request.", request.getMethod());

		return this.prepareResponseEntity(arg0, arg1, arg2, arg3, arg4, authentication, userAgent);
	}
}
