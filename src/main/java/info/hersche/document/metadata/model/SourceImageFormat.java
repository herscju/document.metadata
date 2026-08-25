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

import org.apache.commons.imaging.ImageFormat;

import lombok.Builder;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 */
@Setter
@ToString
@Builder(toBuilder = true, builderMethodName = "toBuilder")
public class SourceImageFormat implements ImageFormat
{
	private ImageFormat format;

	/**
	 * Default constructor
	 */
	public SourceImageFormat()
	{
		// Nothing to do here
	}

	/**
	 * Constructor
	 */
	public SourceImageFormat(ImageFormat format)
	{
		this.format = format;
	}

	/**
	 *
	 */
	@Override
	public String getDefaultExtension()
	{
		return this.format != null ? this.format.getDefaultExtension() : null;
	}

	/**
	 *
	 */
	@Override
	public String[] getExtensions()
	{
		return this.format != null ? this.format.getExtensions() : null;
	}

	/**
	 *
	 */
	@Override
	public String getName()
	{
		return this.format != null ? this.format.getName() : null;
	}

}
