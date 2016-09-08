package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import data.BLeafDB;
import data.LineAndP;
import data.ThreePointLine;
import drawer.BLeafDrawer;
import drawer.IDrawer;
import drawer.OutlineDrawer;

enum DrawModel
{
	NONE, BLEAFMODEL, OUTLINEMODEL
}

enum OperationModel
{
	NONE, DRAGMODEL, ROTATEMODEL, COPYMODEL
}

@SuppressWarnings("serial")
public class PicPanel extends JPanel implements MouseListener,
		MouseMotionListener, KeyListener
{
	private boolean isLoadPic = false;
	private DrawModel model = DrawModel.NONE;
	private OperationModel oModel = OperationModel.NONE;

	private BLeafDB bleafDB = BLeafDB.getBLeafDB();

	private Point startP;
	private Point endP;

	private int width;
	private int height;

	private IDrawer drawer;
	
	private LineAndP lineAndp;
	
	private final int radius = 2;
	
	private Graphics2D g2;

	public PicPanel()
	{
		setPreferredSize(new Dimension(800, 1500));
		setBackground(Color.white);
		this.setFocusable(true);
		addMouseListener(this);
		addMouseMotionListener(this);
		this.addKeyListener(this);

	}

	// 重写组件绘制函数
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if (isLoadPic)
		{
			// 加载图片
			g2 = (Graphics2D) g;
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			String relativelyPath = System.getProperty("user.dir");
			Image image = toolkit.getImage(relativelyPath + "\\img\\leaf.jpg");

			width = (int) (image.getWidth(this) * 3);
			height = (int) (image.getHeight(this) * 3);

			g2.drawImage(image, 0, 0, width, height, this);
			g2.translate(width / 2, height / 2);
			g2.scale(1, -1);

			switch (model)
			{
			case NONE:
				break;
			case BLEAFMODEL:
				drawer = new BLeafDrawer(g2, radius);
				break;
			case OUTLINEMODEL:
				drawer = new OutlineDrawer(g2);
				break;
			}
			if (drawer != null)
				drawer.draw();
			
			if (startP != null && endP != null)
			{
				if (oModel == OperationModel.COPYMODEL)
				{
					g2.setColor(Color.red);
					g2.setStroke(new BasicStroke(2.0f,BasicStroke.CAP_ROUND, 
		                    BasicStroke.JOIN_ROUND, 0, new float[]{0,6,0,6}, 0 ));
					g2.drawLine(startP.x, startP.y, endP.x, endP.y);
				}
				else
				{
					g2.setColor(Color.red);
					g2.setStroke(new BasicStroke(1.0f));
					g2.drawOval(startP.x - radius, startP.y - radius, radius*2, radius*2);
					g2.drawLine(startP.x, startP.y, endP.x, endP.y);
					g2.drawOval(endP.x - radius, endP.y - radius, radius*2, radius*2);
				}
			}	
		}
	}

	// 加载图片
	public void loadPic()
	{
		isLoadPic = true;
	}

	public void switchModel(DrawModel model)
	{
		this.model = model;
	}

	private void dpTolp(Point p)
	{
		p.x -= width / 2;
		p.y -= height / 2;
		p.y *= -1;
	}

	public void newBLeaf()
	{
		bleafDB.newBLeaf();
	}

	// 鼠标事件响应
	@Override
	public void mouseDragged(MouseEvent e)
	{
		if (model == DrawModel.BLEAFMODEL)
		{
			endP = e.getPoint();
			dpTolp(endP);
			
			if (oModel == OperationModel.DRAGMODEL)
			{
				if (lineAndp != null)
				{
					switch(lineAndp.lmr)
					{
					case 1:
						lineAndp.line.setLeftP(endP);
						endP = null;
						break;
					case 2:
						
						break;
					case 3:
						lineAndp.line.setRightP(endP);
						endP = null;
						break;
					}
					
				}
			}
			if (oModel == OperationModel.ROTATEMODEL)
			{
				int distance = endP.x - lineAndp.line.getMidP().x;
				((BLeafDrawer)drawer).rotateLine(lineAndp.line, distance);
				endP = null;
			}
			
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
		switch (model)
		{
		case NONE:
			break;
		case BLEAFMODEL:
			if (e.getButton() == MouseEvent.BUTTON1)
			{
				startP = e.getPoint();
				dpTolp(startP);
				
				
				if (oModel != OperationModel.NONE)
				{
					lineAndp = ((BLeafDrawer)drawer).inWhichLineP(startP);
				}
			}
			break;
		case OUTLINEMODEL:
			break;
		}

	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		if (model == DrawModel.BLEAFMODEL && startP != null)
		{
			
			endP = e.getPoint();
			dpTolp(endP);
			
			switch(oModel)
			{
			case DRAGMODEL:
				if (lineAndp != null)
				{
					switch(lineAndp.lmr)
					{
					case 1:
						lineAndp.line.setLeftP(endP);
						break;
					case 2:
						break;
					case 3:
						lineAndp.line.setRightP(endP);
						break;
					}
					
				}
				break;
			case COPYMODEL:
				if (lineAndp != null && lineAndp.lmr == 2)
					bleafDB.addLine(((BLeafDrawer) drawer).copyNewLine(endP, lineAndp.line));
				break;
			case ROTATEMODEL:
				break;
			case NONE:
				ThreePointLine line = new ThreePointLine(new Point(startP.x,
						startP.y), new Point(endP.x, endP.y));
				bleafDB.addLine(line);
				break;
			}
			startP = null;
			endP = null;
			repaint();
		}

	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		if (model != DrawModel.NONE)
			setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		if (model != DrawModel.NONE)
			setCursor(Cursor.getDefaultCursor());
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		// TODO 自动生成的方法存根

	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		int i = e.getKeyCode();
		switch (i)
		{
		case KeyEvent.VK_D:
			oModel = OperationModel.DRAGMODEL;
			break;
		case KeyEvent.VK_R:
			oModel = OperationModel.ROTATEMODEL;
			break;
		case KeyEvent.VK_C:
			oModel = OperationModel.COPYMODEL;
			break;
		}

	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		oModel = OperationModel.NONE;
	}

}
