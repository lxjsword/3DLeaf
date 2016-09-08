package data;

import java.awt.Point;

public class ThreePointLine
{
	private  Point leftP;
	private Point midP;
	private Point rightP;
	
	public ThreePointLine() {}
	
	public ThreePointLine(Point leftP, Point rightP)
	{
		this.leftP = leftP;
		this.rightP = rightP;
		calMidP();	
	}
	
	ThreePointLine(int x1, int y1, int x2, int y2, int x3, int y3)
	{
		this.leftP = new Point(x1, y1);
		this.rightP = new Point(x3, y3);
		this.midP = new Point(x2, y2);
	}

	private void calMidP()
	{
		this.midP = new Point((leftP.x+rightP.x)/2, (leftP.y+rightP.y)/2);
	}
	
	public Point getLeftP()
	{
		return leftP;
	}
	public void setLeftP(Point leftP)
	{
		this.leftP = leftP;
		if (this.rightP != null)
			calMidP();
	}
	public Point getMidP()
	{
		return midP;
	}
	public void setMidP(Point midP)
	{
		this.midP = midP;
	}
	public Point getRightP()
	{
		return rightP;
	}
	public void setRightP(Point rightP)
	{
		this.rightP = rightP;
		if (this.leftP != null)
			calMidP();
	}
	
	
}
