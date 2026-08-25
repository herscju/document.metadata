/**
 * 
 */
package info.hersche.document.metadata.factory;

import java.util.Set;

import com.drew.metadata.DirectoryNames;

import info.hersche.document.metadata.metadata.KyoceraMakerNoteMetadataDirectory;
import info.hersche.document.metadata.metadata.MetadataDirectory;

/**
 * 
 */
public class KyoceraMakerNoteMetadataDirectoryFactory extends AbstractDirectoryFactory implements MetadataDirectoryFactory
{
	/**
	 * Constructor
	 * 
	 * @param directoryType
	 */
	public KyoceraMakerNoteMetadataDirectoryFactory(DirectoryNames directoryType, Set<String> availableTags)
	{
		super(directoryType, availableTags);
	}

	/**
	 * @return
	 */
	public MetadataDirectory getMetadataDirectory()
	{
		return new KyoceraMakerNoteMetadataDirectory(this.directoryType, this.availableTags);
	}
}
