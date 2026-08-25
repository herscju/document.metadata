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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import info.hersche.document.metadata.error.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author herscju
 *
 *         Annotation '@Component' not required. Registration is done manually.
 */
@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerFilter.class);

	/**
	 * 
	 */
	private ObjectMapper mapper;

	/**
	 * Constructor
	 */
	public ExceptionHandlerFilter(ObjectMapper mapper)
	{
		this.mapper = mapper;

		LOGGER.info("Class '{}' instantiated successfully.", ExceptionHandlerFilter.class.getSimpleName());
	}

	/**
	 * @param value
	 * @return
	 * @throws JsonProcessingException
	 */
	private String convertObjectToJson(Object value) throws JsonProcessingException
	{
		if (value == null)
		{
			return null;
		}

		return this.mapper.writeValueAsString(value);
	}

	/**
	 *
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
	{
		LOGGER.trace("Run 'once per request' filter. Request URL '{}'.", request.getRequestURL());

		try
		{
			filterChain.doFilter(request, response);
		}
		catch (RuntimeException e)
		{
			LOGGER.error("Uuups, something weht wrong! See '{}'.", e.getMessage());

			// Custom error response class used across my project
			ErrorResponse errorResponse = new ErrorResponse(e);

			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.getWriter().write(this.convertObjectToJson(errorResponse));
		}
	}
}
