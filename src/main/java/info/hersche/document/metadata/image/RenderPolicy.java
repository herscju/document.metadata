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
package info.hersche.document.metadata.image;

import java.awt.Dimension;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 */
@Getter
@Setter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true, builderMethodName = "toBuilder")
public class RenderPolicy
{
	@Getter
	public enum Orientation {
		SQUARE("Square"), PORTRAIT("Portrait"), LANDSCAPE("Landscape");

		/**
		 * More nice name of orientation
		 */
		private String name;

		/**
		 * @param string
		 */
		private Orientation(String name)
		{
			this.name = name;
		}

	}

	/**
	 * 
	 */
	private final static Logger LOGGER = LoggerFactory.getLogger(RenderPolicy.class);

	/**
	 * Source dimension
	 */
	@Builder.Default
	private Dimension sourceDimension = new Dimension();
	/**
	 * Target dimension
	 */
	@Builder.Default
	private Dimension targetDimension = new Dimension();
	/**
	 * 
	 */
	@Builder.Default
	private Orientation orientation = Orientation.SQUARE;
	/**
	 * Default scale factor
	 */
	@Builder.Default
	private float scaleFactor = 1.0f;
	/**
	 * Default source ratio
	 */
	@Builder.Default
	private float sourceRatio = 1.0f;
	/**
	 * 
	 */
	@Builder.Default
	private boolean crop = false;

	/**
	 * Default constructor
	 */
	public RenderPolicy()
	{
		// Nothing to do here
	}

	/**
	 * 
	 */
	public void calculateRatioAndScaleBySourceAndTarget()
	{
		/**
		 * Width and height of source image
		 */
		double sw = this.sourceDimension.getWidth();
		double sh = this.sourceDimension.getHeight();

		/**
		 * Width and height of target image
		 */
		double tw = this.targetDimension.getWidth();
		double th = this.targetDimension.getHeight();
		// LOGGER.info("sw = {}, sh = {}; tw = {}, th = {}", sw, sh, tw, th);

		if (sw > 0d && sh > 0d && tw > 0d && th > 0d)
		{
			/**
			 * Calculate image ratio and scale factor
			 */
			this.sourceRatio = (float) (sw / sh);

			/**
			 * Determine image orientation
			 */
			this.orientation = Orientation.SQUARE;
			if (this.sourceRatio > 1.0f)
			{
				this.orientation = Orientation.LANDSCAPE;
			}
			else if (this.sourceRatio < 1.0f)
			{
				this.orientation = Orientation.PORTRAIT;
			}

			/**
			 * Calculate scale factor
			 */
			switch (this.orientation)
			{
				case LANDSCAPE:
					this.scaleFactor = (float) (tw / Math.min(sw, sh));
					break;

				case PORTRAIT:
					this.scaleFactor = (float) (th / Math.min(sw, sh));
					break;

				default:
					this.scaleFactor = (float) (tw / sw);
					break;
			}
			// LOGGER.info("tw/sw = {}, th/sh = {}", (tw / sw), (th / sh));
		}

		LOGGER.trace("Source '{}' and target '{}' dimension.", this.sourceDimension, this.targetDimension);
		LOGGER.trace("Image orientation is '{}' with ratio '{}'. Scale factor is '{}'.", this.orientation, this.sourceRatio, this.scaleFactor);
	}

}
