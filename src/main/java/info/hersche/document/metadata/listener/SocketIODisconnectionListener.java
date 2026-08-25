/**
 * 
 */
package info.hersche.document.metadata.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DisconnectListener;

/**
 * @author herscju
 */
public class SocketIODisconnectionListener implements DisconnectListener
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(SocketIODisconnectionListener.class);

	/**
	 * Default constructor
	 */
	public SocketIODisconnectionListener()
	{
		// Nothing to do here
	}

	/**
	 *
	 */
	@Override
	public void onDisconnect(SocketIOClient client)
	{
		client.getNamespace().getAllClients().stream().forEach(data -> {
			LOGGER.trace("User disconnected '{}' ('{}').", data.getSessionId().toString(), client.getSessionId());
		});

	}
}
