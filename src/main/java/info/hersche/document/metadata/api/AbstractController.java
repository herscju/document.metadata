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
package info.hersche.document.metadata.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.cache.Cache;
import javax.cache.Cache.Entry;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import info.hersche.document.metadata.configuration.SecurityConfiguration;
import info.hersche.document.metadata.image.MultiMediaImpl.Type;
import info.hersche.document.metadata.model.ActionMessage;
import info.hersche.document.metadata.model.Counter;
import info.hersche.document.metadata.util.Detector;

/**
 * 
 */
public abstract class AbstractController
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractController.class);
	private static final String ARCHIVE_EXTENSION = ".zip";

	/**
	 * @param fileName
	 * @return
	 */
	public static Type getImageType(String fileName)
	{
		Type type = Detector.toBuilder().name(fileName).build().detectMultiMediaType();
		LOGGER.trace("Type of file '{}' is '{}'.", fileName, type);

		return type;
	}

	/**
	 * JSON marshaling and unmarshaling
	 */
	@Autowired
	private ObjectMapper mapper;

	/**
	 * Default constructor
	 */
	protected AbstractController()
	{
		// Nothing to do here

		LOGGER.info("Class '{}' instantiated successfully.", AbstractController.class.getSimpleName());
	}

	/**
	 * https://stackoverflow.com/a/58884880/6487382
	 * https://stackoverflow.com/a/14350841/6487382
	 * 
	 * @param status
	 * @param counter
	 * @param cache
	 * @param name
	 * @param msg
	 * @param authentication
	 * @param userAgent
	 * @return
	 */
	protected ResponseEntity<InputStreamResource> buildResponseEntity(HttpStatus status, Counter counter, Cache<String, byte[]> cache, String name, String msg,
			Authentication authentication, String userAgent)
	{
		String fileName = new StringBuilder(name).append(ARCHIVE_EXTENSION).toString();
		HttpHeaders responseHeaders = this.setHttpHeaders(userAgent, fileName);

		try
		{
			// Temporary file
			File file = File.createTempFile(name, ARCHIVE_EXTENSION);
			file.deleteOnExit();

			// Define total size of input and number of entries in archive
			AtomicLong total = new AtomicLong(0);
			AtomicInteger number = new AtomicInteger(0);

			// Package files
			FileOutputStream fos = new FileOutputStream(file);
			ZipOutputStream zos = new ZipOutputStream(fos);

			Iterator<Cache.Entry<String, byte[]>> iterator = cache.iterator();
			while (iterator.hasNext())
			{
				Entry<String, byte[]> entry = iterator.next();
				String filename = entry.getKey();
				Resource resource = new ByteArrayResource(entry.getValue());

				// New zip entry and copying inputstream to zipOutputStream, after all closing streams
				ZipEntry zipEntry = new ZipEntry(filename);
				zos.putNextEntry(zipEntry);
				InputStream is = resource.getInputStream();
				IOUtils.copy(is, zos);
				zos.flush();
				zos.closeEntry();
				is.close();

				total.addAndGet(resource.contentLength());
				number.incrementAndGet();
				LOGGER.info("File '{}' added to archive '{}' ('{}' of '{}', totally '{}' bytes).", filename, name, number.intValue(),
						counter.getCounter().intValue(), total.longValue());
			}

			long contentLength = file.length();
			responseHeaders.setContentLength(contentLength);

			// Complete the ZIP file
			zos.close();
			fos.close();

			LOGGER.info("ZIP archive ready to download and contains '{}' files. Content length is '{}'. Current status is '{}'.", number.intValue(),
					contentLength, status);

			return ResponseEntity.status(status) //
					.cacheControl(CacheControl.maxAge(0, TimeUnit.SECONDS).mustRevalidate().noTransform()) //
					.contentType(MediaType.parseMediaType("application/octet-stream")) //
					.contentLength(contentLength) //
					.headers(responseHeaders) //
					.body(new InputStreamResource(new FileInputStream(file)));
		}
		catch (IOException e)
		{
			LOGGER.error("Creating file archive for downloading failed!", e);
		}

		return ResponseEntity.noContent().build();

		// return ResponseEntity.status(status) //
		// .cacheControl(CacheControl.maxAge(0, TimeUnit.SECONDS).mustRevalidate().noTransform()) //
		// .contentType(MediaType.parseMediaType("application/octet-stream")) //
		// .contentLength(contentLength) //
		// .headers(responseHeaders) //
		// .body((out) -> {
		//
		// // Define total size of input and number of entries in archive
		// AtomicLong total = new AtomicLong(0);
		// AtomicInteger number = new AtomicInteger(0);
		//
		// // Package files
		// ZipOutputStream zipOutputStream = new ZipOutputStream(out);
		//
		// // Iterate over the list to get entries to be zipped
		// while (iterator.hasNext())
		// {
		// Entry<String, Byte[]> entry = iterator.next();
		// String filename = entry.getKey();
		// Resource resource = new ByteArrayResource(ArrayUtils.toPrimitive(entry.getValue()));
		//
		// // New zip entry and copying inputstream to zipOutputStream, after all closing streams
		// zipOutputStream.putNextEntry(new ZipEntry(filename));
		// InputStream inputStream = resource.getInputStream();
		// IOUtils.copy(inputStream, zipOutputStream);
		// zipOutputStream.closeEntry();
		// inputStream.close();
		//
		// total.addAndGet(resource.contentLength());
		// number.incrementAndGet();
		//
		// LOGGER.info("File '{}' added to archive '{}' ('{}' of '{}', totally '{}' bytes).", filename, name, number.intValue(),
		// counter.getCounter().intValue(), total.longValue());
		// }
		//
		// // Complete the ZIP file
		// zipOutputStream.finish();
		// zipOutputStream.close();
		// });
	}

	/**
	 * @param status
	 * @param file
	 * @param userAgent
	 * @return
	 */
	protected ResponseEntity<String> buildResponseEntity(HttpStatus status, MultipartFile file, String msg, Authentication authentication, String userAgent)
	{
		ResponseEntity<String> response;
		try
		{
			HttpHeaders responseHeaders = this.setHttpHeaders(userAgent, null);
			String name = file.getOriginalFilename();
			String user = this.getUserName(authentication);
			Object payload = name;
			ActionMessage message = ActionMessage.toBuilder() //
					.code(status) //
					.reason(status.getReasonPhrase()) //
					.status(status.value()) //
					.message(msg) //
					.user(user) //
					.payload(payload) //
					.build();

			response = ResponseEntity.status(status) //
					.cacheControl(CacheControl.maxAge(0, TimeUnit.SECONDS).mustRevalidate().noTransform()) //
					.contentType(MediaType.APPLICATION_JSON) //
					.headers(responseHeaders) //
					.body(this.createMessageBody(message));

			LOGGER.trace("Working on file '{}'. Current status is '{}'.", name, status);
		}
		catch (JsonProcessingException e)
		{
			LOGGER.error("Something went wrong while processing and creating response message!", e);
			response = ResponseEntity.noContent().build();
		}

		return response;
	}

	/**
	 * @param status
	 * @param resource
	 * @param filename
	 * @param userAgent
	 * @return
	 */
	protected ResponseEntity<StreamingResponseBody> buildResponseEntity(HttpStatus status, Resource resource, String filename, String msg,
			Authentication authentication, String userAgent)
	{
		ResponseEntity<StreamingResponseBody> response;
		try
		{
			InputStream inputStream = resource.getInputStream();
			StreamingResponseBody responseBody = (outputStream) -> {

				int numberOfBytesToWrite;
				byte[] data = new byte[1024];
				while ((numberOfBytesToWrite = inputStream.read(data, 0, data.length)) != -1)
				{
					outputStream.write(data, 0, numberOfBytesToWrite);
				}

				outputStream.flush();
				inputStream.close();
			};
			HttpHeaders responseHeaders = this.setHttpHeaders(userAgent, filename);

			response = ResponseEntity.status(status) //
					.cacheControl(CacheControl.maxAge(0, TimeUnit.SECONDS).mustRevalidate().noTransform()) //
					.contentType(MediaType.APPLICATION_OCTET_STREAM) //
					.headers(responseHeaders) //
					.body(responseBody);

			LOGGER.trace("Working on file '{}'. Current status is '{}'.", filename, status);
		}
		catch (IOException e)
		{
			LOGGER.error("Something went wrong while processing and creating response message!", e);
			response = ResponseEntity.noContent().build();
		}

		return response;
	}

	/**
	 * @param status
	 * @param userAgent
	 * @return
	 */
	protected ResponseEntity<String> buildResponseEntity(HttpStatus status, String msg, Authentication authentication, String userAgent)
	{
		ResponseEntity<String> response;
		try
		{
			HttpHeaders responseHeaders = this.setHttpHeaders(userAgent, null);
			String user = this.getUserName(authentication);
			ActionMessage message = ActionMessage.toBuilder() //
					.code(status) //
					.reason(status.getReasonPhrase()) //
					.status(status.value()) //
					.message(msg) //
					.user(user) //
					.payload("") //
					.build();

			response = ResponseEntity.status(status) //
					.contentType(MediaType.APPLICATION_JSON) //
					.headers(responseHeaders) //
					.body(this.createMessageBody(message));

			LOGGER.trace("Working on message '{}'. Current status is '{}'.", msg, status);
		}
		catch (JsonProcessingException e)
		{
			LOGGER.error("Something went wrong while processing and creating response message!", e);
			response = ResponseEntity.noContent().build();
		}

		return response;
	}

	/**
	 * @param status
	 * @param filename
	 * @param authentication
	 * @param userAgent
	 * @return
	 */
	protected ResponseEntity<String> buildResponseEntity(HttpStatus status, String filename, String msg, Authentication authentication, String userAgent)
	{
		ResponseEntity<String> response;
		try
		{
			HttpHeaders responseHeaders = this.setHttpHeaders(userAgent, null);
			String user = this.getUserName(authentication);
			Object payload = filename;
			ActionMessage message = ActionMessage.toBuilder() //
					.code(status) //
					.reason(status.getReasonPhrase()) //
					.status(status.value()) //
					.message(msg) //
					.user(user) //
					.payload(payload) //
					.build();

			response = ResponseEntity.status(status) //
					.cacheControl(CacheControl.maxAge(0, TimeUnit.SECONDS).mustRevalidate().noTransform()) //
					.contentType(MediaType.APPLICATION_JSON) //
					.headers(responseHeaders) //
					.body(this.createMessageBody(message));

			LOGGER.trace("Working on file '{}'. Current status is '{}'.", filename, status);
		}
		catch (JsonProcessingException e)
		{
			LOGGER.error("Something went wrong while processing and creating response message!", e);
			response = ResponseEntity.noContent().build();
		}

		return response;
	}

	/**
	 * https://www.baeldung.com/jackson-ignore-null-fields
	 * 
	 * @return
	 * @throws JsonProcessingException
	 */
	private String createMessageBody(ActionMessage message) throws JsonProcessingException
	{
		final String json = this.mapper.writeValueAsString(message);
		if (LOGGER.isTraceEnabled())
		{
			LOGGER.trace("Response JSON is '{}'.", json);
		}

		return json;
	}

	/**
	 * @param authentication
	 * @return
	 */
	private String getUserName(Authentication authentication)
	{
		if (authentication == null)
		{
			return SecurityConfiguration.ANONYMUS;
		}

		return authentication.getName();
	}

	/**
	 * @param userAgent
	 * @return
	 */
	private HttpHeaders setHttpHeaders(String userAgent, String filename)
	{
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("X-User-Agent", userAgent);
		responseHeaders.set(HttpHeaders.CONNECTION, "keep-alive");
		responseHeaders.set(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, max-age=0, must-revalidate");
		if (filename != null)
		{
			responseHeaders.setContentDisposition(ContentDisposition.builder("attachment").filename(filename).build());
		}
		responseHeaders.set(HttpHeaders.EXPIRES, "0");
		responseHeaders.set(HttpHeaders.PRAGMA, "no-cache");

		return responseHeaders;
	}
}
