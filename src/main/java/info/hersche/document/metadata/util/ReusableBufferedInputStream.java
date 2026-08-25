/**
 * Copyright (c) 2024 Jürg Hersche (Green@rt)
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
 * https://jenkov.com/tutorials/java-io/bufferedinputstream.html
 */
package info.hersche.document.metadata.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author herscju
 */
public class ReusableBufferedInputStream extends InputStream
{
	private byte[] buffer = new byte[0];
	private int writeIndex = 0;
	private int readIndex = 0;
	private InputStream source = null;

	/**
	 * Constructor
	 * 
	 * @param buffer
	 */
	public ReusableBufferedInputStream(byte[] buffer)
	{
		this.buffer = buffer;
	}

	/**
	 * @param fileInputStream
	 * @throws IOException 
	 */
	public ReusableBufferedInputStream(FileInputStream fileInputStream) throws IOException
	{
		this.setSource(fileInputStream);
	}

	/**
	 *
	 */
	@Override
	public void close() throws IOException
	{
		this.source.close();
	}

	/**
	 *
	 */
	@Override
	public int read() throws IOException
	{

		if (this.readIndex == this.writeIndex)
		{
			if (this.writeIndex == this.buffer.length)
			{
				this.writeIndex = 0;
				this.readIndex = 0;
			}

			// Data should be read into buffer.
			int bytesRead = this.readBytesIntoBuffer();
			while (bytesRead == 0)
			{
				// Continue until you actually get some bytes !
				bytesRead = this.readBytesIntoBuffer();
			}

			// If no more data could be read in, return -1;
			if (bytesRead == -1)
			{
				return -1;
			}
		}

		return 255 & this.buffer[this.readIndex++];
	}

	/**
	 * @return
	 * @throws IOException
	 */
	private int readBytesIntoBuffer() throws IOException
	{
		int bytesRead = this.source.read(this.buffer, this.writeIndex, this.buffer.length - this.writeIndex);
		this.writeIndex += bytesRead;

		return bytesRead;
	}

	/**
	 * @param source
	 * @return
	 */
	public InputStream setSource(InputStream source)
	{
		this.source = source;
		this.writeIndex = 0;
		this.readIndex = 0;

		return this;
	}

}
