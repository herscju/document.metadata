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
 * https://jenkov.com/tutorials/java-io/bufferedinputstream.html
 */
package info.hersche.document.metadata;

import java.awt.Dimension;

import org.springframework.core.io.Resource;

import lombok.Getter;

/**
 *  @author Jürg Hersche (herscju)
 */
@Getter
public class Movie
{
	private Resource resource;
	private Dimension dimension;
	private int size;

	/**
	 * @param resource
	 * @param dimension
	 */
	public Movie(Resource resource, Dimension dimension, int size)
	{
		this.resource = resource;
		this.dimension = dimension;
		this.size = size;
	}

}
