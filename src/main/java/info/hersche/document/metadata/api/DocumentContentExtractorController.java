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

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import info.hersche.document.metadata.configuration.ApiKeyProperties;
import info.hersche.document.metadata.image.MultiMediaImpl.Type;
import info.hersche.document.metadata.model.FileMessage;
import info.hersche.document.metadata.storage.JmsDocumentProducer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * @author Jürg Hersche (herscju)
 */
@Configuration
@RestController
@RequestMapping(value = "/api/v1/documents")
@EnableConfigurationProperties(ApiKeyProperties.class)
public class DocumentContentExtractorController extends AbstractController
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentContentExtractorController.class);

	/**
	 * 
	 */
	@Autowired
	private JmsDocumentProducer documentJmsProducer;

	/**
	 * 
	 */
	private final ApiKeyProperties keys;

	/**
	 * @param keys
	 */
	public DocumentContentExtractorController(ApiKeyProperties keys)
	{
		this.keys = keys;

		LOGGER.info("Class '{}' instantiated successfully.", DocumentContentExtractorController.class.getSimpleName());
	}

	/**
	 * @param userAgent
	 * @param file
	 * @param authentication
	 * @param redirectAttributes
	 * @return
	 * @throws IOException
	 */
	@PostMapping(value = "/document/ocr/extract")
	@Consumes({ MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_OCTET_STREAM, "image/jpeg", "image/png" })
	@Produces({ MediaType.MEDIA_TYPE_WILDCARD })
	public ResponseEntity<String> postHandleStandardDocumentFilesUpload( //
			@RequestHeader(value = "User-Agent") final String userAgent, //
			@RequestParam(name = "file", required = true) final MultipartFile file, //
			final HttpServletRequest request, //
			final Authentication authentication, //
			final RedirectAttributes redirectAttributes) throws IOException
	{
		LOGGER.trace("Got '{}' request.", request.getMethod());

		String fileName = file.getOriginalFilename();
		Type type = AbstractController.getImageType(fileName);
		LOGGER.trace("Document: Work on image '{}' of type '{}'.", fileName, type);

		FileMessage message = FileMessage.toBuilder() //
				.name(fileName) //
				.size(file.getSize()) //
				.type(file.getContentType()) //
				.payload(file.getBytes()) //
				.imageType(type) //
				.build();
		this.documentJmsProducer.send(message);

		redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + fileName + "!");
		LOGGER.trace("You successfully uploaded file '{}'.", fileName);

		String msg = new StringBuilder("Document '").append(fileName).append("' successfully uploaded.").toString();

		return this.buildResponseEntity(HttpStatus.CREATED, file, msg, authentication, userAgent);
	}
}
