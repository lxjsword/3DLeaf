package com.lxj.leaf;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.Vector;

import javax.media.j3d.Appearance;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.Material;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;

class BaseOfLeaf
{
	private DataCollector dc;
	private static int n = 30;// 三角面片数目
	private Vector<Point3d> points;

	public BaseOfLeaf(DataCollector dc)
	{
		this.dc = dc;
		points = new Vector<Point3d>();
		pointToPoint3d();
	}

	public TransformGroup getBF(Line2D topLine, Line2D bottomLine)
	{
		// 要计算的端点数目
		int vCount = (n + 1) * 2;

		// 得到叶茎横截面圆心
		float tmpx = (float) ((topLine.getX1() + topLine.getX2()) / 2);
		float tmpy = (float) ((topLine.getY1() + topLine.getY2()) / 2);
		Point3f topcenterP = new Point3f(tmpx, tmpy, 0);
		tmpx = (float) ((bottomLine.getX1() + bottomLine.getX2()) / 2);
		tmpy = (float) ((bottomLine.getY1() + bottomLine.getY2()) / 2);
		Point3f bottomcenterP = new Point3f(tmpx, tmpy, 0);
		double angle = Math.atan((topcenterP.x - bottomcenterP.x)
				/ (topcenterP.y - bottomcenterP.y));
		if (topcenterP.y < bottomcenterP.y)
			angle += Math.PI;
		float length = (float) Math.sqrt((topcenterP.x - bottomcenterP.x)
				* (topcenterP.x - bottomcenterP.x)
				+ (topcenterP.y - bottomcenterP.y)
				* (topcenterP.y - bottomcenterP.y));

		// 得到叶茎横截面半径
		float topr = (float) Math.sqrt((topcenterP.x - (float) topLine.getP1()
				.getX())
				* (topcenterP.x - (float) topLine.getP1().getX())
				+ (topcenterP.y - (float) topLine.getP1().getY())
				* (topcenterP.y - (float) topLine.getP1().getY()));
		float bottomr = (float) Math.sqrt((bottomcenterP.x - (float) bottomLine
				.getP1().getX())
				* (bottomcenterP.x - (float) bottomLine.getP1().getX())
				+ (bottomcenterP.y - (float) bottomLine.getP1().getY())
				* (bottomcenterP.y - (float) bottomLine.getP1().getY()));

		Point3d[] p = new Point3d[vCount];
		int count = 0;

		// 计算叶茎端点坐标
		for (int i = 0; i <= n; ++i)
		{
			float x1 = topr * (float) Math.cos(i * 2 * Math.PI / n);
			float z1 = topr * (float) Math.sin(i * 2 * Math.PI / n);
			float x2 = bottomr * (float) Math.cos(i * 2 * Math.PI / n);
			float z2 = bottomr * (float) Math.sin(i * 2 * Math.PI / n);
			p[count++] = new Point3d(x1, length, z1);
			p[count++] = new Point3d(x2, 0, z2);
		}

		int[] indices = new int[vCount];
		for (int i = 0; i < vCount; ++i)
			indices[i] = i;
		int[] stripCounts = { vCount };

		GeometryInfo gi = new GeometryInfo(GeometryInfo.TRIANGLE_STRIP_ARRAY);
		gi.setCoordinates(p);
		gi.setCoordinateIndices(indices);
		gi.setStripCounts(stripCounts);
		NormalGenerator ng = new NormalGenerator();
		ng.generateNormals(gi);

		Appearance ap = new Appearance();
		ap.setMaterial(new Material());
		PolygonAttributes polyAttrbutes = new PolygonAttributes();
		polyAttrbutes.setCullFace(PolygonAttributes.CULL_NONE);
		// polyAttrbutes.setCullFace( PolygonAttributes.POLYGON_LINE) ;
		ap.setPolygonAttributes(polyAttrbutes);
		Shape3D shape = new Shape3D(gi.getGeometryArray(), ap);

		Transform3D transform = new Transform3D();
		transform.setTranslation(new Vector3f(bottomcenterP.x, bottomcenterP.y,
				bottomcenterP.z));
		transform.setRotation(new AxisAngle4d(0, 0, 1, -angle));
		TransformGroup tg = new TransformGroup(transform);
		tg.addChild(shape);

		return tg;
	}

	public TransformGroup getLeaf()
	{
		Transform3D transform = new Transform3D();
		transform.setScale(0.005);
		TransformGroup tg = new TransformGroup(transform);
		Vector<Integer> vCount = dc.getVCount();
		Vector<Line2D> vLines = dc.getLine();
		int jCount;
		int count = 0;
		for (int i = 0; i < dc.getCount(); ++i)
		{
			jCount = (int) vCount.get(i);
			for (int j = 0; j < jCount - 1; ++j)
			{
				Line2D l1 = vLines.get(count + j);
				Line2D l2 = vLines.get(count + j + 1);
				tg.addChild(getBF(l1, l2));
			}
			count += jCount;
		}
		return tg;
	}

	public TransformGroup getLeafOutline()
	{
		Point3d p0 = null;
		Point3d p1 = null;
		Point3d p2 = null;
		Point3d p3 = null;

		Transform3D transform = new Transform3D();
		transform.setScale(0.005);
		TransformGroup tg = new TransformGroup(transform);

		double x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4;
		int n = points.size();
		if (n == 0)
			return tg;
		p0 = (Point3d) points.get(0);
		p1 = (Point3d) points.get(1);
		p2 = (Point3d) points.get(2);
		p3 = (Point3d) points.get(3);
		x1 = p1.x;
		y1 = p1.y;
		z1 = p1.z;
		x2 = (p1.x + p2.x) / 2.0f;
		y2 = (p1.y + p2.y) / 2.0f;
		z2 = (p1.z + p2.z) / 2.0f;
		x4 = (2.0f * p2.x + p3.x) / 3.0f;
		y4 = (2.0f * p2.y + p3.y) / 3.0f;
		z4 = (2.0f * p2.z + p3.z) / 3.0f;
		x3 = (x2 + x4) / 2.0f;
		y3 = (y2 + y4) / 2.0f;
		z3 = (z2 + z4) / 2.0f;
		BezierCurve la = new BezierCurve(p0, new Point3d(x1, y1, z1),
				new Point3d(x2, y2, z2), new Point3d(x3, y3, z3));

		float colors[]={
	        	1.0f,0.0f,0.0f,      1.0f,0.0f,0.0f,
	        	1.0f,0.0f,0.0f,      1.0f,0.0f,0.0f,
	        	1.0f,0.0f,0.0f,      1.0f,0.0f,0.0f,
	        	1.0f,0.0f,0.0f,      1.0f,0.0f,0.0f,
	        	1.0f,0.0f,0.0f,      1.0f,0.0f,0.0f,
	        	1.0f,0.0f,0.0f,      1.0f,0.0f,0.0f,
	        	1.0f,0.0f,0.0f,      1.0f,0.0f,0.0f,
	        	1.0f,0.0f,0.0f,      1.0f,0.0f,0.0f,
	        	1.0f,0.0f,0.0f
	        };
		la.setColor(0, colors);
		LineAttributes lineattributes = new LineAttributes();
		lineattributes.setLineWidth(2.0f);
		lineattributes.setLineAntialiasingEnable(true);
		lineattributes.setLinePattern(0);

		Appearance ap = new Appearance();
		ap.setLineAttributes(lineattributes);
		//ap.setMaterial(new Material());
		Shape3D shape = new Shape3D(la, ap);
		tg.addChild(shape);

		for (int i = 2; i < n - 4; ++i)
		{
			p0 = new Point3d(x3, y3, z3);
			p1 = p2;
			p2 = p3;
			p3 = (Point3d) points.get(i + 2);
			x1 = x4;
			y1 = y4;
			z1 = z4;
			x2 = (p1.x + 2.0f * p2.x) / 3.0f;
			y2 = (p1.y + 2.0f * p2.y) / 3.0f;
			z2 = (p1.z + 2.0f * p2.z) / 3.0f;
			x4 = (2.0f * p2.x + p3.x) / 3.0f;
			y4 = (2.0f * p2.y + p3.y) / 3.0f;
			z4 = (2.0f * p2.z + p3.z) / 3.0f;
			x3 = (x2 + x4) / 2.0f;
			y3 = (y2 + y4) / 2.0f;
			z3 = (z2 + z4) / 2.0f;
			la = new BezierCurve(p0, new Point3d(x1, y1, z1), new Point3d(x2,
					y2, z2), new Point3d(x3, y3, z3));
			ap = new Appearance();
			ap.setLineAttributes(lineattributes);
			//ap.setMaterial(new Material());
			shape = new Shape3D(la, ap);
			tg.addChild(shape);
		}

		p0 = new Point3d(x3, y3, z3);
		p1 = p2;
		p2 = p3;
		p3 = (Point3d) points.get(n - 2);
		x1 = x4;
		y1 = y4;
		z1 = z4;
		x2 = (p1.x + 2.0f * p2.x) / 3.0f;
		y2 = (p1.y + 2.0f * p2.y) / 3.0f;
		z2 = (p1.z + 2.0f * p2.z) / 3.0f;
		x4 = (p2.x + p3.x) / 2.0f;
		y4 = (p2.y + p3.y) / 2.0f;
		z4 = (p2.z + p3.z) / 2.0f;
		x3 = (x2 + x4) / 2.0f;
		y3 = (y2 + y4) / 2.0f;
		z3 = (z2 + z4) / 2.0f;
		la = new BezierCurve(p0, new Point3d(x1, y1, z1), new Point3d(x2, y2,
				z2), new Point3d(x3, y3, z3));
		ap = new Appearance();
		ap.setLineAttributes(lineattributes);
		//ap.setMaterial(new Material());
		shape = new Shape3D(la, ap);
		tg.addChild(shape);

		p0 = new Point3d(x3, y3, z3);
		p2 = p3;
		p3 = (Point3d) points.get(n - 1);
		x1 = x4;
		y1 = y4;
		z1 = z4;
		x2 = p2.x;
		y2 = p2.y;
		z2 = p2.z;
		x3 = p3.x;
		y3 = p3.y;
		z3 = p3.z;
		la = new BezierCurve(p0, new Point3d(x1, y1, z1), new Point3d(x2, y2,
				z2), new Point3d(x3, y3, z3));
		ap = new Appearance();
		ap.setLineAttributes(lineattributes);
		//ap.setMaterial(new Material());
		shape = new Shape3D(la, ap);
		tg.addChild(shape);

		return tg;
	}

	private void pointToPoint3d()
	{
		for (Point p : dc.getPoint())
		{
			// System.out.println(p.x + "," + p.y);
			points.add(new Point3d((float) p.x, (float) p.y, 0.0f));
		}
	}
}
