/**
 * 
 */
package info.hersche.document.metadata.metadata;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.drew.metadata.Directory;
import com.drew.metadata.DirectoryNames;

import info.hersche.document.metadata.model.Detail;

/**
 * 
 */
public class AviMetadataDirectory extends AbstractDirectory implements MetadataDirectory
{
	/**
	 * Constructor
	 * 
	 * @param directoryType
	 * @param availableTags
	 */
	public AviMetadataDirectory(DirectoryNames directoryType, Set<String> availableTags)
	{
		super(directoryType, availableTags);
	}

	/**
	 * @return
	 */
	@Override
	public Set<String> getAvailableTags()
	{
		return super.getAvailableTags();
	}

	/**
	 * @return
	 */
	@Override
	public Map<String, LocalDateTime> getDates()
	{
		return super.getDates();
	}

	/**
	 * @return
	 */
	@Override
	public <T extends Directory> Collection<Detail> getDetails(T directory)
	{
		return super.getDetails(directory);
	}

}
