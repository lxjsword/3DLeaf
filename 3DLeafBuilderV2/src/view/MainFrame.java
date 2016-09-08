package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import data.BLeafDB;
import data.OutlineDB;

@SuppressWarnings("serial")
public class MainFrame extends JFrame implements ActionListener
{
	
	private JPanel contentPane;
	private PicPanel picPane;
	private ThreeDPanel threeDPanel;
	private JScrollPane jsPane;

	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				}catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
	
	
	public MainFrame()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JMenuBar menuBar = new JMenuBar();
		contentPane.add(menuBar, BorderLayout.NORTH);
		
		// 数据初始化、加载和保存
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		
		JMenuItem initItem = new JMenuItem("Init");
		fileMenu.add(initItem);
		initItem.addActionListener(this);

		JMenuItem loadItem = new JMenuItem("Load");
		fileMenu.add(loadItem);
		loadItem.addActionListener(this);
		
		JMenuItem saveItem = new JMenuItem("Save");
		fileMenu.add(saveItem);
		saveItem.addActionListener(this);
		
		// 切换绘画操作
		JMenu drawMenu = new JMenu("DrawModel");
		menuBar.add(drawMenu);
		
		JMenuItem BLeafItem = new JMenuItem("BLeaf Model");
		drawMenu.add(BLeafItem);
		BLeafItem.addActionListener(this);

		JMenuItem newItem = new JMenuItem("New BLeaf");
		drawMenu.add(newItem);
		newItem.addActionListener(this);
		
		JMenuItem outlineItem = new JMenuItem("Outline Model");
		drawMenu.add(outlineItem);
		outlineItem.addActionListener(this);
		
		// 切换视图
		JMenu viewMenu = new JMenu("View");
		menuBar.add(viewMenu);
		
		JMenuItem picItem = new JMenuItem("PicView");
		viewMenu.add(picItem);
		picItem.addActionListener(this);

		JMenuItem ThreeDItem = new JMenuItem("ThreeDView");
		viewMenu.add(ThreeDItem);
		ThreeDItem.addActionListener(this);
		
		picPane = new PicPanel();
		jsPane = new JScrollPane(picPane);
		contentPane.add(jsPane, BorderLayout.CENTER);
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		String command = e.getActionCommand();
		switch (command)
		{
		case "Init":
			picPane.loadPic();
			picPane.repaint();
			break;
		case "Load":
			BLeafDB.getBLeafDB().loadData();
			OutlineDB.getOutlineDB().loadData();
			picPane.repaint();
			break;
		case "Save":
			BLeafDB.getBLeafDB().saveData();
			OutlineDB.getOutlineDB().saveData();
			break;
		case "BLeaf Model":
			picPane.switchModel(DrawModel.BLEAFMODEL);
			picPane.repaint();
			break;
		case "New BLeaf":
			picPane.newBLeaf();
			break;
		case "Outline Model":
			picPane.switchModel(DrawModel.OUTLINEMODEL);
			picPane.repaint();
			break;
		case "PicView":
			break;
		case "ThreeDView":
			break;
		default:
			break;
		}

	}

}
