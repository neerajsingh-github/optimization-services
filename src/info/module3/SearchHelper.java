package module3;

public class SearchHelper implements ISearchHelper
{
	public boolean search(Integer [] list, Integer number)
	{
		for (int i = 0; i < list.length; i++)
		{
			if (list[i].intValue() == number.intValue())
				return true;
		}
		return false;
	}

	public boolean search(String [] list, String str)
	{
		for (int i = 0; i < list.length; i++)
		{
			if (list[i] == str)
				return true;
		}
		return false;
	}

	public static void main(String[] args) 
	{
		String [] str_list = {"X", "Y", "Z", "Q"};
		Integer [] int_list = {1,2,3,4,5};
		
		ISearchHelper sh = new SearchHelper();
		
		System.out.println(sh.search(int_list, 2));
		System.out.println(sh.search(str_list, "Edureka"));
	}
}

