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
package info.hersche.document.metadata.error;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;

/**
 * @author herscju
 *
 */
@Getter
@Setter
@JsonPropertyOrder({ "code", "status", "timestamp", "dateTime", "message", "data", "stackTrace" })
public class ErrorResponse
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ErrorResponse.class);

	// Customizing dateTime serialization format
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd. MMM yyyy HH:mm:ss:SSS Z")
	private Timestamp dateTime;

	// Customizing timestamp serialization format
	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	private Timestamp timestamp;

	private int code;
	private String status;
	private String message;
	private String stackTrace;
	private Object data;

	/**
	 * Default constructor
	 */
	public ErrorResponse()
	{
		// Create an LocalDateTime object using now(zoneId)
		ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.systemDefault());
		this.timestamp = Timestamp.from(dateTime.toInstant());
		this.dateTime = this.timestamp;

		LOGGER.info("Class '{}' instantiated successfully.", ErrorResponse.class.getSimpleName());
	}

	/**
	 * Constructor
	 * 
	 * @param httpStatus
	 * @param message
	 */
	public ErrorResponse(HttpStatus httpStatus, String message)
	{
		this();

		this.code = httpStatus.value();
		this.status = httpStatus.name();
		this.message = message;
	}

	/**
	 * Constructor
	 * 
	 * @param httpStatus
	 * @param message
	 * @param stackTrace
	 */
	public ErrorResponse(HttpStatus httpStatus, String message, String stackTrace)
	{
		this(httpStatus, message);

		this.stackTrace = stackTrace;
	}

	/**
	 * Constructor
	 * 
	 * @param httpStatus
	 * @param message
	 * @param stackTrace
	 * @param data
	 */
	public ErrorResponse(HttpStatus httpStatus, String message, String stackTrace, Object data)
	{
		this(httpStatus, message, stackTrace);

		this.data = data;
	}

	/**
	 * Constructor
	 * 
	 * @param e
	 */
	public ErrorResponse(RuntimeException e)
	{
		this();

		this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
		this.status = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
		this.message = e.getMessage();
	}
}
