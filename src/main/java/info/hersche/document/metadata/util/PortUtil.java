/**
 * 
 */
package info.hersche.document.metadata.util;

/**
 * 
 */
public class PortUtil
{
	/**
	 * This method to check if a TCP port number is valid or not. See 'https://simplesolution.dev/java-check-a-valid-tcp-port-number/'
	 * 
	 * @param portNumber
	 *            port number
	 * @return true if the port number is valid otherwise return false.
	 */
	public static boolean isValidPort(int portNumber)
	{
		return portNumber >= 0 && portNumber <= 65535;
	}
}
