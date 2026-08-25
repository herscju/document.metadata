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

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;

/**
 * @author herscju
 */
public class SocketIOTextEventListener<T> implements DataListener<T>
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(SocketIOTextEventListener.class);
	
	/**
	 * 
	 */
	private SocketIOServer server;
	
	/**
	 * 
	 */
	private  String channel;

	/**
	 * Constructor
	 * 
	 * @param server
	 */
	public SocketIOTextEventListener(SocketIOServer server, String channel)
	{
		this.server = server;
		this.channel = channel;
	}

	/**
	 *
	 */
	@Override
	public void onData(SocketIOClient ioClient, T data, AckRequest ackSender) throws Exception
	{
		// Broadcast binary data to all clients
		Collection<SocketIOClient> clients = this.server.getAllClients();
		for (SocketIOClient client : clients)
		{
			client.sendEvent(this.channel, data);
			LOGGER.debug("Data has been sent to: " + client.getSessionId().toString());
		}
	}

}
