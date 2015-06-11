/*************************************************
 * Copyright (c) 2015, JiaYing University
 * All rights reserved.
/*************************************************
 
 
/**
 * @ Project : EasyEngine
 * @ File Name : Animation.java
 * @ Created : 2015/6/3
 * @ Updated : 2015/6/10
 * @ Author : Jeason1997
 */

package net.xicp.jeason.Engine.action;

import java.util.Vector;

import javax.swing.ImageIcon;

import net.xicp.jeason.Engine.base.Node;

public class Animation extends Action {

	protected int m_interval = 0; 			// 动作时间间隔
	protected Vector<ImageIcon> m_frames; 	// 动画帧集合
	protected Boolean m_loop = false; 		// 动画是否循环

	/**
	 * @param frames 帧动画的动画集合
	 * @param interval 每一帧的间隔
	 * @param loop 动画是否循环
	 */
	public Animation(Vector<ImageIcon> frames, int interval, Boolean loop) {
		this.m_interval = interval;
		m_frames = frames;
		m_loop = loop;
	}

	@Override
	public void resetState() {
		m_isActionEnd = false;
	}

	@Override
	public void activateAction(Node node) {
		this.m_node = node;
		this.m_isActivate = true;
	}

	@Override
	public void doAction() {
		if (m_isActivate) {
			if (!m_frames.isEmpty()) {
				if(m_delay == m_interval){
					m_delay = 0;
					if (m_loop) {
						m_node.setImageIcon(m_frames.firstElement());
						m_frames.insertElementAt(m_frames.firstElement(), m_frames.size());
					} else {
						m_node.setImageIcon(m_frames.firstElement());
					}
					m_frames.removeElement(m_frames.firstElement());
				}
				++m_delay;
			} else {
				m_isActionEnd = true;
			}
		}
	}

}
