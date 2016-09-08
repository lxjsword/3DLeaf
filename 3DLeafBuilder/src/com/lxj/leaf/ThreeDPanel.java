package com.lxj.leaf;

import java.awt.Color;
import java.awt.GraphicsConfiguration;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Locale;
import javax.media.j3d.PhysicalBody;
import javax.media.j3d.PhysicalEnvironment;
import javax.media.j3d.PointLight;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.media.j3d.ViewPlatform;
import javax.media.j3d.VirtualUniverse;
import javax.swing.JFrame;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.picking.behaviors.PickRotateBehavior;
import com.sun.j3d.utils.picking.behaviors.PickTranslateBehavior;
import com.sun.j3d.utils.picking.behaviors.PickZoomBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;

class ThreeDPanel
{
	private GraphicsConfiguration gc;
	private Canvas3D cv;
	private DataCollector dc;
	SimpleUniverse su;

	public static final BoundingSphere[] bounds = {
			new BoundingSphere(new Point3d(0, 0, 0), 1),
			new BoundingSphere(new Point3d(0, 0, 0), 0.6),
			new BoundingSphere(new Point3d(0, 0, 0), 0.2) };

	public ThreeDPanel(DataCollector dc)
	{
		this.dc = dc;
		// 创建Canvas3D画布对象
		gc = SimpleUniverse.getPreferredConfiguration();
		cv = new Canvas3D(gc);

		// 创建超结构
		VirtualUniverse vu = new VirtualUniverse();
		Locale loc = new Locale(vu);

		// 创建视图分支
		BranchGroup bgView = createViewBranch(cv);
		bgView.compile();
		loc.addBranchGraph(bgView);

		// 创建内容分支
		BranchGroup bg = createContentBrance(cv);
		bg.compile();
		loc.addBranchGraph(bg);
	}
	

	public Canvas3D getCanvas()
	{
		return cv;
	}

	/**
	 * 创建内容分支
	 */
	private BranchGroup createContentBrance(Canvas3D cv)
	{
		// 创建场景图分支
		BranchGroup root = new BranchGroup();

		TransformGroup tg = null;
		PickRotateBehavior pickRotate = null;
		PickZoomBehavior pickZoom = null;
		PickTranslateBehavior pickTranslate = null;

		BoundingSphere behaveBounds = new BoundingSphere();

		/*
		 * TODO:添加物体
		 */

		Transform3D transform = new Transform3D();
		tg = new TransformGroup(transform);
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		tg.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
//		Line2D topLine = new Line2D.Float(new Point2D.Float(1.0f, 1.0f),
//				new Point2D.Float(1.05f, 0.95f));
//		Line2D bottomLine = new Line2D.Float(new Point2D.Float(-0.05f, 0.05f),
//				new Point2D.Float(0.05f, -0.05f));
		BaseOfLeaf bol = new BaseOfLeaf(dc);
		tg.addChild(bol.getLeaf());
		tg.addChild(bol.getLeafOutline());
		root.addChild(tg);

		// 添加旋转
		pickRotate = new PickRotateBehavior(root, cv, behaveBounds);
		root.addChild(pickRotate);
		// 添加缩放
		pickZoom = new PickZoomBehavior(root, cv, behaveBounds);
		root.addChild(pickZoom);
		// 添加旋转
		pickTranslate = new PickTranslateBehavior(root, cv, behaveBounds);
		root.addChild(pickTranslate);

		// 添加点光源
		root.addChild(createPointLight());
		// 添加环境光源
		root.addChild(createAmbientLight());
		// 添加背景
		root.addChild(createBackground());

		return root;
	}

	/**
	 * 创建视图分支
	 */
	private BranchGroup createViewBranch(Canvas3D cv)
	{
		// 创建View组件对象
		View view = new View();
		// 设置为透视投影
		view.setProjectionPolicy(View.PERSPECTIVE_PROJECTION);
		// Viewplatform叶结点
		ViewPlatform vp = new ViewPlatform();
		// 设置View对象属性
		view.addCanvas3D(cv);
		view.attachViewPlatform(vp);
		view.setPhysicalBody(new PhysicalBody());
		view.setPhysicalEnvironment(new PhysicalEnvironment());
		// 几何变换结点
		Transform3D trans = new Transform3D();
		// 设置照相机位置为(0, 0, 2.41)
		Point3d eye = new Point3d(0, 0, 20);// 1 / Math.tan(Math.PI / 8));
		// 设置观察方向指向的点
		Point3d center = new Point3d(0, 0, 0);
		// 设置垂直于观察方向向上的方向
		Vector3d vup = new Vector3d(0, 1, 0);
		// 生成照相机几何变换矩阵
		trans.lookAt(eye, center, vup);
		// 求矩阵的逆
		trans.invert();
		// 集合变换组结点
		TransformGroup tg = new TransformGroup(trans);
		tg.addChild(vp);
		// 创建视图分支
		BranchGroup bgView = new BranchGroup();
		bgView.addChild(tg);
		return bgView;
	}

	/**
	 * 创建背景
	 * 
	 * @throws MalformedURLException
	 */
	private Background createBackground()
	{
		// 添加背景
		Background background = new Background(1.0f, 1.0f, 1.0f);
		background.setApplicationBounds(bounds[0]);
		return background;
	}

	/**
	 * 创建点光源
	 */

	private PointLight createPointLight()
	{
		// 添加点光源
		PointLight pointLight = new PointLight(new Color3f(Color.green),
				new Point3f(-5.0f, -5.0f, -5.0f), new Point3f(1f, 0.01f, 0f));
		// 球体作用范围边界对象
		BoundingSphere bounds = new BoundingSphere(new Point3d(0, 0, 0), 10);
		// 设置作用范围边界
		pointLight.setInfluencingBounds(bounds);
		return pointLight;
	}

	/**
	 * 创建环境光
	 */

	private AmbientLight createAmbientLight()
	{
		// 添加环境光源
		AmbientLight light = new AmbientLight(true, new Color3f(Color.green));
		// 球体作用范围边界对象
		BoundingSphere bounds = new BoundingSphere(new Point3d(0, 0, 0), 10);
		// 设置作用范围边界
		light.setInfluencingBounds(bounds);
		return light;
	}

	public static void main(String[] args)
	{
		JFrame frame = new JFrame();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 450, 300);
		DataCollector dc = new DataCollector();
		try
		{
			dc.loadLines();
		} catch (FileNotFoundException e1)
		{
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}
		ThreeDPanel panel = new ThreeDPanel(dc);
		frame.add(panel.getCanvas());
	}

}
