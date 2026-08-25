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
 * @author Jürg Hersche (herscju)
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
