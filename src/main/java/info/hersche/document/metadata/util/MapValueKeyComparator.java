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
 * https://stackoverflow.com/a/53081966/6487382
 */
package info.hersche.document.metadata.util;

import java.util.Comparator;
import java.util.Map;

/**
 * @author Jürg Hersche (herscju)
 */
public class MapValueKeyComparator<K extends Comparable<? super K>, V extends Comparable<? super V>> implements Comparator<Map.Entry<K, V>>
{
	/**
	 * Default constructor
	 */
	public MapValueKeyComparator()
	{
		// Nothing to do here
	}

	/**
	 *
	 */
	@Override
	public int compare(Map.Entry<K, V> a, Map.Entry<K, V> b)
	{
		// Can reverse a and b position for ascending/descending ordering
		int cmp1 = b.getValue().compareTo(a.getValue());
		if (cmp1 != 0)
		{
			return cmp1;
		}
		else
		{
			// Can reverse a and b position for ascending/descending ordering
			return a.getKey().compareTo(b.getKey()); 
		}
	}
}
