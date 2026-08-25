/**
 * Copyright (c) 2024 Jürg Hersche (Green@rt)
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
package info.hersche.document.metadata.model;

import java.io.Serializable;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * 
 */
@Component
@ToString
@EqualsAndHashCode
public class PageText implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -990700995791639005L;
	private static final String DEFAULT_TEXT = "";

	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(PageText.class);

	/**
	 * Current number of images
	 */
	@Getter
	private String archiveCleanUp = DEFAULT_TEXT;

	/**
	 * Current number of images
	 */
	@Getter
	private String archiveDownload = DEFAULT_TEXT;

	/**
	 * Blog main
	 */
	@Getter
	private String blogMain = DEFAULT_TEXT;

	/**
	 * Blog title
	 */
	@Getter
	private String blogTitle = DEFAULT_TEXT;

	/**
	 * Current number of documents
	 */
	@Getter
	private String countDocProcess = DEFAULT_TEXT;

	/**
	 * Current number of images
	 */
	@Getter
	private String countImageGallery = DEFAULT_TEXT;

	/**
	 * Current number of media
	 */
	@Getter
	private String countMediaProcess = DEFAULT_TEXT;

	/**
	 * File upload
	 */
	@Getter
	private String fileDrop = DEFAULT_TEXT;

	/**
	 * Link texts on footer area
	 */
	@Getter
	private String footerLinksAbout = DEFAULT_TEXT;
	@Getter
	private String footerLinksBlog = DEFAULT_TEXT;
	@Getter
	private String footerLinksHome = DEFAULT_TEXT;
	@Getter
	private String footerLinksPrivacy = DEFAULT_TEXT;
	@Getter
	private String footerDevelopmentTitle = DEFAULT_TEXT;
	@Getter
	private String footerDevelopmentCalendar = DEFAULT_TEXT;
	@Getter
	private String footerDevelopmentScenario = DEFAULT_TEXT;
	@Getter
	private String footerDevelopmentRestApi = DEFAULT_TEXT;
	@Getter
	private String footerLinksProjectTitle = DEFAULT_TEXT;
	@Getter
	private String footerLinksProjectLdapMgmt = DEFAULT_TEXT;
	@Getter
	private String footerLinksProjectOnlyFans = DEFAULT_TEXT;
	@Getter
	private String footerLinksServices = DEFAULT_TEXT;
	@Getter
	private String footerLinksServicesTitle = DEFAULT_TEXT;
	@Getter
	private String footerLinksServicesAll2Jpeg = DEFAULT_TEXT;
	@Getter
	private String footerLinksServicesActuator = DEFAULT_TEXT;
	@Getter
	private String footerLinksServicesCertificate = DEFAULT_TEXT;
	@Getter
	private String footerLinksServicesDoc4Ocr = DEFAULT_TEXT;
	@Getter
	private String footerLinksServicesDng2Jpeg = DEFAULT_TEXT;
	@Getter
	private String footerLinksServicesHawtIo = DEFAULT_TEXT;
	@Getter
	private String footerLinksServicesLightBox = DEFAULT_TEXT;
	@Getter
	private String footerLinksServicesOpenApi = DEFAULT_TEXT;
	@Getter
	private String footerLinksServicesTextChat = DEFAULT_TEXT;
	@Getter
	private String footerLinksServicesWhatsApp = DEFAULT_TEXT;
	@Getter
	private String footerLinksTerms = DEFAULT_TEXT;
	@Getter
	private String footerLinksTitle = DEFAULT_TEXT;

	/**
	 * 
	 */
	private Locale locale;

	/**
	 * 
	 */
	private final MessageSource messageSource;

	/**
	 * Page path
	 */
	@Getter
	private String path = DEFAULT_TEXT;

	/**
	 * Project app 1
	 */
	@Getter
	private String projectApp1 = DEFAULT_TEXT;

	/**
	 * Project app 2 (project 1)
	 */
	@Getter
	private String projectApp2 = DEFAULT_TEXT;

	/**
	 * Project main
	 */
	@Getter
	private String projectMain = DEFAULT_TEXT;

	/**
	 * Project title
	 */
	@Getter
	private String projectTitle = DEFAULT_TEXT;

	/**
	 * Read more...
	 */
	@Getter
	private String readMore = DEFAULT_TEXT;

	/**
	 * Use service
	 */
	@Getter
	private String seeService = DEFAULT_TEXT;

	/**
	 * Service main
	 */
	@Getter
	private String serviceMain = DEFAULT_TEXT;

	/**
	 * Service title
	 */
	@Getter
	private String serviceTitle = DEFAULT_TEXT;

	/**
	 * Name of site
	 */
	@Getter
	private String siteName = DEFAULT_TEXT;

	/**
	 * Page start
	 */
	@Getter
	private String start = DEFAULT_TEXT;

	/**
	 * Current number of documents
	 */
	@Getter
	private String successDocProcess = DEFAULT_TEXT;

	/**
	 * Current number of images
	 */
	@Getter
	private String successImageGallery = DEFAULT_TEXT;

	/**
	 * Current number of media
	 */
	@Getter
	private String successMediaProcess = DEFAULT_TEXT;

	/**
	 * Page title (Header)
	 */
	@Getter
	private String title = DEFAULT_TEXT;

	/**
	 * Page title (main)
	 */
	@Getter
	private String titleHead = DEFAULT_TEXT;

	/**
	 * Page introduction (main)
	 */
	@Getter
	private String titleIntro = DEFAULT_TEXT;

	/**
	 * @param messageSource
	 */
	public PageText(MessageSource messageSource)
	{
		this.messageSource = messageSource;
		
		LOGGER.info("Class '{}' instantiated successfully.", PageText.class.getSimpleName());
	}

	/**
	 * @param code
	 * @return
	 */
	public PageText setArchiveCleanUpId(String code)
	{
		this.archiveCleanUp = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @param code
	 * @return
	 */
	public PageText setArchiveDownloadId(String code)
	{
		this.archiveDownload = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @param code
	 * @return
	 */
	public PageText setBlogMainId(String code)
	{
		this.blogMain = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @param code
	 * @return
	 */
	public PageText setBlogTitleId(String code)
	{
		this.blogTitle = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @param code
	 * @return
	 */
	public PageText setCountDocProcessId(String code)
	{
		this.countDocProcess = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @param code
	 * @return
	 */
	public PageText setCountImageGalleryId(String code)
	{
		this.countImageGallery = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @param code
	 * @return
	 */
	public PageText setCountMediaProcessId(String code)
	{
		this.countMediaProcess = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @param code
	 * @return
	 */
	public PageText setFileDropId(String code)
	{
		this.fileDrop = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @param code
	 * @return
	 */
	public PageText setFooterDevelopmentCalendarId(String code)
	{
		this.footerDevelopmentCalendar = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @param code
	 * @return
	 */
	public PageText setFooterDevelopmentRestApiId(String code)
	{
		this.footerDevelopmentRestApi = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @param code
	 * @return
	 */
	public PageText setFooterDevelopmentScenarioId(String code)
	{
		this.footerDevelopmentScenario = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @param code
	 * @return
	 */
	public PageText setFooterDevelopmentTitleId(String code)
	{
		this.footerDevelopmentTitle = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @param code
	 * @return
	 */
	public PageText setFooterLinksAboutId(String code)
	{
		this.footerLinksAbout = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @param code
	 * @return
	 */
	public PageText setFooterLinksBlogId(String code)
	{
		this.footerLinksBlog = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @param code
	 * @return
	 */
	public PageText setFooterLinksHomeId(String code)
	{
		this.footerLinksHome = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @param code
	 * @return
	 */
	public PageText setFooterLinksPrivacyId(String code)
	{
		this.footerLinksPrivacy = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @param code
	 * @return
	 */
	public PageText setFooterLinksProjectLdapMgmtId(String code)
	{
		this.footerLinksProjectLdapMgmt = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @param code
	 * @return
	 */
	public PageText setFooterLinksProjectOnlyFansId(String code)
	{
		this.footerLinksProjectOnlyFans = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @param code
	 * @return
	 */
	public PageText setFooterLinksProjectTitleId(String code)
	{
		this.footerLinksProjectTitle = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @param code
	 * @return
	 */
	public PageText setFooterLinksServicesActuatorId(String code)
	{
		this.footerLinksServicesActuator = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @param code
	 * @return
	 */
	public PageText setFooterLinksServicesAll2JpegId(String code)
	{
		this.footerLinksServicesAll2Jpeg = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @param code
	 * @return
	 */
	public PageText setFooterLinksServicesCertificateId(String code)
	{
		this.footerLinksServicesCertificate = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @param code
	 * @return
	 */
	public PageText setFooterLinksServicesDng2JpegId(String code)
	{
		this.footerLinksServicesDng2Jpeg = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @param code
	 * @return
	 */
	public PageText setFooterLinksServicesDoc4OcrId(String code)
	{
		this.footerLinksServicesDoc4Ocr = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @param code
	 * @return
	 */
	public PageText setFooterLinksServicesHawtIoId(String code)
	{
		this.footerLinksServicesHawtIo = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @param code
	 * @return
	 */
	public PageText setFooterLinksServicesId(String code)
	{
		this.footerLinksServices = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @param code
	 * @return
	 */
	public PageText setFooterLinksServicesLightBoxId(String code)
	{
		this.footerLinksServicesLightBox = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @param code
	 * @return
	 */
	public PageText setFooterLinksServicesOpenApiId(String code)
	{
		this.footerLinksServicesOpenApi = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @param code
	 * @return
	 */
	public PageText setFooterLinksServicesTextChatId(String code)
	{
		this.footerLinksServicesTextChat = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @param code
	 * @return
	 */
	public PageText setFooterLinksServicesTitleId(String code)
	{
		this.footerLinksServicesTitle = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @param code
	 * @return
	 */
	public PageText setFooterLinksServicesWhatsAppId(String code)
	{
		this.footerLinksServicesWhatsApp = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @param code
	 * @return
	 */
	public PageText setFooterLinksTermsId(String code)
	{
		this.footerLinksTerms = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @param code
	 * @return
	 */
	public PageText setFooterLinksTitleId(String code)
	{
		this.footerLinksTitle = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @param locale
	 * @return
	 */
	public PageText setLocale(Locale locale)
	{
		this.locale = locale;

		return this;
	}

	/**
	 * @param path
	 * @return
	 */
	public PageText setPathId(String code)
	{
		this.path = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @return
	 */
	public PageText setProjectApp1Id(String code)
	{
		this.projectApp1 = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @return
	 */
	public PageText setProjectApp2Id(String code)
	{
		this.projectApp2 = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @return
	 */
	public PageText setProjectMainId(String code)
	{
		this.projectMain = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @return
	 */
	public PageText setProjectTitleId(String code)
	{
		this.projectTitle = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @param code
	 * @return
	 */
	public PageText setReadMoreId(String code)
	{
		this.readMore = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @param code
	 * @return
	 */
	public PageText setSeeServiceId(String code)
	{
		this.seeService = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @param code
	 * @return
	 */
	public PageText setServiceMainId(String code)
	{
		this.serviceMain = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @param code
	 * @return
	 */
	public PageText setServiceTitleId(String code)
	{
		this.serviceTitle = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @param code
	 * @return
	 */
	public PageText setSiteNameId(String code)
	{
		this.siteName = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @param code
	 * @return
	 */
	public PageText setStartId(String code)
	{
		this.start = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @param code
	 * @return
	 */
	public PageText setSuccessDocProcessId(String code)
	{
		this.successDocProcess = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @param code
	 * @return
	 */
	public PageText setSuccessImageGalleryId(String code)
	{
		this.successImageGallery = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @param code
	 * @return
	 */
	public PageText setSuccessMediaProcessId(String code)
	{
		this.successMediaProcess = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @param code
	 * @return
	 */
	public PageText setTitleHeadId(String code)
	{
		this.titleHead = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @param code
	 * @return
	 */
	public PageText setTitleId(String code)
	{
		this.title = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

	/**
	 * @param code
	 * @return
	 */
	public PageText setTitleIntroId(String code)
	{
		this.titleIntro = this.messageSource.getMessage(code, new Object[0], this.locale);

		return this;
	}

}
