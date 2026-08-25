/**
 * 
 */
package info.hersche.document.metadata.factory;

import java.util.Set;

import com.drew.metadata.DirectoryNames;

import info.hersche.document.metadata.metadata.MetadataDirectory;
import info.hersche.document.metadata.metadata.PanasonicRawWbInfo2MetadataDirectory;

/**
 * 
 */
public class PanasonicRawWbInfo2MetadataDirectoryFactory extends AbstractDirectoryFactory implements MetadataDirectoryFactory
{
	/**
	 * Constructor
	 * 
	 * @param directoryType
	 */
	public PanasonicRawWbInfo2MetadataDirectoryFactory(DirectoryNames directoryType, Set<String> availableTags)
	{
		super(directoryType, availableTags);
	}

	/**
	 * @return
	 */
	public MetadataDirectory getMetadataDirectory()
	{
		return new PanasonicRawWbInfo2MetadataDirectory(this.directoryType, this.availableTags);
	}
}
