package module3;

import java.util.Locale;
import java.util.ResourceBundle;

public final class TestI18n 
{
	public static void main(String[] args) 
	{
		String language, country;
		/**
		 * The locale is passed as command line arguments. If nothing is passed
		 * then by default language=en and country=US is set.
		 */
		if (args.length == 0)
		{
			language = "en";
			country = "US";
		}
		else
		{
			language = args[0];
			country = args[1];
		}

		// Create a new locale with the language and country code
		Locale locale = new Locale(language, country);
		
		/** Load the relevant resource bundle based on the locale.
		 * If language=en and country=US then resource from TestI18nBundle_en_US will be loaded.
		 * If language=fr and country=FR then resource from TestI18nBundle_fr_FR will be loaded.
		*/
        ResourceBundle messages = ResourceBundle.getBundle("TestI18nBundle", locale);
        // Print the values
        System.out.println(messages.getString("button_label_1"));
        System.out.println(messages.getString("button_label_2"));
	}
}
