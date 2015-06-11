/*************************************************
 * Copyright (c) 2015, JiaYing University
 * All rights reserved.
/*************************************************
 
 
/**
 * @ Project : EasyEngine
 * @ File Name : Label.java
 * @ Created : 2015/6/3
 * @ Updated : 2015/6/10
 * @ Author : Jeason1997
 */

package net.xicp.jeason.Engine.ui;

import javax.swing.ImageIcon;

import org.jbox2d.common.Vec2;

import net.xicp.jeason.Engine.base.Node;

public class Label extends Node {
	
	protected String m_text = "";

	public Label(int order, Vec2 position) {
		super(order, new ImageIcon(), position);
	}
	
	public void setText(String text){
		m_text = text;
	}
	
	public String getText(){
		return m_text;
	}
	
	public void setFont(){
		
	}
	
	public void setTextColor(){
		
	}
	
	public void setSize(){
		
	}

}
