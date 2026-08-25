/**
 * 
 */
package info.hersche.document.metadata;

import java.awt.Dimension;

import org.springframework.core.io.Resource;

import lombok.Getter;

/**
 * 
 */
@Getter
public class Movie
{
	private Resource resource;
	private Dimension dimension;
	private int size;

	/**
	 * @param resource
	 * @param dimension
	 */
	public Movie(Resource resource, Dimension dimension, int size)
	{
		this.resource = resource;
		this.dimension = dimension;
		this.size = size;
	}

}
