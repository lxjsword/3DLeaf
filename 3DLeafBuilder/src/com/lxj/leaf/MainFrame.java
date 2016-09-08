package com.lxj.leaf;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class MainFrame extends JFrame implements ActionListener
{

	private JPanel contentPane;
	private DataCollector dc;
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
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}


	public MainFrame()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JMenuBar menuBar = new JMenuBar();
		contentPane.add(menuBar, BorderLayout.NORTH);
		
		JMenu dataMenu = new JMenu("Data");
		menuBar.add(dataMenu);
		
		JMenuItem initItem = new JMenuItem("Init");
		dataMenu.add(initItem);
		initItem.addActionListener(this);
		
		JSeparator separator = new JSeparator();
		dataMenu.add(separator);
		
		JMenuItem startItem = new JMenuItem("Start");
		dataMenu.add(startItem);
		startItem.addActionListener(this);
		
		JMenuItem stopItem = new JMenuItem("Stop");
		dataMenu.add(stopItem);
		stopItem.addActionListener(this);
		
		JSeparator separator_1 = new JSeparator();
		dataMenu.add(separator_1);
		
		JMenuItem startGetOutlineItem = new JMenuItem("Start Get Outline");
		dataMenu.add(startGetOutlineItem);
		startGetOutlineItem.addActionListener(this);
		
		JMenuItem finishGetOutlineItem = new JMenuItem("Finish Get Outline");
		dataMenu.add(finishGetOutlineItem);
		finishGetOutlineItem.addActionListener(this);
		
		JSeparator separator_2 = new JSeparator();
		dataMenu.add(separator_2);
		
		JMenuItem saveItem = new JMenuItem("Save");
		dataMenu.add(saveItem);
		saveItem.addActionListener(this);
		
		JMenuItem loadItem = new JMenuItem("Load");
		dataMenu.add(loadItem);
		loadItem.addActionListener(this);
		
		JMenu showMenu = new JMenu("Show");
		menuBar.add(showMenu);
		
		JMenuItem picItem = new JMenuItem("PicPanel");
		showMenu.add(picItem);
		picItem.addActionListener(this);
		
		JMenuItem threeDItem = new JMenuItem("ThreeDPanel");
		showMenu.add(threeDItem);
		threeDItem.addActionListener(this);
		
		dc = new DataCollector();
		picPane = new PicPanel(dc);
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
		case "Start":
			picPane.startGet();
			break;
		case "Stop":
			picPane.stopGet();
			break;
		case "Start Get Outline":
			picPane.startGetOutline();
			break;
		case "Finish Get Outline":
			picPane.finishGetOutline();
			break;
		case "Save":
			try
			{
				dc.saveLines();
			} catch (FileNotFoundException e1)
			{
				e1.printStackTrace();
			}
			break;
		case "Load":
			try
			{
				dc.loadLines();
			} catch (FileNotFoundException e1)
			{
				e1.printStackTrace();
			}
			break;
		case "PicPanel":
			contentPane.remove(1);
			contentPane.add(jsPane, BorderLayout.CENTER);
			this.repaint();
			break;
		case "ThreeDPanel":
			threeDPanel = new ThreeDPanel(dc);
			contentPane.remove(1);
			contentPane.add(threeDPanel.getCanvas(), BorderLayout.CENTER);
			this.repaint();
			break;
		default:
			break;
		}

	}
}
