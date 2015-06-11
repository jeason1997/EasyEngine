/*************************************************
 * Copyright (c) 2015, JiaYing University
 * All rights reserved.
/*************************************************
 
 
/**
 * @ Project : EasyEngine
 * @ File Name : CollisionManager.java
 * @ Created : 2015/6/3
 * @ Updated : 2015/6/11
 * @ Author : Jeason1997
 */

package net.xicp.jeason.Engine.collision;

import java.awt.Point;
import java.util.Vector;

import net.xicp.jeason.Engine.base.Layer;
import net.xicp.jeason.Engine.base.Node;
import net.xicp.jeason.Engine.base.Node.NodeType;
import net.xicp.jeason.Engine.base.Scene;
import net.xicp.jeason.Engine.utils.callbacks.CallBack_0;

public class CollisionManager {

	private Scene m_scene;

	public CollisionManager(Scene scene) {
		this.m_scene = scene;
	}

	/**
	 * 只检测同一Layer上的Node是否碰撞
	 */
	public void collisionDetection() {
		for (Layer layer : m_scene.getLayers()) {
			// 先获取此Layer上的所有Node，当Node的个数小于2时，碰撞无法实现，跳过检测
			Vector<Node> nodes = layer.getNodes();
			if (nodes.size() < 2) {
				continue;
			}
			// Layer中的所有Node互相检测是否相碰，这个检测代价有点高，因此一般情况下Node的默认状态是不允许碰撞检测的，除非手动设置
			for (int index = 0; index != nodes.size() - 1; ++index) {
				Node A = nodes.get(index);
				if (!A.collisionEnable()) {
					continue;
				}
				for (int j = index + 1; j != nodes.size(); ++j) {
					Node B = nodes.get(j);
					if (B.collisionEnable()) {
						if (isCollision(A, B)) {
							// 如果A与B发生碰撞
							System.out.println("a");
						}
					}
				}
			}
		}
	}

	// 选择适当的碰撞检测算法
	private Boolean isCollision(Node A, Node B) {
		switch (A.getNodeType()) {
		case RECT: {
			if (B.getNodeType() == NodeType.RECT) {
				return rect_Rect(A.getBouBoundingBox(), B.getBouBoundingBox());
			} else if (B.getNodeType() == NodeType.CIRCLE) {
				return false;
			}
		}
			break;
		case CIRCLE: {
			if (B.getNodeType() == NodeType.RECT) {
				return false;
			} else if (B.getNodeType() == NodeType.CIRCLE) {
				return false;
			}
		}
			break;
		}
		return false;
	}

	/**
	 * 设置回调函数
	 * @param callback
	 */
	public void setCallBack(CallBack_0 callback) {
	}

	// 矩形碰撞检测
	private Boolean rect_Rect(BoundingBox A, BoundingBox B) {
		if (B.xLeft() >= A.xRight() || B.xRight() <= A.xLeft()) {
			return false;
		} else if (B.yUp() >= A.yDown() || B.yDown() <= A.yUp()) {
			return false;
		}
		return true;
	}

	// 矩形与圆形的碰撞检测
	@SuppressWarnings("unused")
	private Boolean rect_Circle() {
		return false;
	}

	// 圆形与圆形的碰撞检测
	@SuppressWarnings("unused")
	private Boolean circle_Circle() {
		return false;
	}
	
	// 矩形与点的碰撞检测
	private Boolean rect_Point(BoundingBox rect, Point point){
		if (point.x >= rect.xLeft()
				&& point.x < rect.xRight()
				&& point.y >= rect.yUp()
				&& point.y < rect.yDown()){
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 检测鼠标点中哪个Node，即点与矩形的碰撞检测
	 * @param testPoint
	 * @return
	 */
	public Node mouseClickNode(Point testPoint) {

		int border;
		// 如果允许穿透，则查询所有Layer内当前与当前点交叉的Node
		if (m_scene.isSwallowTouches()) {
			border = -1;
		}
		// 如果不允许穿透，则只查询最顶Layer
		else {
			// 倒数第二层
			border = m_scene.getLayers().size() - 2;
		}
		
		if(m_scene.getLayers().size() == 0){
			return null;
		}

		// 之所以选择倒序判断，是因为：如果有两个层有精灵重叠，则只返回上层的精灵
		for (int index = m_scene.getLayers().size() - 1; index != border; --index) {
			Layer layer = m_scene.getLayers().get(index);
			Vector<Node> nodes = layer.getNodes();
			Point point = layer.getCamera().correctTouchPoint(testPoint);
			// 同理，同一个Layer中有多个Node重叠时，只返回上层的Node
			for (int j = nodes.size() - 1; j != -1; --j){
				Node res = nodeTraversal(nodes.get(j), point);
				if(res != null){
					return res;
				}
			}

		}
		return null;
	}
	
	/**
	 * 遍历Node的所有子Node，找出被Point点中的子Node
	 */
	private Node nodeTraversal(Node node, Point point){
		
		if(node.getchildren().size() == 0){
			if(node.touchEnable() && rect_Point(node.getBouBoundingBox(), point)){
				return node;
			}
			return null;
		}
		
		for(int index = node.getchildren().size() - 1; index != -1; --index){
			Node n = nodeTraversal(node.getchildren().get(index), point);
			if(n != null){
				return n;
			}
		}
		
		if(node.touchEnable() && rect_Point(node.getBouBoundingBox(), point)){
			return node;
		}
		return null;
	}
}
