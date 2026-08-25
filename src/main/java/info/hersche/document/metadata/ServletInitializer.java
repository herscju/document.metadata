/**
 * 
 */
package info.hersche.document.metadata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author herscju
 */
public class ServletInitializer extends SpringBootServletInitializer
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ServletInitializer.class);

	/**
	 * Default constructor
	 */
	public ServletInitializer()
	{
		// Nothing to do here

		LOGGER.info("Class '{}' instantiated successfully.", ServletInitializer.class.getSimpleName());
	}

	/**
	 *
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application)
	{
		LOGGER.debug("Configure '{}' on '{}'.", MetaDataApplication.class.getSimpleName(), ServletInitializer.class.getSimpleName());

		return application.sources(MetaDataApplication.class);
	}

}
