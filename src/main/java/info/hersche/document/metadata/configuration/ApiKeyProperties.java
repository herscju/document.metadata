/**
 * 
 */
package info.hersche.document.metadata.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import jakarta.validation.constraints.NotBlank;

/**
 * @author herscju
 */
@JsonSerialize
@ConfigurationProperties(prefix = "application.keys")
public record ApiKeyProperties(@NotBlank String google, String osm)
{
	// Nothing to do here
}
