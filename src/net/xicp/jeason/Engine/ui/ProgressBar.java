/*************************************************
 * Copyright (c) 2015, JiaYing University
 * All rights reserved.
/*************************************************
 
 
/**
 * @ Project : EasyEngine
 * @ File Name : ProgressBar.java
 * @ Created : 2015/6/3
 * @ Updated : 2015/6/10
 * @ Author : Jeason1997
 */

package net.xicp.jeason.Engine.ui;

import javax.swing.ImageIcon;

import org.jbox2d.common.Vec2;

import net.xicp.jeason.Engine.base.Node;

public class ProgressBar extends Node {
	
	protected int m_progress = 0;					// 进度
	protected ImageIcon m_background;
	protected ImageIcon m_bar;

	public ProgressBar(int order, Vec2 position, ImageIcon background, ImageIcon bar) {
		super(order, background, position);
		m_background = background;
		m_bar = bar;
	}
	
	public void setProgressBarImage(ImageIcon background, ImageIcon bar){
		m_background = background;
		m_bar = bar;
	}
	
	public void setProgress(int progress){
		m_progress = progress;
	}
	
	public int getProgress(){
		return m_progress;
	}

}
