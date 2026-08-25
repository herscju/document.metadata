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
package info.hersche.document.metadata.util;

import java.security.Principal;

import org.apache.catalina.connector.CoyotePrincipal;
import org.apache.catalina.realm.GenericPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import waffle.jaas.UserPrincipal;
import waffle.servlet.WindowsPrincipal;
import waffle.spring.WindowsAuthenticationToken;

/**
 * 
 */
public class PrincipalResolver
{

	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(PrincipalResolver.class);

	/**
	 * 
	 */
	private HttpServletRequest request;
	private Principal principal;
	private String name = null;
	private String subject = null;

	/**
	 * @param request
	 */
	public PrincipalResolver(@NonNull HttpServletRequest request)
	{
		this.request = request;
		this.principal = this.request.getUserPrincipal();

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("Class of 'Principal' is '{}'.", this.principal);
			LOGGER.debug("Is 'GenericPrincipal'?            -> '{}'.", (principal instanceof GenericPrincipal));
			LOGGER.debug("Is 'CoyotePrincipal'?             -> '{}'.", (principal instanceof CoyotePrincipal));
			LOGGER.debug("Is 'WindowsAuthenticationToken'?  -> '{}'.", (principal instanceof WindowsAuthenticationToken));
			// LOGGER.debug("Is 'KeycloakAuthenticationToken'? -> '{}'.", (principal instanceof KeycloakAuthenticationToken));
			LOGGER.debug("Is 'WindowsPrincipal'?            -> '{}'.", (principal instanceof WindowsPrincipal));
			LOGGER.debug("Is 'UserPrincipal'?               -> '{}'.", (principal instanceof UserPrincipal));
			// LOGGER.debug("Is 'GenericWindowsPrincipal'? -> '{}'.", (principal instanceof GenericWindowsPrincipal));
		}

		LOGGER.trace("Class '{}' instantiated successfully.", PrincipalResolver.class.getSimpleName());
	}

	/**
	 * @return
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * @return
	 */
	public Principal getPrincipal()
	{
		return this.principal;
	}

	/**
	 * @return
	 */
	public String getSubject()
	{
		return this.subject;
	}

	/**
	 * @throws ServletException
	 */
	public void logout() throws ServletException
	{
		LOGGER.info("Log out of '{}'.", this.name);

		this.request.logout();
	}

	/**
	 * @return
	 */
	public PrincipalResolver resolve()
	{
		LOGGER.trace("Current principal is '{}'.", this.principal);
		
		if (this.principal != null)
		{
			if (this.principal instanceof GenericPrincipal)
			{
				GenericPrincipal genericPrincipal = ((GenericPrincipal) this.principal);
				this.name = genericPrincipal.getName();
				this.subject = "";
			}
			else if (this.principal instanceof CoyotePrincipal)
			{
				CoyotePrincipal coyotePrincipal = ((CoyotePrincipal) this.principal);
				this.name = coyotePrincipal.getName();
				this.subject = "";
			}
			else if (this.principal instanceof WindowsAuthenticationToken)
			{
				WindowsAuthenticationToken windowsPrincipal = ((WindowsAuthenticationToken) this.principal);
				this.name = windowsPrincipal.getName();
				this.subject = "";
			}
			// else if (this.principal instanceof KeycloakAuthenticationToken)
			// {
			// KeycloakAuthenticationToken keycloakPrincipal = ((KeycloakAuthenticationToken) this.principal);
			// IDToken token = keycloakPrincipal.getAccount().getKeycloakSecurityContext().getIdToken();
			// this.name = token.getName();
			// this.subject = token.getSubject();
			// }
			else if (this.principal instanceof WindowsPrincipal)
			{
				WindowsPrincipal windowsPrincipal = ((WindowsPrincipal) this.principal);
				this.name = windowsPrincipal.getName();
				this.subject = "";
			}
			else if (this.principal instanceof UserPrincipal)
			{
				UserPrincipal userPrincipal = ((UserPrincipal) this.principal);
				this.name = userPrincipal.getName();
				this.subject = "";
			}
			// else if (this.principal instanceof GenericWindowsPrincipal)
			// {
			// GenericWindowsPrincipal genericWindowsPrincipal = ((GenericWindowsPrincipal) this.principal);
			// this.name = genericWindowsPrincipal.getName();
			// this.subject = "";
			// }
			else
			{
				// Nothing to do here
			}
		}

		return this;
	}

}
