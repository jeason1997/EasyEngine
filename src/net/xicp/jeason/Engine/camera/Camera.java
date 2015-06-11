/*************************************************
 * Copyright (c) 2015, JiaYing University
 * All rights reserved.
/*************************************************
 
 
/**
 * @ Project : EasyEngine
 * @ File Name : Camera.java
 * @ Created : 2015/6/3
 * @ Updated : 2015/6/10
 * @ Author : Jeason1997
 */

/**
 * 摄影机是一个Layer上的概念，每个Layer都有各自的Camera
 */

package net.xicp.jeason.Engine.camera;

import java.awt.Graphics2D;
import java.awt.Point;

import org.jbox2d.common.Vec2;

import net.xicp.jeason.Engine.base.Layer;
import net.xicp.jeason.Engine.base.Node;
import net.xicp.jeason.Engine.utils.Constant;

public class Camera {

	protected Vec2 m_center = new Vec2(0, 0);
	protected Vec2 m_scale = new Vec2(1, 1);
	protected float m_angle = 0;
	protected Boolean m_lockX = false;
	protected Boolean m_lockY = false;
	protected Node m_followTarget;
	protected Vec2 m_offset;
	protected Layer m_layer;						// 绑定的Layer
	
	public Camera(Layer layer){
		m_layer = layer;
	}
	
	
	public void setScale(Vec2 scale){
		m_scale = scale;
	}
	
	public void setScale(float scale){
		m_scale.x = scale;
		m_scale.y = scale;
	}
	
	/**
	 * 设置镜头左上角的位置
	 * @param center
	 */
	public void setCenter(Vec2 center){
		m_center = center;
	}
	
	/**
	 * 将镜头绕着左上角旋转
	 * @param angle
	 */
	public void setAngle(float angle){
		m_angle = angle;
	}
	
	/**
	 * 设置镜头跟随Node移动,必须是绑定的Layer中的Node才可以跟随，不能跟随其他Layer的Node
	 * @param target 要跟随的Node
	 * @param offset 默认是将跟随Node置于屏幕中间，可以设置偏移来决定跟随Node的位置
	 */
	public void setFollow(Node target, Vec2 offset){
		
		if(m_layer.getNodes().contains(target)){
			m_followTarget = target;
			m_offset = offset;
		} else {
			return;
		}
		
	}
	
	/**
	 * 取消镜头跟随
	 */
	public void removeFollow(){
		if(m_followTarget != null){
			m_followTarget = null;
		}
	}
	
	/**
	 * 锁定X轴
	 * @param bool
	 */
	public void lockXaxle(Boolean bool){
		m_lockX = bool;
	}
	
	/**
	 * 锁定Y轴
	 * @param bool
	 */
	public void lockYaxle(Boolean bool){
		m_lockY = bool;
	}
	
	/**
	 * 更正触摸点的值
	 * 由于Camera的变化，鼠标点击屏幕时，相对于Layer的位置可能发生改变
	 * 例如：鼠标点击屏幕的（100， 100），若Camera未改变，则Layer中的
	 * （100， 100）被点中。如果Camera的中心点设置为（500， 500）,则此
	 * 时Layer中被点中的坐标应该是（100 - 500， 100 - 500）
	 * @param oldPoint 鼠标点击屏幕的坐标
	 * @return Layer中被鼠标点中的坐标
	 */
	public Point correctTouchPoint(Point oldPoint){
		Point newPoint = oldPoint;
		// center
		newPoint.x -= m_center.x;
		newPoint.y -= m_center.y;
		// scale
		newPoint.x /= m_scale.x;
		newPoint.y /= m_scale.y;
		// angle
		return newPoint;
	}
	
	/**
	 * Camera的数据修正
	 * @param g2 调用该Camera的Scene的Graphics2D
	 */
	public void run(){
		// 刷新Layer中的画布
		m_layer.updateCanav();
		// 从Layer中的画布获取画笔
		Graphics2D g2 = (Graphics2D)m_layer.getCanva().getGraphics();
		
		// 设置镜头跟随Node
		if(m_followTarget != null){
			m_center.x = -(m_followTarget.getPosition().x - m_offset.x - Constant.SCREEN_WIDTH / 2);
			m_center.y = -(m_followTarget.getPosition().y - m_offset.y - Constant.SCREEN_HEIGHT / 2);
		}
		
		if(m_lockX){
			g2.translate(g2.getTransform().getTranslateX(), m_center.y);
		} else if (m_lockY){
			g2.translate(m_center.x, g2.getTransform().getTranslateY());
		} else {
			g2.translate(m_center.x, m_center.y);
		}
		g2.rotate(m_angle);
		g2.scale(m_scale.x, m_scale.y);
		
		m_layer.setGraphics2D(g2);
	}
	
}
