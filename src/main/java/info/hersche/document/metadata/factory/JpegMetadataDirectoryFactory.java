/**
 * 
 */
package info.hersche.document.metadata.factory;

import java.util.Set;

import com.drew.metadata.DirectoryNames;

import info.hersche.document.metadata.metadata.JpegMetadataDirectory;
import info.hersche.document.metadata.metadata.MetadataDirectory;

/**
 * 
 */
public class JpegMetadataDirectoryFactory extends AbstractDirectoryFactory implements MetadataDirectoryFactory
{
	/**
	 * Constructor
	 * 
	 * @param directoryType
	 */
	public JpegMetadataDirectoryFactory(DirectoryNames directoryType, Set<String> availableTags)
	{
		super(directoryType, availableTags);
	}

	/**
	 * @return
	 */
	public MetadataDirectory getMetadataDirectory()
	{
		return new JpegMetadataDirectory(this.directoryType, this.availableTags);
	}
}
