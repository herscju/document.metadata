/**
 * 
 */
package info.hersche.document.metadata;

import info.hersche.document.metadata.image.MultiMediaImpl.Type;
import lombok.Getter;

/**
 * 
 */
@Getter
public class FileType
{
	private String name;
	private Type type;
	
	/**
	 * @param name
	 * @param type
	 */
	public FileType(String name, Type type)
	{
		this.name = name;
		this.type = type;
	}

}
