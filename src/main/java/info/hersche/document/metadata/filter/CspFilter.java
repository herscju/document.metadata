/**
 * 
 */
package info.hersche.document.metadata.filter;

import java.io.IOException;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author herscju
 */
@Component
public class CspFilter extends OncePerRequestFilter
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CspFilter.class);
	private static final String CSP_HEADER_NAME = "Content-Security-Policy";
	private static final String HAWTIO_URL = "/hawtio/";
	private static final String FRAME_ANCESTORS = "frame-ancestors";
	private static final String SCRIPT_SOURCE_ELEM = "script-src-elem";

	/**
	 * 
	 */
	@Value("${management.endpoints.web.base-path}")
	private String actuatorBasePath;

	/**
	 * Default constructor
	 */
	public CspFilter()
	{
		// Nothing to do here

		LOGGER.info("Class '{}' instantiated successfully.", CspFilter.class.getSimpleName());
	}

	/**
	 *
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
	{
		String requestUrl = request.getRequestURL().toString();
		String hawtioUrl = new StringBuilder(this.actuatorBasePath).append(HAWTIO_URL).toString();
		if (requestUrl.contains(hawtioUrl))
		{
			Collection<String> headers = response.getHeaderNames();
			for (String header : headers)
			{
				if (header.equalsIgnoreCase(CSP_HEADER_NAME))
				{
					StringBuilder update = new StringBuilder();
					Collection<String> values = response.getHeaders(header);
					for (String value : values)
					{
						String[] settings = value.split(";");
						for (String setting : settings)
						{
							if (setting.contains(FRAME_ANCESTORS))
							{
								update.append(" ").append(FRAME_ANCESTORS).append(" 'self'");
							}
							else if (setting.contains(SCRIPT_SOURCE_ELEM))
							{
								update.append(setting);
							}
							else
							{
								update.append(setting);
							}
							update.append(";");
						}
					}

					response.setHeader(CSP_HEADER_NAME, update.toString());
				}
			}
		}

		filterChain.doFilter(request, response);
	}

}
