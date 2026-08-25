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

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InaccessibleObjectException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.cache.Cache;
import javax.cache.CacheManager;

import org.apache.commons.imaging.bytesource.ByteSource;
import org.jcodec.api.JCodecException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;

import info.hersche.document.metadata.client.StompClient;
import info.hersche.document.metadata.configuration.ApiKeyProperties;
import info.hersche.document.metadata.configuration.HostsProperties;
import info.hersche.document.metadata.configuration.TlsEncryptionProperties;
import info.hersche.document.metadata.configuration.WebSocketServerConfiguration;
import info.hersche.document.metadata.handler.ProcessorStompSessionHandler;
import info.hersche.document.metadata.image.Converter;
import info.hersche.document.metadata.image.MultiMediaImpl;
import info.hersche.document.metadata.image.MultiMediaImpl.Type;
import info.hersche.document.metadata.model.Action;
import info.hersche.document.metadata.model.Counter;
import info.hersche.document.metadata.processor.MetaDataProcessable;
import info.hersche.document.metadata.processor.UpdateFileImageMetaDataProcessor;
import info.hersche.document.metadata.processor.UpdateMemoryImageMetaDataProcessor;
import info.hersche.document.metadata.storage.DownloadStorageProperties;
import info.hersche.document.metadata.storage.FileSystemUploadStorageService;
import info.hersche.document.metadata.util.NetWorkUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.Setter;

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
public class DateTimeSynchronizeController extends AbstractController
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DateTimeSynchronizeController.class);
	private static final String DATETIME_PATTERN = "yyyy:MM:dd HH:mm:ss";
	private static final Charset UTF8 = StandardCharsets.UTF_8;

	/**
	 * 
	 */
	@NotNull
	@Value("${application.ehcache.name}")
	private String cacheName;

	/**
	 * 
	 */
	@Setter
	@NotNull
	private CacheManager cacheManager;

	/**
	 * 
	 */
	private final ObjectMapper mapper;

	/**
	 * 
	 */
	private final FileSystemUploadStorageService uploadStorageService;

	/**
	 * 
	 */
	private final DownloadStorageProperties downloadStorageProperties;

	/**
	 * 
	 */
	private final TlsEncryptionProperties encryptionProperties;
	private final HostsProperties hostsProperties;

	/**
	 * 
	 */
	private final ApiKeyProperties keys;

	/**
	 * 
	 */
	private final Counter counter;

	/**
	 * Constructor
	 * 
	 * @param uploadStorageService
	 */
	public DateTimeSynchronizeController(ObjectMapper mapper, FileSystemUploadStorageService uploadStorageService,
			DownloadStorageProperties downloadStorageProperties, CacheManager cacheManager, TlsEncryptionProperties encryptionProperties,
			HostsProperties hostsProperties, ApiKeyProperties keys, Counter counter)
	{
		this.mapper = mapper;
		this.uploadStorageService = uploadStorageService;
		this.downloadStorageProperties = downloadStorageProperties;
		this.cacheManager = cacheManager;
		this.encryptionProperties = encryptionProperties;
		this.hostsProperties = hostsProperties;
		this.keys = keys;
		this.counter = counter;

		LOGGER.info("Class '{}' instantiated successfully.", DateTimeSynchronizeController.class.getSimpleName());
	}

	/**
	 * @param userAgent
	 * @param arg0
	 * @param arg1
	 * @param authentication
	 * @param redirectAttributes
	 * @return
	 * @throws JCodecException
	 */
	@PutMapping(value = "/image/metadata/synchronize/{name}/{date}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.MEDIA_TYPE_WILDCARD })
	public ResponseEntity<String> putHandleImageSynchronizeByName( //
			@RequestHeader(value = "User-Agent") final String userAgent, //
			@PathVariable("name") final String arg0, //
			@PathVariable("date") final String arg1, //
			final HttpServletRequest request, //
			final Authentication authentication, //
			final RedirectAttributes redirectAttributes) throws JCodecException
	{
		LOGGER.trace("Got '{}' request.", request.getMethod());

		String name = URLDecoder.decode(arg0, StandardCharsets.UTF_8);
		String date = URLDecoder.decode(arg1, StandardCharsets.UTF_8);
		Path path = this.uploadStorageService.load(name);
		File file = path.toFile();
		redirectAttributes.addFlashAttribute("message", "You successfully updated " + name + "!");
		LOGGER.trace("You successfully updated file '{}' with date '{}'.", path, date);

		String mimeType = NetWorkUtil.detectMimtype(name);
		String extension = NetWorkUtil.getExtension(Paths.get(name));

		byte[] bytes = new byte[0];
		Boolean status = Boolean.FALSE;
		try (InputStream is = new FileInputStream(file); ByteArrayOutputStream baos = new ByteArrayOutputStream())
		{
			is.transferTo(baos);

			Type type = AbstractController.getImageType(name);
			BufferedImage bufferedImage = null;
			switch (type)
			{
				case DOCUMENT_SCAN:
					bufferedImage = Converter.getBufferedImageFromDocument(baos.toByteArray());
					break;

				case STANDARD_SCAN:
				case STANDARD_IMAGE:
				case WHATSAPP_IMAGE:
				case WHATSAPP_STATUS:
					bufferedImage = Converter.getBufferedImageFromPicture(new ByteArrayInputStream(baos.toByteArray()));
					break;

				case STANDARD_VIDEO:
				case WHATSAPP_VIDEO:
					bufferedImage = Converter.getBufferedImageFromMovie(file);
					break;
			}

			final MultiMediaImpl multiMedia = MultiMediaImpl.toBuilder() //
					.bufferedImage(bufferedImage) //
					.byteSource(ByteSource.array(baos.toByteArray())) //
					.bytes(bytes) //
					.path(path) //
					.name(name) //
					.mimeType(mimeType) //
					.extension(extension) //
					.mediaType(type) //
					.build();

			/**
			 * Configure processor and process image. The processor is file based.
			 */
			LOGGER.info("Processing stored image '{}' of type '{}' ('{}').", name, type, URLEncoder.encode(name, UTF8));
			String destination = ProcessorStompSessionHandler.DEST_DNG2JPEG;
			String subscription = ProcessorStompSessionHandler.SUB_DNG2JPEG;
			final MetaDataProcessable processor = UpdateFileImageMetaDataProcessor.toBuilder() //
					.date(date) //
					.directory(this.downloadStorageProperties.directory()) //
					.destination(destination) //
					.subscription(subscription) //
					.path(WebSocketServerConfiguration.PUSH) //
					.build();

			bytes = processor.process(multiMedia, Action.UPLOAD);
			status = processor.getStatus();
		}
		catch (FileNotFoundException e)
		{
			LOGGER.error("Missing file!", e);
		}
		catch (IOException e)
		{
			LOGGER.error("Couldn't open file!", e);
		}

		LOGGER.info("Processing image was {}successful.", (status.booleanValue()) ? "" : "not ");

		String msg = new StringBuilder("Multimedia file '") //
				.append(file.getAbsolutePath()) //
				.append((status.booleanValue()) ? "'" : "' not") //
				.append(" successfully updated.") //
				.toString();

		return this.buildResponseEntity(HttpStatus.OK, URLEncoder.encode(name, StandardCharsets.UTF_8), msg, authentication, userAgent);

//		return ResponseEntity.noContent().build();
	}

	/**
	 * @param userAgent
	 * @param arg0
	 * @param arg1
	 * @param authentication
	 * @param redirectAttributes
	 * @return
	 * @throws JCodecException
	 */
	@PutMapping(value = "/timeline/image/metadata/synchronize/{name}/{date}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.MEDIA_TYPE_WILDCARD })
	public ResponseEntity<String> putHandleImageUpdateByName( //
			@RequestHeader(value = "User-Agent") final String userAgent, //
			@PathVariable("name") final String arg0, //
			@PathVariable("date") final String arg1, //
			final HttpServletRequest request, //
			final Authentication authentication, //
			final RedirectAttributes redirectAttributes) throws JCodecException
	{
		String name = URLDecoder.decode(arg0, StandardCharsets.UTF_8);
		String date = URLDecoder.decode(arg1, StandardCharsets.UTF_8);
		String mimeType = NetWorkUtil.detectMimtype(name);
		String extension = NetWorkUtil.getExtension(Paths.get(name));
		Type type = AbstractController.getImageType(name);

		Boolean status = Boolean.FALSE;
		Cache<String, byte[]> cache = this.cacheManager.getCache(this.cacheName);
		Boolean isAvailable = Boolean.valueOf(cache.containsKey(name));
		if (isAvailable.booleanValue())
		{
			try
			{
				String destination = ProcessorStompSessionHandler.DEST_TIMELINE;
				String subscription = ProcessorStompSessionHandler.SUB_TIMELINE;

				byte[] content = new byte[0];
				try
				{
					content = cache.get(name);
				}
				catch (InaccessibleObjectException e)
				{
					LOGGER.error("Unable to make field accessible!", e);
				}
				LOGGER.trace("Byte source length of '{}' is '{}'.", name, content.length);
				BufferedImage bufferedImage = null;
				switch (type)
				{
					case DOCUMENT_SCAN:
						bufferedImage = Converter.getBufferedImageFromDocument(content);
						break;

					case STANDARD_SCAN:
					case STANDARD_IMAGE:
					case WHATSAPP_IMAGE:
					case WHATSAPP_STATUS:
						bufferedImage = Converter.getBufferedImageFromPicture(content);
						break;

					case STANDARD_VIDEO:
					case WHATSAPP_VIDEO:
						bufferedImage = Converter.getBufferedImageFromMovie(content);
						break;
				}

				/**
				 * DO NOT change type of variable!
				 */
				final MultiMediaImpl inputMedia = MultiMediaImpl.toBuilder() //
						.bufferedImage(bufferedImage) //
						.byteSource(ByteSource.array(content)) //
						.bytes(content) //
						.name(name) //
						.mimeType(mimeType) //
						.extension(extension) //
						.mediaType(type) //
						.build();

				/**
				 * Configure processor and process image. The processor is cache based.
				 */
				String originalDate = Converter.dateToLocalDateTime(date, DATETIME_PATTERN);
				LOGGER.debug("Original date & time of image is '{}'.", originalDate);

				LOGGER.info("Processing cached image '{}' ('{}').", name, URLEncoder.encode(name, UTF8));
				MetaDataProcessable processor = UpdateMemoryImageMetaDataProcessor.toBuilder() //
						.counter(this.counter) //
						.date(originalDate) //
						.destination(destination) //
						.subscription(subscription) //
						.path(WebSocketServerConfiguration.PUSH) //
						.build();

				Action action = Action.MEMORY;
				byte[] bytes = processor.process(inputMedia, action);
				LOGGER.trace("Byte source length of '{}' is '{}'.", inputMedia.getName(), bytes.length);

				BufferedImage processedImage = null;
				switch (type)
				{
					case DOCUMENT_SCAN:
						bufferedImage = Converter.getBufferedImageFromDocument(bytes);
						break;

					case STANDARD_SCAN:
					case STANDARD_IMAGE:
					case WHATSAPP_IMAGE:
					case WHATSAPP_STATUS:
						processedImage = Converter.getBufferedImageFromPicture(bytes);
						break;

					case STANDARD_VIDEO:
					case WHATSAPP_VIDEO:
						processedImage = Converter.getBufferedImageFromMovie(bytes);
						break;
				}

				/**
				 * DO NOT change type of variable!
				 */
				final MultiMediaImpl outputMedia = MultiMediaImpl.toBuilder() //
						.bufferedImage(processedImage) //
						.byteSource(ByteSource.array(bytes)) //
						.bytes(bytes) //
						.name(inputMedia.getName()) //
						.mimeType(inputMedia.getMimeType()) //
						.extension(inputMedia.getExtension()) //
						.mediaType(inputMedia.getMediaType()) //
						.build();

				status = processor.getStatus();

				final String path = WebSocketServerConfiguration.PUSH;
				final StompSessionHandler handler = new ProcessorStompSessionHandler( //
						this.mapper, //
						this.counter, //
						this.cacheName, //
						this.cacheManager, //
						destination, //
						subscription, //
						action, //
						outputMedia);
				final StompClient client = new StompClient( //
						this.encryptionProperties, //
						this.hostsProperties, //
						destination, //
						subscription, //
						path, //
						inputMedia, //
						handler);

				status = Boolean.valueOf(client.connect());

				String message = status.booleanValue() ? "Connected successfully to STOMP client." : "Not connected to STOMP client.";
				client.sendMessage(message);
			}
			catch (IOException e)
			{
				LOGGER.error("Couldn't read bytes from cache!", e);
			}
			catch (InaccessibleObjectException e)
			{
				LOGGER.error("Unable to make field accessible!", e);
			}
		}

		String msg = new StringBuilder("Multimedia file '") //
				.append(name) //
				.append((status.booleanValue()) ? "'" : "' not") //
				.append(" successfully updated.") //
				.toString();

		return this.buildResponseEntity(HttpStatus.OK, URLEncoder.encode(name, StandardCharsets.UTF_8), msg, authentication, userAgent);
	}

	/**
	 * @param userAgent
	 * @param arg0
	 * @param arg1
	 * @param authentication
	 * @param redirectAttributes
	 * @return
	 * @throws JCodecException
	 */
	@PutMapping(value = "/adjust/image/metadata/synchronize/{name}/{date}")
	@Consumes({ MediaType.MEDIA_TYPE_WILDCARD })
	@Produces({ MediaType.MEDIA_TYPE_WILDCARD })
	public ResponseEntity<String> putHandleImageAdjustByName( //
			@RequestHeader(value = "User-Agent") final String userAgent, //
			@PathVariable("name") final String arg0, //
			@PathVariable("date") final String arg1, //
			final HttpServletRequest request, //
			final Authentication authentication, //
			final RedirectAttributes redirectAttributes) throws JCodecException
	{
		String name = URLDecoder.decode(arg0, StandardCharsets.UTF_8);
		String date = URLDecoder.decode(arg1, StandardCharsets.UTF_8);
		String mimeType = NetWorkUtil.detectMimtype(name);
		String extension = NetWorkUtil.getExtension(Paths.get(name));
		Type type = AbstractController.getImageType(name);

		Boolean status = Boolean.FALSE;
		Cache<String, byte[]> cache = this.cacheManager.getCache(this.cacheName);
		Boolean isAvailable = Boolean.valueOf(cache.containsKey(name));
		LOGGER.info("name = {}, extension = {}, mimetype = {}, type = {}", name, extension, mimeType, type);
		LOGGER.info("available = {}", isAvailable);
		if (isAvailable.booleanValue())
		{
			try
			{
				String destination = ProcessorStompSessionHandler.DEST_ALL2JPEG;
				String subscription = ProcessorStompSessionHandler.SUB_ALL2JPEG;
				LOGGER.info("destination = {}, subscription = {}", destination, subscription);

				byte[] content = new byte[0];
				try
				{
					content = cache.get(name);
				}
				catch (InaccessibleObjectException e)
				{
					LOGGER.error("Unable to make field accessible!", e);
				}
				LOGGER.trace("Byte source length of '{}' is '{}'.", name, content.length);
				BufferedImage bufferedImage = null;
				switch (type)
				{
					case DOCUMENT_SCAN:
						bufferedImage = Converter.getBufferedImageFromDocument(content);
						break;

					case STANDARD_SCAN:
					case STANDARD_IMAGE:
					case WHATSAPP_IMAGE:
					case WHATSAPP_STATUS:
						bufferedImage = Converter.getBufferedImageFromPicture(content);
						break;

					case STANDARD_VIDEO:
					case WHATSAPP_VIDEO:
						bufferedImage = Converter.getBufferedImageFromMovie(content);
						break;
				}

				/**
				 * DO NOT change type of variable!
				 */
				final MultiMediaImpl inputMedia = MultiMediaImpl.toBuilder() //
						.bufferedImage(bufferedImage) //
						.byteSource(ByteSource.array(content)) //
						.bytes(content) //
						.name(name) //
						.mimeType(mimeType) //
						.extension(extension) //
						.mediaType(type) //
						.build();

				/**
				 * Configure processor and process image. The processor is cache based.
				 */
				LOGGER.debug("Input date & time of image is '{}'.", date);

				LOGGER.info("Processing cached image '{}' ('{}').", name, URLEncoder.encode(name, UTF8));
				MetaDataProcessable processor = UpdateMemoryImageMetaDataProcessor.toBuilder() //
						.counter(this.counter) //
						.date(date) //
						.destination(destination) //
						.subscription(subscription) //
						.path(WebSocketServerConfiguration.PUSH) //
						.build();

				Action action = Action.MEMORY;
				byte[] bytes = processor.process(inputMedia, action);
				LOGGER.trace("Byte source length of '{}' is '{}'.", inputMedia.getName(), bytes.length);

				BufferedImage processedImage = null;
				switch (type)
				{
					case DOCUMENT_SCAN:
						bufferedImage = Converter.getBufferedImageFromDocument(bytes);
						break;

					case STANDARD_SCAN:
					case STANDARD_IMAGE:
					case WHATSAPP_IMAGE:
					case WHATSAPP_STATUS:
						processedImage = Converter.getBufferedImageFromPicture(bytes);
						break;

					case STANDARD_VIDEO:
					case WHATSAPP_VIDEO:
						processedImage = Converter.getBufferedImageFromMovie(bytes);
						break;
				}

				/**
				 * DO NOT change type of variable!
				 */
				final MultiMediaImpl outputMedia = MultiMediaImpl.toBuilder() //
						.bufferedImage(processedImage) //
						.byteSource(ByteSource.array(bytes)) //
						.bytes(bytes) //
						.name(inputMedia.getName()) //
						.mimeType(inputMedia.getMimeType()) //
						.extension(inputMedia.getExtension()) //
						.mediaType(inputMedia.getMediaType()) //
						.build();

				status = processor.getStatus();

				final String path = WebSocketServerConfiguration.PUSH;
				final StompSessionHandler handler = new ProcessorStompSessionHandler( //
						this.mapper, //
						this.counter, //
						this.cacheName, //
						this.cacheManager, //
						destination, //
						subscription, //
						action, //
						outputMedia);
				final StompClient client = new StompClient( //
						this.encryptionProperties, //
						this.hostsProperties, //
						destination, //
						subscription, //
						path, //
						inputMedia, //
						handler);

				status = Boolean.valueOf(client.connect());

				String message = status.booleanValue() ? "Connected successfully to STOMP client." : "Not connected to STOMP client.";
				client.sendMessage(message);
			}
			catch (IOException e)
			{
				LOGGER.error("Couldn't read bytes from cache!", e);
			}
			catch (InaccessibleObjectException e)
			{
				LOGGER.error("Unable to make field accessible!", e);
			}
		}

		String msg = new StringBuilder("Multimedia file '") //
				.append(name) //
				.append((status.booleanValue()) ? "'" : "' not") //
				.append(" successfully updated.") //
				.toString();

		return this.buildResponseEntity(HttpStatus.OK, URLEncoder.encode(name, StandardCharsets.UTF_8), msg, authentication, userAgent);
	}
}
