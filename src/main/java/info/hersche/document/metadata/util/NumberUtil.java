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
package info.hersche.document.metadata.util;

/**
 * 
 */
public class NumberUtil
{
	/**
	 * https://stackoverflow.com/a/5439547/6487382
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isInteger(String s)
	{
		return NumberUtil.isInteger(s, 10);
	}

	/**
	 * https://stackoverflow.com/a/5439547/6487382
	 * 
	 * @param s
	 * @param radix
	 * @return
	 */
	public static boolean isInteger(String s, int radix)
	{
		if (s.isEmpty())
		{
			return false;
		}

		for (int i = 0; i < s.length(); i++)
		{
			if (i == 0 && s.charAt(i) == '-')
			{
				if (s.length() == 1)
				{
					return false;
				}
				else
				{
					continue;
				}
			}
			if (Character.digit(s.charAt(i), radix) < 0)
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * Private constructor
	 */
	private NumberUtil()
	{
		// Nothing to do here
	}
}
