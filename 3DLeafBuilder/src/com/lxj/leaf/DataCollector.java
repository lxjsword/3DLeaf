package com.lxj.leaf;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Vector;

class DataCollector
{
	private Vector<Line2D> vLines;
	private Vector<Integer> vCount;//每个叶茎有多少节
	private int blCount;// 叶茎数
	private Vector<Point> vPoints;
	
	public DataCollector()
	{
		vLines = new Vector<Line2D>();
		vCount = new Vector<Integer>();
		vPoints = new Vector<Point>();
	}
	
	public void addLine(Line2D line)
	{
		vLines.add(line);
	}
	
	public void addCount(int count)
	{
		++blCount;
		vCount.add(count);
	}
	
	public int getCount()
	{
		return blCount;
	}
	
	public Vector<Integer> getVCount()
	{
		return vCount;
	}
	
	public Vector<Line2D> getLine()
	{
		return vLines;
	}
	
	public void removeLine()
	{
		vLines.remove(vLines.size()-1);
	}
	
	public Vector<Point> getPoint()
	{
		return vPoints;
	}
	
	public void addPoint(Point p)
	{
		vPoints.add(p);
	}
	
	public void removePoint()
	{
		vPoints.remove(vPoints.size() - 1);
	}
	
	public void saveLines() throws FileNotFoundException
	{
		PrintWriter out = new PrintWriter(new FileOutputStream("leafData.txt"));
		// 有几个叶茎
		out.println(blCount);
		int count = 0;
		int jCount;
		for (int i = 0; i < blCount; ++i)
		{
			jCount = (int)vCount.get(i);
			out.println(jCount);
			for (int j = 0; j < jCount; ++j)
			{
				Line2D l = vLines.get(count++);
				out.println(l.getX1() + "," + l.getY1() + ","+ l.getX2() + ","+ l.getY2());
				//System.out.println(l.getX1() + "," + l.getY1() + ","+ l.getX2() + ","+ l.getY2());
			}	
		}
		
		// 叶子轮廓点
		out.println(vPoints.size());
		for (Point p : vPoints)
		{
			out.println(p.x + "," + p.y);
		}
		
		out.close();
	}
	
	public void loadLines() throws FileNotFoundException
	{
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(new FileInputStream("leafData.txt"));
		blCount = Integer.parseInt(scanner.nextLine());
		int jCount;
		for (int i = 0; i < blCount; ++i)
		{
			jCount = Integer.parseInt(scanner.nextLine());
			vCount.add(jCount);
			for (int j = 0; j < jCount; ++j)
			{
				String s = scanner.nextLine();
				String[] tmp = s.split(",");
				vLines.add(new Line2D.Float(Float.parseFloat(tmp[0]), Float.parseFloat(tmp[1]), 
						Float.parseFloat(tmp[2]), Float.parseFloat(tmp[3])));
			}
		}
		int count = Integer.parseInt(scanner.nextLine());
		for (int i = 0; i < count; ++i)
		{
			String s = scanner.nextLine();
			String[] tmp = s.split(",");
			vPoints.add(new Point(Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1])));
		}
		
	}
}
