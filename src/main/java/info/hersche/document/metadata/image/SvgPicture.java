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
package info.hersche.document.metadata.image;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.apache.commons.imaging.ImagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.hersche.document.metadata.model.OutputImageMetadataMessage;

/**
 * @author herscju
 */
public class SvgPicture implements Picture
{
	/**
	 * 
	 */
	private final static Logger LOGGER = LoggerFactory.getLogger(SvgPicture.class);
	private static final int TYPE = BufferedImage.TYPE_3BYTE_BGR;

	/**
	 * Default constructor
	 */
	public SvgPicture()
	{
		// Nothing to do here
	}

	/**
	 * Constructor
	 * 
	 * @param outputFormat
	 * @param image
	 * @param inputStream 
	 */
	public SvgPicture(String outputFormat, BufferedImage image, InputStream inputStream)
	{
		// TODO Auto-generated constructor stub
	}

	/**
	 * Generate thumbnail of image and return it as Base64 encoded string
	 */
	@Override
	public String generateThumbnail(boolean doGenerateThumbnail, Dimension sourceDimension, Dimension targetDimension) throws IOException
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Convert image to Base64 encoded string
	 * See'https://www.baeldung.com/convert-input-stream-to-string'
	 */
	@Override
	public String generateImage(boolean doGenerateImage, Dimension sourceDimension, Dimension targetDimension) throws IOException
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Get the original date
	 */
	@Override
	public LocalDateTime getOriginalDate()
	{
		return LocalDateTime.now();
	}

	/**
	 * Read full set of image metadata
	 */
	@Override
	public OutputImageMetadataMessage readMetadata() throws ImagingException, IOException
	{
		// TODO Auto-generated method stub
		return null;
	}
}
