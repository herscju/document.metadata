/**
 * 
 */
package info.hersche.document.metadata.metadata;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.drew.metadata.Directory;

import info.hersche.document.metadata.model.Detail;

/**
 * 
 */
public interface MetadataDirectory
{
	/**
	 * @return
	 */
	public Set<String> getAvailableTags();

	/**
	 * @return
	 */
	public Map<String, LocalDateTime> getDates();

	/**
	 * @param <T>
	 * @param directory
	 * @return
	 */
	public <T extends Directory> Collection<Detail> getDetails(T directory);

	/**
	 * @return
	 */
	public GeoLocation getLocation();
}
