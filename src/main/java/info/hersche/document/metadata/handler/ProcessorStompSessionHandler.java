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
package info.hersche.document.metadata.handler;

import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import javax.cache.Cache;
import javax.cache.CacheManager;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;

import com.drew.imaging.ImageProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import info.hersche.document.metadata.configuration.SecurityConfiguration;
import info.hersche.document.metadata.configuration.WebSocketSendToUserConfiguration;
import info.hersche.document.metadata.factory.AbstractDocumentFactory;
import info.hersche.document.metadata.factory.MediaFactory;
import info.hersche.document.metadata.image.Converter;
import info.hersche.document.metadata.image.MultiMediaImpl;
import info.hersche.document.metadata.image.Picture;
import info.hersche.document.metadata.model.Action;
import info.hersche.document.metadata.model.Counter;
import info.hersche.document.metadata.model.OutputDataMessage;
import info.hersche.document.metadata.model.OutputImageDataMessage;
import info.hersche.document.metadata.model.OutputImageMetadataMessage;
import info.hersche.document.metadata.model.OutputMessage;
import info.hersche.document.metadata.storage.MultiMedia;
import info.hersche.document.metadata.util.DateTimeUtil;
import lombok.Setter;

/**
 * This class is an implementation for <code>StompSessionHandlerAdapter</code>.
 * Once a connection is established, We subscribe to /topicX/... and
 * send a sample message to server.
 * 
 * See also 'https://www.baeldung.com/spring-websockets-send-message-to-user' and 'https://stackoverflow.com/a/74783052/6487382'
 * 
 * @author Jürg Hersche (herscju)
 */
@Component
public class ProcessorStompSessionHandler extends StompSessionHandlerAdapter
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessorStompSessionHandler.class);
	private static final String DATETIME_PATTERN = "yyyy:MM:dd HH:mm:ss";
	private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DATETIME_PATTERN);
	private static final String WHATSAPP_PATTERN = "yyyy:MM:dd'T'HH.mm.ss.SSS";
	private static final DateTimeFormatter WHATSAPP_FORMATTER = DateTimeFormatter.ofPattern(WHATSAPP_PATTERN);
	private static final String DOWNLOAD_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";
	private static final DateTimeFormatter DOWNLOAD_FORMATTER = DateTimeFormatter.ofPattern(DOWNLOAD_PATTERN);
	private static final String NAME = "Europe/Zurich";
	private static final ZoneId ZONE = ZoneId.of(NAME);

	/**
	 * 
	 */
	public static final String SUB_ALL2JPEG = new StringBuilder(WebSocketSendToUserConfiguration.TOPIC).append("/all2jpg").toString();
	public static final String SUB_CERTIFICATE = new StringBuilder(WebSocketSendToUserConfiguration.TOPIC).append("/certificates").toString();
	public static final String SUB_DNG2JPEG = new StringBuilder(WebSocketSendToUserConfiguration.TOPIC).append("/dng2jpg").toString();
	public static final String SUB_DOCUMENT = new StringBuilder(WebSocketSendToUserConfiguration.TOPIC).append("/document").toString();
	public static final String SUB_LDAP_OBJ = new StringBuilder(WebSocketSendToUserConfiguration.TOPIC).append("/objects").toString();
	public static final String SUB_LIGHTBOX = new StringBuilder(WebSocketSendToUserConfiguration.TOPIC).append("/lightbox").toString();
	public static final String SUB_TIMELINE = new StringBuilder(WebSocketSendToUserConfiguration.TOPIC).append("/timeline").toString();
	public static final String PATH_ALL2JPEG = "/originator";
	public static final String PATH_CERTIFICATE = "/certificate";
	public static final String PATH_DNG2JPEG = "/processor";
	public static final String PATH_DOCUMENT = "/parser";
	public static final String PATH_LDAP_OBJ = "/object";
	public static final String PATH_LIGHTBOX = "/display";
	public static final String PATH_TIMELINE = "/creator";
	public static final String DEST_ALL2JPEG = new StringBuilder(WebSocketSendToUserConfiguration.APP).append(PATH_ALL2JPEG).toString();
	public static final String DEST_CERTIFICATE = new StringBuilder(WebSocketSendToUserConfiguration.APP).append(PATH_CERTIFICATE).toString();
	public static final String DEST_DNG2JPEG = new StringBuilder(WebSocketSendToUserConfiguration.APP).append(PATH_DNG2JPEG).toString();
	public static final String DEST_DOCUMENT = new StringBuilder(WebSocketSendToUserConfiguration.APP).append(PATH_DOCUMENT).toString();
	public static final String DEST_LDAP_OBJ = new StringBuilder(WebSocketSendToUserConfiguration.APP).append(PATH_LDAP_OBJ).toString();
	public static final String DEST_LIGHTBOX = new StringBuilder(WebSocketSendToUserConfiguration.APP).append(PATH_LIGHTBOX).toString();
	public static final String DEST_TIMELINE = new StringBuilder(WebSocketSendToUserConfiguration.APP).append(PATH_TIMELINE).toString();

	/**
	 * 
	 */
	private ObjectMapper mapper;

	/**
	 * 
	 */
	@Setter
	private Counter counter;

	/**
	 * 
	 */
	@Setter
	private String cacheName;

	/**
	 * 
	 */
	@Setter
	private CacheManager cacheManager;

	/**
	 * 
	 */
	@Setter
	private String destination;

	/**
	 * 
	 */
	@Setter
	private String subscription;

	/**
	 * 
	 */
	@Setter
	private String user = SecurityConfiguration.ANONYMUS;

	/**
	 * 
	 */
	@Setter
	private MultiMedia multiMedia;

	/**
	 * 
	 */
	@Setter
	private Action action;

	/**
	 * Default constructor
	 */
	public ProcessorStompSessionHandler()
	{
		// Nothing to do here
	}

	/**
	 * Constructor
	 * 
	 * @param mapper
	 * @param counter
	 * @param cacheName
	 * @param cacheManager
	 * @param destination
	 * @param subscription
	 * @param action
	 * @param multiMedia
	 */
	public ProcessorStompSessionHandler( //
			ObjectMapper mapper, //
			Counter counter, //
			String cacheName, //
			CacheManager cacheManager, //
			String destination, //
			String subscription, //
			Action action, //
			MultiMedia multiMedia)
	{
		this.mapper= mapper;
		this.counter = counter;
		this.cacheName = cacheName;
		this.cacheManager = cacheManager;
		this.destination = destination;
		this.subscription = subscription;
		this.action = action;
		this.multiMedia = multiMedia;
	}

	/**
	 *
	 */
	@Override
	public void afterConnected(StompSession session, StompHeaders connectedHeaders)
	{
		LOGGER.info("New session established: '{}'.", session.getSessionId());

		session.subscribe(this.subscription, this);
		LOGGER.info("Subscribed to '{}'.", this.subscription);

		session.setAutoReceipt(true);

		try
		{
			session.send(this.destination, this.getOutputMessage());
			LOGGER.info("Message sent to websocket server URL '{}'.", this.destination);
		}
		catch (IllegalStateException e)
		{
			LOGGER.error("An error has been occured -> {}", e.getLocalizedMessage());
		}
	}

	/**
	 *
	 */
	@Override
	public Type getPayloadType(StompHeaders headers)
	{
		return OutputMessage.class;
	}

	/**
	 * A sample message instance.
	 * 
	 * @return instance of <code>Message</code>
	 */
	private OutputMessage getOutputMessage()
	{
		final String time = DATETIME_FORMATTER.format(OffsetDateTime.now(ZONE));

		OutputMessage message;
		try
		{
			message = OutputMessage.toBuilder() //
					.from(this.multiMedia.getName()) //
					.text("The uploaded file has been examined and the metadata is available.") //
					.data(this.buildThumbnailAndReadMetadata()) //
					.user(this.user) //
					.time(time) //
					.build();
		}
		catch (FileNotFoundException e)
		{
			LOGGER.error("Missing file!", e);
			message = OutputMessage.toBuilder() //
					.from(this.multiMedia.getName()) //
					.text("Missing file!") //
					.data(OutputDataMessage.toBuilder().build()) //
					.user(this.user) //
					.time(time) //
					.build();
		}
		catch (IOException e)
		{
			LOGGER.error("Couldn't generate thumbnail!", e);
			message = OutputMessage.toBuilder() //
					.from(this.multiMedia.getName()) //
					.text("Couldn't generate thumbnail!") //
					.data(OutputDataMessage.toBuilder().build()) //
					.user(this.user) //
					.time(time) //
					.build();
		}
		catch (ImageProcessingException e)
		{
			LOGGER.error("Couldn't process original upload!", e);
			message = OutputMessage.toBuilder() //
					.from(this.multiMedia.getName()) //
					.text("Couldn't process original upload!") //
					.data(OutputDataMessage.toBuilder().build()) //
					.user(this.user) //
					.time(time) //
					.build();
		}

		return message;
	}

	/**
	 * package com.hubersuhner.epdb.data.image -> Converter
	 * 
	 * @return
	 * @throws IOException
	 * @throws ImageProcessingException
	 */
	private OutputDataMessage buildThumbnailAndReadMetadata() throws IOException, ImageProcessingException
	{
		try (final InputStream inputStream = this.multiMedia.getByteSource().getInputStream())
		{
			final Picture picture = this.buildPicture(inputStream);
			final OutputImageMetadataMessage metadata = picture.readMetadata();
			LOGGER.trace("Metadata of multimedia is '{}'.", metadata);
			final LocalDateTime dateTime = picture.getOriginalDate();
			final String originalDate = dateTime.format(DATETIME_FORMATTER);
			LOGGER.debug("Original date & time of multimedia is '{}'.", originalDate);

			final Dimension sourceDimension = Converter.getDimensionOfBufferedImage(this.multiMedia.getBufferedImage());
			final Dimension targetDimension = new Dimension(Picture.THUMBNAIL_WIDTH, Picture.THUMBNAIL_HEIGHT);
			LOGGER.trace("Get thumbnail with '{}' / '{}' dimension.", sourceDimension, targetDimension);
			final String thumbnailContent = picture.generateThumbnail(true, sourceDimension, targetDimension);

			// Add image to cache for further use.
			if (Action.MEMORY.equals(this.action))
			{
				Cache<String, byte[]> cache = this.cacheManager.getCache(this.cacheName);
				String key = this.multiMedia.getName();
				if (!cache.containsKey(key))
				{
					cache.put(key, this.multiMedia.getBytes());
					this.counter.increment();
					LOGGER.debug("Image '{}' added to cache ('{}').", key, this.counter);
				}
				else
				{
					cache.put(key, this.multiMedia.getBytes());
					this.counter.increment();
					LOGGER.debug("Image '{}' updated on cache ('{}').", key, this.counter);
				}
			}

			// Get WhatsApp time from name
			final LocalDateTime downloadDateTime = this.getDownloadTimeFromName(dateTime);
			final String downloadOriginalDate = downloadDateTime.format(DOWNLOAD_FORMATTER);
			final UUID uuid = UUID.nameUUIDFromBytes(this.multiMedia.getName().getBytes());

			// Keep original message empty!
			final OutputImageDataMessage original = OutputImageDataMessage.toBuilder().build();

			// Send thumbnail image
			final OutputImageDataMessage thumbnail = OutputImageDataMessage.toBuilder() //
					.id(uuid) //
					.action(this.action) //
					.content(thumbnailContent) //
					.path(this.multiMedia.getPath()) //
					.date(originalDate) //
					.name(this.multiMedia.getName()) //
					.extendsion(this.multiMedia.getExtension()) //
					.mimetype(this.multiMedia.getMimeType()) //
					.metadata(metadata) //
					.imageType(this.multiMedia.getMediaType()) //
					.downloadDateTime(downloadOriginalDate) //
					.build();
			LOGGER.trace("Thumbnail image:\n{}\n", this.mapper.writeValueAsString(thumbnail));

			// Put all, original and thumbnail, together and return
			final OutputDataMessage output = OutputDataMessage.toBuilder().image(original).thumbnail(thumbnail).build();

			return output;
		}
	}

	/**
	 * https://www.w3resource.com/java-exercises/basic/java-basic-exercise-55.php
	 * https://stackoverflow.com/a/6118964/6487382
	 * 
	 * @param pictureDateTime
	 * @return
	 */
	private LocalDateTime getDownloadTimeFromName(final LocalDateTime pictureDateTime)
	{
		final boolean isWhatsAppMedia = MultiMediaImpl.Type.WHATSAPP_IMAGE.equals(this.multiMedia.getMediaType())
				|| MultiMediaImpl.Type.WHATSAPP_VIDEO.equals(this.multiMedia.getMediaType());
		if (isWhatsAppMedia)
		{
			StringBuilder fileDate = new StringBuilder();

			if (DateTimeUtil.DATETIME_V1_FILTER.matcher(this.multiMedia.getName()).find())
			{
				fileDate = DateTimeUtil.parse(DateTimeUtil.DATETIME_V1_FILTER, this.multiMedia.getName());
				LOGGER.trace("Download time from file name '{}' is '{}' (v1).", this.multiMedia.getName(), fileDate);
			}

			if (DateTimeUtil.DATETIME_V2_FILTER.matcher(this.multiMedia.getName()).find())
			{
				fileDate = DateTimeUtil.parse(DateTimeUtil.DATETIME_V2_FILTER, this.multiMedia.getName());
				LOGGER.trace("Download time from file name '{}' is '{}' (v2).", this.multiMedia.getName(), fileDate);
			}

			return LocalDateTime.parse(fileDate.toString(), WHATSAPP_FORMATTER);
		}

		return pictureDateTime;
	}

	/**
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	private Picture buildPicture(final InputStream inputStream) throws IOException
	{
		final MediaFactory factory = AbstractDocumentFactory.getMediaFactory(this.multiMedia.getExtension());
		final Picture picture = factory.getPicture(this.multiMedia.getBufferedImage(), inputStream);

		return picture;
	}

	/**
	 *
	 */
	@Override
	public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable throwable)
	{
		LOGGER.error("An exception has been occured.", throwable);
	}

	/**
	 *
	 */
	@Override
	public void handleFrame(StompHeaders headers, Object payload)
	{
		final OutputMessage msg = (OutputMessage) payload;
		LOGGER.info("Received: '{}' from: '{}'.", msg.getText(), msg.getFrom());
	}
}
