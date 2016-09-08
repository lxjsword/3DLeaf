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
		// ����Canvas3D��������
		gc = SimpleUniverse.getPreferredConfiguration();
		cv = new Canvas3D(gc);

		// �������ṹ
		VirtualUniverse vu = new VirtualUniverse();
		Locale loc = new Locale(vu);

		// ������ͼ��֧
		BranchGroup bgView = createViewBranch(cv);
		bgView.compile();
		loc.addBranchGraph(bgView);

		// �������ݷ�֧
		BranchGroup bg = createContentBrance(cv);
		bg.compile();
		loc.addBranchGraph(bg);
	}
	

	public Canvas3D getCanvas()
	{
		return cv;
	}

	/**
	 * �������ݷ�֧
	 */
	private BranchGroup createContentBrance(Canvas3D cv)
	{
		// ��������ͼ��֧
		BranchGroup root = new BranchGroup();

		TransformGroup tg = null;
		PickRotateBehavior pickRotate = null;
		PickZoomBehavior pickZoom = null;
		PickTranslateBehavior pickTranslate = null;

		BoundingSphere behaveBounds = new BoundingSphere();

		/*
		 * TODO:�������
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

		// �����ת
		pickRotate = new PickRotateBehavior(root, cv, behaveBounds);
		root.addChild(pickRotate);
		// �������
		pickZoom = new PickZoomBehavior(root, cv, behaveBounds);
		root.addChild(pickZoom);
		// �����ת
		pickTranslate = new PickTranslateBehavior(root, cv, behaveBounds);
		root.addChild(pickTranslate);

		// ��ӵ��Դ
		root.addChild(createPointLight());
		// ��ӻ�����Դ
		root.addChild(createAmbientLight());
		// ��ӱ���
		root.addChild(createBackground());

		return root;
	}

	/**
	 * ������ͼ��֧
	 */
	private BranchGroup createViewBranch(Canvas3D cv)
	{
		// ����View�������
		View view = new View();
		// ����Ϊ͸��ͶӰ
		view.setProjectionPolicy(View.PERSPECTIVE_PROJECTION);
		// ViewplatformҶ���
		ViewPlatform vp = new ViewPlatform();
		// ����View��������
		view.addCanvas3D(cv);
		view.attachViewPlatform(vp);
		view.setPhysicalBody(new PhysicalBody());
		view.setPhysicalEnvironment(new PhysicalEnvironment());
		// ���α任���
		Transform3D trans = new Transform3D();
		// ���������λ��Ϊ(0, 0, 2.41)
		Point3d eye = new Point3d(0, 0, 20);// 1 / Math.tan(Math.PI / 8));
		// ���ù۲췽��ָ��ĵ�
		Point3d center = new Point3d(0, 0, 0);
		// ���ô�ֱ�ڹ۲췽�����ϵķ���
		Vector3d vup = new Vector3d(0, 1, 0);
		// ������������α任����
		trans.lookAt(eye, center, vup);
		// ��������
		trans.invert();
		// ���ϱ任����
		TransformGroup tg = new TransformGroup(trans);
		tg.addChild(vp);
		// ������ͼ��֧
		BranchGroup bgView = new BranchGroup();
		bgView.addChild(tg);
		return bgView;
	}

	/**
	 * ��������
	 * 
	 * @throws MalformedURLException
	 */
	private Background createBackground()
	{
		// ��ӱ���
		Background background = new Background(1.0f, 1.0f, 1.0f);
		background.setApplicationBounds(bounds[0]);
		return background;
	}

	/**
	 * �������Դ
	 */

	private PointLight createPointLight()
	{
		// ��ӵ��Դ
		PointLight pointLight = new PointLight(new Color3f(Color.green),
				new Point3f(-5.0f, -5.0f, -5.0f), new Point3f(1f, 0.01f, 0f));
		// �������÷�Χ�߽����
		BoundingSphere bounds = new BoundingSphere(new Point3d(0, 0, 0), 10);
		// �������÷�Χ�߽�
		pointLight.setInfluencingBounds(bounds);
		return pointLight;
	}

	/**
	 * ����������
	 */

	private AmbientLight createAmbientLight()
	{
		// ��ӻ�����Դ
		AmbientLight light = new AmbientLight(true, new Color3f(Color.green));
		// �������÷�Χ�߽����
		BoundingSphere bounds = new BoundingSphere(new Point3d(0, 0, 0), 10);
		// �������÷�Χ�߽�
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
			// TODO �Զ����ɵ� catch ��
			e1.printStackTrace();
		}
		ThreeDPanel panel = new ThreeDPanel(dc);
		frame.add(panel.getCanvas());
	}

}
