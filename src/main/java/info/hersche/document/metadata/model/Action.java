/**
 * 
 */
package info.hersche.document.metadata.model;

import java.util.Arrays;

import lombok.Getter;

/**
 * 
 */
public enum Action {
					UPLOAD("Upload"),
					DOWNLOAD("Download"),
					MEMORY("Memory");

	/**
	 * @param name
	 * @return
	 */
	public static Action fromName(String name)
	{
		return Arrays.stream(Action.values()) //
				.filter(e -> e.name.equalsIgnoreCase(name)) //
				.findFirst() //
				.orElseThrow(() -> new IllegalStateException(String.format("Unsupported type '%s'.", name)));
	}

	@Getter
	private String name;

	/**
	 * @param name
	 */
	private Action(String name)
	{
		this.name = name;
	}

}
