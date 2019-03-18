package module3.randevent;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Event source.
 * @author pbose
 *
 */
public final class RandEventSource 
{
	/**
	 * Number of iterations for the main loop, play around with this value.
	 */
	private static final int ITERATIONS = 10;
	/**
	 * Holds the reference of the event consumer.
	 */
	private RandEventListener evlistener = null;
	
	/**
	 * This method is called by event consumers to get registered to the 
	 * event source. One can have a list of listeners, in that case change the 
	 * argument to a list {@link Vector} or {@link ArrayList}.
	 * @param listener
	 */
	public void registerListener(RandEventListener listener)
	{
		evlistener = listener;
	}

	/**
	 * This method is called by the event source to notify the 
	 * event consumers about the event.
	 * @param evObj
	 */
	public void notifyListener(RandEvent evObj)
	{
		evlistener.eventHandler(evObj);;
	}

	public static void main(String[] args) 
	{
		// Create event object
		RandEventSource evSource = new RandEventSource();
		// Register event consumer
		evSource.registerListener(new RandEventConsumer());
		
		double randomNum;
		int truncRandomNum;
		for (int i = 0; i < ITERATIONS; i++)
		{
			// Get a 0 < random number < 1
			randomNum = Math.random();
			/* Extract the 10000th decimal part by multiplying by 10000
			 * and casting to int to get rid off the remaining decimal part.
			*/
			truncRandomNum = (int)(randomNum*10000);	
			if (truncRandomNum%2 == 0) // Test for even number
			{
				// Create event with payload as string and source = event source
				RandEvent ev = new RandEvent("10000th decimal place is even for random number = " + randomNum, 
						evSource);
				// Notify the listeners.
				evSource.notifyListener(ev);
			}
		}
	}
}