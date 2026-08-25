/**
 * 
 */
package info.hersche.document.metadata.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import jakarta.validation.constraints.NotBlank;

/**
 * @author herscju
 */
@ConfigurationProperties(prefix = "server.ssl")
public record TlsEncryptionProperties(@NotBlank String keyStoreType, @NotBlank String keyStore, @NotBlank String keyStorePassword, @NotBlank String keyAlias,
		@NotBlank String trustStoreType, @NotBlank String trustStore, @NotBlank String trustStorePassword)
{
	// Nothing to do here
}
