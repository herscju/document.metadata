/**
 * 
 */
package info.hersche.document.metadata.storage;

/**
 * @author herscju
 */
public class StorageException extends RuntimeException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5535590721921003377L;

	/**
	 * Constructor
	 * 
	 * @param message
	 */
	public StorageException(String message)
	{
		super(message);
	}

	/**
	 * Constructor
	 * 
	 * @param message
	 * @param cause
	 */
	public StorageException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
