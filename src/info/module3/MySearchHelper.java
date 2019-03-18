package module3;

public class MySearchHelper extends SearchHelper {

	/**
	 * Returns the index of the searched value else -1 if not found.
	 * @param list
	 * @param number
	 * @return
	 */
	public int search(Float [] list, Float number)
	{
		for (int i = 0; i < list.length; i++)
		{
			if (list[i].floatValue() == number.floatValue())
				return i;
		}
		return -1;
	}
	
	public Integer [] search(Double [] list, Double number)
	{
		return null;
	}
}
