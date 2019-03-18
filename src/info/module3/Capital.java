package module3;

import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

public final class Capital 
{
	public static void main(String[] args) 
	{
		ResourceBundle resBundle = ResourceBundle.getBundle("Capital", new Locale("en", "IN"));
		/**
		 * If you are running the code in India then this line is enough
		 * because the JVM will get the default locale info from the computer.
		 *  ResourceBundle resBundle = ResourceBundle.getBundle("Capital"); 
		 *  */
		Enumeration<String> keys = resBundle.getKeys();
		String country = null, capital = null;
		
		while (keys.hasMoreElements())
		{
			country = (String)keys.nextElement();
			capital = resBundle.getString(country);
			System.out.println("The capital of " + country + " is " + capital + ".");
		}
	}
}
