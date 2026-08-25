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
package info.hersche.document.metadata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author herscju
 */
public class ServletInitializer extends SpringBootServletInitializer
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ServletInitializer.class);

	/**
	 * Default constructor
	 */
	public ServletInitializer()
	{
		// Nothing to do here

		LOGGER.info("Class '{}' instantiated successfully.", ServletInitializer.class.getSimpleName());
	}

	/**
	 *
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application)
	{
		LOGGER.debug("Configure '{}' on '{}'.", MetaDataApplication.class.getSimpleName(), ServletInitializer.class.getSimpleName());

		return application.sources(MetaDataApplication.class);
	}

}
