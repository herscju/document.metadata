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
package info.hersche.document.metadata.storage;

import java.awt.image.BufferedImage;
import java.nio.file.Path;

import org.apache.commons.imaging.bytesource.ByteSource;

import info.hersche.document.metadata.image.MultiMediaImpl.Type;

/**
 * @author Jürg Hersche (herscju)
 */
public interface MultiMedia
{
	/**
	 * @return
	 */
	public BufferedImage getBufferedImage();

	/**
	 * @return
	 */
	public byte[] getBytes();

	/**
	 * @return
	 */
	public ByteSource getByteSource();

	/**
	 * @return
	 */
	public String getExtension();

	/**
	 * @return
	 */
	public Type getMediaType();

	/**
	 * @return
	 */
	public String getMimeType();

	/**
	 * @return
	 */
	public String getName();

	/**
	 * @return
	 */
	public Path getPath();

	/**
	 * @param bufferedImage
	 */
	public void setBufferedImage(BufferedImage bufferedImage);

	/**
	 * @param bytes
	 */
	public void setBytes(byte[] bytes);

	/**
	 * @param byteSource
	 */
	public void setByteSource(ByteSource byteSource);

	/**
	 * @param extension
	 */
	public void setExtension(String extension);

	/**
	 * @param type
	 */
	public void setMediaType(Type type);

	/**
	 * @param mimeType
	 */
	public void setMimeType(String mimeType);

	/**
	 * @param name
	 */
	public void setName(String name);

	/**
	 * @param path
	 */
	public void setPath(Path path);
}
