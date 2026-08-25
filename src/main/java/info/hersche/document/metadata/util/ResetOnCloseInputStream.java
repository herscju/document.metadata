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
