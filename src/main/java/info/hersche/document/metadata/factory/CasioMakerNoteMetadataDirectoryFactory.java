/**
 * 
 */
package info.hersche.document.metadata.factory;

import java.util.Set;

import com.drew.metadata.DirectoryNames;

import info.hersche.document.metadata.metadata.CasioMakerNoteMetadataDirectory;
import info.hersche.document.metadata.metadata.MetadataDirectory;

/**
 * 
 */
public class CasioMakerNoteMetadataDirectoryFactory extends AbstractDirectoryFactory implements MetadataDirectoryFactory
{
	/**
	 * Constructor
	 * 
	 * @param directoryType
	 */
	public CasioMakerNoteMetadataDirectoryFactory(DirectoryNames directoryType, Set<String> availableTags)
	{
		super(directoryType, availableTags);
	}

	/**
	 * @return
	 */
	public MetadataDirectory getMetadataDirectory()
	{
		return new CasioMakerNoteMetadataDirectory(this.directoryType, this.availableTags);
	}
}
