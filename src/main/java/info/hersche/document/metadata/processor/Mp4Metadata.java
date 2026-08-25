/**
 * 
 */
package info.hersche.document.metadata.processor;

import info.hersche.document.metadata.processor.Mp4Writer.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 */
@Setter
@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true, builderMethodName = "toBuilder")
public class Mp4Metadata
{
	@NotNull
	private Tag tag;

	@NotNull
	private String value;

	/**
	 * Default constructor
	 */
	public Mp4Metadata()
	{
		// Nothing to do here
	}
}
