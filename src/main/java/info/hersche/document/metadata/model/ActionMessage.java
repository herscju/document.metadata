/**
 * 
 */
package info.hersche.document.metadata.model;

import java.io.Serializable;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import info.hersche.document.metadata.configuration.SecurityConfiguration;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Jürg Hersche
 */
@Component
@ToString
@EqualsAndHashCode
@JsonPropertyOrder({ "code", "status", "reason", "user", "message", "payload" })
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true, builderMethodName = "toBuilder")
public class ActionMessage implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7499054100275754723L;

	/**
	 * Current HTTP status code
	 */
	@Getter
	@Setter
	private HttpStatus code;

	/**
	 * Current HTTP status
	 */
	@Getter
	@Setter
	private int status;

	/**
	 * Current status reason
	 */
	@Getter
	@Setter
	private String reason;

	/**
	 * Current user
	 */
	@Getter
	@Setter
	@Builder.Default
	private String user = SecurityConfiguration.ANONYMUS;

	/**
	 * Current status message
	 */
	@Getter
	@Setter
	private String message;

	/**
	 * Current payload
	 */
	@Getter
	@Setter
	@Builder.Default
	private transient Object payload = new Object();

	/**
	 * Default constructor
	 */
	public ActionMessage()
	{
		// Nothing to do here
	}
}
