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

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import info.hersche.document.metadata.model.PageText;
import info.hersche.document.metadata.util.PrincipalResolver;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author herscju
 */
@Controller
public class PageController extends MainController
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(PageController.class);

	/**
	 * 
	 */
	private PageText pageText;

	/**
	 * 
	 */
	public PageController(PageText pageText)
	{
		super();
		this.pageText = pageText;

		LOGGER.info("Class '{}' instantiated successfully.", PageController.class.getSimpleName());
	}

	/**
	 * @param request
	 * @param authentication
	 * @param model
	 * @return
	 */
	@ResponseBody
	@GetMapping({ "/blog/blog", "/blog/blog.htm", "/blog/blog.html" })
	public ModelAndView blogPage(HttpServletRequest request, Authentication authentication, Model model)
	{
		LOGGER.debug("================================================================================");
		LOGGER.debug("Page '{}' requested.", request.getServletPath());

		Locale locale = this.getLocaleFromRequest(request);
		this.pageText.setLocale(locale) //
				.setFooterLinksAboutId("label.footer.links.about") //
				.setFooterLinksBlogId("label.footer.links.blog") //
				.setFooterLinksHomeId("label.footer.links.home") //
				.setFooterLinksPrivacyId("label.footer.links.privacy") //
				.setFooterDevelopmentTitleId("label.footer.development.title") //
				.setFooterDevelopmentCalendarId("label.footer.development.calendar") //
				.setFooterDevelopmentRestApiId("label.footer.development.restapi") //
				.setFooterDevelopmentScenarioId("label.footer.development.scenario") //
				.setFooterLinksProjectLdapMgmtId("label.footer.projects.ldapmgmt") //
				.setFooterLinksProjectOnlyFansId("label.footer.projects.onlyfans") //
				.setFooterLinksProjectTitleId("label.footer.projects.title") //
				.setFooterLinksServicesId("label.footer.links.services") //
				.setFooterLinksServicesActuatorId("label.footer.services.actuator") //
				.setFooterLinksServicesAll2JpegId("label.footer.services.all2jpg") //
				.setFooterLinksServicesCertificateId("label.footer.services.certificate") //
				.setFooterLinksServicesDng2JpegId("label.footer.services.dng2jpg") //
				.setFooterLinksServicesDoc4OcrId("label.footer.services.doc4ocr") //
				.setFooterLinksServicesHawtIoId("label.footer.services.hawtio") //
				.setFooterLinksServicesLightBoxId("label.footer.services.lightbox") //
				.setFooterLinksServicesOpenApiId("label.footer.services.openapi") //
				.setFooterLinksServicesTextChatId("label.footer.services.txtchat") //
				.setFooterLinksServicesTitleId("label.footer.services.title") //
				.setFooterLinksServicesWhatsAppId("label.footer.services.whatsapp") //
				.setFooterLinksTermsId("label.footer.links.terms") //
				.setFooterLinksTitleId("label.footer.links.title") //
				.setSiteNameId("label.sitename") //
				.setFileDropId("label.file.drop") //
				.setReadMoreId("label.read.more") //
				.setSeeServiceId("label.see.service") //
				.setCountDocProcessId("label.count.doc.process") //
				.setCountMediaProcessId("label.count.media.process") //
				.setCountImageGalleryId("label.count.image.gallery") //
				.setSuccessDocProcessId("label.success.doc.process") //
				.setSuccessMediaProcessId("label.success.media.process") //
				.setSuccessImageGalleryId("label.success.image.gallery") //
				.setArchiveDownloadId("label.archive.download") //
				.setArchiveCleanUpId("label.archive.cleanup") //
				.setTitleId("label.blog.title") //
				.setTitleHeadId("label.blog.title.head") //
				.setTitleIntroId("label.blog.title.intro") //
				.setStartId("label.blog.start") //
				.setPathId("label.blog.path") //
				.setServiceTitleId("label.blog.service.head") //
				.setServiceMainId("label.blog.service.main");
		this.setDefaultMetadata(request, authentication, model, "", this.pageText);

		return new ModelAndView("blog/blog");
	}

	/**
	 * @param request
	 * @param authentication
	 * @param model
	 * @return
	 */
	@ResponseBody
	@GetMapping({ "/development/development-calendar", "/development/development-calendar.htm", "/development/development-calendar.html" })
	public ModelAndView developmentCalendarPage(HttpServletRequest request, Authentication authentication, Model model)
	{
		LOGGER.debug("================================================================================");
		LOGGER.debug("Page '{}' requested.", request.getServletPath());

		Locale locale = this.getLocaleFromRequest(request);
		this.pageText.setLocale(locale) //
				.setFooterLinksAboutId("label.footer.links.about") //
				.setFooterLinksBlogId("label.footer.links.blog") //
				.setFooterLinksHomeId("label.footer.links.home") //
				.setFooterLinksPrivacyId("label.footer.links.privacy") //
				.setFooterDevelopmentTitleId("label.footer.development.title") //
				.setFooterDevelopmentCalendarId("label.footer.development.calendar") //
				.setFooterDevelopmentRestApiId("label.footer.development.restapi") //
				.setFooterDevelopmentScenarioId("label.footer.development.scenario") //
				.setFooterLinksProjectLdapMgmtId("label.footer.projects.ldapmgmt") //
				.setFooterLinksProjectOnlyFansId("label.footer.projects.onlyfans") //
				.setFooterLinksProjectTitleId("label.footer.projects.title") //
				.setFooterLinksServicesId("label.footer.links.services") //
				.setFooterLinksServicesActuatorId("label.footer.services.actuator") //
				.setFooterLinksServicesAll2JpegId("label.footer.services.all2jpg") //
				.setFooterLinksServicesCertificateId("label.footer.services.certificate") //
				.setFooterLinksServicesDng2JpegId("label.footer.services.dng2jpg") //
				.setFooterLinksServicesDoc4OcrId("label.footer.services.doc4ocr") //
				.setFooterLinksServicesHawtIoId("label.footer.services.hawtio") //
				.setFooterLinksServicesLightBoxId("label.footer.services.lightbox") //
				.setFooterLinksServicesOpenApiId("label.footer.services.openapi") //
				.setFooterLinksServicesTextChatId("label.footer.services.txtchat") //
				.setFooterLinksServicesTitleId("label.footer.services.title") //
				.setFooterLinksServicesWhatsAppId("label.footer.services.whatsapp") //
				.setFooterLinksTermsId("label.footer.links.terms") //
				.setFooterLinksTitleId("label.footer.links.title") //
				.setSiteNameId("label.sitename") //
				.setFileDropId("label.file.drop") //
				.setReadMoreId("label.read.more") //
				.setSeeServiceId("label.see.service") //
				.setCountDocProcessId("label.count.doc.process") //
				.setCountMediaProcessId("label.count.media.process") //
				.setCountImageGalleryId("label.count.image.gallery") //
				.setSuccessDocProcessId("label.success.doc.process") //
				.setSuccessMediaProcessId("label.success.media.process") //
				.setSuccessImageGalleryId("label.success.image.gallery") //
				.setArchiveDownloadId("label.archive.download") //
				.setArchiveCleanUpId("label.archive.cleanup") //
				.setTitleId("label.development.calendar.title") //
				.setTitleHeadId("label.development.calendar.title.head") //
				.setTitleIntroId("label.development.calendar.title.intro") //
				.setStartId("label.development.calendar.start") //
				.setPathId("label.development.calendar.path") //
				.setServiceTitleId("label.development.calendar.service.head") //
				.setServiceMainId("label.development.calendar.service.main");
		this.setDefaultMetadata(request, authentication, model, "", this.pageText);

		return new ModelAndView("development/development-calendar");
	}

	/**
	 * @param request
	 * @param authentication
	 * @param model
	 * @return
	 */
	@ResponseBody
	@GetMapping({ "/development/development", "/development/development.htm", "/development/development.html" })
	public ModelAndView developmentPage(HttpServletRequest request, Authentication authentication, Model model)
	{
		LOGGER.debug("================================================================================");
		LOGGER.debug("Page '{}' requested.", request.getServletPath());

		Locale locale = this.getLocaleFromRequest(request);
		this.pageText.setLocale(locale) //
				.setFooterLinksAboutId("label.footer.links.about") //
				.setFooterLinksBlogId("label.footer.links.blog") //
				.setFooterLinksHomeId("label.footer.links.home") //
				.setFooterLinksPrivacyId("label.footer.links.privacy") //
				.setFooterDevelopmentTitleId("label.footer.development.title") //
				.setFooterDevelopmentCalendarId("label.footer.development.calendar") //
				.setFooterDevelopmentRestApiId("label.footer.development.restapi") //
				.setFooterDevelopmentScenarioId("label.footer.development.scenario") //
				.setFooterLinksProjectLdapMgmtId("label.footer.projects.ldapmgmt") //
				.setFooterLinksProjectOnlyFansId("label.footer.projects.onlyfans") //
				.setFooterLinksProjectTitleId("label.footer.projects.title") //
				.setFooterLinksServicesId("label.footer.links.services") //
				.setFooterLinksServicesActuatorId("label.footer.services.actuator") //
				.setFooterLinksServicesAll2JpegId("label.footer.services.all2jpg") //
				.setFooterLinksServicesCertificateId("label.footer.services.certificate") //
				.setFooterLinksServicesDng2JpegId("label.footer.services.dng2jpg") //
				.setFooterLinksServicesDoc4OcrId("label.footer.services.doc4ocr") //
				.setFooterLinksServicesHawtIoId("label.footer.services.hawtio") //
				.setFooterLinksServicesLightBoxId("label.footer.services.lightbox") //
				.setFooterLinksServicesOpenApiId("label.footer.services.openapi") //
				.setFooterLinksServicesTextChatId("label.footer.services.txtchat") //
				.setFooterLinksServicesTitleId("label.footer.services.title") //
				.setFooterLinksServicesWhatsAppId("label.footer.services.whatsapp") //
				.setFooterLinksTermsId("label.footer.links.terms") //
				.setFooterLinksTitleId("label.footer.links.title") //
				.setSiteNameId("label.sitename") //
				.setFileDropId("label.file.drop") //
				.setReadMoreId("label.read.more") //
				.setSeeServiceId("label.see.service") //
				.setCountDocProcessId("label.count.doc.process") //
				.setCountMediaProcessId("label.count.media.process") //
				.setCountImageGalleryId("label.count.image.gallery") //
				.setSuccessDocProcessId("label.success.doc.process") //
				.setSuccessMediaProcessId("label.success.media.process") //
				.setSuccessImageGalleryId("label.success.image.gallery") //
				.setArchiveDownloadId("label.archive.download") //
				.setArchiveCleanUpId("label.archive.cleanup") //
				.setTitleId("label.development.title") //
				.setTitleHeadId("label.development.title.head") //
				.setTitleIntroId("label.development.title.intro") //
				.setStartId("label.development.start") //
				.setPathId("label.development.path") //
				.setServiceTitleId("label.development.service.head") //
				.setServiceMainId("label.development.service.main");
		this.setDefaultMetadata(request, authentication, model, "", this.pageText);

		return new ModelAndView("development/development");
	}

	/**
	 * @param request
	 * @param authentication
	 * @param model
	 * @return
	 */
	@ResponseBody
	@GetMapping({ "/development/development-restapi", "/development/development-restapi.htm", "/development/development-restapi.html" })
	public ModelAndView developmentRestApiPage(HttpServletRequest request, Authentication authentication, Model model)
	{
		LOGGER.debug("================================================================================");
		LOGGER.debug("Page '{}' requested.", request.getServletPath());

		Locale locale = this.getLocaleFromRequest(request);
		this.pageText.setLocale(locale) //
				.setFooterLinksAboutId("label.footer.links.about") //
				.setFooterLinksBlogId("label.footer.links.blog") //
				.setFooterLinksHomeId("label.footer.links.home") //
				.setFooterLinksPrivacyId("label.footer.links.privacy") //
				.setFooterDevelopmentTitleId("label.footer.development.title") //
				.setFooterDevelopmentCalendarId("label.footer.development.calendar") //
				.setFooterDevelopmentRestApiId("label.footer.development.restapi") //
				.setFooterDevelopmentScenarioId("label.footer.development.scenario") //
				.setFooterLinksProjectLdapMgmtId("label.footer.projects.ldapmgmt") //
				.setFooterLinksProjectOnlyFansId("label.footer.projects.onlyfans") //
				.setFooterLinksProjectTitleId("label.footer.projects.title") //
				.setFooterLinksServicesId("label.footer.links.services") //
				.setFooterLinksServicesActuatorId("label.footer.services.actuator") //
				.setFooterLinksServicesAll2JpegId("label.footer.services.all2jpg") //
				.setFooterLinksServicesCertificateId("label.footer.services.certificate") //
				.setFooterLinksServicesDng2JpegId("label.footer.services.dng2jpg") //
				.setFooterLinksServicesDoc4OcrId("label.footer.services.doc4ocr") //
				.setFooterLinksServicesHawtIoId("label.footer.services.hawtio") //
				.setFooterLinksServicesLightBoxId("label.footer.services.lightbox") //
				.setFooterLinksServicesOpenApiId("label.footer.services.openapi") //
				.setFooterLinksServicesTextChatId("label.footer.services.txtchat") //
				.setFooterLinksServicesTitleId("label.footer.services.title") //
				.setFooterLinksServicesWhatsAppId("label.footer.services.whatsapp") //
				.setFooterLinksTermsId("label.footer.links.terms") //
				.setFooterLinksTitleId("label.footer.links.title") //
				.setSiteNameId("label.sitename") //
				.setFileDropId("label.file.drop") //
				.setReadMoreId("label.read.more") //
				.setSeeServiceId("label.see.service") //
				.setCountDocProcessId("label.count.doc.process") //
				.setCountMediaProcessId("label.count.media.process") //
				.setCountImageGalleryId("label.count.image.gallery") //
				.setSuccessDocProcessId("label.success.doc.process") //
				.setSuccessMediaProcessId("label.success.media.process") //
				.setSuccessImageGalleryId("label.success.image.gallery") //
				.setArchiveDownloadId("label.archive.download") //
				.setArchiveCleanUpId("label.archive.cleanup") //
				.setTitleId("label.development.restapi.title") //
				.setTitleHeadId("label.development.restapi.title.head") //
				.setTitleIntroId("label.development.restapi.title.intro") //
				.setStartId("label.development.restapi.start") //
				.setPathId("label.development.restapi.path") //
				.setServiceTitleId("label.development.restapi.service.head") //
				.setServiceMainId("label.development.restapi.service.main");
		this.setDefaultMetadata(request, authentication, model, "", this.pageText);

		return new ModelAndView("development/development-restapi");
	}

	/**
	 * @param request
	 * @param authentication
	 * @param model
	 * @return
	 */
	@ResponseBody
	@GetMapping({ "/development/development-timer", "/development/development-timer.htm", "/development/development-timer.html" })
	public ModelAndView developmentTimerPage(HttpServletRequest request, Authentication authentication, Model model)
	{
		LOGGER.debug("================================================================================");
		LOGGER.debug("Page '{}' requested.", request.getServletPath());

		Locale locale = this.getLocaleFromRequest(request);
		this.pageText.setLocale(locale) //
				.setFooterLinksAboutId("label.footer.links.about") //
				.setFooterLinksBlogId("label.footer.links.blog") //
				.setFooterLinksHomeId("label.footer.links.home") //
				.setFooterLinksPrivacyId("label.footer.links.privacy") //
				.setFooterDevelopmentTitleId("label.footer.development.title") //
				.setFooterDevelopmentCalendarId("label.footer.development.calendar") //
				.setFooterDevelopmentRestApiId("label.footer.development.restapi") //
				.setFooterDevelopmentScenarioId("label.footer.development.scenario") //
				.setFooterLinksProjectLdapMgmtId("label.footer.projects.ldapmgmt") //
				.setFooterLinksProjectOnlyFansId("label.footer.projects.onlyfans") //
				.setFooterLinksProjectTitleId("label.footer.projects.title") //
				.setFooterLinksServicesId("label.footer.links.services") //
				.setFooterLinksServicesActuatorId("label.footer.services.actuator") //
				.setFooterLinksServicesAll2JpegId("label.footer.services.all2jpg") //
				.setFooterLinksServicesCertificateId("label.footer.services.certificate") //
				.setFooterLinksServicesDng2JpegId("label.footer.services.dng2jpg") //
				.setFooterLinksServicesDoc4OcrId("label.footer.services.doc4ocr") //
				.setFooterLinksServicesHawtIoId("label.footer.services.hawtio") //
				.setFooterLinksServicesLightBoxId("label.footer.services.lightbox") //
				.setFooterLinksServicesOpenApiId("label.footer.services.openapi") //
				.setFooterLinksServicesTextChatId("label.footer.services.txtchat") //
				.setFooterLinksServicesTitleId("label.footer.services.title") //
				.setFooterLinksServicesWhatsAppId("label.footer.services.whatsapp") //
				.setFooterLinksTermsId("label.footer.links.terms") //
				.setFooterLinksTitleId("label.footer.links.title") //
				.setSiteNameId("label.sitename") //
				.setFileDropId("label.file.drop") //
				.setReadMoreId("label.read.more") //
				.setSeeServiceId("label.see.service") //
				.setCountDocProcessId("label.count.doc.process") //
				.setCountMediaProcessId("label.count.media.process") //
				.setCountImageGalleryId("label.count.image.gallery") //
				.setSuccessDocProcessId("label.success.doc.process") //
				.setSuccessMediaProcessId("label.success.media.process") //
				.setSuccessImageGalleryId("label.success.image.gallery") //
				.setArchiveDownloadId("label.archive.download") //
				.setArchiveCleanUpId("label.archive.cleanup") //
				.setTitleId("label.development.timer.title") //
				.setTitleHeadId("label.development.timer.title.head") //
				.setTitleIntroId("label.development.timer.title.intro") //
				.setStartId("label.development.timer.start") //
				.setPathId("label.development.timer.path") //
				.setServiceTitleId("label.development.timer.service.head") //
				.setServiceMainId("label.development.timer.service.main");
		this.setDefaultMetadata(request, authentication, model, "", this.pageText);

		return new ModelAndView("development/development-timer");
	}

	/**
	 * @param request
	 * @param authentication
	 * @param model
	 * @return
	 */
	@ResponseBody
	@GetMapping({ "/", "/index", "/index.htm", "/index.html" })
	public ModelAndView indexPage(HttpServletRequest request, Authentication authentication, Model model)
	{
		LOGGER.debug("================================================================================");
		LOGGER.debug("Page '{}' requested.", request.getServletPath());

		Locale locale = this.getLocaleFromRequest(request);
		this.pageText.setLocale(locale) //
				.setFooterLinksAboutId("label.footer.links.about") //
				.setFooterLinksBlogId("label.footer.links.blog") //
				.setFooterLinksHomeId("label.footer.links.home") //
				.setFooterLinksPrivacyId("label.footer.links.privacy") //
				.setFooterDevelopmentTitleId("label.footer.development.title") //
				.setFooterDevelopmentCalendarId("label.footer.development.calendar") //
				.setFooterDevelopmentRestApiId("label.footer.development.restapi") //
				.setFooterDevelopmentScenarioId("label.footer.development.scenario") //
				.setFooterLinksProjectLdapMgmtId("label.footer.projects.ldapmgmt") //
				.setFooterLinksProjectOnlyFansId("label.footer.projects.onlyfans") //
				.setFooterLinksProjectTitleId("label.footer.projects.title") //
				.setFooterLinksServicesId("label.footer.links.services") //
				.setFooterLinksServicesActuatorId("label.footer.services.actuator") //
				.setFooterLinksServicesAll2JpegId("label.footer.services.all2jpg") //
				.setFooterLinksServicesCertificateId("label.footer.services.certificate") //
				.setFooterLinksServicesDng2JpegId("label.footer.services.dng2jpg") //
				.setFooterLinksServicesDoc4OcrId("label.footer.services.doc4ocr") //
				.setFooterLinksServicesHawtIoId("label.footer.services.hawtio") //
				.setFooterLinksServicesLightBoxId("label.footer.services.lightbox") //
				.setFooterLinksServicesOpenApiId("label.footer.services.openapi") //
				.setFooterLinksServicesTextChatId("label.footer.services.txtchat") //
				.setFooterLinksServicesTitleId("label.footer.services.title") //
				.setFooterLinksServicesWhatsAppId("label.footer.services.whatsapp") //
				.setFooterLinksTermsId("label.footer.links.terms") //
				.setFooterLinksTitleId("label.footer.links.title") //
				.setSiteNameId("label.sitename") //
				.setFileDropId("label.file.drop") //
				.setReadMoreId("label.read.more") //
				.setSeeServiceId("label.see.service") //
				.setCountDocProcessId("label.count.doc.process") //
				.setCountMediaProcessId("label.count.media.process") //
				.setCountImageGalleryId("label.count.image.gallery") //
				.setSuccessDocProcessId("label.success.doc.process") //
				.setSuccessMediaProcessId("label.success.media.process") //
				.setSuccessImageGalleryId("label.success.image.gallery") //
				.setArchiveDownloadId("label.archive.download") //
				.setArchiveCleanUpId("label.archive.cleanup") //
				.setTitleId("label.index.title") //
				.setTitleHeadId("label.index.title.head") //
				.setTitleIntroId("label.index.title.intro") //
				.setStartId("label.index.start") //
				.setPathId("label.index.path") //
				.setProjectTitleId("label.index.project.head") //
				.setProjectMainId("label.index.project.main") //
				.setProjectApp1Id("label.index.project.app.1") //
				.setProjectApp2Id("label.index.project.app.2") //
				.setServiceTitleId("label.index.service.head") //
				.setServiceMainId("label.index.service.main")//
				.setBlogTitleId("label.index.blog.head") //
				.setBlogMainId("label.index.blog.main");
		this.setDefaultMetadata(request, authentication, model, "", this.pageText);

		return new ModelAndView("index");
	}

	/**
	 * @param request
	 * @param authentication
	 */
	private boolean logout(HttpServletRequest request, Authentication authentication)
	{
		boolean isSuccess = false;

		try
		{
			PrincipalResolver resolver = new PrincipalResolver(request);
			resolver.resolve().logout();

			isSuccess = true;
		}
		catch (ServletException e)
		{
			LOGGER.error("Login out failed.", e);
		}

		return isSuccess;
	}

	/**
	 * @param request
	 * @param authentication
	 * @param model
	 * @return
	 */
	@ResponseBody
	@GetMapping({ "/logout", "/logout.htm", "/logout.html" })
	public ModelAndView logoutPage(HttpServletRequest request, Authentication authentication, ModelMap model)
	{
		LOGGER.debug("================================================================================");
		LOGGER.debug("Page '{}' requested.", request.getServletPath());

		boolean isSuccess = this.logout(request, authentication);
		LOGGER.trace("The logout was successful? '{}'", isSuccess);

		return new ModelAndView("redirect:/index.html", model);
	}

	/**
	 * @param request
	 * @param authentication
	 * @param model
	 * @return
	 */
	@ResponseBody
	@GetMapping({ "/projects/projects-ldapmgmt", "/projects/projects-ldapmgmt.htm", "/projects/projects-ldapmgmt.html" })
	public ModelAndView projectsLdapMgmtPage(HttpServletRequest request, Authentication authentication, Model model)
	{
		LOGGER.debug("================================================================================");
		LOGGER.debug("Page '{}' requested.", request.getServletPath());

		Locale locale = this.getLocaleFromRequest(request);
		this.pageText.setLocale(locale) //
				.setFooterLinksAboutId("label.footer.links.about") //
				.setFooterLinksBlogId("label.footer.links.blog") //
				.setFooterLinksHomeId("label.footer.links.home") //
				.setFooterLinksPrivacyId("label.footer.links.privacy") //
				.setFooterDevelopmentTitleId("label.footer.development.title") //
				.setFooterDevelopmentCalendarId("label.footer.development.calendar") //
				.setFooterDevelopmentRestApiId("label.footer.development.restapi") //
				.setFooterDevelopmentScenarioId("label.footer.development.scenario") //
				.setFooterLinksProjectLdapMgmtId("label.footer.projects.ldapmgmt") //
				.setFooterLinksProjectOnlyFansId("label.footer.projects.onlyfans") //
				.setFooterLinksProjectTitleId("label.footer.projects.title") //
				.setFooterLinksServicesId("label.footer.links.services") //
				.setFooterLinksServicesActuatorId("label.footer.services.actuator") //
				.setFooterLinksServicesAll2JpegId("label.footer.services.all2jpg") //
				.setFooterLinksServicesCertificateId("label.footer.services.certificate") //
				.setFooterLinksServicesDng2JpegId("label.footer.services.dng2jpg") //
				.setFooterLinksServicesDoc4OcrId("label.footer.services.doc4ocr") //
				.setFooterLinksServicesHawtIoId("label.footer.services.hawtio") //
				.setFooterLinksServicesLightBoxId("label.footer.services.lightbox") //
				.setFooterLinksServicesOpenApiId("label.footer.services.openapi") //
				.setFooterLinksServicesTextChatId("label.footer.services.txtchat") //
				.setFooterLinksServicesTitleId("label.footer.services.title") //
				.setFooterLinksServicesWhatsAppId("label.footer.services.whatsapp") //
				.setFooterLinksTermsId("label.footer.links.terms") //
				.setFooterLinksTitleId("label.footer.links.title") //
				.setSiteNameId("label.sitename") //
				.setFileDropId("label.file.drop") //
				.setReadMoreId("label.read.more") //
				.setSeeServiceId("label.see.service") //
				.setCountDocProcessId("label.count.doc.process") //
				.setCountMediaProcessId("label.count.media.process") //
				.setCountImageGalleryId("label.count.image.gallery") //
				.setSuccessDocProcessId("label.success.doc.process") //
				.setSuccessMediaProcessId("label.success.media.process") //
				.setSuccessImageGalleryId("label.success.image.gallery") //
				.setArchiveDownloadId("label.archive.download") //
				.setArchiveCleanUpId("label.archive.cleanup") //
				.setTitleId("label.projects.ldapmgmt.title") //
				.setTitleHeadId("label.projects.ldapmgmt.title.head") //
				.setTitleIntroId("label.projects.ldapmgmt.title.intro") //
				.setStartId("label.projects.ldapmgmt.start") //
				.setPathId("label.projects.ldapmgmt.path") //
				.setServiceTitleId("label.projects.ldapmgmt.service.head") //
				.setServiceMainId("label.projects.ldapmgmt.service.main");
		this.setDefaultMetadata(request, authentication, model, "", this.pageText);

		return new ModelAndView("projects/projects-ldapmgmt");
	}

	/**
	 * @param request
	 * @param authentication
	 * @param model
	 * @return
	 */
	@ResponseBody
	@GetMapping({ "/projects/projects-onlyfans", "/projects/projects-onlyfans.htm", "/projects/projects-onlyfans.html" })
	public ModelAndView projectsOnlyFansPage(HttpServletRequest request, Authentication authentication, Model model)
	{
		LOGGER.debug("================================================================================");
		LOGGER.debug("Page '{}' requested.", request.getServletPath());

		Locale locale = this.getLocaleFromRequest(request);
		this.pageText.setLocale(locale) //
				.setFooterLinksAboutId("label.footer.links.about") //
				.setFooterLinksBlogId("label.footer.links.blog") //
				.setFooterLinksHomeId("label.footer.links.home") //
				.setFooterLinksPrivacyId("label.footer.links.privacy") //
				.setFooterDevelopmentTitleId("label.footer.development.title") //
				.setFooterDevelopmentCalendarId("label.footer.development.calendar") //
				.setFooterDevelopmentRestApiId("label.footer.development.restapi") //
				.setFooterDevelopmentScenarioId("label.footer.development.scenario") //
				.setFooterLinksProjectLdapMgmtId("label.footer.projects.ldapmgmt") //
				.setFooterLinksProjectOnlyFansId("label.footer.projects.onlyfans") //
				.setFooterLinksProjectTitleId("label.footer.projects.title") //
				.setFooterLinksServicesId("label.footer.links.services") //
				.setFooterLinksServicesActuatorId("label.footer.services.actuator") //
				.setFooterLinksServicesAll2JpegId("label.footer.services.all2jpg") //
				.setFooterLinksServicesCertificateId("label.footer.services.certificate") //
				.setFooterLinksServicesDng2JpegId("label.footer.services.dng2jpg") //
				.setFooterLinksServicesDoc4OcrId("label.footer.services.doc4ocr") //
				.setFooterLinksServicesHawtIoId("label.footer.services.hawtio") //
				.setFooterLinksServicesLightBoxId("label.footer.services.lightbox") //
				.setFooterLinksServicesOpenApiId("label.footer.services.openapi") //
				.setFooterLinksServicesTextChatId("label.footer.services.txtchat") //
				.setFooterLinksServicesTitleId("label.footer.services.title") //
				.setFooterLinksServicesWhatsAppId("label.footer.services.whatsapp") //
				.setFooterLinksTermsId("label.footer.links.terms") //
				.setFooterLinksTitleId("label.footer.links.title") //
				.setSiteNameId("label.sitename") //
				.setFileDropId("label.file.drop") //
				.setReadMoreId("label.read.more") //
				.setSeeServiceId("label.see.service") //
				.setCountDocProcessId("label.count.doc.process") //
				.setCountMediaProcessId("label.count.media.process") //
				.setCountImageGalleryId("label.count.image.gallery") //
				.setSuccessDocProcessId("label.success.doc.process") //
				.setSuccessMediaProcessId("label.success.media.process") //
				.setSuccessImageGalleryId("label.success.image.gallery") //
				.setArchiveDownloadId("label.archive.download") //
				.setArchiveCleanUpId("label.archive.cleanup") //
				.setTitleId("label.projects.onlyfans.title") //
				.setTitleHeadId("label.projects.onlyfans.title.head") //
				.setTitleIntroId("label.projects.onlyfans.title.intro") //
				.setStartId("label.projects.onlyfans.start") //
				.setPathId("label.projects.onlyfans.path") //
				.setServiceTitleId("label.projects.onlyfans.service.head") //
				.setServiceMainId("label.projects.onlyfans.service.main");
		this.setDefaultMetadata(request, authentication, model, "", this.pageText);

		return new ModelAndView("projects/projects-onlyfans");
	}

	/**
	 * @param request
	 * @param authentication
	 * @param model
	 * @return
	 */
	@ResponseBody
	@GetMapping({ "/projects/projects", "/projects/projects.htm", "/projects/projects.html" })
	public ModelAndView projectsPage(HttpServletRequest request, Authentication authentication, Model model)
	{
		LOGGER.debug("================================================================================");
		LOGGER.debug("Page '{}' requested.", request.getServletPath());

		Locale locale = this.getLocaleFromRequest(request);
		this.pageText.setLocale(locale) //
				.setFooterLinksAboutId("label.footer.links.about") //
				.setFooterLinksBlogId("label.footer.links.blog") //
				.setFooterLinksHomeId("label.footer.links.home") //
				.setFooterLinksPrivacyId("label.footer.links.privacy") //
				.setFooterDevelopmentTitleId("label.footer.development.title") //
				.setFooterDevelopmentCalendarId("label.footer.development.calendar") //
				.setFooterDevelopmentRestApiId("label.footer.development.restapi") //
				.setFooterDevelopmentScenarioId("label.footer.development.scenario") //
				.setFooterLinksProjectLdapMgmtId("label.footer.projects.ldapmgmt") //
				.setFooterLinksProjectOnlyFansId("label.footer.projects.onlyfans") //
				.setFooterLinksProjectTitleId("label.footer.projects.title") //
				.setFooterLinksServicesId("label.footer.links.services") //
				.setFooterLinksServicesActuatorId("label.footer.services.actuator") //
				.setFooterLinksServicesAll2JpegId("label.footer.services.all2jpg") //
				.setFooterLinksServicesCertificateId("label.footer.services.certificate") //
				.setFooterLinksServicesDng2JpegId("label.footer.services.dng2jpg") //
				.setFooterLinksServicesDoc4OcrId("label.footer.services.doc4ocr") //
				.setFooterLinksServicesHawtIoId("label.footer.services.hawtio") //
				.setFooterLinksServicesLightBoxId("label.footer.services.lightbox") //
				.setFooterLinksServicesOpenApiId("label.footer.services.openapi") //
				.setFooterLinksServicesTextChatId("label.footer.services.txtchat") //
				.setFooterLinksServicesTitleId("label.footer.services.title") //
				.setFooterLinksServicesWhatsAppId("label.footer.services.whatsapp") //
				.setFooterLinksTermsId("label.footer.links.terms") //
				.setFooterLinksTitleId("label.footer.links.title") //
				.setSiteNameId("label.sitename") //
				.setFileDropId("label.file.drop") //
				.setReadMoreId("label.read.more") //
				.setSeeServiceId("label.see.service") //
				.setCountDocProcessId("label.count.doc.process") //
				.setCountMediaProcessId("label.count.media.process") //
				.setCountImageGalleryId("label.count.image.gallery") //
				.setSuccessDocProcessId("label.success.doc.process") //
				.setSuccessMediaProcessId("label.success.media.process") //
				.setSuccessImageGalleryId("label.success.image.gallery") //
				.setArchiveDownloadId("label.archive.download") //
				.setArchiveCleanUpId("label.archive.cleanup") //
				.setTitleId("label.projects.title") //
				.setTitleHeadId("label.projects.title.head") //
				.setTitleIntroId("label.projects.title.intro") //
				.setStartId("label.projects.start") //
				.setPathId("label.projects.path") //
				.setProjectTitleId("label.index.project.head") //
				.setProjectMainId("label.index.project.main") //
				.setProjectApp1Id("label.index.project.app.1") //
				.setProjectApp2Id("label.index.project.app.2") //
				.setServiceTitleId("label.projects.service.head") //
				.setServiceMainId("label.projects.service.main");
		this.setDefaultMetadata(request, authentication, model, "", this.pageText);

		return new ModelAndView("projects/projects");
	}

	/**
	 * @param request
	 * @param authentication
	 * @param model
	 * @return
	 */
	@ResponseBody
	@GetMapping({ "/services/services-actuator", "/services/services-actuator.htm", "/services/services-actuator.html" })
	public ModelAndView servicesActuatorPage(HttpServletRequest request, Authentication authentication, Model model)
	{
		LOGGER.debug("================================================================================");
		LOGGER.debug("Page '{}' requested.", request.getServletPath());

		Locale locale = this.getLocaleFromRequest(request);
		this.pageText.setLocale(locale) //
				.setFooterLinksAboutId("label.footer.links.about") //
				.setFooterLinksBlogId("label.footer.links.blog") //
				.setFooterLinksHomeId("label.footer.links.home") //
				.setFooterLinksPrivacyId("label.footer.links.privacy") //
				.setFooterDevelopmentTitleId("label.footer.development.title") //
				.setFooterDevelopmentCalendarId("label.footer.development.calendar") //
				.setFooterDevelopmentRestApiId("label.footer.development.restapi") //
				.setFooterDevelopmentScenarioId("label.footer.development.scenario") //
				.setFooterLinksProjectLdapMgmtId("label.footer.projects.ldapmgmt") //
				.setFooterLinksProjectOnlyFansId("label.footer.projects.onlyfans") //
				.setFooterLinksProjectTitleId("label.footer.projects.title") //
				.setFooterLinksServicesId("label.footer.links.services") //
				.setFooterLinksServicesActuatorId("label.footer.services.actuator") //
				.setFooterLinksServicesAll2JpegId("label.footer.services.all2jpg") //
				.setFooterLinksServicesCertificateId("label.footer.services.certificate") //
				.setFooterLinksServicesDng2JpegId("label.footer.services.dng2jpg") //
				.setFooterLinksServicesDoc4OcrId("label.footer.services.doc4ocr") //
				.setFooterLinksServicesHawtIoId("label.footer.services.hawtio") //
				.setFooterLinksServicesLightBoxId("label.footer.services.lightbox") //
				.setFooterLinksServicesOpenApiId("label.footer.services.openapi") //
				.setFooterLinksServicesTextChatId("label.footer.services.txtchat") //
				.setFooterLinksServicesTitleId("label.footer.services.title") //
				.setFooterLinksServicesWhatsAppId("label.footer.services.whatsapp") //
				.setFooterLinksTermsId("label.footer.links.terms") //
				.setFooterLinksTitleId("label.footer.links.title") //
				.setSiteNameId("label.sitename") //
				.setFileDropId("label.file.drop") //
				.setReadMoreId("label.read.more") //
				.setSeeServiceId("label.see.service") //
				.setCountDocProcessId("label.count.doc.process") //
				.setCountMediaProcessId("label.count.media.process") //
				.setCountImageGalleryId("label.count.image.gallery") //
				.setSuccessDocProcessId("label.success.doc.process") //
				.setSuccessMediaProcessId("label.success.media.process") //
				.setSuccessImageGalleryId("label.success.image.gallery") //
				.setArchiveDownloadId("label.archive.download") //
				.setArchiveCleanUpId("label.archive.cleanup") //
				.setTitleId("label.services.actuator.title") //
				.setTitleHeadId("label.services.actuator.title.head") //
				.setTitleIntroId("label.services.actuator.title.intro") //
				.setStartId("label.services.actuator.start") //
				.setPathId("label.services.actuator.path") //
				.setServiceTitleId("label.services.actuator.head") //
				.setServiceMainId("label.services.actuator.main");
		this.setDefaultMetadata(request, authentication, model, "", this.pageText);

		return new ModelAndView("services/services-actuator");
	}

	/**
	 * @param request
	 * @param authentication
	 * @param model
	 * @return
	 */
	@ResponseBody
	@GetMapping({ "/services/services-all2jpg", "/services/services-all2jpg.htm", "/services/services-all2jpg.html" })
	public ModelAndView servicesAll2JpgPage(HttpServletRequest request, Authentication authentication, Model model)
	{
		LOGGER.debug("================================================================================");
		LOGGER.debug("Page '{}' requested.", request.getServletPath());

		Locale locale = this.getLocaleFromRequest(request);
		this.pageText.setLocale(locale) //
				.setFooterLinksAboutId("label.footer.links.about") //
				.setFooterLinksBlogId("label.footer.links.blog") //
				.setFooterLinksHomeId("label.footer.links.home") //
				.setFooterLinksPrivacyId("label.footer.links.privacy") //
				.setFooterDevelopmentTitleId("label.footer.development.title") //
				.setFooterDevelopmentCalendarId("label.footer.development.calendar") //
				.setFooterDevelopmentRestApiId("label.footer.development.restapi") //
				.setFooterDevelopmentScenarioId("label.footer.development.scenario") //
				.setFooterLinksProjectLdapMgmtId("label.footer.projects.ldapmgmt") //
				.setFooterLinksProjectOnlyFansId("label.footer.projects.onlyfans") //
				.setFooterLinksProjectTitleId("label.footer.projects.title") //
				.setFooterLinksServicesId("label.footer.links.services") //
				.setFooterLinksServicesActuatorId("label.footer.services.actuator") //
				.setFooterLinksServicesAll2JpegId("label.footer.services.all2jpg") //
				.setFooterLinksServicesCertificateId("label.footer.services.certificate") //
				.setFooterLinksServicesDng2JpegId("label.footer.services.dng2jpg") //
				.setFooterLinksServicesDoc4OcrId("label.footer.services.doc4ocr") //
				.setFooterLinksServicesHawtIoId("label.footer.services.hawtio") //
				.setFooterLinksServicesLightBoxId("label.footer.services.lightbox") //
				.setFooterLinksServicesOpenApiId("label.footer.services.openapi") //
				.setFooterLinksServicesTextChatId("label.footer.services.txtchat") //
				.setFooterLinksServicesTitleId("label.footer.services.title") //
				.setFooterLinksServicesWhatsAppId("label.footer.services.whatsapp") //
				.setFooterLinksTermsId("label.footer.links.terms") //
				.setFooterLinksTitleId("label.footer.links.title") //
				.setSiteNameId("label.sitename") //
				.setFileDropId("label.file.drop") //
				.setReadMoreId("label.read.more") //
				.setSeeServiceId("label.see.service") //
				.setCountDocProcessId("label.count.doc.process") //
				.setCountMediaProcessId("label.count.media.process") //
				.setCountImageGalleryId("label.count.image.gallery") //
				.setSuccessDocProcessId("label.success.doc.process") //
				.setSuccessMediaProcessId("label.success.media.process") //
				.setSuccessImageGalleryId("label.success.image.gallery") //
				.setArchiveDownloadId("label.archive.download") //
				.setArchiveCleanUpId("label.archive.cleanup") //
				.setTitleId("label.services.all2jpg.title") //
				.setTitleHeadId("label.services.all2jpg.title.head") //
				.setTitleIntroId("label.services.all2jpg.title.intro") //
				.setStartId("label.services.all2jpg.start") //
				.setPathId("label.services.all2jpg.path") //
				.setServiceTitleId("label.services.all2jpg.head") //
				.setServiceMainId("label.services.all2jpg.main");
		this.setDefaultMetadata(request, authentication, model, "", this.pageText);

		return new ModelAndView("services/services-all2jpg");
	}

	/**
	 * @param request
	 * @param authentication
	 * @param model
	 * @return
	 */
	@ResponseBody
	@GetMapping({ "/services/services-certificate", "/services/services-certificate.htm", "/services/services-certificate.html" })
	public ModelAndView servicesCertificatePage(HttpServletRequest request, Authentication authentication, Model model)
	{
		LOGGER.debug("================================================================================");
		LOGGER.debug("Page '{}' requested.", request.getServletPath());

		Locale locale = this.getLocaleFromRequest(request);
		this.pageText.setLocale(locale) //
				.setFooterLinksAboutId("label.footer.links.about") //
				.setFooterLinksBlogId("label.footer.links.blog") //
				.setFooterLinksHomeId("label.footer.links.home") //
				.setFooterLinksPrivacyId("label.footer.links.privacy") //
				.setFooterDevelopmentTitleId("label.footer.development.title") //
				.setFooterDevelopmentCalendarId("label.footer.development.calendar") //
				.setFooterDevelopmentRestApiId("label.footer.development.restapi") //
				.setFooterDevelopmentScenarioId("label.footer.development.scenario") //
				.setFooterLinksProjectLdapMgmtId("label.footer.projects.ldapmgmt") //
				.setFooterLinksProjectOnlyFansId("label.footer.projects.onlyfans") //
				.setFooterLinksProjectTitleId("label.footer.projects.title") //
				.setFooterLinksServicesId("label.footer.links.services") //
				.setFooterLinksServicesActuatorId("label.footer.services.actuator") //
				.setFooterLinksServicesAll2JpegId("label.footer.services.all2jpg") //
				.setFooterLinksServicesCertificateId("label.footer.services.certificate") //
				.setFooterLinksServicesDng2JpegId("label.footer.services.dng2jpg") //
				.setFooterLinksServicesDoc4OcrId("label.footer.services.doc4ocr") //
				.setFooterLinksServicesHawtIoId("label.footer.services.hawtio") //
				.setFooterLinksServicesLightBoxId("label.footer.services.lightbox") //
				.setFooterLinksServicesOpenApiId("label.footer.services.openapi") //
				.setFooterLinksServicesTextChatId("label.footer.services.txtchat") //
				.setFooterLinksServicesTitleId("label.footer.services.title") //
				.setFooterLinksServicesWhatsAppId("label.footer.services.whatsapp") //
				.setFooterLinksTermsId("label.footer.links.terms") //
				.setFooterLinksTitleId("label.footer.links.title") //
				.setSiteNameId("label.sitename") //
				.setFileDropId("label.file.drop") //
				.setReadMoreId("label.read.more") //
				.setSeeServiceId("label.see.service") //
				.setCountDocProcessId("label.count.doc.process") //
				.setCountMediaProcessId("label.count.media.process") //
				.setCountImageGalleryId("label.count.image.gallery") //
				.setSuccessDocProcessId("label.success.doc.process") //
				.setSuccessMediaProcessId("label.success.media.process") //
				.setSuccessImageGalleryId("label.success.image.gallery") //
				.setArchiveDownloadId("label.archive.download") //
				.setArchiveCleanUpId("label.archive.cleanup") //
				.setTitleId("label.services.certificate.title") //
				.setTitleHeadId("label.services.certificate.title.head") //
				.setTitleIntroId("label.services.certificate.title.intro") //
				.setStartId("label.services.certificate.start") //
				.setPathId("label.services.certificate.path") //
				.setServiceTitleId("label.services.certificate.head") //
				.setServiceMainId("label.services.certificate.main");
		this.setDefaultMetadata(request, authentication, model, "", this.pageText);

		return new ModelAndView("services/services-certificate");
	}

	/**
	 * @param request
	 * @param authentication
	 * @param model
	 * @return
	 */
	@ResponseBody
	@GetMapping({ "/services/services-dng2jpg", "/services/services-dng2jpg.htm", "/services/services-dng2jpg.html" })
	public ModelAndView servicesDng2JpgPage(HttpServletRequest request, Authentication authentication, Model model)
	{
		LOGGER.debug("================================================================================");
		LOGGER.debug("Page '{}' requested.", request.getServletPath());

		Locale locale = this.getLocaleFromRequest(request);
		this.pageText.setLocale(locale) //
				.setFooterLinksAboutId("label.footer.links.about") //
				.setFooterLinksBlogId("label.footer.links.blog") //
				.setFooterLinksHomeId("label.footer.links.home") //
				.setFooterLinksPrivacyId("label.footer.links.privacy") //
				.setFooterDevelopmentTitleId("label.footer.development.title") //
				.setFooterDevelopmentCalendarId("label.footer.development.calendar") //
				.setFooterDevelopmentRestApiId("label.footer.development.restapi") //
				.setFooterDevelopmentScenarioId("label.footer.development.scenario") //
				.setFooterLinksProjectLdapMgmtId("label.footer.projects.ldapmgmt") //
				.setFooterLinksProjectOnlyFansId("label.footer.projects.onlyfans") //
				.setFooterLinksProjectTitleId("label.footer.projects.title") //
				.setFooterLinksServicesId("label.footer.links.services") //
				.setFooterLinksServicesActuatorId("label.footer.services.actuator") //
				.setFooterLinksServicesAll2JpegId("label.footer.services.all2jpg") //
				.setFooterLinksServicesCertificateId("label.footer.services.certificate") //
				.setFooterLinksServicesDng2JpegId("label.footer.services.dng2jpg") //
				.setFooterLinksServicesDoc4OcrId("label.footer.services.doc4ocr") //
				.setFooterLinksServicesHawtIoId("label.footer.services.hawtio") //
				.setFooterLinksServicesLightBoxId("label.footer.services.lightbox") //
				.setFooterLinksServicesOpenApiId("label.footer.services.openapi") //
				.setFooterLinksServicesTextChatId("label.footer.services.txtchat") //
				.setFooterLinksServicesTitleId("label.footer.services.title") //
				.setFooterLinksServicesWhatsAppId("label.footer.services.whatsapp") //
				.setFooterLinksTermsId("label.footer.links.terms") //
				.setFooterLinksTitleId("label.footer.links.title") //
				.setSiteNameId("label.sitename") //
				.setFileDropId("label.file.drop") //
				.setReadMoreId("label.read.more") //
				.setSeeServiceId("label.see.service") //
				.setCountDocProcessId("label.count.doc.process") //
				.setCountMediaProcessId("label.count.media.process") //
				.setCountImageGalleryId("label.count.image.gallery") //
				.setSuccessDocProcessId("label.success.doc.process") //
				.setSuccessMediaProcessId("label.success.media.process") //
				.setSuccessImageGalleryId("label.success.image.gallery") //
				.setArchiveDownloadId("label.archive.download") //
				.setArchiveCleanUpId("label.archive.cleanup") //
				.setTitleId("label.services.dng2jpg.title") //
				.setTitleHeadId("label.services.dng2jpg.title.head") //
				.setTitleIntroId("label.services.dng2jpg.title.intro") //
				.setStartId("label.services.dng2jpg.start") //
				.setPathId("label.services.dng2jpg.path") //
				.setServiceTitleId("label.services.dng2jpg.head") //
				.setServiceMainId("label.services.dng2jpg.main");
		this.setDefaultMetadata(request, authentication, model, "", this.pageText);

		return new ModelAndView("services/services-dng2jpg");
	}

	/**
	 * @param request
	 * @param authentication
	 * @param model
	 * @return
	 */
	@ResponseBody
	@GetMapping({ "/services/services-doc4ocr", "/services/services-doc4ocr.htm", "/services/services-doc4ocr.html" })
	public ModelAndView servicesDoc4OcrPage(HttpServletRequest request, Authentication authentication, Model model)
	{
		LOGGER.debug("================================================================================");
		LOGGER.debug("Page '{}' requested.", request.getServletPath());

		Locale locale = this.getLocaleFromRequest(request);
		this.pageText.setLocale(locale) //
				.setFooterLinksAboutId("label.footer.links.about") //
				.setFooterLinksBlogId("label.footer.links.blog") //
				.setFooterLinksHomeId("label.footer.links.home") //
				.setFooterLinksPrivacyId("label.footer.links.privacy") //
				.setFooterDevelopmentTitleId("label.footer.development.title") //
				.setFooterDevelopmentCalendarId("label.footer.development.calendar") //
				.setFooterDevelopmentRestApiId("label.footer.development.restapi") //
				.setFooterDevelopmentScenarioId("label.footer.development.scenario") //
				.setFooterLinksProjectLdapMgmtId("label.footer.projects.ldapmgmt") //
				.setFooterLinksProjectOnlyFansId("label.footer.projects.onlyfans") //
				.setFooterLinksProjectTitleId("label.footer.projects.title") //
				.setFooterLinksServicesId("label.footer.links.services") //
				.setFooterLinksServicesActuatorId("label.footer.services.actuator") //
				.setFooterLinksServicesAll2JpegId("label.footer.services.all2jpg") //
				.setFooterLinksServicesCertificateId("label.footer.services.certificate") //
				.setFooterLinksServicesDng2JpegId("label.footer.services.dng2jpg") //
				.setFooterLinksServicesDoc4OcrId("label.footer.services.doc4ocr") //
				.setFooterLinksServicesHawtIoId("label.footer.services.hawtio") //
				.setFooterLinksServicesLightBoxId("label.footer.services.lightbox") //
				.setFooterLinksServicesOpenApiId("label.footer.services.openapi") //
				.setFooterLinksServicesTextChatId("label.footer.services.txtchat") //
				.setFooterLinksServicesTitleId("label.footer.services.title") //
				.setFooterLinksServicesWhatsAppId("label.footer.services.whatsapp") //
				.setFooterLinksTermsId("label.footer.links.terms") //
				.setFooterLinksTitleId("label.footer.links.title") //
				.setSiteNameId("label.sitename") //
				.setFileDropId("label.file.drop") //
				.setReadMoreId("label.read.more") //
				.setSeeServiceId("label.see.service") //
				.setCountDocProcessId("label.count.doc.process") //
				.setCountMediaProcessId("label.count.media.process") //
				.setCountImageGalleryId("label.count.image.gallery") //
				.setSuccessDocProcessId("label.success.doc.process") //
				.setSuccessMediaProcessId("label.success.media.process") //
				.setSuccessImageGalleryId("label.success.image.gallery") //
				.setArchiveDownloadId("label.archive.download") //
				.setArchiveCleanUpId("label.archive.cleanup") //
				.setTitleId("label.services.doc4ocr.title") //
				.setTitleHeadId("label.services.doc4ocr.title.head") //
				.setTitleIntroId("label.services.doc4ocr.title.intro") //
				.setStartId("label.services.doc4ocr.start") //
				.setPathId("label.services.doc4ocr.path") //
				.setServiceTitleId("label.services.doc4ocr.head") //
				.setServiceMainId("label.services.doc4ocr.main");
		this.setDefaultMetadata(request, authentication, model, "", this.pageText);

		return new ModelAndView("services/services-doc4ocr");
	}

	/**
	 * @param request
	 * @param authentication
	 * @param model
	 * @return
	 */
	@ResponseBody
	@GetMapping({ "/services/services-hawtio", "/services/services-hawtio.htm", "/services/services-hawtio.html" })
	public ModelAndView servicesHawtioPage(HttpServletRequest request, Authentication authentication, Model model)
	{
		LOGGER.debug("================================================================================");
		LOGGER.debug("Page '{}' requested.", request.getServletPath());

		Locale locale = this.getLocaleFromRequest(request);
		this.pageText.setLocale(locale) //
				.setFooterLinksAboutId("label.footer.links.about") //
				.setFooterLinksBlogId("label.footer.links.blog") //
				.setFooterLinksHomeId("label.footer.links.home") //
				.setFooterLinksPrivacyId("label.footer.links.privacy") //
				.setFooterDevelopmentTitleId("label.footer.development.title") //
				.setFooterDevelopmentCalendarId("label.footer.development.calendar") //
				.setFooterDevelopmentRestApiId("label.footer.development.restapi") //
				.setFooterDevelopmentScenarioId("label.footer.development.scenario") //
				.setFooterLinksProjectLdapMgmtId("label.footer.projects.ldapmgmt") //
				.setFooterLinksProjectOnlyFansId("label.footer.projects.onlyfans") //
				.setFooterLinksProjectTitleId("label.footer.projects.title") //
				.setFooterLinksServicesId("label.footer.links.services") //
				.setFooterLinksServicesActuatorId("label.footer.services.actuator") //
				.setFooterLinksServicesAll2JpegId("label.footer.services.all2jpg") //
				.setFooterLinksServicesCertificateId("label.footer.services.certificate") //
				.setFooterLinksServicesDng2JpegId("label.footer.services.dng2jpg") //
				.setFooterLinksServicesDoc4OcrId("label.footer.services.doc4ocr") //
				.setFooterLinksServicesHawtIoId("label.footer.services.hawtio") //
				.setFooterLinksServicesLightBoxId("label.footer.services.lightbox") //
				.setFooterLinksServicesOpenApiId("label.footer.services.openapi") //
				.setFooterLinksServicesTextChatId("label.footer.services.txtchat") //
				.setFooterLinksServicesTitleId("label.footer.services.title") //
				.setFooterLinksServicesWhatsAppId("label.footer.services.whatsapp") //
				.setFooterLinksTermsId("label.footer.links.terms") //
				.setFooterLinksTitleId("label.footer.links.title") //
				.setSiteNameId("label.sitename") //
				.setFileDropId("label.file.drop") //
				.setReadMoreId("label.read.more") //
				.setSeeServiceId("label.see.service") //
				.setCountDocProcessId("label.count.doc.process") //
				.setCountMediaProcessId("label.count.media.process") //
				.setCountImageGalleryId("label.count.image.gallery") //
				.setSuccessDocProcessId("label.success.doc.process") //
				.setSuccessMediaProcessId("label.success.media.process") //
				.setSuccessImageGalleryId("label.success.image.gallery") //
				.setArchiveDownloadId("label.archive.download") //
				.setArchiveCleanUpId("label.archive.cleanup") //
				.setTitleId("label.services.hawtio.title") //
				.setTitleHeadId("label.services.hawtio.title.head") //
				.setTitleIntroId("label.services.hawtio.title.intro") //
				.setStartId("label.services.hawtio.start") //
				.setPathId("label.services.hawtio.path") //
				.setServiceTitleId("label.services.hawtio.head") //
				.setServiceMainId("label.services.hawtio.main");
		this.setDefaultMetadata(request, authentication, model, "", this.pageText);

		return new ModelAndView("services/services-hawtio");
	}

	/**
	 * @param request
	 * @param authentication
	 * @param model
	 * @return
	 */
	@ResponseBody
	@GetMapping({ "/services/services-lightbox", "/services/services-lightbox.htm", "/services/services-lightbox.html" })
	public ModelAndView servicesLightBoxPage(HttpServletRequest request, Authentication authentication, Model model)
	{
		LOGGER.debug("================================================================================");
		LOGGER.debug("Page '{}' requested.", request.getServletPath());

		Locale locale = this.getLocaleFromRequest(request);
		this.pageText.setLocale(locale) //
				.setFooterLinksAboutId("label.footer.links.about") //
				.setFooterLinksBlogId("label.footer.links.blog") //
				.setFooterLinksHomeId("label.footer.links.home") //
				.setFooterLinksPrivacyId("label.footer.links.privacy") //
				.setFooterDevelopmentTitleId("label.footer.development.title") //
				.setFooterDevelopmentCalendarId("label.footer.development.calendar") //
				.setFooterDevelopmentRestApiId("label.footer.development.restapi") //
				.setFooterDevelopmentScenarioId("label.footer.development.scenario") //
				.setFooterLinksProjectLdapMgmtId("label.footer.projects.ldapmgmt") //
				.setFooterLinksProjectOnlyFansId("label.footer.projects.onlyfans") //
				.setFooterLinksProjectTitleId("label.footer.projects.title") //
				.setFooterLinksServicesId("label.footer.links.services") //
				.setFooterLinksServicesActuatorId("label.footer.services.actuator") //
				.setFooterLinksServicesAll2JpegId("label.footer.services.all2jpg") //
				.setFooterLinksServicesCertificateId("label.footer.services.certificate") //
				.setFooterLinksServicesDng2JpegId("label.footer.services.dng2jpg") //
				.setFooterLinksServicesDoc4OcrId("label.footer.services.doc4ocr") //
				.setFooterLinksServicesHawtIoId("label.footer.services.hawtio") //
				.setFooterLinksServicesLightBoxId("label.footer.services.lightbox") //
				.setFooterLinksServicesOpenApiId("label.footer.services.openapi") //
				.setFooterLinksServicesTextChatId("label.footer.services.txtchat") //
				.setFooterLinksServicesTitleId("label.footer.services.title") //
				.setFooterLinksServicesWhatsAppId("label.footer.services.whatsapp") //
				.setFooterLinksTermsId("label.footer.links.terms") //
				.setFooterLinksTitleId("label.footer.links.title") //
				.setSiteNameId("label.sitename") //
				.setFileDropId("label.file.drop") //
				.setReadMoreId("label.read.more") //
				.setSeeServiceId("label.see.service") //
				.setCountDocProcessId("label.count.doc.process") //
				.setCountMediaProcessId("label.count.media.process") //
				.setCountImageGalleryId("label.count.image.gallery") //
				.setSuccessDocProcessId("label.success.doc.process") //
				.setSuccessMediaProcessId("label.success.media.process") //
				.setSuccessImageGalleryId("label.success.image.gallery") //
				.setArchiveDownloadId("label.archive.download") //
				.setArchiveCleanUpId("label.archive.cleanup") //
				.setTitleId("label.services.lightbox.title") //
				.setTitleHeadId("label.services.lightbox.title.head") //
				.setTitleIntroId("label.services.lightbox.title.intro") //
				.setStartId("label.services.lightbox.start") //
				.setPathId("label.services.lightbox.path") //
				.setServiceTitleId("label.services.lightbox.head") //
				.setServiceMainId("label.services.lightbox.main");
		this.setDefaultMetadata(request, authentication, model, "", this.pageText);

		return new ModelAndView("services/services-lightbox");
	}

	/**
	 * @param request
	 * @param authentication
	 * @param model
	 * @return
	 */
	@ResponseBody
	@GetMapping({ "/services/services-openapi", "/services/services-openapi.htm", "/services/services-openapi.html" })
	public ModelAndView servicesOpenApiPage(HttpServletRequest request, Authentication authentication, Model model)
	{
		LOGGER.debug("================================================================================");
		LOGGER.debug("Page '{}' requested.", request.getServletPath());

		Locale locale = this.getLocaleFromRequest(request);
		this.pageText.setLocale(locale) //
				.setFooterLinksAboutId("label.footer.links.about") //
				.setFooterLinksBlogId("label.footer.links.blog") //
				.setFooterLinksHomeId("label.footer.links.home") //
				.setFooterLinksPrivacyId("label.footer.links.privacy") //
				.setFooterDevelopmentTitleId("label.footer.development.title") //
				.setFooterDevelopmentCalendarId("label.footer.development.calendar") //
				.setFooterDevelopmentRestApiId("label.footer.development.restapi") //
				.setFooterDevelopmentScenarioId("label.footer.development.scenario") //
				.setFooterLinksProjectLdapMgmtId("label.footer.projects.ldapmgmt") //
				.setFooterLinksProjectOnlyFansId("label.footer.projects.onlyfans") //
				.setFooterLinksProjectTitleId("label.footer.projects.title") //
				.setFooterLinksServicesId("label.footer.links.services") //
				.setFooterLinksServicesActuatorId("label.footer.services.actuator") //
				.setFooterLinksServicesAll2JpegId("label.footer.services.all2jpg") //
				.setFooterLinksServicesCertificateId("label.footer.services.certificate") //
				.setFooterLinksServicesDng2JpegId("label.footer.services.dng2jpg") //
				.setFooterLinksServicesDoc4OcrId("label.footer.services.doc4ocr") //
				.setFooterLinksServicesHawtIoId("label.footer.services.hawtio") //
				.setFooterLinksServicesLightBoxId("label.footer.services.lightbox") //
				.setFooterLinksServicesOpenApiId("label.footer.services.openapi") //
				.setFooterLinksServicesTextChatId("label.footer.services.txtchat") //
				.setFooterLinksServicesTitleId("label.footer.services.title") //
				.setFooterLinksServicesWhatsAppId("label.footer.services.whatsapp") //
				.setFooterLinksTermsId("label.footer.links.terms") //
				.setFooterLinksTitleId("label.footer.links.title") //
				.setSiteNameId("label.sitename") //
				.setFileDropId("label.file.drop") //
				.setReadMoreId("label.read.more") //
				.setSeeServiceId("label.see.service") //
				.setCountDocProcessId("label.count.doc.process") //
				.setCountMediaProcessId("label.count.media.process") //
				.setCountImageGalleryId("label.count.image.gallery") //
				.setSuccessDocProcessId("label.success.doc.process") //
				.setSuccessMediaProcessId("label.success.media.process") //
				.setSuccessImageGalleryId("label.success.image.gallery") //
				.setArchiveDownloadId("label.archive.download") //
				.setArchiveCleanUpId("label.archive.cleanup") //
				.setTitleId("label.services.openapi.title") //
				.setTitleHeadId("label.services.openapi.title.head") //
				.setTitleIntroId("label.services.openapi.title.intro") //
				.setStartId("label.services.openapi.start") //
				.setPathId("label.services.openapi.path") //
				.setServiceTitleId("label.services.openapi.head") //
				.setServiceMainId("label.services.openapi.main");
		this.setDefaultMetadata(request, authentication, model, "", this.pageText);

		return new ModelAndView("services/services-openapi");
	}

	/**
	 * @param request
	 * @param authentication
	 * @param model
	 * @return
	 */
	@ResponseBody
	@GetMapping({ "/services/services", "/services/services.htm", "/services/services.html" })
	public ModelAndView servicesPage(HttpServletRequest request, Authentication authentication, Model model)
	{
		LOGGER.debug("================================================================================");
		LOGGER.debug("Page '{}' requested.", request.getServletPath());

		Locale locale = this.getLocaleFromRequest(request);
		this.pageText.setLocale(locale) //
				.setFooterLinksAboutId("label.footer.links.about") //
				.setFooterLinksBlogId("label.footer.links.blog") //
				.setFooterLinksHomeId("label.footer.links.home") //
				.setFooterLinksPrivacyId("label.footer.links.privacy") //
				.setFooterDevelopmentTitleId("label.footer.development.title") //
				.setFooterDevelopmentCalendarId("label.footer.development.calendar") //
				.setFooterDevelopmentRestApiId("label.footer.development.restapi") //
				.setFooterDevelopmentScenarioId("label.footer.development.scenario") //
				.setFooterLinksProjectLdapMgmtId("label.footer.projects.ldapmgmt") //
				.setFooterLinksProjectOnlyFansId("label.footer.projects.onlyfans") //
				.setFooterLinksProjectTitleId("label.footer.projects.title") //
				.setFooterLinksServicesId("label.footer.links.services") //
				.setFooterLinksServicesActuatorId("label.footer.services.actuator") //
				.setFooterLinksServicesAll2JpegId("label.footer.services.all2jpg") //
				.setFooterLinksServicesCertificateId("label.footer.services.certificate") //
				.setFooterLinksServicesDng2JpegId("label.footer.services.dng2jpg") //
				.setFooterLinksServicesDoc4OcrId("label.footer.services.doc4ocr") //
				.setFooterLinksServicesHawtIoId("label.footer.services.hawtio") //
				.setFooterLinksServicesLightBoxId("label.footer.services.lightbox") //
				.setFooterLinksServicesOpenApiId("label.footer.services.openapi") //
				.setFooterLinksServicesTextChatId("label.footer.services.txtchat") //
				.setFooterLinksServicesTitleId("label.footer.services.title") //
				.setFooterLinksServicesWhatsAppId("label.footer.services.whatsapp") //
				.setFooterLinksTermsId("label.footer.links.terms") //
				.setFooterLinksTitleId("label.footer.links.title") //
				.setSiteNameId("label.sitename") //
				.setFileDropId("label.file.drop") //
				.setReadMoreId("label.read.more") //
				.setSeeServiceId("label.see.service") //
				.setCountDocProcessId("label.count.doc.process") //
				.setCountMediaProcessId("label.count.media.process") //
				.setCountImageGalleryId("label.count.image.gallery") //
				.setSuccessDocProcessId("label.success.doc.process") //
				.setSuccessMediaProcessId("label.success.media.process") //
				.setSuccessImageGalleryId("label.success.image.gallery") //
				.setArchiveDownloadId("label.archive.download") //
				.setArchiveCleanUpId("label.archive.cleanup") //
				.setTitleId("label.services.title") //
				.setTitleHeadId("label.services.title.head") //
				.setTitleIntroId("label.services.title.intro") //
				.setStartId("label.services.start") //
				.setPathId("label.services.path") //
				.setServiceTitleId("label.services.service.head") //
				.setServiceMainId("label.services.service.main");
		this.setDefaultMetadata(request, authentication, model, "", this.pageText);

		return new ModelAndView("services/services");
	}

	/**
	 * @param request
	 * @param authentication
	 * @param model
	 * @return
	 */
	@ResponseBody
	@GetMapping({ "/services/services-txtchat", "/services/services-txtchat.htm", "/services/services-txtchat.html" })
	public ModelAndView servicesTextChatPage(HttpServletRequest request, Authentication authentication, Model model)
	{
		LOGGER.debug("================================================================================");
		LOGGER.debug("Page '{}' requested.", request.getServletPath());

		Locale locale = this.getLocaleFromRequest(request);
		this.pageText.setLocale(locale) //
				.setFooterLinksAboutId("label.footer.links.about") //
				.setFooterLinksBlogId("label.footer.links.blog") //
				.setFooterLinksHomeId("label.footer.links.home") //
				.setFooterLinksPrivacyId("label.footer.links.privacy") //
				.setFooterDevelopmentTitleId("label.footer.development.title") //
				.setFooterDevelopmentCalendarId("label.footer.development.calendar") //
				.setFooterDevelopmentRestApiId("label.footer.development.restapi") //
				.setFooterDevelopmentScenarioId("label.footer.development.scenario") //
				.setFooterLinksProjectLdapMgmtId("label.footer.projects.ldapmgmt") //
				.setFooterLinksProjectOnlyFansId("label.footer.projects.onlyfans") //
				.setFooterLinksProjectTitleId("label.footer.projects.title") //
				.setFooterLinksServicesId("label.footer.links.services") //
				.setFooterLinksServicesActuatorId("label.footer.services.actuator") //
				.setFooterLinksServicesAll2JpegId("label.footer.services.all2jpg") //
				.setFooterLinksServicesCertificateId("label.footer.services.certificate") //
				.setFooterLinksServicesDng2JpegId("label.footer.services.dng2jpg") //
				.setFooterLinksServicesDoc4OcrId("label.footer.services.doc4ocr") //
				.setFooterLinksServicesHawtIoId("label.footer.services.hawtio") //
				.setFooterLinksServicesLightBoxId("label.footer.services.lightbox") //
				.setFooterLinksServicesOpenApiId("label.footer.services.openapi") //
				.setFooterLinksServicesTextChatId("label.footer.services.txtchat") //
				.setFooterLinksServicesTitleId("label.footer.services.title") //
				.setFooterLinksServicesWhatsAppId("label.footer.services.whatsapp") //
				.setFooterLinksTermsId("label.footer.links.terms") //
				.setFooterLinksTitleId("label.footer.links.title") //
				.setSiteNameId("label.sitename") //
				.setFileDropId("label.file.drop") //
				.setReadMoreId("label.read.more") //
				.setSeeServiceId("label.see.service") //
				.setCountDocProcessId("label.count.doc.process") //
				.setCountMediaProcessId("label.count.media.process") //
				.setCountImageGalleryId("label.count.image.gallery") //
				.setSuccessDocProcessId("label.success.doc.process") //
				.setSuccessMediaProcessId("label.success.media.process") //
				.setSuccessImageGalleryId("label.success.image.gallery") //
				.setArchiveDownloadId("label.archive.download") //
				.setArchiveCleanUpId("label.archive.cleanup") //
				.setTitleId("label.services.txtchat.title") //
				.setTitleHeadId("label.services.txtchat.title.head") //
				.setTitleIntroId("label.services.txtchat.title.intro") //
				.setStartId("label.services.txtchat.start") //
				.setPathId("label.services.txtchat.path") //
				.setServiceTitleId("label.services.txtchat.head") //
				.setServiceMainId("label.services.txtchat.main");
		this.setDefaultMetadata(request, authentication, model, "", this.pageText);

		return new ModelAndView("services/services-txtchat");
	}

	/**
	 * @param request
	 * @param authentication
	 * @param model
	 * @return
	 */
	@ResponseBody
	@GetMapping({ "/services/services-timeline", "/services/services-timeline.htm", "/services/services-timeline.html" })
	public ModelAndView servicesWhatsAppPage(HttpServletRequest request, Authentication authentication, Model model)
	{
		LOGGER.debug("================================================================================");
		LOGGER.debug("Page '{}' requested.", request.getServletPath());

		Locale locale = this.getLocaleFromRequest(request);
		this.pageText.setLocale(locale) //
				.setFooterLinksAboutId("label.footer.links.about") //
				.setFooterLinksBlogId("label.footer.links.blog") //
				.setFooterLinksHomeId("label.footer.links.home") //
				.setFooterLinksPrivacyId("label.footer.links.privacy") //
				.setFooterDevelopmentTitleId("label.footer.development.title") //
				.setFooterDevelopmentCalendarId("label.footer.development.calendar") //
				.setFooterDevelopmentRestApiId("label.footer.development.restapi") //
				.setFooterDevelopmentScenarioId("label.footer.development.scenario") //
				.setFooterLinksProjectLdapMgmtId("label.footer.projects.ldapmgmt") //
				.setFooterLinksProjectOnlyFansId("label.footer.projects.onlyfans") //
				.setFooterLinksProjectTitleId("label.footer.projects.title") //
				.setFooterLinksServicesId("label.footer.links.services") //
				.setFooterLinksServicesActuatorId("label.footer.services.actuator") //
				.setFooterLinksServicesAll2JpegId("label.footer.services.all2jpg") //
				.setFooterLinksServicesCertificateId("label.footer.services.certificate") //
				.setFooterLinksServicesDng2JpegId("label.footer.services.dng2jpg") //
				.setFooterLinksServicesDoc4OcrId("label.footer.services.doc4ocr") //
				.setFooterLinksServicesHawtIoId("label.footer.services.hawtio") //
				.setFooterLinksServicesLightBoxId("label.footer.services.lightbox") //
				.setFooterLinksServicesOpenApiId("label.footer.services.openapi") //
				.setFooterLinksServicesTextChatId("label.footer.services.txtchat") //
				.setFooterLinksServicesTitleId("label.footer.services.title") //
				.setFooterLinksServicesWhatsAppId("label.footer.services.whatsapp") //
				.setFooterLinksTermsId("label.footer.links.terms") //
				.setFooterLinksTitleId("label.footer.links.title") //
				.setSiteNameId("label.sitename") //
				.setFileDropId("label.file.drop") //
				.setReadMoreId("label.read.more") //
				.setSeeServiceId("label.see.service") //
				.setCountDocProcessId("label.count.doc.process") //
				.setCountMediaProcessId("label.count.media.process") //
				.setCountImageGalleryId("label.count.image.gallery") //
				.setSuccessDocProcessId("label.success.doc.process") //
				.setSuccessMediaProcessId("label.success.media.process") //
				.setSuccessImageGalleryId("label.success.image.gallery") //
				.setArchiveDownloadId("label.archive.download") //
				.setArchiveCleanUpId("label.archive.cleanup") //
				.setTitleId("label.services.whatsapp.title") //
				.setTitleHeadId("label.services.whatsapp.title.head") //
				.setTitleIntroId("label.services.whatsapp.title.intro") //
				.setStartId("label.services.whatsapp.start") //
				.setPathId("label.services.whatsapp.path") //
				.setServiceTitleId("label.services.whatsapp.head") //
				.setServiceMainId("label.services.whatsapp.main");
		this.setDefaultMetadata(request, authentication, model, "", this.pageText);
		LOGGER.info("+++ {}", pageText);


		return new ModelAndView("services/services-timeline");
	}

}
