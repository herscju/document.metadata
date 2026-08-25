package com.hillert.image.metadata.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "metadata")
public class MetadataConfigProperties
{

	/**
	 * Do not resize and label returned images.
	 */
	private Boolean doNotResize;

	/**
	 * @return
	 */
	public Boolean getDoNotResize()
	{
		return this.doNotResize;
	}

	/**
	 * @param doNotResize
	 */
	public void setDoNotResize(Boolean doNotResize)
	{
		this.doNotResize = doNotResize;
	}

}
