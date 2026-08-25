/**
 * 
 */
package info.hersche.document.metadata.configuration;

import org.apache.catalina.Context;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.apache.tomcat.util.http.CookieProcessorBase;
import org.apache.tomcat.util.http.Rfc6265CookieProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;

/**
 * @author herscju
 *
 */
public class CustomTomcatServletWebServerFactory extends TomcatServletWebServerFactory
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomTomcatServletWebServerFactory.class);

	/**
	 * 
	 */
	private String pattern;
	private String constraint;

	/**
	 * Constructor
	 * 
	 * @param constraint
	 * @param pattern
	 */
	public CustomTomcatServletWebServerFactory(String pattern, String constraint)
	{
		this.pattern = pattern;
		this.constraint = constraint;

		LOGGER.info("Class '{}' instantiated successfully.", CustomTomcatServletWebServerFactory.class.getSimpleName());
	}

	/**
	 * Post process the Tomcat context before it's used with the Tomcat server
	 * 
	 * https://stackoverflow.com/a/39196298
	 */
	@Override
	protected void postProcessContext(Context context)
	{
		CookieProcessorBase processor = new Rfc6265CookieProcessor();

		LOGGER.trace("Set pattern to '{}'.", this.pattern);
		LOGGER.trace("Set user constraint to '{}'.", this.constraint);
		LOGGER.trace("Set cookie processor to '{}'.", processor);

		SecurityCollection securityCollection = new SecurityCollection();
		securityCollection.addPattern(this.pattern);

		SecurityConstraint securityConstraint = new SecurityConstraint();
		securityConstraint.setUserConstraint(this.constraint);
		securityConstraint.addCollection(securityCollection);

		context.addConstraint(securityConstraint);
		context.setCookieProcessor(processor);
	}

}
