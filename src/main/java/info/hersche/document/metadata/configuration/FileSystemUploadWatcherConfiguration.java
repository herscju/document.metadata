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
package info.hersche.document.metadata.configuration;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.devtools.filewatch.FileSystemWatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import info.hersche.document.metadata.listener.AddFileChangeListener;
import info.hersche.document.metadata.model.Action;
import info.hersche.document.metadata.processor.ProcessImageMetaDataProcessor;
import info.hersche.document.metadata.storage.UploadWatcherProperties;
import jakarta.annotation.PreDestroy;

/**
 * @author herscju
 */
@Configuration
@EnableConfigurationProperties({ HostsProperties.class, UploadWatcherProperties.class, TlsEncryptionProperties.class })
public class FileSystemUploadWatcherConfiguration
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(FileSystemUploadWatcherConfiguration.class);

	/**
	 * 
	 */
	private final UploadWatcherProperties watcherProperties;
	private final TlsEncryptionProperties encryptionProperties;
	private final HostsProperties hostsProperties;
	private final ProcessImageMetaDataProcessor processor;

	/**
	 * Constructor
	 * 
	 * @param watcherProperties
	 * @param processor
	 */
	public FileSystemUploadWatcherConfiguration(UploadWatcherProperties watcherProperties, TlsEncryptionProperties encryptionProperties,
			HostsProperties hostsProperties, ProcessImageMetaDataProcessor processor)
	{
		this.watcherProperties = watcherProperties;
		this.encryptionProperties = encryptionProperties;
		this.hostsProperties = hostsProperties;
		this.processor = processor;

		LOGGER.info("Class '{}' instantiated successfully.", FileSystemUploadWatcherConfiguration.class.getSimpleName());
	}

	/**
	 * @return
	 */
	@Bean
	public FileSystemWatcher fileSystemWatcherUpload()
	{
		File directory = Path.of(Paths.get("").toAbsolutePath().toString(), this.watcherProperties.directory()).toFile();
		FileSystemWatcher fileSystemWatcher = new FileSystemWatcher(this.watcherProperties.daemon(), //
				Duration.ofSeconds(this.watcherProperties.pollInterval()), //
				Duration.ofSeconds(this.watcherProperties.quietPeriod()));
		fileSystemWatcher.addSourceDirectory(directory);
		fileSystemWatcher.addListener(new AddFileChangeListener(this.encryptionProperties, this.hostsProperties, this.processor, Action.UPLOAD));
		// fileSystemWatcher.setTriggerFilter(f -> f.toPath().endsWith(".jpeg"));
		fileSystemWatcher.start();
		LOGGER.info(String.format("'FileSystemWatcher' initialized. Monitoring directory '%s'.", directory.getAbsolutePath()));

		return fileSystemWatcher;
	}

	/**
	 * @throws Exception
	 */
	@PreDestroy
	public void onDestroy() throws Exception
	{
		LOGGER.info("Shutting down 'FileSystemWatcher'.");
		this.fileSystemWatcherUpload().stop();
	}

}
