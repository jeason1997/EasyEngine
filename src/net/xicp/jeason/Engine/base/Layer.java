/*************************************************
 * Copyright (c) 2015, JiaYing University
 * All rights reserved.
/*************************************************
 
 
/**
 * @ Project : EasyEngine
 * @ File Name : Layer.java
 * @ Created : 2015/6/3
 * @ Updated : 2015/6/10
 * @ Author : Jeason1997
 */


package net.xicp.jeason.Engine.base;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Vector;

import net.xicp.jeason.Engine.camera.Camera;
import net.xicp.jeason.Engine.utils.Constant;
import net.xicp.jeason.Engine.utils.callbacks.CallBack_1;


public class Layer{
	
	// 加载此Layer的Scene
	protected Scene m_scene;
	// Layer的层次等级，等级越低，越在底层，越先绘画
	protected int m_levelOrder = 0;
	// 一个Scene里的基本元素是Layer，而一个Layer里的基本元素是Node，每个Node都是引擎世界的一个物件
	protected Vector<Node> m_nodes = new Vector<Node>();
	// 摄影机
	protected Camera m_camera = new Camera(this);
	// 画布
	protected BufferedImage m_canva;
	// 画笔
	protected Graphics2D m_g2;
	
	public Layer(int levelOrder) {
		if(levelOrder >= 0){
			m_levelOrder = levelOrder;
		}
	}
	
	/**
	 * @return 返回加载此Layer的Scene
	 */
	public Scene getScene(){
		return m_scene;
	}
	
	/**
	 * 将此Layer附加到Scene里
	 * @param scene
	 */
	public void addToScene(Scene scene){
		m_scene = scene;
	}
	
	/**
	 * @return 获取此Layer所绑定的Camera
	 */
	public Camera getCamera(){
		return m_camera;
	}
	
	/**
	 * @return 获取此Layer的画布
	 */
	public BufferedImage getCanva(){
		return m_canva;
	}
	
	/**
	 * @return 获取此Layer的画笔
	 */
	public Graphics2D getGraphics2D(){
		return m_g2;
	}
	
	public void setGraphics2D(Graphics2D g2){
		m_g2 = g2;
	}
	
	/**
	 * 更新此Layer的画布。由于画布是透明的，所以上一帧画上的内容会存在，
	 * 必须新建一张画布
	 */
	public void updateCanav(){
		m_canva = new BufferedImage(Constant.SCREEN_WIDTH, Constant.SCREEN_HEIGHT,
				BufferedImage.TYPE_INT_ARGB);
	}
	
	// 添加Node
	public Boolean addNode(Node node) {
		if(node != null){
			int newNodeIndex = compareOrder(node.getDrawOrder());
			m_nodes.insertElementAt(node, newNodeIndex);
			node.addToLayer(this);
			return true;
		}
		return false;
	}
	
	// 移除Node
	public Boolean removeNode(Node node) {
		if(node != null){
			if(m_nodes.contains(node)){
				m_nodes.remove(node);
				return true;
			}
			return false;
		}
		return true;
	}
	
	/**
	 * 设置Layer的层次等级
	 * @param levelOrder
	 */
	public void setLevelOrder(int levelOrder){
		if(levelOrder >= 0){
			m_levelOrder = levelOrder;
		}
	}
	
	public int getLevelOrder(){
		return m_levelOrder;
	}
	
	public Vector<Node> getNodes(){
		return m_nodes;
	}
	
	// 比较新增加的Node的层次位置，以决定该Node的插入位置
	private int compareOrder(int order){
		for(int index = 0; index < m_nodes.size(); ++index){
			if(m_nodes.get(index).getDrawOrder() > order){
				return index;
			}
		}
		return m_nodes.size();
	}
	
	/**
	 * 正序遍历所有子节点（一个传入参数）
	 */
	public void nodeTraversal(Node node, CallBack_1 callback){
		callback.doSome(node);
		for(Node child : node.getchildren()){
			nodeTraversal(child, callback);
		}
	}
	
	// 模拟的线程
	public void run(){
		for(Node node : m_nodes){
			nodeTraversal(node, new CallBack_1() {
				
				@Override
				public void doSome(Object arg) {
					Node node = (Node) arg;
					node.run();
				}
			});
		}
	}
	
}
