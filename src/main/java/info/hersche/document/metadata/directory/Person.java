/**
 * 
 */
package info.hersche.document.metadata.directory;

import java.io.Serializable;

import javax.naming.Name;

import org.springframework.data.annotation.Id;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;

import lombok.Data;
import lombok.ToString;

/**
 * @author herscju
 */
@Data
@Entry(base = "CN=Users,DC=hersche,DC=name", objectClasses = { "organizationalPerson", "person", "user", "top" })
@ToString
public class Person implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6612702718720179296L;

	/**
	 * Necessary
	 */
	@Id
	private Name id;

	@Attribute(name = "cn")
	private String commonName;

	@Attribute(name = "givenName")
	private String givenName;

	@Attribute(name = "sn")
	private String surName;

	/**
	 * Default constructor
	 */
	public Person()
	{
		// Nothing to do here
	}
}
