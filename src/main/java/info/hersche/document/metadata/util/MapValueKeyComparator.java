/**
 * https://stackoverflow.com/a/53081966/6487382
 */
package info.hersche.document.metadata.util;

import java.util.Comparator;
import java.util.Map;

/**
 * @author herscju
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
