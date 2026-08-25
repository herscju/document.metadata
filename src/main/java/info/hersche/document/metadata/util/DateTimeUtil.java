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
package info.hersche.document.metadata.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author herscju
 */
public class DateTimeUtil
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DateTimeUtil.class);

	/**
	 * 
	 */
	public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd. LLL. uuuu - HH:mm:ss'.000' Z");
	public static final Pattern DATETIME_V1_FILTER = Pattern.compile("(\\d{4})(\\d{2})(\\d{2})\\D*(\\d{2})(\\d{2})");
	public static final Pattern DATETIME_V2_FILTER = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2}).*(\\d{2})\\.(\\d{2})\\.(\\d{2})(\\((\\d{1,2})\\))?");
	public static final Pattern DATETIME_V3_FILTER = Pattern.compile("(\\d{4})(\\d{2})(\\d{2})_(\\d{2})(\\d{2})(\\d{2})");
	public static final Pattern DATETIME_V4_FILTER = Pattern.compile("(\\d{4})(\\d{2})(\\d{2})-\\D{2}(\\d{2})(\\d{2})");
	public static final String DATE_SEPARATOR = ":";
	public static final String ZONE_SEPARATOR = "T";
	public static final String TIME_SEPARATOR = ".";

	/**
	 * Calculate the remaining minutes
	 * 
	 * @param data
	 * @return
	 */
	private static String calculateMinutes(final int data)
	{
		return String.format("%02d", (data / 60) % 60);
	}

	/**
	 * Calculate the remaining seconds
	 * 
	 * @param data
	 * @return
	 */
	private static String calculateSeconds(final int data)
	{
		return String.format("%02d", data % 60);
	}

	/**
	 * @param date
	 * @return
	 */
	public static ZonedDateTime convertToZonedDateTime(Date date)
	{
		return ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}

	/**
	 * Get Integer value of matcher group
	 * 
	 * @param matcher
	 * @param group
	 * @return
	 */
	private static int getIntValue(final Matcher matcher, final int group)
	{
		return Integer.valueOf(matcher.group(group)).intValue();
	}

	/**
	 * @return
	 */
	public static String now()
	{
		return FORMATTER.format(ZonedDateTime.now());
	}

	/**
	 * @param pattern
	 * @param fileName
	 * @return
	 */
	public static StringBuilder parse(Pattern pattern, String fileName)
	{
		final StringBuilder fileDate = new StringBuilder();
		final Matcher matcher = pattern.matcher(fileName);

		if (DATETIME_V1_FILTER.equals(pattern) && matcher.find())
		{
			int data = DateTimeUtil.getIntValue(matcher, 5);
			String s = DateTimeUtil.calculateSeconds(data); // Calculate the remaining seconds
			String m = DateTimeUtil.calculateMinutes(data); // Calculate the remaining minutes

			fileDate.append(matcher.group(1)) //
					.append(DATE_SEPARATOR) //
					.append(matcher.group(2)) //
					.append(DATE_SEPARATOR) //
					.append(matcher.group(3)) //
					.append(ZONE_SEPARATOR) //
					.append(matcher.group(4)) //
					.append(TIME_SEPARATOR) //
					.append(m) //
					.append(TIME_SEPARATOR) //
					.append(s) //
					.append(TIME_SEPARATOR) //
					.append("000");

			return fileDate;
		}

		if (DATETIME_V2_FILTER.equals(pattern) && matcher.find())
		{
			fileDate.append(matcher.group(1)) //
					.append(DATE_SEPARATOR) //
					.append(matcher.group(2)) //
					.append(DATE_SEPARATOR) //
					.append(matcher.group(3)) //
					.append(ZONE_SEPARATOR) //
					.append(matcher.group(4)) //
					.append(TIME_SEPARATOR) //
					.append(matcher.group(5)) //
					.append(TIME_SEPARATOR) //
					.append(matcher.group(6)) //
					.append(TIME_SEPARATOR); //

			if (matcher.group(8) != null)
			{
				String s = matcher.group(8);
				fileDate.append(s); //
				if (s.length() == 1)
				{
					fileDate.append("00");
				}
				else
				{
					fileDate.append("0");
				}
			}
			else
			{
				fileDate.append("000");
			}

			return fileDate;
		}

		if (DATETIME_V3_FILTER.equals(pattern) && matcher.find())
		{
			fileDate.append(matcher.group(1)) //
					.append(DATE_SEPARATOR) //
					.append(matcher.group(2)) //
					.append(DATE_SEPARATOR) //
					.append(matcher.group(3)) //
					.append(ZONE_SEPARATOR) //
					.append(matcher.group(4)) //
					.append(TIME_SEPARATOR) //
					.append(matcher.group(5)) //
					.append(TIME_SEPARATOR) //
					.append(matcher.group(6)) //
					.append(TIME_SEPARATOR) //
					.append("000");

			return fileDate;
		}

		if (DATETIME_V4_FILTER.equals(pattern) && matcher.find())
		{
			int data = DateTimeUtil.getIntValue(matcher, 5);
			String s = DateTimeUtil.calculateSeconds(data); // Calculate the remaining seconds
			String m = DateTimeUtil.calculateMinutes(data); // Calculate the remaining minutes

			fileDate.append(matcher.group(1)) //
					.append(DATE_SEPARATOR) //
					.append(matcher.group(2)) //
					.append(DATE_SEPARATOR) //
					.append(matcher.group(3)) //
					.append(ZONE_SEPARATOR) //
					.append(matcher.group(4)) //
					.append(TIME_SEPARATOR) //
					.append(m) //
					.append(TIME_SEPARATOR) //
					.append(s) //
					.append(TIME_SEPARATOR) //
					.append("000");

			return fileDate;
		}

		return fileDate;
	}

	/**
	 * https://stackoverflow.com/a/23885950/6487382
	 * 
	 * @param input
	 * @return
	 */
	public static Date parse(String input)
	{
		return Date.from(ZonedDateTime.parse(input, FORMATTER).toInstant());
	}

	/**
	 * Private constructor
	 */
	private DateTimeUtil()
	{
		// Nothing to do here
	}
}
