package module3;

abstract class AbstractTVRemote
{
	protected int dummyField;
	
	public abstract void onPressRed();
	public abstract void onPressGreen();
	
	public static String getClassName()
	{ return "AbstractTVRemote"; }
}

public class MyTVRemote extends AbstractTVRemote
{
	protected int dummyField;
	
	public static String getClassName()
	{ return "MyTVRemote"; }

	@Override
	public void onPressRed()
	{ System.out.println("On red..."); }

	@Override
	public void onPressGreen()
	{ System.out.println("On greeen..."); }
	
	public static void main(String[] args) 
	{
		AbstractTVRemote myTVRemote = new MyTVRemote();
		myTVRemote.onPressRed();
		myTVRemote.onPressGreen();
		System.out.println(AbstractTVRemote.getClassName());
		System.out.println(MyTVRemote.getClassName());
	}
}
