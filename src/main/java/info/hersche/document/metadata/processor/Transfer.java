/**
 * 
 */
package info.hersche.document.metadata.processor;

import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

import lombok.Getter;

/**
 * @author herscju
 */
@Getter
public class Transfer
{
	private RandomAccessFile file;
	private FileChannel channel;

	/**
	 * Constructor
	 * @param file
	 * @param channel
	 */
	public Transfer(RandomAccessFile file, FileChannel channel)
	{
		this.file = file;
		this.channel = channel;
	}

}
