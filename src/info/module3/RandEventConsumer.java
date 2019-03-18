package module3.randevent;

/**
 * The event consumer realising the {@link RandEventListener} interface
 * @author pbose
 *
 */
public final class RandEventConsumer implements RandEventListener
{
	@Override
	public void eventHandler(RandEvent evObj) 
	{
		System.out.println("Source: " + evObj.toString() + " "
				+ "Payload: " + evObj.getPayload());
	}
}
