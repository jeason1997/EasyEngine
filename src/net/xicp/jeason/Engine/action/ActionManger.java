/*************************************************
 * Copyright (c) 2015, JiaYing University
 * All rights reserved.
/*************************************************
 
 
/**
 * @ Project : EasyEngine
 * @ File Name : ActionManger.java
 * @ Created : 2015/6/3
 * @ Updated : 2015/6/10
 * @ Author : Jeason1997
 */

package net.xicp.jeason.Engine.action;

import java.util.ArrayList;

import net.xicp.jeason.Engine.base.Node;

public class ActionManger {
	
	private ArrayList<Action> m_actions = new ArrayList<Action>();		// 动作列表
	private ArrayList<Action> m_recycleBin = new ArrayList<Action>();	// 等待回收的动作列表
	private Node m_node = null;											// 动作的执行对象
	private Boolean m_isPause = false;									// 是否暂停执行动作
	
	public ActionManger(Node node) {
		this.m_node = node;
	}
	
	/**
	 * 是否正在执行动作
	 * @return
	 */
	public Boolean isThereAnyActions(){
		if(m_actions.size() == 0){
			return false;
		}else{
			return true;
		}
	}
		
	public Boolean removeAction(Action action){
		m_actions.remove(action);
		return true;
	}
	
	// Action必须等到真正加入管理器时才激活，以防有些Action创建后没有立刻加入管理器，
	// 待到Node变化后再加入，会导致行为异常
	public void addAction(Action action){
		m_actions.add(action);
		action.activateAction(m_node);
	}
		
	public void runAction(){
		if(!m_isPause){
			for(Action action : m_actions){
				action.doAction();
				// 如果动作已经执行完毕，先将完毕的动作添加到回收站，此循环结束后再一并销毁
				// 不能再循环里删除列表里的元素，否则会出错
				if(action.isActionEnd()){
					m_recycleBin.add(action);
				}
			}
			for(Action action : m_recycleBin){
				m_actions.remove(action);
			}
			m_recycleBin.clear();
		}
	}
	
	public void pauseAction(){
		this.m_isPause = true;
	}
	
	public void resumeAction(){
		this.m_isPause = false;
	}
		
	public void stopAction(){
		m_actions.clear();
	}

		
}
