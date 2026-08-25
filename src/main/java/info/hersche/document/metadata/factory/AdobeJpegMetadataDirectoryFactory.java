/**
 * 
 */
package info.hersche.document.metadata.factory;

import java.util.Set;

import com.drew.metadata.DirectoryNames;

import info.hersche.document.metadata.metadata.AdobeJpegMetadataDirectory;
import info.hersche.document.metadata.metadata.MetadataDirectory;

/**
 * 
 */
public class AdobeJpegMetadataDirectoryFactory extends AbstractDirectoryFactory implements MetadataDirectoryFactory
{
	/**
	 * Constructor
	 * 
	 * @param directoryType
	 * @param availableTags 
	 */
	public AdobeJpegMetadataDirectoryFactory(DirectoryNames directoryType, Set<String> availableTags)
	{
		super(directoryType, availableTags);
	}

	/**
	 * @return
	 */
	public MetadataDirectory getMetadataDirectory()
	{
		return new AdobeJpegMetadataDirectory(this.directoryType, this.availableTags);
	}

}
