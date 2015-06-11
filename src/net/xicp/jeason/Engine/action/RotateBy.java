/*************************************************
 * Copyright (c) 2015, JiaYing University
 * All rights reserved.
/*************************************************
 
 
/**
 * @ Project : EasyEngine
 * @ File Name : RotateBy.java
 * @ Created : 2015/6/3
 * @ Updated : 2015/6/10
 * @ Author : Jeason1997
 */

package net.xicp.jeason.Engine.action;

import net.xicp.jeason.Engine.base.Node;

public class RotateBy extends Action {

	private float m_angle = 0.0f;
	private float m_angleOffset = 0.0f;
	
	public RotateBy(float angle, int delay){
		this.m_delay = delay;
		this.m_delay_bak = delay;
		this.m_angle = angle;
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
		
		this.m_angleOffset = m_angle / m_delay;
	}
	
	@Override
	public void doAction(){
		if(m_isActivate){
			if(m_delay != 0){
				m_node.setAngle(getNodeAngle() + m_angleOffset);
				--m_delay;
			}else{
				m_isActionEnd = true;
			}
		}
	}
	
	private float getNodeAngle(){
		if(m_node.getFatehr() != null){
			return m_node.getRelativeAngle();
		} else {
			return m_node.getAngle();
		}
	}
}
