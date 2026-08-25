/**
 * 
 */
package info.hersche.document.metadata.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

import jakarta.validation.constraints.NotBlank;

/**
 * @author herscju
 */
@ConfigurationProperties("application.file.download")
public record DownloadStorageProperties(@NotBlank String directory)
{
	// Nothing to do here
}
