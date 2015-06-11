/*************************************************
 * Copyright (c) 2015, JiaYing University
 * All rights reserved.
/*************************************************
 
 
/**
 * @ Project : EasyEngine
 * @ File Name : Page.java
 * @ Created : 2015/6/3
 * @ Updated : 2015/6/10
 * @ Author : Jeason1997
 */

package net.xicp.jeason.Engine.ui;

import java.math.BigDecimal;
import java.util.Vector;

import javax.swing.ImageIcon;

import org.jbox2d.common.Vec2;

import net.xicp.jeason.Engine.utils.Enum.Layout;

import net.xicp.jeason.Engine.base.Node;

public class Page extends Node {
	
	protected Boolean m_isClipping = false;				// 是否裁剪内容，即超过容器范围的子页面不显示
	protected Layout m_layout = Layout.SUDOKU;
	protected float m_childInterval_X = 0;				// 子控件的垂直间隔
	protected float m_childInterval_Y = 0;				// 子控件的水平间隔

	public Page(int order, ImageIcon img, Vec2 position) {
		super(order, img, position);
	}
	
	
	
	@Override
	public void addChild(Node child) {
		super.addChild(child);
		updateLayout();
	}


	
	@Override
	public void removeChild(Node child) {
		super.removeChild(child);
		updateLayout();
	}


	/**
	 * 设置页面容器内的控件的排序方式，默认为“九宫格”模式
	 * @param layout
	 */
	public void setLayout(Layout layout){
		m_layout = layout;
		updateLayout();
	}
	
	
	// 每次更改布局或者添加子控件时，都会重新更改所有子控件的排序方式
	private void updateLayout(){
		switch(m_layout){
		case SUDOKU:
			sudokuPlace();
			break;
		case VERTICAL:
			verticalPlace();
			break;
		case HORIZONTAL:
			horizontalPlace();
			break;
		}
	}
	
	private void sudokuPlace(){
		Vector<Node> children = getchildren();
		int size = children.size();
		if(size == 0){
			return;
		}
		
		int column = new BigDecimal(Math.sqrt(size)).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
		int row = column;
		if(size > column * column){
			row += 1;
		}
		
		int c_center = column / 2;
		int r_center = row / 2;
		
		float height = children.get(0).getHeight();
		float width = children.get(0).getWidth();
		
		Vec2 offset = new Vec2(0, 0);
		if(row % 2 == 0){
			offset.x += width / 2;
		}
		if(column % 2 == 0){
			offset.y += height / 2;
		}
		
		// 排序
		for(int index = 0, c = 0; c < column; ++c){	// 列 Y
			for(int r = 0; index < size && r < row; ++r){	// 行 X
				children.get(index++).setPosition(new Vec2(
						offset.x + (r - r_center) * (width + m_childInterval_X),
						offset.y + (c - c_center) * (height + m_childInterval_Y)));
			}
		}
	}
	
	private void verticalPlace(){
		Vector<Node> children = getchildren();
		int size = children.size();
		if(size == 0){
			return;
		}
		
		int centerChild = size / 2;
		
		// 排序
		for(int index = 0; index != size ; ++index){
			children.get(index).setPosition(new Vec2(0,
					(index - centerChild) * (children.get(index).getHeight() + m_childInterval_X)));
		}
		
	}
	
	private void horizontalPlace(){
		Vector<Node> children = getchildren();
		int size = children.size();
		if(size == 0){
			return;
		}
		
		int centerChild = size / 2;
		
		// 排序
		for(int index = 0; index != size ; ++index){
			children.get(index).setPosition(new Vec2(
					(index - centerChild) * (children.get(index).getWidth() + m_childInterval_Y), 0));
		}
	}
	

}
