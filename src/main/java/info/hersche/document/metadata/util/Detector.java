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

import java.util.regex.Matcher;

import info.hersche.document.metadata.factory.AbstractDocumentFactory;
import info.hersche.document.metadata.image.MultiMediaImpl.Type;
import info.hersche.document.metadata.image.Picture;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Jürg Hersche (herscju)
 */
@Getter
@Setter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true, builderMethodName = "toBuilder")
public class Detector
{
	/**
	 * Name (of file) to test
	 */
	private String name;

	/**
	 * Default constructor
	 */
	public Detector()
	{
		// Nothing to do here
	}

	/**
	 * @return
	 */
	public Type detectMultiMediaType()
	{
		Matcher v1WhatsAppImageMatcher = Picture.WA_IMG_V1.matcher(this.name);
		Matcher v2WhatsAppImageMatcher = Picture.WA_IMG_V2.matcher(this.name);
		Boolean isWhatsAppImage = Boolean.valueOf(v1WhatsAppImageMatcher.find() || v2WhatsAppImageMatcher.find());

		Matcher v1StandardScanMatcher = Picture.ST_SCN_V1.matcher(this.name);
		Boolean isStandardScan = Boolean.valueOf(v1StandardScanMatcher.find());

		Matcher v1StandardVideoMatcher = Picture.ST_VID_V1.matcher(this.name);
		Boolean isStandardVideo = Boolean.valueOf(v1StandardVideoMatcher.find());

		Matcher v1WhatsAppVideoMatcher = Picture.WA_VID_V1.matcher(this.name);
		Boolean isWhatsAppVideo = Boolean.valueOf(v1WhatsAppVideoMatcher.find());

		Matcher v1WhatsAppStatusMatcher = Picture.WA_STA_V1.matcher(this.name);
		Boolean isWhatsAppStatus = Boolean.valueOf(v1WhatsAppStatusMatcher.find());

		if (isWhatsAppImage.booleanValue())
		{
			return Type.WHATSAPP_IMAGE;
		}

		if (isStandardScan.booleanValue())
		{
			return Type.STANDARD_SCAN;
		}

		if (isStandardVideo.booleanValue())
		{
			return Type.STANDARD_VIDEO;
		}

		if (isWhatsAppVideo.booleanValue())
		{
			return Type.WHATSAPP_VIDEO;
		}

		if (isWhatsAppStatus.booleanValue())
		{
			return Type.WHATSAPP_STATUS;
		}
		
		if (this.name.endsWith(AbstractDocumentFactory.Extension.PDF.getExtension())) {
			return Type.DOCUMENT_SCAN;
		}

		return Type.STANDARD_IMAGE;
	}
}
