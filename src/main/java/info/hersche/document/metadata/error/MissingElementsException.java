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
package info.hersche.document.metadata.error;

import java.util.ArrayList;
import java.util.Collection;

import org.osgi.annotation.versioning.ProviderType;

/**
 * @author Jürg Hersche (herscju)
 */
@ProviderType
public class MissingElementsException extends RuntimeException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2785984450534931949L;

	/**
	 * 
	 */
	private Collection<MissingElementException> missingElements;

	/**
	 * Constructor
	 * See 'https://github.com/apache/sling-org-apache-sling-models-api/blob/master/src/main/java/org/apache/sling/models/factory/MissingElementsException.java'
	 * 
	 * @param message
	 */
	public MissingElementsException(String message)
	{
		super(message);
		
		this.missingElements = new ArrayList<MissingElementException>();
	}

	/**
	 * @param e
	 */
	public void addMissingElementExceptions(MissingElementException e)
	{
		// Also add to suppressed list to make sure they appear as well with their full stack traces in the
		// printStackTrace for this throwable
		this.addSuppressed(e);
		
		this.missingElements.add(e);
	}

	/**
	 * @return
	 */
	public boolean isEmpty()
	{
		return this.missingElements.isEmpty();
	}

	/**
	 * @return
	 */
	public Collection<MissingElementException> getMissingElements()
	{
		return missingElements;
	}
}
