/**
 * 
 */
package info.hersche.document.metadata.api.websocket;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

/**
 * 
 */
public enum MessageType {
							@JsonProperty("Chat")
							CHAT("Chat"), //
							@JsonProperty("Join")
							JOIN("Join"), //
							@JsonProperty("Leave")
							LEAVE("Leave");

	/**
	 * @param name
	 * @return
	 */
	public static MessageType getTypeFromName(String name)
	{
		return Arrays.stream(MessageType.values()) //
				.filter(e -> e.name.equalsIgnoreCase(name)) //
				.findFirst() //
				.orElseThrow(() -> new IllegalStateException(String.format("Unsupported type '%s'.", name)));
	}

	/**
	 * 
	 */
	@Getter
	private String name;

	/**
	 * @param name
	 */
	private MessageType(String name)
	{
		this.name = name;
	}
}
