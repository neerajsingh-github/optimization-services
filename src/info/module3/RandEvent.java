package module3.randevent;

/**
 * Class modelling the an event
 * @author pbose
 *
 */
public class RandEvent
{
	/**
	 * Event payload, can be anything
	 */
	protected String payload;

	/**
	 * The event source
	 */
	protected Object evSource;
	
	public RandEvent(String payload, Object evSource)
	{
		this.payload = payload;
		this.evSource = evSource;
	}
	
	public String getPayload()
	{ return payload; }
	
	@Override
	public String toString()
	{ return evSource.getClass().getSimpleName(); }
}