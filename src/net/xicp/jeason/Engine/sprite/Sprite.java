/*************************************************
 * Copyright (c) 2015, JiaYing University
 * All rights reserved.
/*************************************************
 
 
/**
 * @ Project : EasyEngine
 * @ File Name : Sprite.java
 * @ Created : 2015/6/3
 * @ Updated : 2015/6/10
 * @ Author : Jeason1997
 */

package net.xicp.jeason.Engine.sprite;

import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import net.xicp.jeason.Engine.action.Animation;
import net.xicp.jeason.Engine.base.Node;

import org.jbox2d.common.Vec2;

public class Sprite extends Node {
	
	protected String m_currentAnimationKey; 		// 当前帧动画的Key
	protected Map<String, Animation> m_animations = new HashMap<String, Animation>();
													// 帧动画合集

	public Sprite(int order, ImageIcon img, Vec2 position) {
		super(order, img, position);
	}
	
	/**
	 * 为此Sprite添加帧动画
	 * @param animation 帧动画
	 * @param animationName 帧动画的名字
	 */
	public void addAnimation(Animation animation, String animationName) {
		m_animations.put(animationName, animation);
		setCurrentAnimation(animationName);
	}

	/**
	 * 设置当前帧动画
	 * @param animationName 该动画的Key
	 * @return
	 */
	public Boolean setCurrentAnimation(String animationName) {
		if (m_animations.get(animationName) != null) {
			if (m_currentAnimationKey != null) {
				this.getActionManger().removeAction(
						m_animations.get(m_currentAnimationKey));
			}
			m_currentAnimationKey = animationName;
			this.getActionManger().addAction(
					m_animations.get(m_currentAnimationKey));
			return true;
		}
		return false;
	}

	/**
	 * 移除对于的帧动画
	 * @param animationName 该帧动画的Key
	 * @return
	 */
	public Boolean removeAnimation(String animationName) {
		if (m_animations.get(animationName) != null) {
			if (m_currentAnimationKey != animationName) {
				m_animations.remove(animationName);
				return true;
			}
			return false;
		}
		return false;
	}

}
