/*************************************************
 * Copyright (c) 2015, JiaYing University
 * All rights reserved.
/*************************************************
 
 
/**
 * @ Project : EasyEngine
 * @ File Name : Action.java
 * @ Created : 2015/6/3
 * @ Updated : 2015/6/10
 * @ Author : Jeason1997
 */

package net.xicp.jeason.Engine.action;

import net.xicp.jeason.Engine.base.Node;

public abstract class Action {
	
	protected Boolean m_isActionEnd = false;		// 动作是否已经做完
	protected Boolean m_isActivate = false;			// 动作是否已经激活
	protected int m_delay = 0;						// 动作剩余时间
	protected int m_delay_bak = 0;					// 动作所需时间
	protected Node m_node = null;					// 执行动作的Node
	
	public Boolean isActionEnd(){
		return m_isActionEnd;
	}
	
	/**
	 * 执行动作
	 */
	public void doAction(){
	}
	
	/**
	 * 激活动作，主要用于同步Node，假设创建一个动作，但并没有立刻执行，在Node的状态改变后才执行，就会出现动作与预期不同
	 * 因此动作需要激活，即动作激活后才将Node的属性赋予动作。而且一旦激活后立刻执行。确保结果与预期一样。
	 * @param node 执行此动作的Node
	 */
	public void activateAction(Node node){
	}
	
	/**
	 * 恢复动作初始状态，主要是用于集合动作循环时，新的循环动作恢复到最初状态
	 */
	public void resetState(){
	}
}
