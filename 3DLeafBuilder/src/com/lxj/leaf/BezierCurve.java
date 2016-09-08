package com.lxj.leaf;

import javax.media.j3d.LineArray;
import javax.media.j3d.LineStripArray;
import javax.vecmath.Point3d;

class BezierCurve extends LineStripArray
{
	static int level = 4; // 固定的最大递归调用层数
	static int[] vCnts = { (1 << level) + 1};
	int index = 0; // 当前将设置的直线段端点的序列
	
	// BezierCurve构造函数， 参数为4个控制点
	public BezierCurve(Point3d p0, Point3d p1,Point3d p2,Point3d p3)
	{
		super(vCnts[0], LineArray.COORDINATES|LineArray.COLOR_3, vCnts);
		setCoordinate(index, p0); // 设置第一个直线段的第一个端点
		//System.out.println(p0.x + "," + p0.y + "," + p0.z);
		index++;
		subdivide(0, p0, p1, p2, p3); // 递归调用subdivide方法
	}

	private void subdivide(int lev, Point3d p0, Point3d p1, Point3d p2, Point3d p3)
	{
		// 如果已经达到最大递归调用层数
		if (lev >= level)
		{
			setCoordinate(index, p3);  // 设置LineStripArray对象中的直线段端点
			//System.out.println(p3.x + "," + p3.y + "," + p3.z);
			index++;
		}
		else
		{
			Point3d p10 = new Point3d();
			p10.add(p0, p1);
			p10.scale(0.5);
			Point3d p11 = new Point3d();
			p11.add(p1, p2);
			p11.scale(0.5);
			Point3d p12 = new Point3d();
			p12.add(p2, p3);
			p12.scale(0.5);
			Point3d p20 = new Point3d();
			p20.add(p10, p11);
			p20.scale(0.5);
			Point3d p21 = new Point3d();
			p21.add(p11, p12);
			p21.scale(0.5);
			Point3d p30 = new Point3d();
			p30.add(p20, p21);
			p30.scale(0.5);
			subdivide(lev + 1, p0, p10, p20, p30);
			subdivide(lev + 1, p30, p21, p12, p3);
		}
	}
}
