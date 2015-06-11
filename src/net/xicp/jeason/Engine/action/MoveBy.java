/*************************************************
 * Copyright (c) 2015, JiaYing University
 * All rights reserved.
/*************************************************
 
 
/**
 * @ Project : EasyEngine
 * @ File Name : MoveBy.java
 * @ Created : 2015/6/3
 * @ Updated : 2015/6/10
 * @ Author : Jeason1997
 */

package net.xicp.jeason.Engine.action;

import net.xicp.jeason.Engine.base.Node;

import org.jbox2d.common.Vec2;

public class MoveBy extends Action {
	
	private Vec2 m_offset = null;				// 发生的位移总量
	private Vec2 m_positionOffset = null;		// 位移偏移量
	
	public MoveBy(Vec2 offset, int delay){
		this.m_offset = offset;
		this.m_delay = delay;
		this.m_delay_bak = delay;
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
		// 位移偏移量，即每ms里Node发生的位移变化
		this.m_positionOffset = new Vec2(m_offset.x / m_delay,
				m_offset.y / m_delay);
	}
	
	@Override
	public void doAction(){
		if(m_isActivate){
			if(m_delay != 0){
				Vec2 oldPosition = getNodePositon();
				m_node.setPosition(new Vec2((oldPosition.x + m_positionOffset.x),
						(oldPosition.y + m_positionOffset.y)));
				--m_delay;
			}else{
				m_isActionEnd = true;
			}
		}
	}
	
	// 如果存在父节点，则目标位置应该是相对于父节点的位置，如果不存在，则目标位置就是相对于Scene的位置
	private Vec2 getNodePositon(){
		if(m_node.getFatehr() != null){
			return m_node.getRelativePosition();
		} else {
			return m_node.getPosition();
		}
	}

}
