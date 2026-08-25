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
package info.hersche.document.metadata.model;

import java.util.Arrays;

import lombok.Getter;

/**
 * 
 */
public enum Action {
					UPLOAD("Upload"),
					DOWNLOAD("Download"),
					MEMORY("Memory");

	/**
	 * @param name
	 * @return
	 */
	public static Action fromName(String name)
	{
		return Arrays.stream(Action.values()) //
				.filter(e -> e.name.equalsIgnoreCase(name)) //
				.findFirst() //
				.orElseThrow(() -> new IllegalStateException(String.format("Unsupported type '%s'.", name)));
	}

	@Getter
	private String name;

	/**
	 * @param name
	 */
	private Action(String name)
	{
		this.name = name;
	}

}
