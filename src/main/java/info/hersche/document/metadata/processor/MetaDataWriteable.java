/**
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
 * 
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
