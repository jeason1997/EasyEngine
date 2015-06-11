/*************************************************
 * Copyright (c) 2015, JiaYing University
 * All rights reserved.
/*************************************************
 
 
/**
 * @ Project : EasyEngine
 * @ File Name : Scene.java
 * @ Created : 2015/6/3
 * @ Updated : 2015/6/10
 * @ Author : Jeason1997
 */

package net.xicp.jeason.Engine.base;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JPanel;

import net.xicp.jeason.Engine.collision.CollisionManager;
import net.xicp.jeason.Engine.physics.PhysicsWorldManager;
import net.xicp.jeason.Engine.utils.Constant;
import net.xicp.jeason.Engine.utils.Enum.MouseType;
import net.xicp.jeason.Engine.utils.callbacks.CallBack_1;

public class Scene extends JPanel implements MouseListener, MouseMotionListener{
	
	private static final long serialVersionUID = 1L;
	// 物理世界管理器，用于管理物理世界的运行，每个Scene都有单独的物理世界，因此都有单独的管理器
	protected PhysicsWorldManager m_worldManager = null;
	// 碰撞管理器
	protected CollisionManager m_collisionManager = new CollisionManager(this);
	// 所有的Layer在这个Scene里都存放在一个容器里，它们按levelOrde以从小到大的顺序排序
	protected Vector<Layer> m_layers = new Vector<Layer>();
	// 是否穿透触摸(即是否能触摸到底层的Layer)，否的话只能触摸到最顶层的Layer
	protected Boolean m_isSwallowTouches = false;
	// 是否允许物理模拟
	protected Boolean m_isAllowPhysics = false;
	// 鼠标选中的Node
	protected Node m_mouseClickNode = null;
	// 画布
	protected BufferedImage m_physicsDebugDraw;

	public Scene() {
		// 设置画布
		m_physicsDebugDraw = new BufferedImage(Constant.SCREEN_WIDTH, Constant.SCREEN_HEIGHT,
				BufferedImage.TYPE_INT_ARGB);
		// 设置透明度
        this.setBackground(new Color(0, 0, 0, 0.0f));
        // 设置鼠标监听
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
	}
	
	/**
	 * 开启物理模拟
	 * @param allow
	 */
	public void setPhysicalSimu(Boolean allow){
		// 设置物理管理器
		if(allow){
			this.m_isAllowPhysics = true;
			if(this.m_worldManager == null){
				this.m_worldManager = new PhysicsWorldManager(this);
			}
		}else{
			this.m_isAllowPhysics = false;
		}
	}
	
	public Boolean isAllowPhysics(){
		return m_isAllowPhysics;
	}
	
	public PhysicsWorldManager getWorldManager(){
		if(m_isAllowPhysics){
			return m_worldManager;
		}
		return null;
	}
	
	public Boolean isSwallowTouches() {
		return m_isSwallowTouches;
	}

	public void setIsSwallowTouches(Boolean isSwallowTouches) {
		this.m_isSwallowTouches = isSwallowTouches;
	}
	
	public Vector<Layer> getLayers(){
		return m_layers;
	}
	
	//　添加Layer
	public Boolean addLayer(Layer layer) {
		if(layer != null){
			int newLayerIndex = compareOrder(layer.getLevelOrder());
			m_layers.insertElementAt(layer, newLayerIndex);
			layer.addToScene(this);
			return true;
		}
		return false;
	}
	
	// 移除Layer
	public Boolean removeLayer(Layer layer) {
		return true;
	}
	
	// 比较新增加的Layer的levelOrder，以决定该Layer的插入位置
	private int compareOrder(int order){
		for(int index = 0; index < m_layers.size(); ++index){
			if(m_layers.get(index).getLevelOrder() > order){
				return index;
			}
		}
		return m_layers.size();
	}
	
	// 获取Graphics2D画笔
	public Graphics2D getGraphics2D(){
		return (Graphics2D) m_physicsDebugDraw.getGraphics();
	}
	
	
	/**
	 * 在所有Layer的画布上进行绘画
	 * @return
	 */
	public ArrayList<BufferedImage> drawCanvas(){
		ArrayList<BufferedImage> m_canvas = new ArrayList<BufferedImage>(); 
		//Graphics2D g2 = (Graphics2D) m_canvas.getGraphics();
		// 一个Scene里的基本元素是Layer，而一个Layer里的基本元素是Node，每个Node都是引擎世界的一个物件
        // 要画出整个世界，只需将整个世界的Node都画出即可
        // 每个Node都有若干的子Node，必须遍历后全部画出
        
        // 图片的绘画实际位置 = Node.position - Node.getHeight/Width * Node.getAnchor
		for(Layer layer : m_layers){
			// 修正每个层的摄影机
			layer.getCamera().run();
			Vector<Node> nodes = layer.getNodes();
			for(final Node node : nodes){
				layer.nodeTraversal(node, new CallBack_1() {
					Graphics2D g2 = node.getLayer().getGraphics2D();
					@Override
					public void doSome(Object arg) {
						Node node = (Node) arg;
						if(!node.getVisible()){
							return;
						}
						// 画
						g2.rotate(node.getAngle(), node.getPosition().x,
								node.getPosition().y);
						g2.drawImage(node.getImage(), 
								(int)(node.getPosition().x - node.getWidth() * node.getAnchor().x),
								(int)(node.getPosition().y - node.getHeight() * node.getAnchor().y), 
								(int)node.getWidth(), (int)node.getHeight(), null);
						g2.rotate(-node.getAngle(), node.getPosition().x,
								node.getPosition().y);
					}
				});
			}
			
			m_canvas.add(layer.getCanva());
		}
		
		return m_canvas;
	}
	

	@Override
	public void paint(Graphics g){

		super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        // 将所有Layer的画布画到屏幕上
        for(BufferedImage canva : drawCanvas()){
        	g2.drawImage(canva, 0, 0, null);
        }
        if(m_worldManager != null && m_worldManager.allowDebugDraw()){
        	// 将画布上的内容画到屏幕上
    		g2.drawImage(m_physicsDebugDraw, 0, 0, null);
    		// 画布清空，避免屏幕出现拖影
    		m_physicsDebugDraw = new BufferedImage(Constant.SCREEN_WIDTH, Constant.SCREEN_HEIGHT,
    				BufferedImage.TYPE_INT_ARGB);
        }
	}
	
	// 模拟的线程
	public void run() {
		
		// 如果存在World,则开启物理模拟
		if(m_isAllowPhysics){
			if(m_worldManager != null){
				m_worldManager.run();
			}
		}
		
		// Layer运算
		for(Layer layer : m_layers){
			layer.run();
		}
		
		// 碰撞检测
		m_collisionManager.collisionDetection();
		
		// 重绘
		repaint();	
		
	}
	
	
	@Override
	public void mouseDragged(MouseEvent a) {
		/*
		if(m_mouseClickNode != null){
			// 修正触摸点
			Point e = m_mouseClickNode.getLayer().getCamera().correctTouchPoint(a.getPoint());
			if(m_mouseClickNode.getFatehr() != null){
				Vec2 p = m_mouseClickNode.getFatehr().getPosition();
				m_mouseClickNode.setPosition(new Vec2(e.x - p.x, e.y - p.y));
			} else {
				m_mouseClickNode.setPosition(new Vec2(e.x, e.y));
			}
		}
		*/
	}

	
	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		m_mouseClickNode = m_collisionManager.mouseClickNode(e.getPoint());
		if(m_mouseClickNode != null){
			m_mouseClickNode.mouseEvent(MouseType.CLICK);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		m_mouseClickNode = m_collisionManager.mouseClickNode(e.getPoint());
		if(m_mouseClickNode != null){
			m_mouseClickNode.mouseEvent(MouseType.PRESS);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		m_mouseClickNode = m_collisionManager.mouseClickNode(e.getPoint());
		if(m_mouseClickNode != null){
			m_mouseClickNode.mouseEvent(MouseType.RELEASE);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
	
}
