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
package info.hersche.document.metadata.storage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.imaging.bytesource.ByteSource;
import org.apache.commons.io.FilenameUtils;
import org.jcodec.api.JCodecException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import info.hersche.document.metadata.handler.ProcessorStompSessionHandler;
import info.hersche.document.metadata.handler.ReceiveImageEvent;
import info.hersche.document.metadata.image.Converter;
import info.hersche.document.metadata.image.MultiMediaImpl;
import info.hersche.document.metadata.model.FileMessage;
import jakarta.jms.Session;

/**
 * @author Jürg Hersche (herscju)
 */
@Service
public class JmsConsumer
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(JmsConsumer.class);

	/**
	 * 
	 */
	private ApplicationEventPublisher eventPublisher;

	/**
	 * Constructor
	 */
	public JmsConsumer(ApplicationEventPublisher eventPublisher)
	{
		this.eventPublisher = eventPublisher;

		LOGGER.info("Class '{}' instantiated successfully.", JmsConsumer.class.getSimpleName());
	}

	/**
	 * @param message
	 * @param arg0
	 * @return
	 * @throws IOException
	 * @throws JCodecException
	 */
	private MultiMedia prepareMedia(FileMessage message, String arg0) throws IOException, JCodecException
	{
		String name = message.getName();
		String extension = FilenameUtils.getExtension(name);
		String prefix = new StringBuilder(arg0).append(FilenameUtils.getBaseName(name)).toString();
		String suffix = new StringBuilder(".").append(extension).toString();

		File file = File.createTempFile(prefix, suffix, null);
		file.deleteOnExit();

		byte[] bytes = message.getPayload();
		try (FileOutputStream fos = new FileOutputStream(file))
		{
			fos.write(bytes);
		}

		ByteSource byteSource = ByteSource.array(bytes);
		BufferedImage bufferedImage = null;
		switch (message.getImageType())
		{
			case DOCUMENT_SCAN:
				bufferedImage = Converter.getBufferedImageFromDocument(bytes);
				break;

			case STANDARD_SCAN:
			case STANDARD_IMAGE:
			case WHATSAPP_IMAGE:
			case WHATSAPP_STATUS:
				bufferedImage = Converter.getBufferedImageFromPicture(bytes);
				break;

			case STANDARD_VIDEO:
			case WHATSAPP_VIDEO:
				bufferedImage = Converter.getBufferedImageFromMovie(file);
				break;
		}

		final MultiMedia multiMedia = MultiMediaImpl.toBuilder() //
				.bufferedImage(bufferedImage) //
				.byteSource(byteSource) //
				.bytes(bytes) //
				.path(file.toPath()) //
				.name(name) //
				.mimeType(message.getType()) //
				.extension(extension) //
				.mediaType(message.getImageType()) //
				.build();

		return multiMedia;
	}

	/**
	 * See 'https://memorynotfound.com/spring-boot-embedded-activemq-configuration-example/'
	 * 
	 * @param message
	 * @param headers
	 * @param session
	 * @throws IOException
	 * @throws JCodecException
	 */
	@JmsListener(destination = "${application.activemq.queue.document.name}")
	public void receiveDocument(@Payload FileMessage message, @Headers MessageHeaders headers, Session session) throws IOException, JCodecException
	{
		LOGGER.trace("Document: Message headers '{}' for session '{}'.", headers, session);
		LOGGER.debug("Received message '{}'.", message);

		// ALL = All media (general)
		final MultiMedia multiMedia = this.prepareMedia(message, "ALL_");
		final ReceiveImageEvent event = new ReceiveImageEvent( //
				multiMedia, //
				ProcessorStompSessionHandler.DEST_DOCUMENT, //
				ProcessorStompSessionHandler.SUB_DOCUMENT);
		this.eventPublisher.publishEvent(event);
	}

	/**
	 * See 'https://memorynotfound.com/spring-boot-embedded-activemq-configuration-example/'
	 * 
	 * @param message
	 * @param headers
	 * @param session
	 * @throws IOException
	 * @throws JCodecException
	 */
	@JmsListener(destination = "${application.activemq.queue.standard.name}")
	public void receiveStandard(@Payload FileMessage message, @Headers MessageHeaders headers, Session session) throws IOException, JCodecException
	{
		LOGGER.trace("Standard: Message headers '{}' for session '{}'.", headers, session);
		LOGGER.debug("Received message '{}'.", message);

		// ALL = All media (general)
		final MultiMedia multiMedia = this.prepareMedia(message, "ALL_");
		final ReceiveImageEvent event = new ReceiveImageEvent( //
				multiMedia, //
				ProcessorStompSessionHandler.DEST_ALL2JPEG, //
				ProcessorStompSessionHandler.SUB_ALL2JPEG);
		this.eventPublisher.publishEvent(event);
	}

	/**
	 * See 'https://memorynotfound.com/spring-boot-embedded-activemq-configuration-example/'
	 * 
	 * @param message
	 * @param headers
	 * @param session
	 * @throws IOException
	 * @throws JCodecException
	 */
	@JmsListener(destination = "${application.activemq.queue.whatsapp.name}")
	public void receiveWhatsApp(@Payload FileMessage message, @Headers MessageHeaders headers, Session session) throws IOException, JCodecException
	{
		LOGGER.trace("WhatsApp: Message headers '{}' for session '{}'.", headers, session);
		LOGGER.debug("Received message '{}'.", message);

		// WAM = WhatsApp media
		final MultiMedia multiMedia = this.prepareMedia(message, "WAM_");
		final ReceiveImageEvent event = new ReceiveImageEvent( //
				multiMedia, //
				ProcessorStompSessionHandler.DEST_TIMELINE, //
				ProcessorStompSessionHandler.SUB_TIMELINE);
		this.eventPublisher.publishEvent(event);
	}

	/**
	 * See 'https://memorynotfound.com/spring-boot-embedded-activemq-configuration-example/'
	 * 
	 * @param message
	 * @param headers
	 * @param session
	 * @throws IOException
	 * @throws JCodecException
	 */
	@JmsListener(destination = "${application.activemq.queue.lightbox.name}")
	public void receiveLightBox(@Payload FileMessage message, @Headers MessageHeaders headers, Session session) throws IOException, JCodecException
	{
		LOGGER.trace("WhatsApp: Message headers '{}' for session '{}'.", headers, session);
		LOGGER.debug("Received message '{}'.", message);

		// LBM = Lightbox media
		final MultiMedia multiMedia = this.prepareMedia(message, "LBM_");
		final ReceiveImageEvent event = new ReceiveImageEvent( //
				multiMedia, //
				ProcessorStompSessionHandler.DEST_LIGHTBOX, //
				ProcessorStompSessionHandler.SUB_LIGHTBOX);
		this.eventPublisher.publishEvent(event);
	}

}
