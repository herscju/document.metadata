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

import java.time.LocalDateTime;

import com.drew.metadata.DirectoryNames;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * @author Jürg Hersche (herscju)
 */
@JsonDeserialize(as = MetadataDetail.class)
public interface Detail
{
	/**
	 * Get the name of the tag, such as Aperture, or InteropVersion.
	 * 
	 * @return
	 */
	@JsonIgnore
	public String getTagName();

	/**
	 * Get a description of the tag's value, considering enumerated values and units.
	 * 
	 * @return
	 */
	@JsonIgnore
	public String getDescription();

	/**
	 * Get the name of the com.drew.metadata.Directory in which the tag exists, such as Exif, GPS or Interoperability.
	 * 
	 * @return
	 */
	public String getDirectory();

	/**
	 * Gets the tag type in hex notation as a String with padded leading zeroes if necessary (i.e. 0x100e).
	 * 
	 * @return
	 */
	public String getTagTypeHex();

	/**
	 * Gets the tag type as an int.
	 * 
	 * @return
	 */
	public int getTagTypeInt();

	/**
	 * Gets the label of the attribute
	 * 
	 * @return
	 */
	public String getLabel();

	/**
	 * Gets the value of the attribute
	 * 
	 * @return
	 */
	public String getValue();

	/**
	 * Get the type of the directory
	 */
	public DirectoryNames getDirectoryType();
	
	/**
	 * Set the value as ISO date string.
	 * 
	 * @param isoDate
	 */
	public void setIsoDate(String isoDate);

	/**
	 * Set the value as ISO time string.
	 * 
	 * @param isoTime
	 */
	public void setIsoTime(String isoTime);
}
