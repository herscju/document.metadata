/**
 * 
 */
package info.hersche.document.metadata.processor;

import java.util.Optional;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * 
 */
@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true, builderMethodName = "toBuilder")
public class Mp4OutputSet
{
	/**
	 * Title
	 */
	@Builder.Default
	private Optional<Mp4Metadata> title = Optional.empty();
	
	/**
	 * Locale, eg. de / CH / WIN
	 */
	@Builder.Default
	private Optional<Mp4Metadata> locale = Optional.empty();
	
	/**
	 * Date & time of creation
	 */
	@Builder.Default
	private Optional<Mp4Metadata> creationDateTime = Optional.empty();
	
	/**
	 * Date & time of creation
	 */
	@Builder.Default
	private Optional<Mp4Metadata> modificationDateTime = Optional.empty();

	/**
	 * Default constructor
	 */
	public Mp4OutputSet()
	{
		// Nothing to do here
	}
}
