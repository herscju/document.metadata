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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import info.hersche.document.metadata.model.PageText;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author Jürg Hersche (herscju)
 */
@Controller
public class FaultController extends MainController implements ErrorController
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(FaultController.class);

	@Autowired
	private PageText pageText;

	/**
	 * Default constructor
	 */
	public FaultController()
	{
		// Nothing to do here

		LOGGER.info("Class '{}' instantiated successfully.", FaultController.class.getSimpleName());
	}

	/**
	 * @param request
	 * @param authentication
	 * @param model
	 * @return
	 */
	@ResponseBody
	@GetMapping({ "/error", "/error.htm", "/error.html" })
	public ModelAndView errorPage(HttpServletRequest request, Authentication authentication, Model model)
	{
		LOGGER.debug("================================================================================");
		LOGGER.debug("Error page '{}' requested.", request.getServletPath());

		Locale locale = this.getLocaleFromRequest(request);
		String title = "label.error.title";
		String titleHead = "label.error.title.head";
		String titleIntro = "label.error.title.intro";
		String start = "label.error.start";

		ModelAndView mv = new ModelAndView("error");

		String siteName = "label.sitename";
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		if (status != null)
		{
			Integer statusCode = Integer.valueOf(status.toString());
			if (statusCode == HttpStatus.FORBIDDEN.value())
			{
				title = "label.error.403.title";
				titleHead = "label.error.403.title.head";
				titleIntro = "label.error.403.title.intro";
				start = "label.error.403.start";

				mv = new ModelAndView("error-403");
			}
			else if (statusCode == HttpStatus.NOT_FOUND.value())
			{
				title = "label.error.404.title";
				titleHead = "label.error.404.title.head";
				titleIntro = "label.error.404.title.intro";
				start = "label.error.404.start";

				mv = new ModelAndView("error-404");
			}
			else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value())
			{
				title = "label.error.500.title";
				titleHead = "label.error.500.title.head";
				titleIntro = "label.error.500.title.intro";
				start = "label.error.404.start";

				mv = new ModelAndView("error-500");
			}
		}

		this.pageText.setLocale(locale) //
				.setSiteNameId("label.sitename") //
				.setReadMoreId("label.read.more") //
				.setSeeServiceId("label.see.service") //
				.setSiteNameId(siteName) //
				.setTitleId(title) //
				.setTitleHeadId(titleHead) //
				.setTitleIntroId(titleIntro) //
				.setStartId(start) //
				.setServiceTitleId("label.services.txtchat.title") //
				.setServiceMainId("label.services.txtchat.main");
		this.setDefaultMetadata(request, authentication, model, "", this.pageText);

		return mv;
	}
}
