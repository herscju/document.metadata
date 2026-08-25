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
package info.hersche.document.metadata.factory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import org.apache.commons.imaging.ImagingException;

/**
 * 
 */
public interface MetadataFactory
{

	/**
	 * @param source
	 * @return
	 * @throws IOException 
	 * @throws ImagingException 
	 */
	public ImageMetaData getPictureMetaData(Path source) throws ImagingException, IOException;

	/**
	 * @param source
	 * @param filename
	 * @return
	 * @throws IOException 
	 * @throws ImagingException 
	 */
	public ImageMetaData getPictureMetadata(InputStream source, String filename) throws ImagingException, IOException;

	/**
	 * @param source
	 * @return
	 * @throws IOException 
	 * @throws ImagingException 
	 */
	public ImageMetaData getImageMetaData(InputStream source) throws ImagingException, IOException;
}
