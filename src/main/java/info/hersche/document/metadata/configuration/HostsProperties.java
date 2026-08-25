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
@ConfigurationProperties(prefix = "application.hosts.host")
public record HostsProperties(@NotBlank String protocol, @NotBlank String name, @Positive Integer port, @NotBlank String wiki)
{
	// Nothing to do here
}
