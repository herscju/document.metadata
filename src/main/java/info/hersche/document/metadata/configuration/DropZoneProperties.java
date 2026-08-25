/**
 * 
 */
package info.hersche.document.metadata.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import jakarta.validation.constraints.Positive;

/**
 * @author herscju
 */
@JsonSerialize
@ConfigurationProperties(prefix = "application.dropzone")
public record DropZoneProperties( //
		String acceptedFiles, //
		Boolean addRemoveLinks, //
		Boolean autoProcessQueue, //
		Boolean autoQueue, //
		String capture, //
		Boolean clickable, //
		Boolean chunking, //
		@Positive Integer chunkSize, //
		Boolean createImageThumbnails, //
		String dictDefaultMessage, //
		Boolean disablePreviews, //
		@Positive Integer filesizeBase, //
		Boolean forceChunking, //
		String hiddenInputContainer, //
		Boolean ignoreHiddenFiles, //
		@Positive Integer maxFiles, //
		@Positive Integer maxFilesize, //
		@Positive Integer maxThumbnailFilesize, //
		Boolean parallelChunkUploads, //
		@Positive Integer parallelUploads, //
		String paramName, //
		String previewsContainer, //
		String renameFile, //
		@Positive Integer resizeHeight, //
		String resizeMimeType, //
		@Positive Integer resizeWidth, //
		String resizeMethod, //
		@Positive Integer resizeQuality, //
		Boolean retryChunks, //
		Boolean retryChunksLimit, //
		@Positive Integer timeout, //
		@Positive Integer thumbnailHeight, //
		String thumbnailMethod, //
		@Positive Integer thumbnailWidth, //
		Boolean uploadMultiple, //
		Boolean withCredentials, //
		Boolean forceFallback)
{
	// Nothing to do here
}
