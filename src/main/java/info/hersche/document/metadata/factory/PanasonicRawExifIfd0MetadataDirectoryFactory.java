/**
 * 
 */
package info.hersche.document.metadata.factory;

import java.util.Set;

import com.drew.metadata.DirectoryNames;

import info.hersche.document.metadata.metadata.MetadataDirectory;
import info.hersche.document.metadata.metadata.PanasonicRawExifIfd0MetadataDirectory;

/**
 * 
 */
public class PanasonicRawExifIfd0MetadataDirectoryFactory extends AbstractDirectoryFactory implements MetadataDirectoryFactory
{
	/**
	 * Constructor
	 * 
	 * @param directoryType
	 */
	public PanasonicRawExifIfd0MetadataDirectoryFactory(DirectoryNames directoryType, Set<String> availableTags)
	{
		super(directoryType, availableTags);
	}

	/**
	 * @return
	 */
	public MetadataDirectory getMetadataDirectory()
	{
		return new PanasonicRawExifIfd0MetadataDirectory(this.directoryType, this.availableTags);
	}
}
