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
package info.hersche.document.metadata.storage;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author herscju
 */
public interface StorageServiceable
{
	/**
	 * 
	 */
	public void deleteAll();

	/**
	 * 
	 */
	public void delete(String filename);

	/**
	 * 
	 */
	public void init();

	/**
	 * @param filename
	 * @return
	 */
	public Path load(String filename);

	/**
	 * @return
	 */
	public Stream<Path> loadAll();

	/**
	 * @param filename
	 * @return
	 */
	public Resource loadAsResource(String filename);

	/**
	 * @param file
	 */
	public void store(MultipartFile file);
}
