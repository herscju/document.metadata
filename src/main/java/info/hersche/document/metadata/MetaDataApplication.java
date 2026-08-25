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
package info.hersche.document.metadata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import info.hersche.document.metadata.configuration.ApiKeyProperties;
import info.hersche.document.metadata.configuration.DropZoneProperties;
import info.hersche.document.metadata.configuration.HostsProperties;
import info.hersche.document.metadata.configuration.LdapProperties;
import info.hersche.document.metadata.storage.DownloadStorageProperties;
import info.hersche.document.metadata.storage.FileSystemDownloadStorageService;
import info.hersche.document.metadata.storage.FileSystemUploadStorageService;
import info.hersche.document.metadata.storage.UploadStorageProperties;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;

/**
 * @author Jürg Hersche (herscju)
 */
@SpringBootApplication
@EnableCaching
@SecurityScheme(name = "hersche", scheme = "negotiate", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
@EnableConfigurationProperties({ ApiKeyProperties.class, //
		DropZoneProperties.class, //
		HostsProperties.class, //
		DownloadStorageProperties.class, //
		LdapProperties.class, //
		UploadStorageProperties.class //
})
public class MetaDataApplication extends SpringBootServletInitializer
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(MetaDataApplication.class);

	/**
	 * @param builder
	 * @return
	 */
	private static SpringApplicationBuilder configureApplication(SpringApplicationBuilder builder)
	{
		return builder.sources(MetaDataApplication.class);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		SpringApplication application = MetaDataApplication.configureApplication(new SpringApplicationBuilder()).build();
		ApplicationContext context = application.run(args);
		Environment environment = context.getEnvironment();

		// Display some generic data
		String name = environment.getProperty("spring.application.name");
		String profileName = environment.getProperty("spring.profiles.active");
		String onProfileName = environment.getProperty("spring.config.activate.on-profile");
		String importFile = environment.getProperty("spring.config.import");
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("===============================================================================");
			LOGGER.debug("Application name is '{}'. The active profile is set to '{}'.", name, profileName);
			LOGGER.debug("The active-on profile is '{}' and loads from '{}' the custom properties.", onProfileName, importFile);
			LOGGER.debug("Application started successfull. '{}' is now ready to use.", name);
		}
	}

	/**
	 * Default constructor
	 */
	public MetaDataApplication()
	{
		// Nothing to do here

		LOGGER.info("Class '{}' instantiated successfully.", MetaDataApplication.class.getSimpleName());
	}

	/**
	 * @param uploadStorageService
	 * @param downloadStorageService
	 * @return
	 */
	@Bean
	public CommandLineRunner initCommandLineRunner(FileSystemUploadStorageService uploadStorageService, FileSystemDownloadStorageService downloadStorageService)
	{
		return (args) -> {
			uploadStorageService.deleteAll();
			uploadStorageService.init();
			downloadStorageService.deleteAll();
			downloadStorageService.init();
		};
	}

	/**
	 * @return
	 */
	@Bean
	public LayoutDialect layoutDialect()
	{
		return new LayoutDialect();
	}

}
