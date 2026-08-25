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

import java.util.Set;

import com.drew.metadata.DirectoryNames;

/**
 * 
 */
public class AbstractMetadataFactory
{
	/**
	 * @param directoryType
	 * @param availableTags 
	 * @return
	 */
	public static MetadataDirectoryFactory getMetadataFactory(DirectoryNames directoryType, Set<String> availableTags)
	{
		switch (directoryType)
		{
			case ADOBE_JPEG:
				return new AdobeJpegMetadataDirectoryFactory(directoryType, availableTags);

			case AVI:
				return new AviMetadataDirectoryFactory(directoryType, availableTags);

			case BMP:
				return new BmpMetadataDirectoryFactory(directoryType, availableTags);

			case EPS:
				return new EpsMetadataDirectoryFactory(directoryType, availableTags);

			case EXIF_IFD0:
				return new ExifIdf0MetadataDirectoryFactory(directoryType, availableTags);

			case EXIF_IMAGE:
				return new ExifImageMetadataDirectoryFactory(directoryType, availableTags);

			case EXIF_INTEROPERABILITY:
				return new ExifInteroperabilityMetadataDirectoryFactory(directoryType, availableTags);

			case EXIF_SUBIFD:
				return new ExifSubIfdMetadataDirectoryFactory(directoryType, availableTags);

			case EXIF_THUMBNAIL:
				return new ExifThumbnailMetadataDirectoryFactory(directoryType, availableTags);

			case PANASONIC_RAW_DISTORSION_INFO:
				return new PanasonicRawDistorsionInfoMetadataDirectoryFactory(directoryType, availableTags);

			case PANASONIC_RAW_EXIF_IFD0:
				return new PanasonicRawExifIfd0MetadataDirectoryFactory(directoryType, availableTags);

			case PANASONIC_RAW_WB_INFO1:
				return new PanasonicRawWbInfo1MetadataDirectoryFactory(directoryType, availableTags);

			case PANASONIC_RAW_WB_INFO2:
				return new PanasonicRawWbInfo2MetadataDirectoryFactory(directoryType, availableTags);

			case PRINT_IM:
				return new PrintImMetadataDirectoryFactory(directoryType, availableTags);

			case APPLE_MAKERNOTE:
				return new AppleMakerNoteMetadataDirectoryFactory(directoryType, availableTags);

			case APPLE_RUNTIME:
				return new AppleRuntimeMetadataDirectoryFactory(directoryType, availableTags);

			case CANON_MAKERNOTE:
				return new CanonMakerNoteMetadataDirectoryFactory(directoryType, availableTags);

			case CASIO_MAKERNOTE:
				return new CasioMakerNoteMetadataDirectoryFactory(directoryType, availableTags);

			case FUJIFILM_MAKERNOTE:
				return new FujiFilmMakerNoteMetadataDirectoryFactory(directoryType, availableTags);

			case KODAK_MAKERNOTE:
				return new KodakMakerNoteMetadataDirectoryFactory(directoryType, availableTags);

			case KYOCERA_MAKERNOTE:
				return new KyoceraMakerNoteMetadataDirectoryFactory(directoryType, availableTags);

			case LEICA_MAKERNOTE:
				return new LeicaMakerNoteMetadataDirectoryFactory(directoryType, availableTags);

			case NIKON_PICTURE_CONTROL1:
				return new NikonPictureControl1MetadataDirectoryFactory(directoryType, availableTags);

			case NIKON_PICTURE_CONTROL2:
				return new NikonPictureControl2MetadataDirectoryFactory(directoryType, availableTags);

			case NIKON_MAKERNOTE:
				return new NikonMakerNoteMetadataDirectoryFactory(directoryType, availableTags);

			case OLYMPUS_CAMERA_MAKERNOTE:
				return new OlympusCameraMakerNoteMetadataDirectoryFactory(directoryType, availableTags);

			case OLYMPUS_EQUIPMENT_MAKERNOTE:
				return new OlympusEquipmentMakerNoteMetadataDirectoryFactory(directoryType, availableTags);

			case OLYMPUS_FOCUS_MAKERNOTE:
				return new OlympusFocusMakerNoteMetadataDirectoryFactory(directoryType, availableTags);

			//
			case FILE_SYSTEM:
				return new FileSystemMetadataDirectoryFactory(directoryType, availableTags);

			case FILE_TYPE:
				return new FileTypeMetadataDirectoryFactory(directoryType, availableTags);

			case GPS:
				return new GpsMetadataDirectoryFactory(directoryType, availableTags);

			//
			case HEIF:
				return new HeifMetadataDirectoryFactory(directoryType, availableTags);

			//
			case ICC:
				return new IccMetadataDirectoryFactory(directoryType, availableTags);

			case ICO:
				return new IcoMetadataDirectoryFactory(directoryType, availableTags);

			case IPTC:
				return new IptcMetadataDirectoryFactory(directoryType, availableTags);

			case JFIF:
				return new JfifMetadataDirectoryFactory(directoryType, availableTags);

			case JFXX:
				return new JfxxMetadataDirectoryFactory(directoryType, availableTags);

			case JPEG:
				return new JpegMetadataDirectoryFactory(directoryType, availableTags);

//			case TIFF:
//				
			case NONE:
			default:
				return new NoneMetadataDirectoryFactory(directoryType, availableTags);

		}
	}

	/**
	 * Private constructor
	 */
	private AbstractMetadataFactory()
	{
		// Nothing to do here
	}
}
