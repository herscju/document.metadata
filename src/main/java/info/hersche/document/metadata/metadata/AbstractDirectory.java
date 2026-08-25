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
package info.hersche.document.metadata.metadata;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drew.metadata.Directory;
import com.drew.metadata.DirectoryNames;
import com.drew.metadata.SectionNames;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifDirectoryBase;
import com.drew.metadata.exif.GpsDirectory;
import com.drew.metadata.icc.IccDirectory;
import com.drew.metadata.iptc.IptcDirectory;

import info.hersche.document.metadata.metadata.GeoLocation.GeoLocationBuilder;
import info.hersche.document.metadata.model.Detail;
import info.hersche.document.metadata.model.MetadataDetail;

/**
 * 
 */
public abstract class AbstractDirectory implements MetadataDirectory
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDirectory.class);
	private static final String DATE_PATTERN = "yyyy:MM:dd";
	private static final String TIME1_PATTERN = "HH:mm:ss";
	private static final String TIME2_PATTERN = "HH:mm:ssxxxx";
	private static final List<String> EXCLUDES = AbstractDirectory.compileExcludes(String.valueOf(IccDirectory.TAG_PROFILE_DATETIME));
	private static final List<String> INCLUDES = AbstractDirectory.compileIncludes(String.valueOf(ExifDirectoryBase.TAG_DATETIME), //
			String.valueOf(IptcDirectory.TAG_DATE_CREATED), //
			String.valueOf(IptcDirectory.TAG_TIME_CREATED), //
			String.valueOf(ExifDirectoryBase.TAG_DATETIME_ORIGINAL), //
			String.valueOf(ExifDirectoryBase.TAG_DATETIME_DIGITIZED));

	/**
	 * @param tagTypes
	 * @return
	 */
	private static List<String> compileExcludes(String... tagTypes)
	{
		List<String> excludes = new ArrayList<>();
		excludes.addAll(Arrays.asList(tagTypes));

		return excludes;
	}

	/**
	 * @param tagTypes
	 * @return
	 */
	private static List<String> compileIncludes(String... tagTypes)
	{
		List<String> includes = new ArrayList<>();
		includes.addAll(Arrays.asList(tagTypes));

		return includes;
	}

	/**
	 * 
	 */
	private StringBuilder dateTimeCreated = new StringBuilder();

	/**
	 * 
	 */
	protected DirectoryNames directoryType;

	/**
	 * 
	 */
	protected Set<String> availableTags;

	/**
	 * 
	 */
	private Map<String, LocalDateTime> dates;

	/**
	 * 
	 */
	private GeoLocationBuilder geoLocationBuilder;

	/**
	 * 
	 */
	private String orientation;

	/**
	 * Constructor
	 * 
	 * @param directoryType
	 * @param availableTags
	 */
	public AbstractDirectory(DirectoryNames directoryType, Set<String> availableTags)
	{
		this.directoryType = directoryType;
		this.availableTags = availableTags;
		this.dates = new TreeMap<>();
		this.geoLocationBuilder = GeoLocation.toBuilder();
	}

	/**
	 * Search for GPS data and keep value for later use...
	 * 
	 * @param directoryName
	 * @param tagType
	 * @param tagValue
	 */
	private void buildGeoLocation(String directoryName, String tagType, String tagValue)
	{
		if (directoryName.equalsIgnoreCase(SectionNames.GPS.getLabel()))
		{
			Long typeOfTag = Long.decode(tagType);

			// "0x0001":
			if (GpsDirectory.TAG_LATITUDE_REF == typeOfTag.intValue())
			{
				this.geoLocationBuilder.latitudeRef(tagValue);

				return;
			}
			// "0x0002":
			if (GpsDirectory.TAG_LATITUDE == typeOfTag.intValue())
			{
				this.geoLocationBuilder.latitudeVal(tagValue);

				return;
			}
			// "0x0003":
			if (GpsDirectory.TAG_LONGITUDE_REF == typeOfTag.intValue())
			{
				this.geoLocationBuilder.longitudeRef(tagValue);

				return;
			}
			// "0x0004":
			if (GpsDirectory.TAG_LONGITUDE == typeOfTag.intValue())
			{
				this.geoLocationBuilder.longitudeVal(tagValue);

				return;
			}
			// "0x0005":
			if (GpsDirectory.TAG_ALTITUDE_REF == typeOfTag.intValue())
			{
				this.geoLocationBuilder.altitudeRef(tagValue);

				return;
			}
			// "0x0006":
			if (GpsDirectory.TAG_ALTITUDE == typeOfTag.intValue())
			{
				this.geoLocationBuilder.altitudeVal(tagValue);

				return;
			}
		}
	}

	/**
	 * @param directoryName
	 * @param tagType
	 * @param tagValue
	 * @return
	 */
	private Optional<LocalDateTime> buildLocalDateTime(String directoryName, String tagType, String tagValue)
	{
		/**
		 * Check for tags with
		 * -> 'Date/Time' (0x132)
		 * -> 'Date/Time Original' (0x9003)
		 * -> 'Date/Time Digitized' (0x9004)
		 * -> 'Date Created' (0x0237) plus 'Time Created' (0x023c)
		 * 
		 * Skip following tags
		 * -> 'Profile Date/Time' (0x0018)
		 */
		Long typeOfTag = Long.decode(tagType);
		if (INCLUDES.contains(String.valueOf(typeOfTag)) && !EXCLUDES.contains(String.valueOf(typeOfTag)))
		{
			DateTimeFormatter formatter = null;
			LocalDateTime localDateTime = null;

			DateTimeFormatter date = DateTimeFormatter.ofPattern(DATE_PATTERN);
			DateTimeFormatter time1 = DateTimeFormatter.ofPattern(TIME1_PATTERN);
			DateTimeFormatter time2 = DateTimeFormatter.ofPattern(TIME2_PATTERN);

			// "0x0237"
			if (IptcDirectory.TAG_DATE_CREATED == typeOfTag.intValue())
			{
				this.dateTimeCreated.append(tagValue);

				return Optional.empty();
			}
			// "0x023c"
			if (IptcDirectory.TAG_TIME_CREATED == typeOfTag.intValue())
			{
				if (!tagValue.contains("+"))
				{
					this.dateTimeCreated.append(' ').append(tagValue);
					formatter = new DateTimeFormatterBuilder().append(date).appendLiteral(' ').append(time1).toFormatter(Locale.ENGLISH);
					localDateTime = LocalDateTime.parse(this.dateTimeCreated, formatter);
				}
				else
				{
					this.dateTimeCreated.append('T').append(tagValue);
					formatter = new DateTimeFormatterBuilder().append(date).appendLiteral('T').append(time2).toFormatter(Locale.ENGLISH);
					OffsetDateTime offsetDateTime = OffsetDateTime.parse(this.dateTimeCreated, formatter);
					ZonedDateTime zonedDateTime = offsetDateTime.atZoneSameInstant(ZoneId.of("UTC"));
					localDateTime = zonedDateTime.toLocalDateTime();
				}
				this.dates.put(tagValue, localDateTime);

				return Optional.of(localDateTime);
			}
			// "0x132"
			if (ExifDirectoryBase.TAG_DATETIME == typeOfTag.intValue())
			{
				formatter = new DateTimeFormatterBuilder().append(date).appendLiteral(' ').append(time1).toFormatter(Locale.ENGLISH);
				localDateTime = LocalDateTime.parse(tagValue, formatter);
				this.dates.put(tagValue, localDateTime);

				return Optional.of(localDateTime);
			}
			// "0x9003":
			if (ExifDirectoryBase.TAG_DATETIME_ORIGINAL == typeOfTag.intValue())
			{
				formatter = new DateTimeFormatterBuilder().append(date).appendLiteral(' ').append(time1).toFormatter(Locale.ENGLISH);
				localDateTime = LocalDateTime.parse(tagValue, formatter);
				this.dates.put(tagValue, localDateTime);

				return Optional.of(localDateTime);
			}
			// "0x9004":
			if (ExifDirectoryBase.TAG_DATETIME_DIGITIZED == typeOfTag.intValue())
			{
				formatter = new DateTimeFormatterBuilder().append(date).appendLiteral(' ').append(time1).toFormatter(Locale.ENGLISH);
				localDateTime = LocalDateTime.parse(tagValue, formatter);
				this.dates.put(tagValue, localDateTime);

				return Optional.of(localDateTime);
			}
		}

		return Optional.empty();
	}

	/**
	 * @return
	 */
	@Override
	public Set<String> getAvailableTags()
	{
		return this.availableTags;
	}

	/**
	 * See 'https://stackoverflow.com/a/2864923/6487382'
	 * 
	 * @return
	 */
	@Override
	public Map<String, LocalDateTime> getDates()
	{
		return this.dates;
	}

	/**
	 * @param <T>
	 * @param directory
	 * @return
	 */
	@Override
	public <T extends Directory> Collection<Detail> getDetails(T directory)
	{
		Map<String, Detail> details = new TreeMap<>();
		Collection<Tag> tags = directory.getTags();
		for (Tag tag : tags)
		{
			String directoryName = tag.getDirectoryName();
			String tagName = tag.getTagName();
			String tagType = tag.getTagTypeHex();
			String tagValue = tag.getDescription();
			String keyName = new StringBuilder(directory.getName()).append(".").append(tagType).toString();
			tagValue = (tagValue != null) ? tagValue : "n/a";
			LOGGER.debug("Current tag is '{}' -> '{}' ('{}') with value '{}'. Key is '{}'.", directoryName, tagName, tagType, tagValue, keyName);

			/**
			 * Build date and time information
			 */
			Optional<LocalDateTime> localeDateTime = this.buildLocalDateTime(directory.getName(), tagType, tagValue);
			LOGGER.trace("List of dates is '{}'.", this.dates.values());

			/**
			 * Build GEO location
			 */
			this.buildGeoLocation(directory.getName(), tagType, tagValue);

			/**
			 * 'Exif SubIFD.0xa001' and 'GPS.0x0001' are available multiple times, etc.
			 * Therefore test additionally.
			 */
			if (!this.availableTags.contains(keyName)
					|| (directoryName.equalsIgnoreCase(SectionNames.IPTC.getLabel()) || directoryName.equalsIgnoreCase(SectionNames.GPS.getLabel())))
			{
				String date = "";
				String time = "";
				if (localeDateTime.isPresent())
				{
					date = DateTimeFormatter.ISO_DATE.format(localeDateTime.get());
					time = DateTimeFormatter.ofPattern(TIME1_PATTERN).format(localeDateTime.get());
				}

				Detail detail = MetadataDetail.toBuilder() //
						.label(tagName) //
						.value(tagValue) //
						.isoDate(date) //
						.isoTime(time) //
						.directoryType(this.directoryType) //
						.description(tagValue) //
						.directory(directoryName) //
						.tagName(tagName) //
						.tagTypeHex(tagType) //
						.tagTypeInt(tag.getTagType()) //
						.build();
				details.put(tagType, detail);

				this.availableTags.add(keyName);
			}
		}
		LOGGER.trace("Details are '{}'.", details);

		return details.values();
	}

	/**
	 * @return
	 */
	@Override
	public GeoLocation getLocation()
	{
		return this.geoLocationBuilder.build();
	}
}
