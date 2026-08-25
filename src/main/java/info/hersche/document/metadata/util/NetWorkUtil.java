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

import java.nio.file.Path;
import java.util.Arrays;

import org.apache.tika.Tika;

import info.hersche.document.metadata.factory.AbstractDocumentFactory.Extension;

/**
 * @author herscju
 *
 */
public class NetWorkUtil
{

	/**
	 * Tika detector
	 */
	private static final Tika TIKA = new Tika();

	/**
	 * Returns a comma separated string of file-extensions of given extensions
	 *
	 * @param extensions
	 * @return Comma separated string
	 */
	public static String buildExtensionList(Extension... extensions)
	{
		if (extensions.length == 0)
		{
			return "";
		}

		StringBuilder sb = new StringBuilder();
		for (Extension extension : extensions)
		{
			if (sb.length() > 0)
			{
				sb.append(",");
			}

			sb.append(".");
			sb.append(extension.getExtension());
		}

		return sb.toString();
	}

	/**
	 * Returns a comma separated string of mimetypes of given extensions
	 *
	 * @param extensions
	 * @return Comma separated string
	 */
	public static String buildMimetypeList(Extension... extensions)
	{
		if (extensions.length == 0)
		{
			return "";
		}

		StringBuilder sb = new StringBuilder();
		for (Extension extension : extensions)
		{
			if (sb.length() > 0)
			{
				sb.append(",");
			}

			sb.append(extension.getMimetype());
		}

		return sb.toString();
	}

	/**
	 * Detects the mimetype of a file
	 *
	 * @param name
	 * @return mimetype string
	 */
	public static String detectMimtype(String name)
	{
		return TIKA.detect(name);
	}

	/**
	 * Detects the mimetype of a file
	 *
	 * @param name
	 * @param extension
	 * @return Mimetype string
	 */
	public static String detectMimtype(String name, String extension)
	{
		StringBuilder sb = new StringBuilder(name.length() + 1 + extension.length());

		sb.append(name);
		sb.append(".");
		sb.append(extension);

		return NetWorkUtil.detectMimtype(sb.toString());
	}

	/**
	 * This technique makes use of stream features to separate the filename using the dot “.” and obtain the part, which indicates the extension.
	 * See 'https://www.geeksforgeeks.org/how-to-get-the-file-extension-in-java/'
	 * 
	 * @param path
	 * @return
	 */
	public static String getExtension(Path path)
	{
		// Extract the filename from the Path and split it into parts
		String[] filenameParts = path.getFileName().toString().split("\\.");

		// Extract the extension using a stream and reduce it to the last part
		return Arrays.stream(filenameParts).reduce((a, b) -> b).orElse("");
	}

	/**
	 * Private constructor
	 */
	private NetWorkUtil()
	{
		// Nothing to do here
	}
}
