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
package info.hersche.document.metadata.processor;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.imaging.ImagingException;
import org.apache.commons.imaging.formats.tiff.constants.TiffDirectoryType;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoAscii;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoBytes;

/**
 * @author Jürg Hersche (herscju)
 */
public interface MetaDataWriteable
{

	/**
	 * @param type
	 * @param tagInfo
	 * @param value
	 * @throws ImagingException
	 */
	public void prepareExifDirectory(TiffDirectoryType type, TagInfoAscii tagInfo, String value) throws ImagingException;

	/**
	 * @param type
	 * @param gpsInfo
	 * @param gpsVersionId
	 */
	public void prepareGpsDirectory(TiffDirectoryType type, TagInfoBytes gpsInfo, byte[] gpsVersionId) throws ImagingException;

	/**
	 * See 'https://stackoverflow.com/a/66215172/6487382', 'https://stackoverflow.com/a/68942970/6487382', 'https://stackoverflow.com/a/57757312/6487382',
	 * and 'https://github.com/apache/commons-imaging/blob/master/src/test/java/org/apache/commons/imaging/examples/WriteExifMetadataExample.java'
	 * 
	 * @param type
	 * @param tagInfo
	 * @param value
	 * @throws IOException
	 * @throws FileNotFoundException
	 * 
	 */
	public void prepareRootDirectory(TiffDirectoryType type, TagInfoAscii tagInfo, String value) throws ImagingException;

	/**
	 * See 'https://github.com/sannies/mp4parser/blob/master/examples/src/main/java/org/mp4parser/examples/metadata/MetaDataInsert.java'
	 * 
	 * @param outputSet
	 */
	public void prepareRootDirectory(Mp4OutputSet outputSet);

	/**
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public boolean write(); // throws FileNotFoundException, IOException;

}
