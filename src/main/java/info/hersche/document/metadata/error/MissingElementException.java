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

import java.lang.reflect.AnnotatedElement;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Exception which is used whenever one element (field, method or constructor) could not be set.
 * 
 * @see MissingElementsException
 * 
 * @author Jürg Hersche (herscju)
 */
@ProviderType
public class MissingElementException extends RuntimeException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4045175398998181395L;

	/**
	 * 
	 */
	private final AnnotatedElement element;

	/**
	 * Constructor
	 * See 'https://github.com/apache/sling-org-apache-sling-models-api/blob/master/src/main/java/org/apache/sling/models/factory/MissingElementException.java'
	 * 
	 * @param element
	 * @param cause
	 */
	public MissingElementException(AnnotatedElement element, Throwable cause)
	{
		super("Could not inject " + element, cause);
		this.element = element;
	}

	/**
	 * @return
	 */
	public AnnotatedElement getElement()
	{
		return this.element;
	}
}
