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
package info.hersche.document.metadata.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.hersche.document.metadata.storage.MultiMedia;
import lombok.Getter;

/**
 * @author herscju
 */
public class ReceiveImageEvent
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ReceiveImageEvent.class);

	/**
	 * 
	 */
	@Getter
	private MultiMedia multiMedia;

	@Getter
	private String destination;

	@Getter
	private String subscription;

	/**
	 * Constructor
	 * 
	 * @param multiMedia
	 */
	public ReceiveImageEvent(MultiMedia multiMedia)
	{
		this(multiMedia, ProcessorStompSessionHandler.DEST_ALL2JPEG, ProcessorStompSessionHandler.SUB_ALL2JPEG);
	}

	/**
	 * Constructor
	 * 
	 * @param multiMedia
	 * @param destination
	 * @param subscription
	 */
	public ReceiveImageEvent(MultiMedia multiMedia, String destination, String subscription)
	{
		this.multiMedia = multiMedia;
		this.destination = destination;
		this.subscription = subscription;

		LOGGER.info("Class '{}' instantiated successfully.", ReceiveImageEvent.class.getSimpleName());
	}

}
