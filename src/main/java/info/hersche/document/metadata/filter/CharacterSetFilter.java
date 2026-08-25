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
package info.hersche.document.metadata.filter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

/**
 * @author Jürg Hersche (herscju)
 */
@Component
public class CharacterSetFilter extends GenericFilterBean
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CharacterSetFilter.class);
	private static final Charset UTF8 = StandardCharsets.UTF_8;
	
	/**
	 * Default constructor
	 */
	public CharacterSetFilter()
	{
		// Nothing to do here

		LOGGER.info("Class '{}' instantiated successfully.", CharacterSetFilter.class.getSimpleName());
	}

	/**
	 * @param servletRequest
	 * @param servletResponse
	 * @param chain
	 * @throws IOException
	 * @throws ServletExceptions
	 */
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException
	{
		servletRequest.setCharacterEncoding(UTF8.toString());
		servletResponse.setCharacterEncoding(UTF8.toString());

		chain.doFilter(servletRequest, servletResponse);
	}
}
