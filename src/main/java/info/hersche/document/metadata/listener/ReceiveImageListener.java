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
package info.hersche.document.metadata.listener;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.cache.CacheManager;

import org.apache.commons.imaging.ImagingException;
import org.apache.commons.imaging.bytesource.ByteSource;
import org.jcodec.api.JCodecException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.drew.imaging.ImageProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import info.hersche.document.metadata.client.StompClient;
import info.hersche.document.metadata.configuration.HostsProperties;
import info.hersche.document.metadata.configuration.NettyWebSocketConfiguration;
import info.hersche.document.metadata.configuration.TlsEncryptionProperties;
import info.hersche.document.metadata.configuration.WebSocketServerConfiguration;
import info.hersche.document.metadata.factory.AbstractDocumentFactory;
import info.hersche.document.metadata.factory.MediaFactory;
import info.hersche.document.metadata.handler.ProcessorStompSessionHandler;
import info.hersche.document.metadata.handler.ReceiveImageEvent;
import info.hersche.document.metadata.image.Converter;
import info.hersche.document.metadata.image.MultiMediaImpl;
import info.hersche.document.metadata.image.Picture;
import info.hersche.document.metadata.model.Action;
import info.hersche.document.metadata.model.Counter;
import info.hersche.document.metadata.model.OutputImageMetadataMessage;
import info.hersche.document.metadata.processor.MetaDataProcessable;
import info.hersche.document.metadata.processor.UpdateMemoryImageMetaDataProcessor;
import info.hersche.document.metadata.storage.MultiMedia;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import lombok.Getter;

/**
 * @author herscju
 */
@Component
@PropertySource("classpath:./custom.properties")
public class ReceiveImageListener
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ReceiveImageListener.class);
	private static final String DATETIME_PATTERN = "yyyy:MM:dd HH:mm:ss";
	private static final Charset UTF8 = StandardCharsets.UTF_8;

	/**
	 * 
	 */
	@Value("${application.ehcache.name}")
	private String cacheName;

	/**
	 * 
	 */
	private ObjectMapper mapper;

	/**
	* 
	*/
	private CacheManager cacheManager;

	/**
	 * 
	 */
	private SocketIOServer socketServer;

	/**
	 * 
	 */
	@Getter
	private Boolean status = null;

	/**
	 * 
	 */
	private TlsEncryptionProperties encryptionProperties;
	private HostsProperties hostsProperties;
	private Counter counter;

	/**
	 * Constructor
	 * 
	 * @param mapper
	 * @param cacheManager
	 * @param encryptionProperties
	 * @param hostsProperties
	 * @param socketServer
	 * @param counter
	 */
	public ReceiveImageListener(ObjectMapper mapper, CacheManager cacheManager, TlsEncryptionProperties encryptionProperties, HostsProperties hostsProperties,
			SocketIOServer socketServer, Counter counter)
	{
		this.mapper = mapper;
		this.cacheManager = cacheManager;
		this.encryptionProperties = encryptionProperties;
		this.hostsProperties = hostsProperties;
		this.socketServer = socketServer;
		this.counter = counter;

		LOGGER.info("Class '{}' instantiated successfully.", ReceiveImageListener.class.getSimpleName());
	}

	/**
	 * @param event
	 * @throws IOException
	 * @throws ImageProcessingException
	 * @throws ImagingException
	 * @throws JCodecException
	 */
	@EventListener
	public void handleImageReceivedEvent(final ReceiveImageEvent event) throws ImagingException, ImageProcessingException, IOException, JCodecException
	{
		final MultiMedia multiMedia = event.getMultiMedia();
		final String destination = event.getDestination();
		final String subscription = event.getSubscription();

		LOGGER.debug("Byte source length of '{}' is '{}'.", multiMedia.getName(), multiMedia.getByteSource().size());
		final MediaFactory factory = AbstractDocumentFactory.getMediaFactory(multiMedia.getExtension());
		final Picture picture = factory.getPicture(multiMedia.getBufferedImage(), multiMedia.getByteSource());
		final OutputImageMetadataMessage metadata = picture.readMetadata();
		LOGGER.trace("Metadata of image is '{}'.", metadata);
		final String originalDate = Converter.dateToLocalDateTime(picture.getOriginalDate(), DATETIME_PATTERN);
		LOGGER.debug("Original date & time of image is '{}'.", originalDate);

		/**
		 * Configure processor and process image. The processor is queue based.
		 */
		LOGGER.info("Processing queued image '{}' of type '{}' ('{}').", multiMedia.getName(), multiMedia.getMediaType(),
				URLEncoder.encode(multiMedia.getName(), UTF8));
		final MetaDataProcessable processor = UpdateMemoryImageMetaDataProcessor.toBuilder() //
				.counter(this.counter) //
				.date(originalDate) //
				.destination(destination) //
				.subscription(subscription) //
				.path(WebSocketServerConfiguration.PUSH) //
				.build();

		final byte[] bytes = processor.process(multiMedia, Action.MEMORY);
		LOGGER.debug("Byte source length of '{}' is '{}'.", multiMedia.getName(), bytes.length);

		final BufferedImage bufferedImage;
		switch (multiMedia.getMediaType())
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
			default:
				bufferedImage = Converter.getBufferedImageFromMovie(bytes);
				break;
		}

		final MultiMedia multiMediaOut = MultiMediaImpl.toBuilder() //
				.bufferedImage(bufferedImage) //
				.byteSource(ByteSource.array(bytes)) //
				.bytes(bytes) //
				.name(multiMedia.getName()) //
				.mimeType(multiMedia.getMimeType()) //
				.extension(multiMedia.getExtension()) //
				.mediaType(multiMedia.getMediaType()) //
				.build();

		this.status = processor.getStatus();

		final Action action = Action.MEMORY;
		switch (multiMedia.getMediaType())
		{
			case DOCUMENT_SCAN:
				final Configuration configuration = this.socketServer.getConfiguration();

				LOGGER.info("?????????????????????????????????????????????????????????????");
				LOGGER.info("?????????????????????????????????????????????????????????????");
				LOGGER.info("?????????????????????????????????????????????????????????????");
				LOGGER.info("?????????????????????????????????????????????????????????????");

				String url = String.format("%s://%s:%d", NettyWebSocketConfiguration.PROTOCOL, configuration.getHostname(), configuration.getPort());
				LOGGER.info("WebSocket server listens on '{}'. SSL/TLS Protocol is '{}'", url, configuration.getSSLProtocol());
				URI uri = URI.create(url);
				IO.Options options = IO.Options.builder().build();
				Socket socket = IO.socket(uri, options);
				Emitter emitter = null;

				LOGGER.info("---A---------------------------------------------------------");
				emitter = socket.on(Socket.EVENT_CONNECT, (args) -> {
					LOGGER.trace(">>> CONNECT -> {}", args);
				});
				LOGGER.info("---B---------------------------------------------------------");
				emitter = socket.on(NettyWebSocketConfiguration.EVENT_SENDBINARY, (args) -> {
					LOGGER.trace(">>> {} -> {}", NettyWebSocketConfiguration.EVENT_SENDBINARY, args);
				});
				LOGGER.info("---C---------------------------------------------------------");
				emitter = socket.on(Socket.EVENT_DISCONNECT, (args) -> {
					LOGGER.trace(">>> DISCONNECT -> {}", args);
				});
				LOGGER.info("---D---------------------------------------------------------");

				socket.connect();
				LOGGER.info("---E---------------------------------------------------------");
				socket.emit(NettyWebSocketConfiguration.EVENT_SENDTEXT, "EVENT_SENDTEXT -> FooBar");
				emitter.emit(NettyWebSocketConfiguration.EVENT_SENDTEXT, "EVENT_SENDTEXT -> FooBar");
				socket.emit(NettyWebSocketConfiguration.EVENT_SENDBINARY, "EVENT_SENDBINARY -> FooBar");
				emitter.emit(NettyWebSocketConfiguration.EVENT_SENDBINARY, "EVENT_SENDBINARY -> FooBar");
				LOGGER.info("---F---------------------------------------------------------");
				socket.emit(NettyWebSocketConfiguration.CHANNEL_TEXT, "CHANNEL_TEXT -> FooBar");
				emitter.emit(NettyWebSocketConfiguration.CHANNEL_TEXT, "CHANNEL_TEXT -> FooBar");
				socket.emit(NettyWebSocketConfiguration.CHANNEL_BINARY, "CHANNEL_BINARY -> FooBar");
				emitter.emit(NettyWebSocketConfiguration.CHANNEL_BINARY, "CHANNEL_BINARY -> FooBar");
				LOGGER.info("---G---------------------------------------------------------");
				socket.disconnect();
				LOGGER.info("---H---------------------------------------------------------");

				this.status = Boolean.TRUE;
				break;

			default:
				final String path = WebSocketServerConfiguration.PUSH;
				final StompSessionHandler handler = new ProcessorStompSessionHandler( //
						this.mapper, //
						this.counter, //
						this.cacheName, //
						this.cacheManager, //
						destination, //
						subscription, //
						action, //
						multiMediaOut);
				final StompClient client = new StompClient( //
						this.encryptionProperties, //
						this.hostsProperties, //
						destination, //
						subscription, //
						path, //
						multiMedia, //
						handler);

				this.status = Boolean.valueOf(client.connect());

				final String message = this.status.booleanValue() ? "Connected successfully to STOMP client." : "Not connected to STOMP client.";
				client.sendMessage(message);
				break;
		}
	}

}
