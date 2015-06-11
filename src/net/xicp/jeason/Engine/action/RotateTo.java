/*************************************************
 * Copyright (c) 2015, JiaYing University
 * All rights reserved.
/*************************************************
 
 
/**
 * @ Project : EasyEngine
 * @ File Name : RotateTo.java
 * @ Created : 2015/6/3
 * @ Updated : 2015/6/10
 * @ Author : Jeason1997
 */

package net.xicp.jeason.Engine.action;

import net.xicp.jeason.Engine.base.Node;
import net.xicp.jeason.Engine.utils.Constant;

public class RotateTo extends Action {

	private float m_angle = 0.0f;
	private float m_angleOffset = 0.0f;
	
	public RotateTo(float angle, int delay){
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
		/* 
		 * 因为Node本身的Angle可能超过2π，但给我们的感觉就是它还在2π内。假设此时Node的Angle为2π，我们看上去以为是0，
		 * 我们要它转到π（即顺时针180°），但实际上它会逆时针转180°。导致结果与预期不同。为此要消除这种异常，只能把多余的
		 * Angle消除掉，即此时Node的Angle与2π取余数
		 */
		this.m_angleOffset = (m_angle - (getNodeAngle() % (2 * Constant.PI))) / m_delay;
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
