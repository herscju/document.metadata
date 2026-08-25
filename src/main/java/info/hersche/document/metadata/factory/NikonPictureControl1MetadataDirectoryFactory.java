/**
 * 
 */
package info.hersche.document.metadata.factory;

import java.util.Set;

import com.drew.metadata.DirectoryNames;

import info.hersche.document.metadata.metadata.MetadataDirectory;
import info.hersche.document.metadata.metadata.NikonPictureControl1MetadataDirectory;

/**
 * 
 */
public class NikonPictureControl1MetadataDirectoryFactory extends AbstractDirectoryFactory implements MetadataDirectoryFactory
{
	/**
	 * Constructor
	 * 
	 * @param directoryType
	 */
	public NikonPictureControl1MetadataDirectoryFactory(DirectoryNames directoryType, Set<String> availableTags)
	{
		super(directoryType, availableTags);
	}

	/**
	 * @return
	 */
	public MetadataDirectory getMetadataDirectory()
	{
		return new NikonPictureControl1MetadataDirectory(this.directoryType, this.availableTags);
	}
}
