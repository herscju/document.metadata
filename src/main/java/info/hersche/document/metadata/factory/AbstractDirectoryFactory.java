/**
 * 
 */
package info.hersche.document.metadata.factory;

import java.util.Set;

import com.drew.metadata.DirectoryNames;

/**
 * 
 */
public abstract class AbstractDirectoryFactory implements MetadataDirectoryFactory
{
	/**
	 * 
	 */
	protected DirectoryNames directoryType;
	
	/**
	 * 
	 */
	protected Set<String> availableTags;

	/**
	 * Constructor
	 * 
	 * @param directoryType
	 * @param availableTags 
	 */
	public AbstractDirectoryFactory(DirectoryNames directoryType, Set<String> availableTags)
	{
		this.directoryType = directoryType;
		this.availableTags = availableTags;
	}

}
