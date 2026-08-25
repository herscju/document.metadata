/**
 * 
 */
package info.hersche.document.metadata;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.apache.commons.imaging.ImagingException;
import org.apache.commons.io.FilenameUtils;
import org.jcodec.api.JCodecException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.web.WebAppConfiguration;

import com.drew.imaging.ImageProcessingException;

import info.hersche.document.metadata.factory.AbstractDocumentFactory;
import info.hersche.document.metadata.factory.MediaFactory;
import info.hersche.document.metadata.image.Converter;
import info.hersche.document.metadata.image.MultiMediaImpl.Type;
import info.hersche.document.metadata.image.Picture;
import info.hersche.document.metadata.model.OutputImageMetadataMessage;
import info.hersche.document.metadata.processor.Mp4Metadata;
import info.hersche.document.metadata.processor.Mp4OutputSet;
import info.hersche.document.metadata.processor.Mp4Writer.Tag;
import info.hersche.document.metadata.processor.Mp4WriterExample;
import info.hersche.document.metadata.util.DateTimeUtil;
import info.hersche.document.metadata.util.Detector;
import info.hersche.document.metadata.util.FileUtil;

/**
 * @author herscju
 */
@SpringBootTest
@WebAppConfiguration
@TestInstance(Lifecycle.PER_CLASS)
public class MovieTests
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(MovieTests.class);
	private static final String DATETIME_PATTERN = "yyyy:MM:dd HH:mm:ss";
	private static final Dimension STANDARD_DIM = new Dimension(2400, 1080);
	private static final Dimension WHATSAPP_DIM = new Dimension(1280, 720);
	private static final Dimension TEMPMOVIE_DIM = new Dimension(1920, 1080);

	/**
	 * Required file resources
	 */
	@Value("classpath:images/VID_20250204_133618.mp4")
	private Resource standardMovie;

	@Value("classpath:images/VID-20250209-WA0256.mp4")
	private Resource whatsAppMovie;

	@Value("C:\\Temp\\VID-20250209-WA0256.mp4")
	private String whatsAppTemporaryPath;

	/**
	 * 
	 */
	private List<Movie> files = new ArrayList<>();
	private Map<String, Movie> movies = new LinkedHashMap<>();
	private Map<String, BufferedImage> images = new LinkedHashMap<>();

	/**
	 * Default constructor
	 */
	public MovieTests()
	{
		// Nothing to do here

		LOGGER.info("Class '{}' instantiated successfully.", MovieTests.class.getSimpleName());
	}

	/**
	 * 
	 */
	@AfterAll
	private void after()
	{
		LOGGER.info("=== The end! ===================================================================");
	}

	/**
	 * @param extension
	 * @param bufferedImage
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	private Picture buildPicture(String extension, BufferedImage bufferedImage, InputStream inputStream) throws IOException
	{
		final MediaFactory factory = AbstractDocumentFactory.getMediaFactory(extension);
		final Picture picture = factory.getPicture(bufferedImage, inputStream);

		return picture;
	}

	/**
	 * @throws IOException
	 * @throws ImageProcessingException
	 */
	@BeforeAll
	private void init() throws IOException, ImageProcessingException
	{
		// this.files.add(new Movie(new FileSystemResource(this.standardMovie), STANDARD_DIM));
		// this.files.add(new Movie(new FileSystemResource(this.whatsAppMovie), TEMPMOVIE_DIM, 1138933663));
		this.files.add(new Movie(new FileSystemResource(this.whatsAppTemporaryPath), WHATSAPP_DIM, 2251694));

		/**
		 * Build linked hash map for all resources with file name as key
		 */
		for (Movie movie : this.files)
		{
			this.movies.put(movie.getResource().getFilename(), movie);
		}

		/**
		 * Generate buffered image for all given resources and build linked hash map with file name as key.
		 */
		for (Map.Entry<String, Movie> entry : this.movies.entrySet())
		{
			final String fileName = entry.getKey();
			final Movie movie = entry.getValue();
			final Resource resource = movie.getResource();

			try
			{
				LOGGER.info("Create buffered image of file '{}'.", fileName);
				final BufferedImage bufferedImage = Converter.getBufferedImageFromMovie(resource.getFile());
				this.images.put(fileName, bufferedImage);
			}
			catch (IOException e)
			{
				LOGGER.error("IOException while reading file.", e);
			}
			catch (JCodecException e)
			{
				LOGGER.error("JCodecException while extracting frame.", e);
			}
		}
	}

	/**
	 * 
	 */
	@Test
	public void testImageSize()
	{
		for (Entry<String, BufferedImage> entry : this.images.entrySet())
		{
			final String fileName = entry.getKey();
			final BufferedImage bufferedImage = entry.getValue();
			final Dimension imageDimension = new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight());
			LOGGER.info("=== Size of images =============================================================");
			LOGGER.info("Working on file '{}'.", fileName);

			final Movie movie = this.movies.get(fileName);
			final Dimension movieDimension = movie.getDimension();
			LOGGER.info("Dimension of movie '{}' are '{}x{}' with start frame of size of '{}x{}'.", fileName, (int) movieDimension.getWidth(),
					(int) movieDimension.getHeight(), (int) imageDimension.getWidth(), (int) imageDimension.getHeight());

			assertEquals(movieDimension, imageDimension);
		}
	}

	/**
	 * @throws IOException
	 * @throws ImageProcessingException
	 * @throws ImagingException
	 * 
	 */
	@Test
	public void testOriginalDate() throws ImagingException, ImageProcessingException, IOException
	{
		for (Entry<String, Movie> entry : this.movies.entrySet())
		{
			final String fileName = entry.getKey();
			final String extension = FilenameUtils.getExtension(fileName);
			final Movie movie = entry.getValue();
			final Resource resource = movie.getResource();
			LOGGER.info("=== Original date ==============================================================");
			LOGGER.info("Working on file '{}'.", fileName);

			try (InputStream is = resource.getInputStream())
			{
				final Picture picture = this.buildPicture(extension, this.images.get(fileName), is);
				final OutputImageMetadataMessage metadata = picture.readMetadata();
				final LocalDateTime dateTime = picture.getOriginalDate();
				final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIME_PATTERN);
				final String originalDate = dateTime.format(formatter);
				final Type type = Detector.toBuilder().name(fileName).build().detectMultiMediaType();
				LOGGER.info("The original date of file '{}' ('{}') is '{}'.", fileName, type, originalDate);

				StringBuilder fileDate = new StringBuilder();
				switch (type)
				{
					case STANDARD_VIDEO:
						fileDate = DateTimeUtil.parse(DateTimeUtil.DATETIME_V3_FILTER, fileName);
						break;

					case WHATSAPP_VIDEO:
						fileDate = DateTimeUtil.parse(DateTimeUtil.DATETIME_V4_FILTER, fileName);
						break;

					case STANDARD_IMAGE:
					case WHATSAPP_IMAGE:
					case WHATSAPP_STATUS:
					default:
						break;
				}

				LOGGER.info("The date by file name '{}' is '{}'.", fileName, fileDate);

				// assertEquals(fileDate.toString(), originalDate);

				// this.pictures.put(fileName, picture);
				// this.messages.put(fileName, metadata);
			}
		}
	}

	/**
	 * @throws IOException
	 * 
	 */
	@Test
	public void testMetadataUpdate() throws IOException
	{
		for (Entry<String, Movie> entry : this.movies.entrySet())
		{
			final String fileName = entry.getKey();
			final Movie movie = entry.getValue();
			final Resource resource = movie.getResource();
			final File file = resource.getFile();
			LOGGER.info("=== Metadata update ============================================================");
			LOGGER.info("Working on file '{}' ('{}'). File exists: {}! Is readable: {}! Is writeable: {}!", fileName, file.getAbsolutePath(), file.exists(),
					file.canRead(), file.canWrite());

			String dateTime = DateTimeUtil.now();
			String title = new StringBuilder(file.getName()).append(" :: ").append(dateTime).toString();
			Locale locale = new Locale("de", "CH", "WIN");

			Mp4OutputSet outputSet = Mp4OutputSet.toBuilder() //
					.title(Optional.of(Mp4Metadata.toBuilder().tag(Tag.TITLE).value(title).build())) //
					.locale(Optional.of(Mp4Metadata.toBuilder().tag(Tag.LOCALE).value(locale.toLanguageTag()).build())) //
					.creationDateTime(Optional.of(Mp4Metadata.toBuilder().tag(Tag.CREATION_DATETIME).value(dateTime).build())) //
					.modificationDateTime(Optional.of(Mp4Metadata.toBuilder().tag(Tag.MODIFICATION_DATETIME).value(dateTime).build())) //
					.build();

			 String path = FileUtil.addPrefixToFileName(file, "TEMP-");
			
			 LOGGER.info("=== Single processor 'new Mp4Processor(path, new FileInputStream(file))' =======");
			 Mp4WriterExample processor = new Mp4WriterExample(path, new FileInputStream(file));
			 processor.prepare(outputSet);
			 processor.write();
			
			 long size1 = processor.finalize(file);
			 LOGGER.info("1: Size of updated file is '{}'.", size1);
			
			 long size2 = processor.finalize(new FileOutputStream(file));
			 LOGGER.info("2: Size of updated file is '{}'.", size2);
			
			 LOGGER.info("=== Single reader 'new Mp4Processor(file)' =====================================");
			 Mp4WriterExample reader = new Mp4WriterExample(file);
			
			 String movieTitle1 = reader.readMetadata(Tag.TITLE);
			 LOGGER.info("Title of movie is '{}'.", movieTitle1);
			
			 String movieLocale1 = reader.readMetadata(Tag.LOCALE);
			 LOGGER.info("Locale of movie is '{}'.", movieLocale1);
			
			 String movieCreationTime1 = reader.readMetadata(Tag.CREATION_DATETIME);
			 LOGGER.info("Creation time of movie is '{}'.", movieCreationTime1);
			
			 String movieModificationTime1 = reader.readMetadata(Tag.MODIFICATION_DATETIME);
			 LOGGER.info("Modification time of movie is '{}'.", movieModificationTime1);
			
			 assertEquals(size1, movie.getSize());
			 assertEquals(size1, file.length());
			 assertEquals(size2, movie.getSize());
			 assertEquals(size2, file.length());
			 assertEquals(movieTitle1, title);
			 assertEquals(movieLocale1, locale.toLanguageTag());
			 assertEquals(movieCreationTime1, dateTime);
			 assertEquals(movieModificationTime1, dateTime);

//			LOGGER.info("=== Single writer 'new Mp4Writer(source)' ======================================");
//
//			// Convert 'file' to 'source' InputStream and to 'target' FileOutputStream to make sure we have the same situation as
//			FileInputStream source = FileUtils.openInputStream(file);
//			FileOutputStream target = FileUtils.openOutputStream(file);
//
//			Mp4Writeable writer = new Mp4Writer(source);
//			writer.prepare(outputSet);
//			writer.write(target);
//			LOGGER.info(">>> A >>> {}", target);
//
//			long size3 = writer.getSize();
//			LOGGER.info("1: Size of updated file is '{}'.", size3);
//
//			LOGGER.info("=== Single scanner 'new Mp4Processor(file)' ====================================");
//			Mp4Writeable scanner = new Mp4Writer(file);
//
//			String movieTitle2 = scanner.readMetadata(Tag.TITLE);
//			LOGGER.info("Title of movie is '{}'.", movieTitle2);
//
//			String movieLocale2 = scanner.readMetadata(Tag.LOCALE);
//			LOGGER.info("Locale of movie is '{}'.", movieLocale2);
//
//			String movieCreationTime2 = scanner.readMetadata(Tag.CREATION_DATETIME);
//			LOGGER.info("Creation time of movie is '{}'.", movieCreationTime2);
//
//			String movieModificationTime2 = scanner.readMetadata(Tag.MODIFICATION_DATETIME);
//			LOGGER.info("Modification time of movie is '{}'.", movieModificationTime2);
		}
	}

}
