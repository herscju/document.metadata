/**
 * 
 */
package info.hersche.document.metadata.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import jakarta.validation.constraints.Positive;

/**
 * @author herscju
 */
@JsonSerialize
@ConfigurationProperties(prefix = "application.hosts.ldap")
public record LdapProperties( //
		String protocol, //
		String name, //
		@Positive Integer port)
{
	// Noting to do here
}
