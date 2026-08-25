/**
 * 
 */
package info.hersche.document.metadata.storage;

/**
 * @author herscju
 */
public class StorageFileNotFoundException extends StorageException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5096406558522184119L;

	/**
	 * Constructor
	 * 
	 * @param message
	 */
	public StorageFileNotFoundException(String message)
	{
		super(message);
	}

	/**
	 * Constructor
	 * 
	 * @param message
	 * @param cause
	 */
	public StorageFileNotFoundException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
