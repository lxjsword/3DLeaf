package data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BLeafDB implements IData
{
	private static BLeafDB blDB = new BLeafDB();
	private List<ArrayList<ThreePointLine>> lists = new ArrayList<ArrayList<ThreePointLine>>();
	private ArrayList<ThreePointLine> curlist;

	private BLeafDB()
	{
	}

	public static BLeafDB getBLeafDB()
	{
		return blDB;
	}

	public void newBLeaf()
	{
		curlist = new ArrayList<ThreePointLine>();
		lists.add(curlist);
	}

	public void addLine(ThreePointLine line)
	{
		if (curlist != null)
			curlist.add(line);
	}

	public int getBLeafCount()
	{
		return lists.size();
	}

	public int getLineCount(int index)
	{
		return lists.get(index).size();
	}
	
	public List<ArrayList<ThreePointLine>> getList() { return lists; }

	@Override
	public void saveData()
	{
		PrintWriter out = null;
		try
		{
			out = new PrintWriter(new FileOutputStream("BLeafData.dat"));
			out.println(getBLeafCount());
			for (ArrayList<ThreePointLine> curlist : lists)
			{
				out.println(curlist.size());
				for (ThreePointLine line : curlist)
					out.println(line.getLeftP().x + ","
							+ line.getLeftP().y + ","
							+ line.getMidP().x + ","
							+ line.getMidP().y + ","
							+ line.getRightP().x + ","
							+ line.getRightP().y);
			}
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (out != null)
				out.close();
		}

	}

	@Override
	public void loadData()
	{
		Scanner scanner = null;
		try
		{
			scanner = new Scanner(new FileInputStream("BLeafData.dat"));
			lists.clear();
			int bleafCount = Integer.parseInt(scanner.nextLine());
			for (int i = 0; i < bleafCount; ++i)
			{
				int size = Integer.parseInt(scanner.nextLine());
				ArrayList<ThreePointLine> list = new ArrayList<ThreePointLine>(size);

				for (int j = 0; j < size; j++)
				{
					String s = scanner.nextLine();
					String[] tmp = s.split(",");
					list.add(new ThreePointLine(Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1]), 
							Integer.parseInt(tmp[2]),Integer.parseInt(tmp[3]),
							Integer.parseInt(tmp[4]),Integer.parseInt(tmp[5])));
				}
				lists.add(list);
			}
			
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (scanner != null)
				scanner.close();
		}
	}

}
