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
 * https://stackoverflow.com/a/31826550
 * https://frugalisminds.com/using-custom-authentication-provider-spring-security/
 * https://www.baeldung.com/spring-security-authentication-provider
 */
package info.hersche.document.metadata.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.sun.jna.platform.win32.Win32Exception;

import waffle.servlet.WindowsPrincipal;
import waffle.spring.GrantedAuthorityFactory;
import waffle.spring.GuestLoginDisabledAuthenticationException;
import waffle.spring.WindowsAuthenticationProvider;
import waffle.spring.WindowsAuthenticationToken;
import waffle.windows.auth.IWindowsAuthProvider;
import waffle.windows.auth.IWindowsIdentity;
import waffle.windows.auth.PrincipalFormat;

/**
 * @author herscju
 *
 */
@Component
public class CustomAuthenticationProvider extends WindowsAuthenticationProvider
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

	/**
	 * 
	 */
	private IWindowsAuthProvider authProvider = null;
	private boolean allowGuestLogin = true;
    private PrincipalFormat principalFormat = PrincipalFormat.FQN;
    private PrincipalFormat roleFormat = PrincipalFormat.FQN;
    private GrantedAuthorityFactory grantedAuthorityFactory = WindowsAuthenticationToken.DEFAULT_GRANTED_AUTHORITY_FACTORY;
    private GrantedAuthority defaultGrantedAuthority = WindowsAuthenticationToken.DEFAULT_GRANTED_AUTHORITY;

	/**
	 * @param authentication
	 * @return
	 */
	private static String getUserName(Authentication authentication)
	{
		String name = SecurityConfiguration.ANONYMUS;
		if (authentication != null && authentication.getPrincipal() != null)
		{
			name = authentication.getName();
		}

		return name;
	}

	/**
	 * Default constructor
	 */
	public CustomAuthenticationProvider()
	{
		// Nothing to do here

		LOGGER.info("Class '{}' instantiated successfully.", CustomAuthenticationProvider.class.getSimpleName());
	}

	/**
	 *
	 */
	@Override
	public Authentication authenticate(Authentication authentication)
	{
		this.authProvider = super.getAuthProvider();
		this.allowGuestLogin = super.isAllowGuestLogin();
		this.principalFormat = super.getPrincipalFormat();
		this.roleFormat = super.getRoleFormat();
		this.grantedAuthorityFactory = super.getGrantedAuthorityFactory();
		this.defaultGrantedAuthority = super.getDefaultGrantedAuthority();

		LOGGER.info(">>>>>>>>> {}", CustomAuthenticationProvider.getUserName(authentication));
		final UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;
		final IWindowsIdentity windowsIdentity;
		
		LOGGER.info(">>>>>>>>> {}", auth);

		try
		{
			windowsIdentity = this.authProvider.logonUser(auth.getName(), auth.getCredentials().toString());
		}
		catch (final Win32Exception e)
		{
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
		LOGGER.debug("Logged in user: {} ({})", windowsIdentity.getFqn(), windowsIdentity.getSidString());

		if (!this.allowGuestLogin && windowsIdentity.isGuest())
		{
			LOGGER.warn("Guest login disabled: {}", windowsIdentity.getFqn());
			throw new GuestLoginDisabledAuthenticationException(windowsIdentity.getFqn());
		}

		final WindowsPrincipal windowsPrincipal = new WindowsPrincipal(windowsIdentity, this.principalFormat, this.roleFormat);
		LOGGER.debug("Roles: {}", windowsPrincipal.getRolesString());

		final WindowsAuthenticationToken token = new WindowsAuthenticationToken(windowsPrincipal, this.grantedAuthorityFactory, this.defaultGrantedAuthority);
		LOGGER.info("Successfully logged in user: {}", windowsIdentity.getFqn());

		return token;
	}

	/**
	 *
	 */
	@Override
	public boolean supports(Class<?> authentication)
	{
		return super.supports(authentication);
	}

}
