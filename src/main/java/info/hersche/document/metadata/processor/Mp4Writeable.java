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
package info.hersche.document.metadata.processor;

import java.io.FileOutputStream;
import java.io.IOException;

import info.hersche.document.metadata.processor.Mp4Writer.Tag;

/**
 * @author Jürg Hersche (herscju)
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
