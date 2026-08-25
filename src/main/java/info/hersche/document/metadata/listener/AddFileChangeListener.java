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
 * https://stackoverflow.com/a/60397791
 */
package info.hersche.document.metadata.listener;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Set;

import org.apache.commons.imaging.bytesource.ByteSource;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.devtools.filewatch.ChangedFile;
import org.springframework.boot.devtools.filewatch.ChangedFiles;
import org.springframework.boot.devtools.filewatch.FileChangeListener;

import info.hersche.document.metadata.api.AbstractController;
import info.hersche.document.metadata.configuration.HostsProperties;
import info.hersche.document.metadata.configuration.TlsEncryptionProperties;
import info.hersche.document.metadata.configuration.WebSocketServerConfiguration;
import info.hersche.document.metadata.handler.ProcessorStompSessionHandler;
import info.hersche.document.metadata.image.Converter;
import info.hersche.document.metadata.image.MultiMediaImpl;
import info.hersche.document.metadata.image.MultiMediaImpl.Type;
import info.hersche.document.metadata.model.Action;
import info.hersche.document.metadata.processor.MetaDataProcessable;
import info.hersche.document.metadata.processor.ProcessImageMetaDataProcessor;
import info.hersche.document.metadata.util.NetWorkUtil;

/**
 * @author herscju
 */
public class AddFileChangeListener implements FileChangeListener
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AddFileChangeListener.class);
	private static final Charset UTF8 = StandardCharsets.UTF_8;

	/**
	 * 
	 */
	private TlsEncryptionProperties encryptionProperties;
	private HostsProperties hostsProperties;
	private MetaDataProcessable processor;
	private Action action;

	/**
	 * Constructor
	 * 
	 * @param encryptionProperties
	 * @param hostsProperties
	 * @param processor
	 * @param action
	 */
	public AddFileChangeListener(TlsEncryptionProperties encryptionProperties, HostsProperties hostsProperties, ProcessImageMetaDataProcessor processor,
			Action action)
	{
		this.encryptionProperties = encryptionProperties;
		this.hostsProperties = hostsProperties;
		this.processor = processor;
		this.action = action;
	}

	/**
	 * @param path
	 * @return
	 */
	private boolean isLocked(Path path)
	{
		try (FileChannel ch = FileChannel.open(path, StandardOpenOption.WRITE); FileLock lock = ch.tryLock())
		{
			return lock == null;
		}
		catch (IOException e)
		{
			return true;
		}
	}

	/**
	 *
	 */
	@Override
	public void onChange(Set<ChangedFiles> changeSet)
	{
		for (ChangedFiles changedFiles : changeSet)
		{
			for (ChangedFile changedFile : changedFiles.getFiles())
			{
				if (changedFile.getType().equals(ChangedFile.Type.ADD) && !this.isLocked(changedFile.getFile().toPath()))
				{
					byte[] bytes = new byte[0];
					Boolean status = Boolean.FALSE;
					File file = changedFile.getFile();
					String path = file.getAbsolutePath();
					String name = file.getName();
					String extension = FilenameUtils.getExtension(path);
					String mimeType = NetWorkUtil.detectMimtype(path);

					try
					{
						/**
						 * See 'https://stackoverflow.com/a/5924132/6487382' how to clone an input stream
						 */
						try (InputStream is = new FileInputStream(file); ByteArrayOutputStream baos = new ByteArrayOutputStream())
						{
							is.transferTo(baos);

							final Type type = AbstractController.getImageType(name);
							final BufferedImage bufferedImage = Converter.getBufferedImageFromPicture(new ByteArrayInputStream(baos.toByteArray()));
							final MultiMediaImpl image = MultiMediaImpl.toBuilder() //
									.bufferedImage(bufferedImage) //
									.byteSource(ByteSource.array(baos.toByteArray())) //
									.bytes(bytes) //
									.path(file.toPath()) //
									.name(name) //
									.mimeType(mimeType) //
									.extension(extension) //
									.mediaType(type) //
									.build();

							final String destination = ProcessorStompSessionHandler.DEST_DNG2JPEG;
							final String subscription = ProcessorStompSessionHandler.SUB_DNG2JPEG;

							LOGGER.info("Start processing stored image '{}' of type '{}' ('{}').", name, type, URLEncoder.encode(name, UTF8));
							this.processor.setEncryptionProperties(this.encryptionProperties);
							this.processor.setHostsProperties(this.hostsProperties);
							this.processor.setDestination(destination);
							this.processor.setSubscription(subscription);
							this.processor.setPath(WebSocketServerConfiguration.PUSH);

							bytes = this.processor.process(image, this.action);
							status = this.processor.getStatus();
						}
					}
					catch (NullPointerException e)
					{
						LOGGER.error("File is empty!", e);
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
				}
			}
		}
	}
}
