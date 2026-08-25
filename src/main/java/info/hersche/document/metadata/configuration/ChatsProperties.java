/**
 * 
 */
package info.hersche.document.metadata.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

/**
 * @author herscju
 */
@ConfigurationProperties(prefix = "application.chats.stomp")
public record ChatsProperties(@NotBlank String protocol, @NotBlank String name, @Positive Integer port, @NotBlank String push)
{
	// Nothing to do here
}
