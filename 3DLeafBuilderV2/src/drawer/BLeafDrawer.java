package drawer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import data.BLeafDB;
import data.LineAndP;
import data.ThreePointLine;

public class BLeafDrawer implements IDrawer
{
	Graphics2D g2;
	int radius;
	
	public BLeafDrawer(Graphics2D g, int radius)
	{
		this.g2 = g;
		this.radius = radius;
	}
	
	@Override
	public void draw()
	{
		List<ArrayList<ThreePointLine>> lists = BLeafDB.getBLeafDB().getList();

		for (ArrayList<ThreePointLine> curlist : lists)
		{
			for (ThreePointLine line : curlist)
			{
				drawTPLine(line);
			}
		}
	}
	
	private void drawTPLine(ThreePointLine line)
	{
		g2.setColor(Color.red);
		g2.setStroke(new BasicStroke(1.0f));
		g2.drawOval((int)line.getLeftP().x-radius, (int)line.getLeftP().y-radius, radius*2, radius*2);
		g2.drawLine((int)line.getLeftP().x, (int)line.getLeftP().y, (int)line.getRightP().x, (int)line.getRightP().y);
		g2.drawOval((int)line.getMidP().x-radius, (int)line.getMidP().y-radius, radius*2, radius*2);
		g2.drawOval((int)line.getRightP().x-radius, (int)line.getRightP().y-radius, radius*2, radius*2);
	}
	
	
	
	public ThreePointLine copyNewLine(Point p, ThreePointLine origionLine)
	{
		int xDif, yDif;
		xDif = p.x - origionLine.getMidP().x;
		yDif = p.y - origionLine.getMidP().y;
		Point leftP = new Point(origionLine.getLeftP().x + xDif, origionLine.getLeftP().y + yDif);
		Point rightP = new Point(origionLine.getRightP().x + xDif, origionLine.getRightP().y + yDif);
		return new ThreePointLine(leftP, rightP);
	}
	
	public void rotateLine(ThreePointLine oLine, int distance)
	{
		
	}
	
	public LineAndP inWhichLineP(Point p)
	{
		List<ArrayList<ThreePointLine>> lists = BLeafDB.getBLeafDB().getList();
		int lmr = 0;
		for (ArrayList<ThreePointLine> curlist : lists)
		{
			for (ThreePointLine line : curlist)
			{
				if (ptInArea(p, line.getLeftP()))
				{
					lmr = 1;
				}
				else if (ptInArea(p, line.getMidP()))
				{
					lmr = 2;
				}
				else if (ptInArea(p, line.getRightP()))
				{
					lmr = 3;
				}
				if (lmr != 0)
					return new LineAndP(lmr, line);
				
			}
		}
		
		return null;
	}
	
	private boolean ptInArea(Point p, Point pArea)
	{
		if (p.x > pArea.x-radius && p.x < pArea.x+radius && p.y > pArea.y-radius && p.y < pArea.y+radius)
			return true;
		else
			return false;
	}
}
