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

import java.util.List;

import org.apache.commons.imaging.ImageInfo.ColorType;
import org.apache.commons.imaging.ImageInfo.CompressionAlgorithm;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author herscju
 */
@Getter
@Setter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true, builderMethodName = "toBuilder")
public class OutputImageDefaultMetadataMessage
{
	private int width;
	private int height;
	private int bitsPerPixel;
	private ColorType colorType;
	private List<String> comments;
	private CompressionAlgorithm compressionAlgorithm;
	private SourceImageFormat format;
	private String formatDetails;
	private String formatName;
	private String mimeType;
	private int numberOfImages;
	private int physicalHeightDpi;
	private float physicalHeightInch;
	private int physicalWidthDpi;
	private float physicalWidthInch;
	private boolean progressive;
	private boolean transparent;
	private boolean usesPalette;

	/**
	 * Default constructor
	 */
	public OutputImageDefaultMetadataMessage()
	{
		// Nothing to do here
	}
}
