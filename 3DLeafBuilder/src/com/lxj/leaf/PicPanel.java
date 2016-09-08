package com.lxj.leaf;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;

import javax.swing.JPanel;

@SuppressWarnings("serial")
class PicPanel extends JPanel implements MouseListener,
		MouseMotionListener
{
	private boolean isLoadPic;
	private boolean isGetPoints;
	private boolean isGetOutline;

	private Point startP;
	private Point endP;
	@SuppressWarnings("unused")
	private Line2D ld;
	private Point outlineP;

	private DataCollector dc;
	private int sCount;

	private int width;
	private int height;

	public PicPanel(DataCollector dc)
	{
		isLoadPic = false;
		isGetPoints = false;
		setPreferredSize(new Dimension(800, 1500));
		setBackground(Color.white);

		this.dc = dc;

		addMouseListener(this);
		addMouseMotionListener(this);
	}

	private void dpTolp(Point p)
	{
		p.x -= width / 2;
		p.y -= height / 2;
		p.y *= -1;
	}

	@SuppressWarnings("unused")
	private void lpTodp(Point p)
	{
		p.y *= -1;
		p.x += width / 2;
		p.y += height / 2;
	}

	public void loadPic()
	{
		isLoadPic = true;
	}

	public void startGetOutline()
	{
		isGetOutline = true;
	}

	public void finishGetOutline()
	{
		isGetOutline = false;
	}

	public void startGet()
	{
		isGetPoints = true;
		sCount = 0;
	}

	public void stopGet()
	{
		isGetPoints = false;
		dc.addCount(sCount);
	}

	// 重写组件绘制函数
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if (isLoadPic)
		{
			Graphics2D g2 = (Graphics2D) g;
			Toolkit toolkit = Toolkit.getDefaultToolkit();
//			Image image = toolkit.getImage(this.getClass().getResource(
//					"img/leaf.jpg"));
			String relativelyPath=System.getProperty("user.dir"); 
			Image image = toolkit.getImage(relativelyPath+"\\img\\leaf.jpg");
			
			
			width = (int) (image.getWidth(this) * 3);
			height = (int) (image.getHeight(this) * 3);

			g2.drawImage(image, 0, 0, width, height, this);

			g2.translate(width / 2, height / 2);
			g2.scale(1, -1);

			for (Line2D l : dc.getLine())
			{
				g2.setColor(Color.red);
				g2.setStroke(new BasicStroke(2.0f));
				g2.draw(l);
			}

			if (startP != null && endP != null)
			{
				g2.setColor(Color.red);
				g2.setStroke(new BasicStroke(2.0f));
				g2.drawLine(startP.x, startP.y, endP.x, endP.y);
			}

			for (Point p : dc.getPoint())
			{
				g2.setColor(Color.red);
				g2.setStroke(new BasicStroke(2.0f));
				g2.drawRect(p.x, p.y, 6, 6);
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		// TODO 自动生成的方法存根
		if (isGetPoints)
		{
			endP = e.getPoint();
			dpTolp(endP);
			repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		// TODO 自动生成的方法存根

	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		// TODO 自动生成的方法存根

	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		// TODO 自动生成的方法存根
		if (isGetPoints)
		{
			if (e.getButton() == MouseEvent.BUTTON1)
			{
				startP = e.getPoint();
				dpTolp(startP);
			} else if (e.getButton() == MouseEvent.BUTTON3
					&& !dc.getLine().isEmpty())
			{
				dc.removeLine();
				--sCount;
				repaint();
			}
		}

		if (isGetOutline)
		{
			if (e.getButton() == MouseEvent.BUTTON1)
			{
				outlineP = e.getPoint();
				dpTolp(outlineP);
				dc.addPoint(outlineP);
				repaint();
			} 
			else if (e.getButton() == MouseEvent.BUTTON3 && !dc.getPoint().isEmpty())
			{
				dc.removePoint();
				repaint();
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		// TODO 自动生成的方法存根
		if (isGetPoints && startP != null)
		{
			endP = e.getPoint();
			dpTolp(endP);
			Line2D l = new Line2D.Float(startP, endP);
			dc.addLine(l);
			++sCount;

			startP = null;
			endP = null;
			repaint();
		}
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		// TODO 自动生成的方法存根
		if (isGetPoints || isGetOutline)
			setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		// TODO 自动生成的方法存根
		if (isGetPoints || isGetOutline)
			setCursor(Cursor.getDefaultCursor());
	}
}
