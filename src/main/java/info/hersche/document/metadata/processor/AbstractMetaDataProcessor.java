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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.TiffDirectoryType;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoAscii;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoBytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drew.metadata.exif.ExifDirectoryBase;
import com.drew.metadata.exif.GpsDirectory;
import com.drew.metadata.mp4.Mp4Directory;

import info.hersche.document.metadata.image.MultiMediaImpl.Type;
import info.hersche.document.metadata.storage.MultiMedia;
import jakarta.validation.constraints.NotNull;

/**
 * 
 */
public abstract class AbstractMetaDataProcessor
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractMetaDataProcessor.class);

	/**
	 * Default constructor
	 */
	public AbstractMetaDataProcessor()
	{
		// Nothing to do here
	}

	/**
	 * https://stackoverflow.com/a/57757312/6487382
	 * https://stackoverflow.com/a/66215172/6487382
	 * 
	 * @param multiMedia
	 * @param writer
	 * @param directories
	 * @param date
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	protected List<TagInfo> updateMediaMetaData( //
			final MultiMedia multiMedia, //
			final MetaDataWriteable writer, //
			final Collection<ExifDirectoryBase> directories, //
			@NotNull final String date) throws FileNotFoundException, IOException
	{
		final List<TagInfo> tags = new ArrayList<>();

		boolean originalDateTimeAdded = false;
		boolean digitizedDateTimeAdded = false;
		for (ExifDirectoryBase directory : directories)
		{
			// Date & Time
			if (directory.containsTag(ExifDirectoryBase.TAG_DATETIME))
			{
				// Write the updated metadata back to the image file
				final TiffDirectoryType directoryType = TiffDirectoryType.TIFF_DIRECTORY_ROOT;
				final TagInfoAscii dateTimeInfo = new TagInfoAscii("DateTime", ExifDirectoryBase.TAG_DATETIME, 20, directoryType);
				writer.prepareRootDirectory(directoryType, dateTimeInfo, date);

				tags.add(dateTimeInfo);
			}

			boolean isDirectory = directory.getName().equals("Exif IFD0") || directory.getName().equals("Exif SubIFD");
			boolean isDateTimeOriginal = directory.containsTag(ExifDirectoryBase.TAG_DATETIME_ORIGINAL);
			boolean isDateTimeDigitized = directory.containsTag(ExifDirectoryBase.TAG_DATETIME_DIGITIZED);
			boolean isStandardScan = multiMedia.getMediaType().equals(Type.STANDARD_SCAN);
			boolean isWhatsAppImage = multiMedia.getMediaType().equals(Type.WHATSAPP_IMAGE);
			boolean addOriginalDateTime = (!originalDateTimeAdded && isDirectory && !isDateTimeOriginal && (isWhatsAppImage || isStandardScan));

			// Date & Time Original
			if (addOriginalDateTime)
			{
				// Write the new metadata back to the image file
				final TiffDirectoryType directoryType = TiffDirectoryType.EXIF_DIRECTORY_EXIF_IFD;
				writer.prepareExifDirectory(directoryType, ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL, date);

				tags.add(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL);

				originalDateTimeAdded = true;
			}

			// Date & Time Digitized
			if (!digitizedDateTimeAdded && isDateTimeDigitized)
			{
				// Write the new metadata back to the image file
				final TiffDirectoryType directoryType = TiffDirectoryType.EXIF_DIRECTORY_EXIF_IFD;
				writer.prepareExifDirectory(directoryType, ExifTagConstants.EXIF_TAG_DATE_TIME_DIGITIZED, date);

				tags.add(ExifTagConstants.EXIF_TAG_DATE_TIME_DIGITIZED);

				digitizedDateTimeAdded = true;
			}

			// GPS Version ID
			if (directory.containsTag(GpsDirectory.TAG_VERSION_ID))
			{
				final byte[] gpsVersionId = directory.getByteArray(GpsDirectory.TAG_VERSION_ID);

				// Write the updated metadata back to the image file
				final TiffDirectoryType directoryType = TiffDirectoryType.EXIF_DIRECTORY_GPS;
				final TagInfoBytes gpsInfo = new TagInfoBytes("GPS Version ID", GpsDirectory.TAG_VERSION_ID, -1, directoryType);
				writer.prepareGpsDirectory(directoryType, gpsInfo, gpsVersionId);

				tags.add(gpsInfo);
			}
		}

		writer.write();

		return tags;
	}

	/**
	 * @param multiMedia
	 * @param writer
	 * @param directories
	 * @param date
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	protected List<TagInfo> updateMovieMetaData( //
			MultiMedia multiMedia, //
			MetaDataWriteable writer, //
			Collection<Mp4Directory> directories, //
			@NotNull String date) throws FileNotFoundException, IOException
	{
		final List<TagInfo> tags = new ArrayList<>();

		writer.write();

		return tags;
	}
}
