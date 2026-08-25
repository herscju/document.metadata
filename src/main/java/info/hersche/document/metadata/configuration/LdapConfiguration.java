/**
 * 
 */
package info.hersche.document.metadata.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.ldap.repository.config.EnableLdapRepositories;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

/**
 * @author herscju
 */
@Configuration
@EnableLdapRepositories
public class LdapConfiguration
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(LdapConfiguration.class);

	/**
	 * 
	 */
	private Environment environment;

	/**
	 * Default constructor
	 */
	public LdapConfiguration(Environment environment)
	{
		this.environment = environment;

		LOGGER.info("Class '{}' instantiated successfully.", LdapConfiguration.class.getSimpleName());
	}

	/**
	 * @return
	 */
	@Bean
	public LdapContextSource contextSource()
	{
		LdapContextSource contextSource = new LdapContextSource();
		contextSource.setUrl(this.environment.getProperty("spring.ldap.urls"));
		contextSource.setBase(this.environment.getRequiredProperty("spring.ldap.base"));
		contextSource.setUserDn(this.environment.getRequiredProperty("spring.ldap.username"));
		contextSource.setPassword(this.environment.getRequiredProperty("spring.ldap.password"));
		contextSource.afterPropertiesSet();

		return contextSource;
	}

	/**
	 * @return
	 */
	@Bean
	public LdapTemplate ldapTemplate(LdapContextSource contextSource)
	{
		return new LdapTemplate(contextSource);
	}
}
