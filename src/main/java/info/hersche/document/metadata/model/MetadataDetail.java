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

import com.drew.metadata.DirectoryNames;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Jürg Hersche (herscju)
 */
@Getter
@Setter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true, builderMethodName = "toBuilder")
@JsonIgnoreProperties(value = { "tagName", "description" })
public class MetadataDetail implements Detail
{
	/**
	 * 
	 */
	private String tagName; // See label
	private String description; // See value
	private DirectoryNames directoryType;

	private String directory;
	private String tagTypeHex;
	private int tagTypeInt;

	private String label;
	private String value;
	private String isoDate;
	private String isoTime;

	/**
	 * Default constructor
	 */
	public MetadataDetail()
	{
		// Nothing to do here
	}

	/**
	 * 
	 */
	public void setIsoDate(String isoDate)
	{
		this.isoDate = isoDate;
	}

	/**
	 * 
	 */
	public void setIsoTime(String isoTime)
	{
		this.isoTime = isoTime;
	}
}
