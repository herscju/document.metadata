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
 * https://jenkov.com/tutorials/java-io/bufferedinputstream.html
 */
package info.hersche.document.metadata;

import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.ContainerCriteria;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.test.context.web.WebAppConfiguration;

import info.hersche.document.metadata.directory.Person;

/**
 * @author Jürg Hersche (herscju)
 */
@SpringBootTest
@WebAppConfiguration
@TestInstance(Lifecycle.PER_CLASS)
public class LdapTests
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(LdapTests.class);

	@Autowired
	private LdapTemplate ldapTemplate;

	/**
	 * Default constructor
	 */
	public LdapTests()
	{
		// Nothing to do here

		LOGGER.info("Class '{}' instantiated successfully.", LdapTests.class.getSimpleName());
	}

	/**
	 * Get the domain list. If you want to get a certain domain, the filter can be written like this: (&(objectclass=dcObject)&(dc=example))
	 * String filter = "(&(objectclass=dcObject))";
	 * Get the list of organizations. If you want to get a specific organization, the filter can be written like this:
	 * (&(objectclass=organizationalUnit)&(ou=people)
	 * String filter = "(&(objectclass=organizationalUnit))";
	 * Get the people list. If you want to get a certain person, the filter can be written like this: (&(objectclass=inetOrgPerson)&(uid=uid:13))
	 * 
	 */
	@Test
	public void filterSearch()
	{
		String filter = "(&(objectClass=person)(objectClass=user))";
		LOGGER.debug("Filter for '{}'.", filter);

		List<Person> personList = this.ldapTemplate.search("CN=Users,DC=hersche,DC=name", filter, new AttributesMapper<>()
		{
			@Override
			public Person mapFromAttributes(Attributes attributes) throws NamingException
			{
				LOGGER.trace("List found attributes {}.", attributes);
				NamingEnumeration<? extends Attribute> att = attributes.getAll();
				while (att.hasMore())
				{
					Attribute attribute = att.next();
					LOGGER.debug("Entry ID '{}' has objects '{}'.", attribute.getID(), attribute.get());
				}

				Person person = new Person();
				Attribute attribute = attributes.get("cn");
				if (attribute != null)
				{
					person.setCommonName((String) attribute.get());
				}
				attribute = attributes.get("givenName");
				if (attribute != null)
				{
					person.setGivenName((String) attribute.get());
				}
				attribute = attributes.get("sn");
				if (attribute != null)
				{
					person.setSurName((String) attribute.get());
				}

				return person;
			}
		});

		personList.stream().forEach(person -> {
			LOGGER.info(">>> {}", person);
		});
	}

	/**
	 * 
	 */
	@BeforeAll
	private void init()
	{
		// Nothing to do here
		
		String factory = System.getProperty("factory", "com.sun.jndi.ldap.LdapCtxFactory");
		String authType = System.getProperty("authType", "simple");
	}

	/**
	 * You can also use filter query method, filter is (&(objectClass=user)(!(objectClass=computer))
	 */
	@Test
	public void querySearch()
	{
		// You can also use filter query method, filter is (&(objectClass=user)(!(objectClass=computer))
		ContainerCriteria containerCriteria = LdapQueryBuilder.query().where("objectClass").is("user").and("uid").is("uid:1");
		LOGGER.debug("Query for '{}'.", containerCriteria.filter());

		List<Person> personList = this.ldapTemplate.search(containerCriteria, new AttributesMapper<>()
		{
			@Override
			public Person mapFromAttributes(Attributes attributes) throws NamingException, javax.naming.NamingException
			{
				// If you don't know what attributes are in ldap, you can print them in the following way
				LOGGER.trace("List found attributes {}.", attributes);
				NamingEnumeration<? extends Attribute> att = attributes.getAll();
				while (att.hasMore())
				{
					Attribute attribute = att.next();
					LOGGER.debug("Entry ID '{}' has objects '{}'.", attribute.getID(), attribute.get());
				}

				Person person = new Person();
				Attribute attribute = attributes.get("cn");
				if (attribute != null)
				{
					person.setCommonName((String) attribute.get());
				}
				attribute = attributes.get("givenName");
				if (attribute != null)
				{
					person.setGivenName((String) attribute.get());
				}
				attribute = attributes.get("sn");
				if (attribute != null)
				{
					person.setSurName((String) attribute.get());
				}

				return person;
			}
		});

		personList.stream().forEach(person -> {
			LOGGER.info(">>> {}", person);
		});
	}

}
