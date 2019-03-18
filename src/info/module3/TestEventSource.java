package module3;

// Any idea why the following 2 imports are there?
import java.util.ArrayList;
import java.util.Vector;

/**
 * Event source.
 * @author pbose
 *
 */
public final class TestEventSource 
{
	/**
	 * Number of iterations for the main loop, play around with this value.
	 */
	private static final int ITERATIONS = 10;
	/**
	 * Holds the reference of the event consumer.
	 */
	private TestEventListener evlistener = null;
	
	/**
	 * This method is called by event consumers to get registered to the 
	 * event source. One can have a list of listeners, in that case change the 
	 * argument to a list {@link Vector} or {@link ArrayList}.
	 * @param listener
	 */
	public void registerListener(TestEventListener listener)
	{ evlistener = listener; }

	/**
	 * This method is called by the event source to notify the 
	 * event consumers about the event.
	 * @param evObj
	 */
	public void notifyListener(TestEvent evObj)
	{ evlistener.eventHandler(evObj); }

	public static void main(String[] args) 
	{
		TestEventSource evSource = new TestEventSource();
		evSource.registerListener(new TestEventConsumer());
		
		for (int i = 0; i < ITERATIONS; i++)
		{
			TestEvent ev = new TestEvent("Test Msg: " + (i+1), 
						evSource);
			evSource.notifyListener(ev);
		}
	}
}

/**
 * Class modelling the an event
 * @author pbose
 *
 */
class TestEvent
{
	/**
	* Event payload, can be anything
	*/
	protected String payload;
	/**
	 * The event source
	 */
	protected Object evSource;
	
	public TestEvent(String payload, Object evSource)
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

/**
 * 
 * @author pbose
 * Interface that the event consumers should implement
 */interface TestEventListener
{
	/**
	 * This method is called by event source notifier.
	 * @param evObj
	 */
	public void eventHandler(TestEvent evObj);
}

 /**
  * The event consumer realising the {@link TestEventListener} interface
  * @author pbose
  *
  */class TestEventConsumer implements TestEventListener
{
	@Override
	public void eventHandler(TestEvent evObj)
	{
		System.out.println("Source: " + evObj.toString() + " "
				+ "Payload: " + evObj.getPayload());
	}
	
}