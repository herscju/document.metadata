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
 * https://github.com/eugenp/tutorials/tree/a587d2ec2ea54cb464c6f6e49be738282e582515/spring-websockets
 */
package info.hersche.document.metadata.model;

import info.hersche.document.metadata.configuration.SecurityConfiguration;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author herscju
 */
@Getter
@Setter
@ToString
@Builder(toBuilder = true, builderMethodName = "toBuilder")
public class OutputMessage
{
	private String from;
	private String text;
	@ToString.Exclude
	private OutputDataMessage data;
	private String user;
	private String time;

	/**
	 * Default constructor
	 */
	public OutputMessage()
	{
		this("", "", OutputDataMessage.toBuilder().build(), SecurityConfiguration.ANONYMUS, "");
	}

	/**
	 * Constructor
	 * 
	 * @param from
	 * @param text
	 * @param time
	 */
	public OutputMessage(final String from, final String text, final String user, final String time)
	{

		this(from, text, OutputDataMessage.toBuilder().build(), user, time);
	}

	/**
	 * Constructor
	 * 
	 * @param from
	 * @param text
	 * @param data
	 * @param time
	 * @param wapp
	 */
	public OutputMessage(final String from, final String text, final OutputDataMessage data, final String user, final String time)
	{
		this.from = from;
		this.text = text;
		this.data = data;
		this.user = user;
		this.time = time;
	}

}
