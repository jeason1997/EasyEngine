/*************************************************
 * Copyright (c) 2015, JiaYing University
 * All rights reserved.
/*************************************************
 
 
/**
 * @ Project : EasyEngine
 * @ File Name : CombinAtions.java
 * @ Created : 2015/6/3
 * @ Updated : 2015/6/10
 * @ Author : Jeason1997
 */

package net.xicp.jeason.Engine.action;

import java.util.Vector;

import net.xicp.jeason.Engine.base.Node;


public class CombinAtions extends Action {
	
	private Vector<Action> m_actions;		// 要组合的动作集合
	private Boolean m_isLoop = false;		// 是否循环
	
	/**
	 * @param actions 动作列表
	 * @param isLoop 动作集合是否循环
	 */
	public CombinAtions(Vector<Action> actions, Boolean isLoop) {
		this.m_actions = actions;
		this.m_isLoop = isLoop;
	}
	
	// 组合动作开始后，将集合里的第一个动作先激活，其他的动作就绪。
	@Override
	public void activateAction(Node node){
		this.m_node = node;
		this.m_isActivate = true;
		this.m_actions.firstElement().activateAction(m_node);
	}
	
	@Override
	public void doAction(){
		if(m_isActivate){
			if(!m_actions.isEmpty()){
				// 执行当前动作，即集合里的第一个动作
				Action currentAction = m_actions.firstElement();
				currentAction.doAction();
				
				if(currentAction.isActionEnd()){
					// 当前动作执行完后，如果动作集合循环的话，则将此动作移动到集合尾部，形成一个源源不断的循环
					if(m_isLoop){
						currentAction.resetState();
						m_actions.insertElementAt(currentAction, m_actions.size());
					}
					// 如果集合动作不循环，则直接删除当前动作即可
					m_actions.removeElement(currentAction);
					
					if(!m_actions.isEmpty()){
						m_actions.firstElement().activateAction(m_node);
					}
				}
			}else{
				// 所有动作都执行完，则这个集合动作执行完毕
				m_isActionEnd = true;
			}
		}
	}
}
