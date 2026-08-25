/**
 * 
 */
package info.hersche.document.metadata.factory;

import java.util.Set;

import com.drew.metadata.DirectoryNames;

import info.hersche.document.metadata.metadata.MetadataDirectory;
import info.hersche.document.metadata.metadata.OlympusCameraMakerNoteMetadataDirectory;

/**
 * 
 */
public class OlympusCameraMakerNoteMetadataDirectoryFactory extends AbstractDirectoryFactory implements MetadataDirectoryFactory
{
	/**
	 * Constructor
	 * 
	 * @param directoryType
	 */
	public OlympusCameraMakerNoteMetadataDirectoryFactory(DirectoryNames directoryType, Set<String> availableTags)
	{
		super(directoryType, availableTags);
	}

	/**
	 * @return
	 */
	public MetadataDirectory getMetadataDirectory()
	{
		return new OlympusCameraMakerNoteMetadataDirectory(this.directoryType, this.availableTags);
	}
}
