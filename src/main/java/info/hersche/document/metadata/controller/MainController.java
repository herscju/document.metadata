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
package info.hersche.document.metadata.controller;

import java.time.LocalDate;
import java.util.Locale;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import info.hersche.document.metadata.configuration.ApiKeyProperties;
import info.hersche.document.metadata.configuration.DropZoneProperties;
import info.hersche.document.metadata.configuration.LdapProperties;
import info.hersche.document.metadata.configuration.SecurityConfiguration;
import info.hersche.document.metadata.model.PageText;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author herscju
 *
 */
@Configuration
public abstract class MainController implements MessageSourceAware
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);
	private static final int YEAR = LocalDate.now().getYear();

	public static final String EMPTY = "";

	/**
	 * JSON marshaling and unmarshaling
	 */
	@Autowired
	private ObjectMapper mapper;

	/**
	 * Externally managed API keys
	 */
	@Autowired
	private ApiKeyProperties keys;

	/**
	 * Externally managed DropZone settings
	 */
	@Autowired
	private DropZoneProperties dropZone;

	/**
	 * LDAP (Microsoft AD/DC) settings
	 */
	@Autowired
	private LdapProperties ldap;

	/**
	 * 
	 */
	private MessageSource messageSource;

	/**
	 * Abstract default constructor
	 */
	public MainController()
	{
		// Nothing to do here

		LOGGER.info("Class '{}' instantiated successfully.", MainController.class.getSimpleName());
	}

	/**
	 * @param name
	 * @param level
	 */
	protected void changeLoggerLevel(String name, Level level)
	{
		LoggerContext context = (LoggerContext) LogManager.getContext(false);
		LoggerConfig loggerConfig = context.getConfiguration().getLoggers().get(name);
		loggerConfig.setLevel(level);
		context.updateLoggers();
		LOGGER.trace("Set level of logger '{}' to '{}'.", loggerConfig.getName(), loggerConfig.getLevel());
	}

	/**
	 * @param request
	 * @return
	 */
	public Locale getLocaleFromRequest(HttpServletRequest request)
	{
		return RequestContextUtils.getLocale(request);
	}

	/**
	 * @param id
	 * @param locale
	 * @return
	 */
	public String getLocalisedText(String id, Locale locale)
	{
		return this.messageSource.getMessage(id, new Object[0], locale);
	}

	/**
	 *
	 */
	@Override
	public void setMessageSource(MessageSource messageSource)
	{
		this.messageSource = messageSource;
	}

	/**
	 * @param request
	 * @param authentication
	 * @param model
	 * @throws JsonProcessingException
	 */
	protected void setDefaultMetadata(HttpServletRequest request, Authentication authentication, Model model, String sso, PageText pageText)
	{
		Object name = SecurityConfiguration.ANONYMUS;
		String authorities = "";
		LOGGER.debug(">>> Model          {}", model);
		LOGGER.debug(">>> Page text      {}", pageText);
		LOGGER.debug(">>> Request        {}", request);
		LOGGER.debug(">>> Authentication {}", authentication);

		if (authentication != null)
		{
			LOGGER.debug(">>>  Credentials   {}", authentication.getCredentials());
			LOGGER.debug(">>>  Details       {}", authentication.getDetails());
			LOGGER.debug(">>>  Principal     {}", authentication.getPrincipal());
			LOGGER.debug(">>>  Name          {}", authentication.getName());
			LOGGER.debug(">>>  Authorities   {}", authentication.getAuthorities());

			String grantedAuthorities = authentication.getAuthorities() //
					.stream() //
					.map(authority -> authority.getAuthority()) //
					.collect(Collectors.joining(", "));

			name = authentication.getPrincipal();
			authorities = String.format("%s, you have following authorities: %s", name, grantedAuthorities);
		}

		Locale locale = RequestContextUtils.getLocale(request);
		String language = locale.getLanguage();
		String country = locale.getCountry();
		String keys = "";
		String ldap = ""; //new StringBuffer("ldaps").append("://").append("kassandra.hersche.name").append(":").append(PORT).toString();
		String dropZone = "";

		try
		{
			keys = this.mapper.writeValueAsString(this.keys);
			ldap = this.mapper.writeValueAsString(this.ldap);
			dropZone = this.mapper.writeValueAsString(this.dropZone);
		}
		catch (JsonProcessingException e)
		{
			LOGGER.error("Processing keys failed!", e);
		}

		LOGGER.debug("Set user name to '{}'.", name);
		LOGGER.debug("Set page title to '{}'.", pageText.getTitle());
		LOGGER.debug("Set locale to '{}' (languge = '{}', country = '{}').", locale, language, country);
		LOGGER.debug("Set access keys to '{}'.", keys);
		LOGGER.debug("Set LDAP to '{}'.", ldap);

		model.addAttribute("api", "");
		model.addAttribute("sso", sso);
		model.addAttribute("year", YEAR);
		model.addAttribute("keys", keys);
		model.addAttribute("ldap", ldap);
		model.addAttribute("path", pageText.getPath());
		model.addAttribute("user", name);
		model.addAttribute("start", pageText.getStart());
		model.addAttribute("title", pageText.getTitle());
		model.addAttribute("locale", locale.toString());
		model.addAttribute("country", country);
		model.addAttribute("context", request.getContextPath());
		model.addAttribute("blogMain", pageText.getBlogMain());
		model.addAttribute("dropZone", dropZone);
		model.addAttribute("fileDrop", pageText.getFileDrop());
		model.addAttribute("language", language);
		model.addAttribute("readMore", pageText.getReadMore());
		model.addAttribute("siteName", pageText.getSiteName());
		model.addAttribute("blogTitle", pageText.getBlogTitle());
		model.addAttribute("titleHead", pageText.getTitleHead());
		model.addAttribute("seeService", pageText.getSeeService());
		model.addAttribute("titleIntro", pageText.getTitleIntro());
		model.addAttribute("projectApp1", pageText.getProjectApp1());
		model.addAttribute("projectApp2", pageText.getProjectApp2());
		model.addAttribute("projectMain", pageText.getProjectMain());
		model.addAttribute("serviceMain", pageText.getServiceMain());
		model.addAttribute("projectTitle", pageText.getProjectTitle());
		model.addAttribute("serviceTitle", pageText.getServiceTitle());
		model.addAttribute("authorities", authorities);
		model.addAttribute("archiveCleanUp", pageText.getArchiveCleanUp());
		model.addAttribute("archiveDownload", pageText.getArchiveDownload());
		model.addAttribute("countDocProcess", pageText.getCountDocProcess());
		model.addAttribute("countImageGallery", pageText.getCountImageGallery());
		model.addAttribute("countMediaProcess", pageText.getCountMediaProcess());
		model.addAttribute("successDocProcess", pageText.getSuccessDocProcess());
		model.addAttribute("successImageGallery", pageText.getSuccessImageGallery());
		model.addAttribute("successMediaProcess", pageText.getSuccessMediaProcess());
		model.addAttribute("footerLinksTitle", pageText.getFooterLinksTitle());
		model.addAttribute("footerLinksHome", pageText.getFooterLinksHome());
		model.addAttribute("footerLinksAbout", pageText.getFooterLinksAbout());
		model.addAttribute("footerLinksServices", pageText.getFooterLinksServices());
		model.addAttribute("footerLinksBlog", pageText.getFooterLinksBlog());
		model.addAttribute("footerLinksTerms", pageText.getFooterLinksTerms());
		model.addAttribute("footerLinksPrivacy", pageText.getFooterLinksPrivacy());
		model.addAttribute("footerDevelopmentTitle", pageText.getFooterDevelopmentTitle());
		model.addAttribute("footerDevelopmentCalendar", pageText.getFooterDevelopmentCalendar());
		model.addAttribute("footerDevelopmentRestApi", pageText.getFooterDevelopmentRestApi());
		model.addAttribute("footerDevelopmentScenario", pageText.getFooterDevelopmentScenario());
		model.addAttribute("footerProjectsTitle", pageText.getFooterLinksProjectTitle());
		model.addAttribute("footerProjectsLdapmgmt", pageText.getFooterLinksProjectLdapMgmt());
		model.addAttribute("footerProjectsOnlyfans", pageText.getFooterLinksProjectOnlyFans());
		model.addAttribute("footerServicesTitle", pageText.getFooterLinksServicesTitle());
		model.addAttribute("footerServicesCertificate", pageText.getFooterLinksServicesCertificate());
		model.addAttribute("footerServicesTxtChat", pageText.getFooterLinksServicesTextChat());
		model.addAttribute("footerServicesDng2jpg", pageText.getFooterLinksServicesDng2Jpeg());
		model.addAttribute("footerServicesAll2jpg", pageText.getFooterLinksServicesAll2Jpeg());
		model.addAttribute("footerServicesWhatsApp", pageText.getFooterLinksServicesWhatsApp());
		model.addAttribute("footerServicesLightbox", pageText.getFooterLinksServicesLightBox());
		model.addAttribute("footerServicesDoc4Ocr", pageText.getFooterLinksServicesDoc4Ocr());
		model.addAttribute("footerServicesOpenApi", pageText.getFooterLinksServicesOpenApi());
		model.addAttribute("footerServicesActuator", pageText.getFooterLinksServicesActuator());
		model.addAttribute("footerServicesHawtio", pageText.getFooterLinksServicesHawtIo());

		model.addAttribute("copyright", new StringBuilder("© <span>Copyright 1991 - ") //
				.append(YEAR) //
				.append(" by</span> <strong class=\"px-1 sitename\">") //
				.append(name) //
				.append(" # Green@rt</strong>. <span>All Rights Reserved</span>") //
				.toString());
	}
}
