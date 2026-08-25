/**
 * https://github.com/eugenp/tutorials/tree/a587d2ec2ea54cb464c6f6e49be738282e582515/spring-websockets
 */
package info.hersche.document.metadata.model;

import info.hersche.document.metadata.configuration.SecurityConfiguration;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author herscju
 */
@Getter
@Setter
@ToString
@Builder(toBuilder = true, builderMethodName = "toBuilder")
public class OutputMessage
{
	private String from;
	private String text;
	@ToString.Exclude
	private OutputDataMessage data;
	private String user;
	private String time;

	/**
	 * Default constructor
	 */
	public OutputMessage()
	{
		this("", "", OutputDataMessage.toBuilder().build(), SecurityConfiguration.ANONYMUS, "");
	}

	/**
	 * Constructor
	 * 
	 * @param from
	 * @param text
	 * @param time
	 */
	public OutputMessage(final String from, final String text, final String user, final String time)
	{

		this(from, text, OutputDataMessage.toBuilder().build(), user, time);
	}

	/**
	 * Constructor
	 * 
	 * @param from
	 * @param text
	 * @param data
	 * @param time
	 * @param wapp
	 */
	public OutputMessage(final String from, final String text, final OutputDataMessage data, final String user, final String time)
	{
		this.from = from;
		this.text = text;
		this.data = data;
		this.user = user;
		this.time = time;
	}

}
