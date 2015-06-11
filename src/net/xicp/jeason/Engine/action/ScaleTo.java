/*************************************************
 * Copyright (c) 2015, JiaYing University
 * All rights reserved.
/*************************************************
 
 
/**
 * @ Project : EasyEngine
 * @ File Name : ScaleTo.java
 * @ Created : 2015/6/3
 * @ Updated : 2015/6/10
 * @ Author : Jeason1997
 */

package net.xicp.jeason.Engine.action;

import net.xicp.jeason.Engine.base.Node;

public class ScaleTo extends Action {
	
	private float m_scale;				// 缩放倍数
	private float m_scaleOffset;		// 每帧缩放偏移量，即每一帧Node缩放变化的量
	
	public ScaleTo(float scale, int delay){
		this.m_delay = delay;
		this.m_delay_bak = delay;
		this.m_scale = scale;
	}
	
	@Override
	public void resetState(){
		m_delay = m_delay_bak;
		m_isActionEnd = false;
	}
	
	@Override
	public void activateAction(Node node){
		this.m_node = node;
		this.m_isActivate = true;
		// 没帧缩放偏移量，这里算出的是每ms里Node的变化量
		this.m_scaleOffset = (m_scale - getNodeScale()) / m_delay;
	}
	
	@Override
	public void doAction(){
		if(m_isActivate){
			if(m_delay != 0){
				m_node.setScale(getNodeScale() + m_scaleOffset);
				--m_delay;
			}else{
				m_isActionEnd = true;
			}
		}
	}
	
	private float getNodeScale(){
		if(m_node.getFatehr() != null){
			return m_node.getRelativeScale();
		} else {
			return m_node.getScale();
		}
	}

}
