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
package info.hersche.document.metadata.model;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import lombok.ToString;

/**
 * @author herscju
 */
@Component
@ToString
public class Counter
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(Counter.class);

	/**
	 * 
	 */
	private AtomicInteger counter = new AtomicInteger(0);

	/**
	 * Default constructor
	 */
	public Counter()
	{
		// Nothing to do here

		LOGGER.info("Class '{}' instantiated successfully.", Counter.class.getSimpleName());
	}

	/**
	 * @return
	 */
	public int decrement()
	{
		return this.counter.decrementAndGet();
	}

	/**
	 * @return
	 */
	public AtomicInteger getCounter()
	{
		return this.counter;
	}

	/**
	 * @return
	 */
	public int increment()
	{
		return this.counter.incrementAndGet();
	}

	/**
	 * 
	 */
	public void reset()
	{
		this.counter = new AtomicInteger(0);
	}
}
