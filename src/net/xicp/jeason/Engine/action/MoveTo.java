/*************************************************
 * Copyright (c) 2015, JiaYing University
 * All rights reserved.
/*************************************************
 
 
/**
 * @ Project : EasyEngine
 * @ File Name : MoveTo.java
 * @ Created : 2015/6/3
 * @ Updated : 2015/6/10
 * @ Author : Jeason1997
 */

package net.xicp.jeason.Engine.action;

import net.xicp.jeason.Engine.base.Node;

import org.jbox2d.common.Vec2;

public class MoveTo extends Action {

	private Vec2 m_targetPositon = null;		// 目标位置
	private Vec2 m_initialPosition = null;		// 初始位置
	private Vec2 m_positionOffset = null;		// 每帧位移偏移量
	
	public MoveTo(Vec2 targetPosition, int delay){
		this.m_delay = delay;
		this.m_delay_bak = delay;
		this.m_targetPositon = targetPosition;
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
	}
	
	@Override
	public void doAction(){
		if(m_isActivate){
			if(m_delay != 0){
				// 因为此Action是MoveTo，目标是绝对地址。但在MoveTo的过程中Node的位置可能发生变化。
				// 如果发生变化，会导致最终位置与预期不同。因此需要在每一帧里重新计算偏移量
				m_initialPosition = getNodePositon();
				m_positionOffset = new Vec2((m_targetPositon.x - m_initialPosition.x) / m_delay,
						(m_targetPositon.y - m_initialPosition.y) / m_delay);
				
				m_node.setPosition(new Vec2((m_initialPosition.x + m_positionOffset.x),
						(m_initialPosition.y + m_positionOffset.y)));
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
