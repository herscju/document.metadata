/**
 * 
 */
package info.hersche.document.metadata.model;

/**
 * @author herscju
 */
public class Notifications
{
	private Integer counter;

	/**
	 * Constructor
	 * 
	 * @param counter
	 */
	public Notifications(Integer counter)
	{
		this.counter = counter;
	}

	/**
	 * @return
	 */
	public Notifications increment()
	{
		return new Notifications(Integer.valueOf(this.counter.intValue() + 1));
	}
}
