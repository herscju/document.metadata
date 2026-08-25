/**
 * 
 */
package info.hersche.document.metadata.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ConnectListener;

/**
 *  @author herscju
 */
public class SocketIOConnectionListener implements ConnectListener
{
	/**
	 * Initialise logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(SocketIOConnectionListener.class);

	/**
	 * Default constructor
	 */
	public SocketIOConnectionListener()
	{
		// Nothing to do here
	}

	/**
	 *
	 */
	@Override
	public void onConnect(SocketIOClient client)
	{
		LOGGER.trace("New user connected with socket '{}' ('{}').", client.getSessionId(), client.getRemoteAddress());
	}
}
