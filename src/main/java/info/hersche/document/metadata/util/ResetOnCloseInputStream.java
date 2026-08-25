/**
 * https://stackoverflow.com/a/925036/6487382
 */
package info.hersche.document.metadata.util;

import java.io.IOException;
import java.io.InputStream;

/**
 * 
 */
public class ResetOnCloseInputStream extends InputStream
{
	private final InputStream decorated;

	/**
	 * Constructor
	 * 
	 * @param anInputStream
	 */
	public ResetOnCloseInputStream(InputStream anInputStream)
	{
		if (!anInputStream.markSupported())
		{
			throw new IllegalArgumentException("Marking not supported");
		}

		anInputStream.mark(1 << 24); // magic constant: BEWARE
		this.decorated = anInputStream;
	}

	/**
	 *
	 */
	@Override
	public void close() throws IOException
	{
		this.decorated.reset();
	}

	/**
	 *
	 */
	@Override
	public int read() throws IOException
	{
		return this.decorated.read();
	}

}
