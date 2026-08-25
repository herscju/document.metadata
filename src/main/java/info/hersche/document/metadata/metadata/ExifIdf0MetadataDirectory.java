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
package info.hersche.document.metadata.metadata;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.drew.metadata.Directory;
import com.drew.metadata.DirectoryNames;

import info.hersche.document.metadata.model.Detail;

/**
 * @author Jürg Hersche (herscju)
 */
public class ExifIdf0MetadataDirectory extends AbstractDirectory implements MetadataDirectory
{
	/**
	 * Constructor
	 * 
	 * @param directoryType
	 * @param availableTags
	 */
	public ExifIdf0MetadataDirectory(DirectoryNames directoryType, Set<String> availableTags)
	{
		super(directoryType, availableTags);
	}

	/**
	 * @return
	 */
	@Override
	public Set<String> getAvailableTags()
	{
		return super.getAvailableTags();
	}

	/**
	 * @return
	 */
	@Override
	public Map<String, LocalDateTime> getDates()
	{
		return super.getDates();
	}

	/**
	 * @return
	 */
	@Override
	public <T extends Directory> Collection<Detail> getDetails(T directory)
	{
		return super.getDetails(directory);
	}

}
