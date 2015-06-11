/*************************************************
 * Copyright (c) 2015, JiaYing University
 * All rights reserved.
/*************************************************
 
 
/**
 * @ Project : EasyEngine
 * @ File Name : BoundingBox.java
 * @ Created : 2015/6/3
 * @ Updated : 2015/6/10
 * @ Author : Jeason1997
 */

package net.xicp.jeason.Engine.collision;

import net.xicp.jeason.Engine.base.Node;

public class BoundingBox {
	
	// 坐标系上一对X坐标跟一对Y坐标即可决定一个矩形
	private float m_xLeft;
	private float m_xRight;
	private float m_yUp;
	private float m_yDown;
	
	public BoundingBox(Node node){
		m_xLeft = node.getPosition().x - node.getWidth() * node.getAnchor().x;
		m_xRight = m_xLeft + node.getWidth();
		m_yUp = node.getPosition().y - node.getHeight() * node.getAnchor().y;
		m_yDown = m_yUp + node.getHeight();
	}
	
	public float xLeft(){
		return m_xLeft;
	}
	
	public float xRight(){
		return m_xRight;
	}
	
	public float yUp(){
		return m_yUp;
	}
	
	public float yDown(){
		return m_yDown;
	}
	
	
}
