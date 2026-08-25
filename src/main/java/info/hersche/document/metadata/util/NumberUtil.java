/**
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
