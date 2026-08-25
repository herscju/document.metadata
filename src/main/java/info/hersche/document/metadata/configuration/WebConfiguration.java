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
package info.hersche.document.metadata.configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UrlPathHelper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer, HandlerInterceptor
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(WebConfiguration.class);

	/**
	 * 
	 */
	public WebConfiguration()
	{
		// Nothing do do here

		LOGGER.info("Class '{}' instantiated successfully.", WebConfiguration.class.getSimpleName());
	}

	/**
	 * See 'https://stackoverflow.com/a/66565096/6487382'
	 */
	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry)
	{
		registry.addResourceHandler("/favicon.ico") //
				.addResourceLocations("classpath:/static/") // Trailing slash is important!
				.setCacheControl(CacheControl.maxAge(1, TimeUnit.DAYS).noTransform().mustRevalidate());
	}

	/**
	 *
	 */
	@Override
	public void addViewControllers(final ViewControllerRegistry registry)
	{
		registry.addViewController("/").setViewName("index");
		registry.addViewController("/index.html").setViewName("index");
		registry.addViewController("/logout.html").setViewName("logout");
		registry.addViewController("/blog/blog.html").setViewName("blog/blog");
		registry.addViewController("/development/development.html").setViewName("development/development");
		registry.addViewController("/development/development-calendar.html").setViewName("development/development-calendar");
		registry.addViewController("/development/development-restapi.html").setViewName("development/development-restapi");
		registry.addViewController("/development/development-timer.html").setViewName("development/development-timer");
		registry.addViewController("/projects/projects.html").setViewName("projects/projects");
		registry.addViewController("/projects/projects-ldapmgmt.html").setViewName("projects/projects-ldapmgmt");
		registry.addViewController("/projects/projects-onlyfans.html").setViewName("projects/projects-onlyfans");
		registry.addViewController("/services/services.html").setViewName("services/services");
		registry.addViewController("/services/services-actuator.html").setViewName("services/services-actuator");
		registry.addViewController("/services/services-all2jpg.html").setViewName("services/services-all2jpg");
		registry.addViewController("/services/services-all2jpg.html").setViewName("services/services-certificate");
		registry.addViewController("/services/services-dng2jpg.html").setViewName("services/services-dng2jpg");
		registry.addViewController("/services/services-hawtio.html").setViewName("services/services-hawtio");
		registry.addViewController("/services/services-lightbox.html").setViewName("services/services-lightbox");
		registry.addViewController("/services/services-doc4ocr.html").setViewName("services/services-doc4ocr");
		registry.addViewController("/services/services-openapi.html").setViewName("services/services-openapi");
		registry.addViewController("/services/services-timeline.html").setViewName("services/services-timeline");
		registry.addViewController("/services/services-txtchat.html").setViewName("services/services-txtchat");
	}

	/**
	 *
	 */
	@Override
	public void configureHandlerExceptionResolvers(final List<HandlerExceptionResolver> resolvers)
	{
		// resolvers.add(new CustomExceptionResolver());
	}

	/**
	 *
	 */
	@Override
	public void configurePathMatch(final PathMatchConfigurer configurer)
	{
		UrlPathHelper urlPathHelper = new UrlPathHelper();
		urlPathHelper.setUrlDecode(false);
		configurer.setUrlPathHelper(urlPathHelper);
	}

	/**
	 *
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception
	{
		modelAndView.getModel().entrySet().stream().forEach((entry) -> {
			LOGGER.info(">>> HandlerInterceptor.postHandle() >>> {} -> {}", entry.getKey(), entry.getValue());
		});
	}

}
