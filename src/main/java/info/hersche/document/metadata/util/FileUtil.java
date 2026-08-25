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

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jürg Hersche (herscju)
 */
public class FileUtil
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);
	private static final String SUFFIX = ".temp";
	private static final String SEPARATOR = FileSystems.getDefault().getSeparator();

	/**
	 * @param file
	 * @param string
	 */
	public static String addPrefixToFileName(File file, String prefix)
	{
		return new StringBuilder(file.getParent()).append(SEPARATOR).append(prefix).append(file.getName()).toString();
	}

	/**
	 * @param prefix
	 * @param deleteOnExit
	 * @return
	 * @throws IOException
	 */
	public static File createTempFile(String prefix, boolean deleteOnExit) throws IOException
	{
		return FileUtil.createTempFile(prefix, null, deleteOnExit);
	}

	/**
	 * @param prefix
	 * @param suffix
	 * @param deleteOnExit
	 * @return
	 * @throws IOException
	 */
	public static File createTempFile(String prefix, String suffix, boolean deleteOnExit) throws IOException
	{
		if (suffix == null)
		{
			suffix = SUFFIX;
		}

		File file = File.createTempFile(prefix, suffix);
		if (deleteOnExit == true)
		{
			file.deleteOnExit();
		}

		return file;
	}

	/**
	 * Private constructor
	 */
	private FileUtil()
	{
		// Nothing to do here
	}

}
