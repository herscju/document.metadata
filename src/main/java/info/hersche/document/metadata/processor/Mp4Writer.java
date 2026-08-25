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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.io.IOUtils;
import org.mp4parser.Box;
import org.mp4parser.Container;
import org.mp4parser.IsoFile;
import org.mp4parser.boxes.apple.AppleItemListBox;
import org.mp4parser.boxes.apple.AppleNameBox;
import org.mp4parser.boxes.iso14496.part12.ChunkOffsetBox;
import org.mp4parser.boxes.iso14496.part12.FreeBox;
import org.mp4parser.boxes.iso14496.part12.HandlerBox;
import org.mp4parser.boxes.iso14496.part12.MetaBox;
import org.mp4parser.boxes.iso14496.part12.MovieBox;
import org.mp4parser.boxes.iso14496.part12.MovieHeaderBox;
import org.mp4parser.boxes.iso14496.part12.UserDataBox;
import org.mp4parser.tools.IsoTypeReader;
import org.mp4parser.tools.IsoTypeWriter;
import org.mp4parser.tools.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import com.neovisionaries.i18n.CountryCode;
import com.neovisionaries.i18n.LanguageCode;

import info.hersche.document.metadata.error.MissingElementsException;
import info.hersche.document.metadata.util.DateTimeUtil;
import info.hersche.document.metadata.util.NumberUtil;
import io.hawt.util.Files;

/**
 * @author Jürg Hersche (herscju)
 */
public class Mp4Writer implements Mp4Writeable
{
	/**
	 * 
	 */
	public static class BetterByteArrayOutputStream extends ByteArrayOutputStream
	{
		/**
		 * @return
		 */
		public byte[] getBuffer()
		{
			return buf;
		}
	}

	/**
	 * 
	 */
	public enum Tag {
						TITLE("©nam"),
						LOCALE("©nam"),
						CREATION_DATETIME("mvhd"),
						MODIFICATION_DATETIME("mvhd");

		private String path;

		/**
		 * @param string
		 */
		Tag(String path)
		{
			this.path = path;
		}

		/**
		 * Get path
		 * 
		 * @return
		 */
		public String getPath()
		{
			return this.path;
		}
	}

	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(Mp4Writer.class);
	private static final int LANGUAGE_ID = 32767;
	private static final String DEFAULT_VARIANT = "WIN";
	private static final Locale DEFAULT_LOCALE = new Locale("de", "CH", DEFAULT_VARIANT);

	/**
	 * 
	 */
	private static BidiMap<String, String> language = new DualHashBidiMap<>();

	static
	{
		language.put("0", "English");
		language.put("1", "French");
		language.put("2", "German");
		language.put("3", "Italian");
		language.put("4", "Dutch");
		language.put("5", "Swedish");
		language.put("6", "Spanish");
		language.put("7", "Danish");
		language.put("8", "Portuguese");
		language.put("9", "Norwegian");
		language.put("10", "Hebrew");
		language.put("11", "Japanese");
		language.put("12", "Arabic");
		language.put("13", "Finnish");
		language.put("14", "Greek");
		language.put("15", "Icelandic");
		language.put("16", "Maltese");
		language.put("17", "Turkish");
		language.put("18", "Croatian");
		language.put("19", "Traditional_Chinese");
		language.put("20", "Urdu");
		language.put("21", "Hindi");
		language.put("22", "Thai");
		language.put("23", "Korean");
		language.put("24", "Lithuanian");
		language.put("25", "Polish");
		language.put("26", "Hungarian");
		language.put("27", "Estonian");
		language.put("28", "Lettish");
		language.put("29", "Sami");
		language.put("30", "Faroese");
		language.put("31", "Farsi");
		language.put("32", "Russian");
		language.put("33", "Simplified_Chinese");
		language.put("34", "Flemish");
		language.put("35", "Irish");
		language.put("36", "Albanian");
		language.put("37", "Romanian");
		language.put("38", "Czech");
		language.put("39", "Slovak");
		language.put("40", "Slovenian");
		language.put("41", "Yiddish");
		language.put("42", "Serbian");
		language.put("43", "Macedonian");
		language.put("44", "Bulgarian");
		language.put("45", "Ukrainian");
		language.put("46", "Belarusian");
		language.put("47", "Uzbek");
		language.put("48", "Kazakh");
		language.put("49", "Azerbaijani");
		language.put("50", "AzerbaijanAr");
		language.put("51", "Armenian");
		language.put("52", "Georgian");
		language.put("53", "Moldavian");
		language.put("54", "Kirghiz");
		language.put("55", "Tajiki");
		language.put("56", "Turkmen");
		language.put("57", "Mongolian");
		language.put("58", "MongolianCyr");
		language.put("59", "Pashto");
		language.put("60", "Kurdish");
		language.put("61", "Kashmiri");
		language.put("62", "Sindhi");
		language.put("63", "Tibetan");
		language.put("64", "Nepali");
		language.put("65", "Sanskrit");
		language.put("66", "Marathi");
		language.put("67", "Bengali");
		language.put("68", "Assamese");
		language.put("69", "Gujarati");
		language.put("70", "Punjabi");
		language.put("71", "Oriya");
		language.put("72", "Malayalam");
		language.put("73", "Kannada");
		language.put("74", "Tamil");
		language.put("75", "Telugu");
		language.put("76", "Sinhala");
		language.put("77", "Burmese");
		language.put("78", "Khmer");
		language.put("79", "Lao");
		language.put("80", "Vietnamese");
		language.put("81", "Indonesian");
		language.put("82", "Tagalog");
		language.put("83", "MalayRoman");
		language.put("84", "MalayArabic");
		language.put("85", "Amharic");
		language.put("87", "Galla");
		language.put("87", "Oromo");
		language.put("88", "Somali");
		language.put("89", "Swahili");
		language.put("90", "Kinyarwanda");
		language.put("91", "Rundi");
		language.put("92", "Nyanja");
		language.put("93", "Malagasy");
		language.put("94", "Esperanto");
		language.put("128", "Welsh");
		language.put("129", "Basque");
		language.put("130", "Catalan");
		language.put("131", "Latin");
		language.put("132", "Quechua");
		language.put("133", "Guarani");
		language.put("134", "Aymara");
		language.put("135", "Tatar");
		language.put("136", "Uighur");
		language.put("137", "Dzongkha");
		language.put("138", "JavaneseRom");
		language.put(String.valueOf(LANGUAGE_ID), "Unspecified");
	}

	/**
	 * @param name
	 * @return
	 */
	private static int getCountryIdByName(String name)
	{
		return 0;
	}

	/**
	 * @param dataCountry
	 * @return
	 */
	public static String getCountryNameById(int dataCountry)
	{
		return DEFAULT_LOCALE.getDisplayCountry(Locale.UK);
	}

	/**
	 * @param name
	 * @return
	 */
	public static int getLanguageIdByName(String name)
	{
		String id = Mp4Writer.language.getKey(name);

		if (NumberUtil.isInteger(id))
		{
			return Integer.valueOf(id).intValue();
		}

		return LANGUAGE_ID;
	}

	/**
	 * @param id
	 * @return
	 */
	public static String getLanguageNameById(int id)
	{
		String language = Mp4Writer.language.get(String.valueOf(id));

		if (language == null)
		{
			ByteBuffer b = ByteBuffer.wrap(new byte[2]);
			IsoTypeWriter.writeUInt16(b, id);
			b.reset();

			return new Locale(IsoTypeReader.readIso639(b)).getDisplayLanguage();
		}
		else
		{
			return language;
		}
	}

	/**
	 * Members set by constructor
	 */
	private byte[] bytes;

	/**
	 * 
	 */
	private Mp4OutputSet outputSet;
	
	/**
	 * 
	 */
	private BetterByteArrayOutputStream target;

	/**
	 * 
	 */
	private IsoFile isoFile;
	private List<MovieBox> boxes;
	private MovieBox moovBox;
	private FreeBox freeBox;
	private MovieHeaderBox mvhdBox;
	private UserDataBox udtaBox;
	private MetaBox metaBox;
	private AppleItemListBox ilstBox;
	private AppleNameBox anamBox;

	private boolean correctOffset;
	private long sizeBefore;
	private long sizeAfter;
	private long offset;

	/**
	 * @param path
	 * @param file
	 * @throws IOException
	 */
	public Mp4Writer(@NonNull File file) throws IOException
	{
		this.bytes = Files.readBytes(file);

		LOGGER.info("Class '{}' instantiated successfully.", Mp4Writer.class.getSimpleName());
	}

	/**
	 * @param path
	 * @param source
	 * @throws IOException
	 */
	public Mp4Writer(@NonNull InputStream source) throws IOException
	{
		ByteArrayOutputStream target = new ByteArrayOutputStream();
		IOUtils.copy(source, target);
		this.bytes = target.toByteArray();

		LOGGER.info("Class '{}' instantiated successfully.", Mp4Writer.class.getSimpleName());
	}

	/**
	 * @param movieBox
	 * @param correction
	 */
	private void correctChunkOffsets(MovieBox movieBox, long correction)
	{
		List<ChunkOffsetBox> chunkOffsetBoxes = Path.getPaths((Box) movieBox, "trak/mdia[0]/minf[0]/stbl[0]/stco[0]");
		if (chunkOffsetBoxes.isEmpty())
		{
			chunkOffsetBoxes = Path.getPaths((Box) movieBox, "trak/mdia[0]/minf[0]/stbl[0]/st64[0]");
		}
		for (ChunkOffsetBox chunkOffsetBox : chunkOffsetBoxes)
		{
			long[] cOffsets = chunkOffsetBox.getChunkOffsets();
			for (int i = 0; i < cOffsets.length; i++)
			{
				cOffsets[i] += correction;
			}
		}
	}

	/**
	 * @param fos
	 * @return
	 * @throws IOException
	 */
	private long finalize(FileOutputStream fos) throws IOException
	{
		fos.write(this.target.getBuffer());

		return this.getSize();
	}

	/**
	 * @param container
	 * @return
	 */
	private FreeBox findFreeBox(Container container)
	{
		for (Box box : container.getBoxes())
		{
			if (box instanceof FreeBox)
			{
				LOGGER.trace("Box type '{}' -> Existing free box.", box.getType());

				return (FreeBox) box;
			}

			if (box instanceof Container)
			{
				FreeBox freeBox = this.findFreeBox((Container) box);
				if (freeBox != null)
				{
					LOGGER.trace("Box type '{}' -> New container box.", box.getType());

					return freeBox;
				}
			}

			LOGGER.trace("Box type '{}'.", box.getType());
		}

		return null;
	}

	/**
	 * @param tag
	 * @return
	 */
	private String getAppleNameBoxLocale(Tag tag)
	{
		String fullPath = new StringBuilder("moov[0]/udta[0]/meta[0]/ilst/").append(tag.getPath()).toString();
		LOGGER.trace("Full path to '{}' is '{}'.", tag, fullPath);

		AppleNameBox anam = Path.getPath(this.isoFile, fullPath);
		if (anam != null)
		{
			String countryName = Mp4Writer.getCountryNameById(anam.getDataCountry());
			List<CountryCode> countryNames = CountryCode.findByName(countryName);
			String languageName = Mp4Writer.getLanguageNameById(anam.getDataLanguage());
			List<LanguageCode> languageNames = LanguageCode.findByName(languageName);
			Locale locale = new Locale(languageNames.get(0).toLocale().getLanguage(), countryNames.get(0).getAlpha2(), DEFAULT_VARIANT);

			return locale.toLanguageTag();
		}

		return "";
	}

	/**
	 * @param path
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private String getAppleNameBoxValue(Tag tag)
	{
		String fullPath = new StringBuilder("moov[0]/udta[0]/meta[0]/ilst/").append(tag.getPath()).toString();
		LOGGER.trace("Full path to '{}' is '{}'.", tag, fullPath);

		AppleNameBox anam = Path.getPath(this.isoFile, fullPath);
		if (anam != null)
		{
			return anam.getValue();
		}

		return "";
	}

	/**
	 * @param tag
	 * @return
	 */
	private String getMovieHeaderBox(Tag tag)
	{
		String fullPath = new StringBuilder("moov[0]/").append(tag.getPath()).toString();
		LOGGER.trace("Full path to '{}' is '{}'.", tag, fullPath);

		MovieHeaderBox mvhd = Path.getPath(this.isoFile, fullPath);

		if (mvhd != null)
		{
			switch (tag)
			{
				case CREATION_DATETIME:
					return DateTimeUtil.FORMATTER.format(DateTimeUtil.convertToZonedDateTime(mvhd.getCreationTime()));

				case MODIFICATION_DATETIME:
					return DateTimeUtil.FORMATTER.format(DateTimeUtil.convertToZonedDateTime(mvhd.getModificationTime()));

				default:
					return "";
			}
		}

		return "";
	}

	/**
	 *
	 */
	@Override
	public long getSize()
	{
		return this.target.getBuffer().length;
	}

	/**
	 * @return
	 */
	private boolean needsOffsetCorrection()
	{
		if (Path.getPath(this.isoFile, "moov[0]/mvex[0]") != null)
		{
			// Fragmented files don't need a correction
			return false;
		}
		else
		{
			// no correction needed if mdat is before moov as insert into moov want change the offsets of mdat
			for (Box box : this.isoFile.getBoxes())
			{
				if ("moov".equals(box.getType()))
				{
					return true;
				}
				if ("mdat".equals(box.getType()))
				{
					return false;
				}
			}

			throw new RuntimeException("I need 'moov' or 'mdat'. Otherwise all this doesn't make sense.");
		}
	}

	/**
	 *
	 */
	@Override
	public void prepare(@NonNull Mp4OutputSet outputSet)
	{
		this.outputSet = outputSet;

		boolean isBasicPreparation = this.prepareStructure();

		if (isBasicPreparation)
		{
			Optional<Mp4Metadata> titleData = this.outputSet.getTitle();
			Optional<Mp4Metadata> localeData = this.outputSet.getLocale();
			Locale locale = new Locale.Builder() //
					.setLanguageTag(localeData.orElse( //
							Mp4Metadata.toBuilder() //
									.tag(Tag.LOCALE) //
									.value(Locale.UK.toLanguageTag()) //
									.build()) //
							.getValue()) //
					.build();
			String language = locale.getDisplayLanguage(Locale.ENGLISH);
			String country = locale.getDisplayCountry(Locale.ENGLISH);
			Optional<Mp4Metadata> creationDateTimeData = this.outputSet.getCreationDateTime();
			Optional<Mp4Metadata> modificationDateTimeData = this.outputSet.getModificationDateTime();
			String now = DateTimeUtil.now();
			Date creationDateTime = DateTimeUtil.parse(creationDateTimeData.orElse( //
					Mp4Metadata.toBuilder() //
							.tag(Tag.CREATION_DATETIME) //
							.value(now) //
							.build())
					.getValue());
			Date modificationDateTime = DateTimeUtil.parse(modificationDateTimeData.orElse( //
					Mp4Metadata.toBuilder() //
							.tag(Tag.MODIFICATION_DATETIME) //
							.value(now) //
							.build())
					.getValue());

			/**
			 * 
			 */
			this.correctOffset = this.needsOffsetCorrection();
			this.sizeBefore = this.moovBox.getSize();
			this.offset = 0;

			for (Box box : this.isoFile.getBoxes())
			{
				if ("moov".equals(box.getType()))
				{
					break;
				}
				this.offset += box.getSize();
			}
			LOGGER.trace("Correct offset is required? '{}'. Offset is '{}'.", this.correctOffset, this.offset);

			// Create structure or just navigate to Apple List Box.
			if (creationDateTimeData.isPresent() && creationDateTimeData.get().getTag().equals(Tag.CREATION_DATETIME)
					|| modificationDateTimeData.isPresent() && modificationDateTimeData.get().getTag().equals(Tag.MODIFICATION_DATETIME))
			{
				// Set Movie Header Box data
				this.setMovieHeaderBox(creationDateTime, modificationDateTime);
			}

			this.setUserDataBox();
			this.setMetaBox();
			this.setAppleItemListBox();

			if (this.freeBox == null)
			{
				this.freeBox = new FreeBox(128 * 1024);
				this.metaBox.addBox(this.freeBox);
			}

			if (titleData.isPresent() && titleData.get().getTag().equals(Tag.TITLE))
			{
				int countryId = Mp4Writer.getCountryIdByName(country);
				int languageId = Mp4Writer.getLanguageIdByName(language);
				LOGGER.info("Set country '{}' and language '{}' codes ('{}' - '{}').", countryId, languageId, country, language);

				this.setAppleNameBox(titleData.get().getValue(), countryId, languageId);
			}
		}
	}

	/**
	 * 
	 */
	private boolean prepareStructure()
	{
		ReadableByteChannel in = Channels.newChannel(new ByteArrayInputStream(this.bytes));
		try
		{
			this.isoFile = new IsoFile(in);
			this.boxes = this.isoFile.getBoxes(MovieBox.class);
			LOGGER.trace("'{}' boxe(s) of class '{}' available.", this.boxes.size(), MovieBox.class.getSimpleName());

			if (this.boxes.isEmpty())
			{
				throw new MissingElementsException("Movie box of ISO file is empty.");
			}

			this.moovBox = this.boxes.get(0);
			this.freeBox = this.findFreeBox(this.moovBox);
		}
		catch (IOException e)
		{
			LOGGER.error("An IOException has been occured", e);
		}

		return true;
	}

	@Override
	public String readMetadata(Tag tag)
	{
		switch (tag)
		{
			case TITLE:
				return this.getAppleNameBoxValue(tag);

			case LOCALE:
				return this.getAppleNameBoxLocale(tag);

			case CREATION_DATETIME:
				return this.getMovieHeaderBox(tag);

			case MODIFICATION_DATETIME:
				return this.getMovieHeaderBox(tag);

			default:
				return "";
		}
	}

	/**
	 * 
	 */
	private void setAppleItemListBox()
	{
		if ((this.ilstBox = Path.getPath(this.metaBox, "ilst")) == null)
		{
			this.ilstBox = new AppleItemListBox();
			this.metaBox.addBox(this.ilstBox);
		}
	}

	/**
	 * @param value
	 * 
	 */
	private void setAppleNameBox(String value, int country, int language)
	{
		// Get Apple Name Box
		if ((this.anamBox = Path.getPath(this.ilstBox, "©nam")) == null)
		{
			this.anamBox = new AppleNameBox();
		}

		this.anamBox.setValue(value);
		this.anamBox.setDataCountry(country);
		this.anamBox.setDataLanguage(language);
		this.ilstBox.addBox(this.anamBox);
	}

	/**
	 * 
	 */
	private void setMetaBox()
	{
		if ((this.metaBox = Path.getPath(this.udtaBox, "meta")) == null)
		{
			this.metaBox = new MetaBox();

			HandlerBox hdlr = new HandlerBox();
			hdlr.setHandlerType("mdir");
			this.metaBox.addBox(hdlr);
			this.udtaBox.addBox(this.metaBox);
		}
	}

	/**
	 * @param creationDate
	 * @param modificationDate
	 */
	private void setMovieHeaderBox(Date creationDate, Date modificationDate)
	{
		if ((this.mvhdBox = Path.getPath(this.moovBox, "mvhd")) == null)
		{
			this.mvhdBox = new MovieHeaderBox();
			this.moovBox.addBox(this.mvhdBox);
		}

		this.mvhdBox.setCreationTime(creationDate);
		this.mvhdBox.setModificationTime(modificationDate);
	}

	/**
	 * 
	 */
	private void setUserDataBox()
	{
		if ((this.udtaBox = Path.getPath(this.moovBox, "udta")) == null)
		{
			this.udtaBox = new UserDataBox();
			this.moovBox.addBox(this.udtaBox);
		}
	}

	/**
	 *
	 */
	@Override

	/**
	 * @param fos
	 * @throws IOException
	 */
	public long write(FileOutputStream fos) throws IOException
	{
		this.sizeAfter = this.moovBox.getSize();

		// This is the difference of before/after
		long diff = this.sizeAfter - this.sizeBefore;

		// Can we compensate by resizing a Free Box we have found?
		if (this.freeBox.getData().limit() > diff)
		{
			// Either shrink or grow!
			this.freeBox.setData(ByteBuffer.allocate((int) (this.freeBox.getData().limit() - diff)));
			this.sizeAfter = this.moovBox.getSize();

			diff = this.sizeAfter - this.sizeBefore;
		}

		if (this.correctOffset && diff != 0)
		{
			this.correctChunkOffsets(this.moovBox, diff);
		}

		this.target = new BetterByteArrayOutputStream();
		this.moovBox.getBox(Channels.newChannel(this.target));
		this.isoFile.close();

		Mp4Processable processor = new Mp4Processor(new ByteArrayInputStream(this.bytes));
		processor.setSizeBefore(this.sizeBefore);
		processor.setSizeAfter(this.sizeAfter);
		processor.setOffset(this.offset);
		processor.process(diff, this.target);
		
		return this.finalize(fos);
	}
}
