/**
 * 
 */
package info.hersche.document.metadata.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

/**
 * @author herscju
 */
@ConfigurationProperties(prefix = "application.file.upload")
public record UploadWatcherProperties(@NotBlank String directory, boolean daemon, @Positive Long pollInterval, @Positive Long quietPeriod)
{
	// Nothing to do here
}
