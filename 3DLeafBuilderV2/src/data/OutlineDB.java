package data;

public class OutlineDB implements IData
{
	private static OutlineDB outlineDB = new OutlineDB();
	
	private OutlineDB() {}
	
	public static OutlineDB getOutlineDB()
	{
		return outlineDB;
	}
	
	@Override
	public void saveData()
	{
		

	}

	@Override
	public void loadData()
	{
		

	}

}
