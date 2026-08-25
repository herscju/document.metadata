/**
 * 
 */
package info.hersche.document.metadata.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
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
