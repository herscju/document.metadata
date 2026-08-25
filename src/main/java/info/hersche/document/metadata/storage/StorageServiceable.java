/**
 * 
 */
package info.hersche.document.metadata.storage;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author herscju
 */
public interface StorageServiceable
{
	/**
	 * 
	 */
	public void deleteAll();

	/**
	 * 
	 */
	public void delete(String filename);

	/**
	 * 
	 */
	public void init();

	/**
	 * @param filename
	 * @return
	 */
	public Path load(String filename);

	/**
	 * @return
	 */
	public Stream<Path> loadAll();

	/**
	 * @param filename
	 * @return
	 */
	public Resource loadAsResource(String filename);

	/**
	 * @param file
	 */
	public void store(MultipartFile file);
}
