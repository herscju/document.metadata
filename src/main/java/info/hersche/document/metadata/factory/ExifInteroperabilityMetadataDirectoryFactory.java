/**
 * 
 */
package info.hersche.document.metadata.factory;

import java.util.Set;

import com.drew.metadata.DirectoryNames;

import info.hersche.document.metadata.metadata.ExifInteroperabilityMetadataDirectory;
import info.hersche.document.metadata.metadata.MetadataDirectory;

/**
 * 
 */
public class ExifInteroperabilityMetadataDirectoryFactory extends AbstractDirectoryFactory implements MetadataDirectoryFactory
{
	/**
	 * Constructor
	 * 
	 * @param directoryType
	 */
	public ExifInteroperabilityMetadataDirectoryFactory(DirectoryNames directoryType, Set<String> availableTags)
	{
		super(directoryType, availableTags);
	}

	/**
	 * @return
	 */
	public MetadataDirectory getMetadataDirectory()
	{
		return new ExifInteroperabilityMetadataDirectory(this.directoryType, this.availableTags);
	}
}
