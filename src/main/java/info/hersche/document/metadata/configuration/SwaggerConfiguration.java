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
package info.hersche.document.metadata.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

/**
 * @author herscju
 *
 */
@Configuration
public class SwaggerConfiguration
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(SwaggerConfiguration.class);
	
	@Value("${info.application.version}")
	private String version;

	@Value("${info.application.name}")
	private String title;

	@Value("${info.application.description}")
	private String description;

	// @Value("${XXX}")
	private String name = "Jürg Hersche";

	// @Value("${XXX}")
	private String email = "juerg.hersche@bluewin.ch";

	// @Value("${XXX}")
	private String wikiDescription = "See project wiki";

	@Value("${application.hosts.host.wiki}")
	private String wikiUrl;

	// @Value("${XXX}")
	private String termsOfService = "https://swagger.io/terms/";

	// @Value("${XXX}")
	private String licenseName = "Apache 2.0";

	// @Value("${XXX}")
	private String licenseUrl = "https://springdoc.org/";

	/**
	 * Default constructor
	 */
	public SwaggerConfiguration()
	{
		// Nothing to do here

		LOGGER.info("Class '{}' instantiated successfully.", SwaggerConfiguration.class.getSimpleName());
	}

	/**
	 * @return
	 */
	@Bean
	public OpenAPI springShopOpenAPI()
	{
		Info info = new Info() //
				.title(this.title) //
				.description(this.description) //
				.version(new StringBuilder("v").append(this.version).toString()) //
				.termsOfService(this.termsOfService) //
				.contact(new Contact().name(this.name).email(this.email)) //
				.license(new License().name(this.licenseName).url(this.licenseUrl));

		ExternalDocumentation documentation = new ExternalDocumentation() //
				.description(this.wikiDescription) //
				.url(this.wikiUrl);

		return new OpenAPI() //
				.info(info) //
				.externalDocs(documentation);
	}
}
