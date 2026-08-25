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
package info.hersche.document.metadata.processor;

import java.util.Optional;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * 
 */
@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true, builderMethodName = "toBuilder")
public class Mp4OutputSet
{
	/**
	 * Title
	 */
	@Builder.Default
	private Optional<Mp4Metadata> title = Optional.empty();
	
	/**
	 * Locale, eg. de / CH / WIN
	 */
	@Builder.Default
	private Optional<Mp4Metadata> locale = Optional.empty();
	
	/**
	 * Date & time of creation
	 */
	@Builder.Default
	private Optional<Mp4Metadata> creationDateTime = Optional.empty();
	
	/**
	 * Date & time of creation
	 */
	@Builder.Default
	private Optional<Mp4Metadata> modificationDateTime = Optional.empty();

	/**
	 * Default constructor
	 */
	public Mp4OutputSet()
	{
		// Nothing to do here
	}
}
