/**
 * 
 */
package info.hersche.document.metadata.model;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author herscju
 */
@Deprecated
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true, builderMethodName = "toBuilder")
public class PageContent implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4035925222626827624L;

	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(PageContent.class);

	/**
	 * Name of site
	 */
	@Getter
	@Setter
	private String siteName;

	/**
	 * Read more...
	 */
	@Getter
	@Setter
	private String readMore;

	/**
	 * Use service
	 */
	@Getter
	@Setter
	private String seeService;

	/**
	 * Page title (Header)
	 */
	@Getter
	@Setter
	private String title;

	/**
	 * Page title (main)
	 */
	@Getter
	@Setter
	private String titleHead;

	/**
	 * Page introduction (main)
	 */
	@Getter
	@Setter
	private String titleIntro;

	/**
	 * Page start
	 */
	@Getter
	@Setter
	private String start;

	/**
	 * Service title
	 */
	@Getter
	@Setter
	private String serviceTitle;

	/**
	 * Service main
	 */
	@Getter
	@Setter
	private String serviceMain;

	/**
	 * Default constructor
	 */
	private PageContent()
	{
		// Nothing to do here

		LOGGER.info("Class '{}' instantiated successfully.", PageContent.class.getSimpleName());
	}
}
