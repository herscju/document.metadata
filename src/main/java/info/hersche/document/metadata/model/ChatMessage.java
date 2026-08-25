/**
 * https://github.com/eugenp/tutorials/tree/a587d2ec2ea54cb464c6f6e49be738282e582515/spring-websockets
 */
package info.hersche.document.metadata.model;

import info.hersche.document.metadata.api.websocket.MessageType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author herscju
 */
@Getter
@Setter
@ToString
public class ChatMessage
{
	private MessageType type;
	private String from;
	private String text;
	private String user;
	private OutputDataMessage data;

	/**
	 * Default constructor
	 */
	public ChatMessage()
	{
		// Nothing to do here
	}
}
