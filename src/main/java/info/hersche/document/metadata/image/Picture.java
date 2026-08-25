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
package info.hersche.document.metadata.image;

import java.awt.Dimension;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

import org.apache.commons.imaging.ImagingException;

import com.drew.imaging.ImageProcessingException;

import info.hersche.document.metadata.model.OutputImageMetadataMessage;

/**
 * @author Jürg Hersche (herscju)
 * @version 18-Jun-2014
 */
public interface Picture
{
	/**
	 * Static member
	 */
	public static final int THUMBNAIL_WIDTH = 150;
	public static final int THUMBNAIL_HEIGHT = 150;

	/**
	 * WhatsApp Bild 2025-03-05 um 18.43.53_9562d8f6
	 * WhatsApp Image 2025-02-09 at 17.27.57
	 * WhatsApp Image 2025-02-09 at 15.18.09(1)
	 */
	public static final Pattern ST_SCN_V1 = Pattern.compile("^SCN_{1,2}\\D{1}\\d{2}_\\d{5}\\.\\w{1,}$", Pattern.CASE_INSENSITIVE);
	public static final Pattern ST_VID_V1 = Pattern.compile("^VID_(\\d{8})_(\\d{6})\\.(mp4)$", Pattern.CASE_INSENSITIVE);
	public static final Pattern WA_IMG_V1 = Pattern.compile("^IMG-\\d{8}-[awAW]{2}\\d{4}\\.\\w{1,}$", Pattern.CASE_INSENSITIVE);
	public static final Pattern WA_IMG_V2 = Pattern
			.compile("^[\\w ]*\\d{4}-\\d{2}-\\d{2} \\w* \\d{2}\\.\\d{2}\\.\\d{2}(\\(\\d*\\))?([_a-zA-Z0-9]*)?\\.\\w{1,}$", Pattern.CASE_INSENSITIVE);
	public static final Pattern WA_STA_V1 = Pattern.compile("^[\\w ]* [a-z0-9]*\\.\\w{1,}$", Pattern.CASE_INSENSITIVE);
	public static final Pattern WA_VID_V1 = Pattern.compile("^VID-(\\d{8})-[awAW]{2}(\\d{4})\\.\\w{1,}$", Pattern.CASE_INSENSITIVE);

	/**
	 * Convert image to Base64 encoded string for further use in HTML pages
	 * 
	 * @param doGenerateImage
	 * @param targetDimension 
	 * @param sourceDimension 
	 * @return
	 * @throws IOException 
	 */
	String generateImage(boolean doGenerateImage, Dimension sourceDimension, Dimension targetDimension) throws IOException;

	/**
	 * Get the thumbnail as Base64 encoded string for further use in HTML pages
	 * or CSS files.
	 *
	 * @param doGenerateThumbnail
	 * @param sourceDimension
	 * @param targetDimension
	 * @return Thumbnail
	 * @throws IOException
	 */
	public String generateThumbnail(boolean doGenerateThumbnail, Dimension sourceDimension, Dimension targetDimension) throws IOException;

	/**
	 * @return
	 */
	public LocalDateTime getOriginalDate();

	/**
	 * @return
	 * @throws ImagingException
	 * @throws IOException
	 * @throws ImageProcessingException
	 */
	public OutputImageMetadataMessage readMetadata() throws ImagingException, IOException, ImageProcessingException;

}
