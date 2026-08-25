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

import info.hersche.document.metadata.metadata.GeoLocation;
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
public class OutputImageMetadataMessage
{
	private GeoLocation geoLocation;
	private OutputImageDefaultMetadataMessage info;
	private OutputImageDefaultMetadataDetails general;
	private OutputImageDefaultMetadataDetails exifDetails;
	private OutputImageDefaultMetadataDetails exifSubIfdDetails;
	private OutputImageDefaultMetadataDetails fileSystemDetails;
	private OutputImageDefaultMetadataDetails heifDetails;
	private OutputImageDefaultMetadataDetails iccDetails;
	private OutputImageDefaultMetadataDetails icoDetails;
	private OutputImageDefaultMetadataDetails iptcDetails;
	private OutputImageDefaultMetadataDetails jfifDetails;
	private OutputImageDefaultMetadataDetails jfxxDetails;
	private OutputImageDefaultMetadataDetails jpegDetails;
	private OutputImageDefaultMetadataDetails gpsDetails;
	private OutputImageDefaultMetadataDetails nikonPictureControl1Details;
	private OutputImageDefaultMetadataDetails nikonPictureControl2Details;
	private OutputImageDefaultMetadataDetails nikonType1MakernoteDetails;
	private OutputImageDefaultMetadataDetails nikonType2MakernoteDetails;
	private OutputImageDefaultMetadataDetails pngDetails;
	private OutputImageDefaultMetadataDetails pngChromaticitiesDetails;

	/**
	 * Default constructor
	 */
	public OutputImageMetadataMessage()
	{
		// Nothing to do here
	}
}
