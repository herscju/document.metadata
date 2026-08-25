/**
 * Copyright (c) 2024 - 2026 Jürg Hersche (Green@rt)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package info.hersche.document.metadata.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import jakarta.validation.constraints.Positive;

/**
 * @author Jürg Hersche (herscju)
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
