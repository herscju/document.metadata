/**
 * 
 */
package info.hersche.document.metadata.processor;

import java.io.FileOutputStream;
import java.io.IOException;

import info.hersche.document.metadata.processor.Mp4Writer.Tag;

/**
 * 
 */
public interface Mp4Writeable
{

	/**
	 * @return
	 */
	public long getSize();

	/**
	 * @param outputSet
	 */
	public void prepare(Mp4OutputSet outputSet);

	/**
	 * @param tag
	 * @return
	 */
	public String readMetadata(Tag tag);

	/**
	 * @param target
	 * @return 
	 * @throws IOException
	 * 
	 */
	public long write(FileOutputStream target) throws IOException;
}
