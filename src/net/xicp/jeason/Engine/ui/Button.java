/*************************************************
 * Copyright (c) 2015, JiaYing University
 * All rights reserved.
/*************************************************
 
 
/**
 * @ Project : EasyEngine
 * @ File Name : Button.java
 * @ Created : 2015/6/3
 * @ Updated : 2015/6/10
 * @ Author : Jeason1997
 */

/**
 * 待解救问题 : 设置按钮上显示的文本
 */

package net.xicp.jeason.Engine.ui;

import javax.swing.ImageIcon;

import org.jbox2d.common.Vec2;

import net.xicp.jeason.Engine.base.Node;
import net.xicp.jeason.Engine.utils.Enum.MouseType;
import net.xicp.jeason.Engine.utils.callbacks.CallBack_0;

public class Button extends Node{
	
	protected Boolean m_isEnable = true;		// 按钮是否为禁用
	protected CallBack_0 m_callBack = null;		// 点击按钮的回调函数
	protected ImageIcon m_imageNormal = null;	// 按钮普通状态的贴图
	protected ImageIcon m_imagePress = null;
	protected ImageIcon m_imageUnable = null;
	protected String m_title = "";				// 按钮上显示的文本
	
	public Button(int order, Vec2 position, String title) {
		super(order, new ImageIcon(), position);
		this.m_title = title;
		this.m_touchEnable = true;
	}
	
	/**
	 * 设置按钮上显示的文本
	 * @param title
	 */
	public void setTitle(String title){
		this.m_title = title;
	}
	
	/**
	 * 设置按钮为禁用
	 * @param isEnable
	 */
	public void setEnable(Boolean isEnable){
		this.m_isEnable = isEnable;
		if(m_isEnable){
			this.setImageIcon(m_imageNormal);
		}else{
			this.setImageIcon(m_imageUnable);
		}
	}
	
	public Boolean isEnable(){
		return m_isEnable;
	}
	
	/**
	 * @param normal 按钮普通状态的图片
	 * @param press 按钮按下的图片
	 * @param unabel 按钮禁用状态的图片
	 */
	public void setButtonImage(ImageIcon normal, ImageIcon press,
			ImageIcon unabel){
		m_imageNormal = normal;
		m_imagePress = press;
		m_imageUnable = unabel;
		this.setImageIcon(normal);
	}
	
	public void setCallBack(CallBack_0 callback){
		m_callBack = callback;
	}
	
	/**
	 * 按钮事件，由于Node类无法直接实现鼠标接口，于是这里模拟鼠标接口，但实际的鼠标事件由装载此Button的Scene提供
	 */
	@Override
	public void mouseEvent(MouseType type){
		
		// 如果按钮为禁用状态，则不接受鼠标事件
		if(!m_isEnable){
			return;
		}
		
		switch (type) {
		case CLICK:
			if(m_callBack != null){
				m_callBack.doSome();
			}
			break;
		case PRESS:
			this.setImageIcon(m_imagePress);
			break;
		case RELEASE:
			this.setImageIcon(m_imageNormal);
			break;
		default:
			break;
		}
	}

}
