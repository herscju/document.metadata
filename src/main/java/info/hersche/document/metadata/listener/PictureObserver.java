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
package info.hersche.document.metadata.listener;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jürg Hersche (herscju)
 */
public class PictureObserver implements ImageObserver
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(PictureObserver.class);

	/**
	 * Default constructor
	 */
	public PictureObserver()
	{
		// Nothing to do here
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.image.ImageObserver#imageUpdate(java.awt.Image, int, int, int, int, int)
	 */
	@Override
	public boolean imageUpdate(Image image, int flags, int x, int y, int width, int height)
	{
		if ((flags & HEIGHT) != 0)
		{
			LOGGER.debug("Image height '{}' pixels.", height);
		}

		if ((flags & WIDTH) != 0)
		{
			LOGGER.debug("Image width '{}' pixels.", width);
		}

		if ((flags & FRAMEBITS) != 0)
		{
			LOGGER.debug("Another frame finished.");
		}

		if ((flags & SOMEBITS) != 0)
		{
			Rectangle rectangle = new Rectangle(x, y, width, height);

			LOGGER.debug("Image section '{}'.", rectangle);
		}

		if ((flags & ALLBITS) != 0)
		{
			LOGGER.debug("Image finished!");

			return false;
		}

		if ((flags & ABORT) != 0)
		{
			LOGGER.debug("Image load aborted...");

			return false;
		}

		return true;
	}
}
