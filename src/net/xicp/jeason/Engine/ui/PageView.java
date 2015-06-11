/*************************************************
 * Copyright (c) 2015, JiaYing University
 * All rights reserved.
/*************************************************
 
 
/**
 * @ Project : EasyEngine
 * @ File Name : PageView.java
 * @ Created : 2015/6/3
 * @ Updated : 2015/6/10
 * @ Author : Jeason1997
 */

package net.xicp.jeason.Engine.ui;

import java.util.ArrayList;
import java.util.Vector;

import javax.swing.ImageIcon;

import org.jbox2d.common.Vec2;

import net.xicp.jeason.Engine.action.Action;
import net.xicp.jeason.Engine.action.CombinAtions;
import net.xicp.jeason.Engine.action.MoveBy;
import net.xicp.jeason.Engine.base.Node;

public class PageView extends Node {
	
	protected Boolean m_isClipping = false;			// 是否裁剪内容，即超过容器范围的子页面不显示
	protected Boolean m_isSpringBack = true;		// 回弹效果
	protected Boolean m_animationEffect = true;		// 切换页面时是否附带动画效果
	protected Boolean m_isCycle = true;				// 页面容器是否循环，即最后一页的下一页是不是回到第一页
	protected int m_currentPageIndex = 0;			// 当前页面索引
	protected int m_lastPageIndex = 0;				// 上一个页面索引
	protected int m_pageCount = 0;					// 页面数量
	protected ArrayList<Page> m_pages = new ArrayList<Page>();
													// 页面列表
	protected float m_pageInterval = 0;				// 子页面的间隔

	public PageView(int order, Vec2 position, ImageIcon background) {
		super(order, background, position);
	}
	
	/**
	 * 滚动到指定页面
	 * @param pageIndex
	 */
	public void setPage(int pageIndex){
		if(pageIndex >= m_pageCount || pageIndex < 0){
			return;
		}
		m_lastPageIndex = m_currentPageIndex;
		m_currentPageIndex = pageIndex;
		updatePageView();
	}
	
	/**
	 * 显示下一页
	 */
	public void setNextPage(){
		m_lastPageIndex = m_currentPageIndex;
		// 当前是否是最后一页
		if(m_currentPageIndex == m_pageCount - 1){
			// 页面容器循环的话，则回到第一页
			if(m_isCycle){
				m_currentPageIndex = 0;
			}
		} else {
			++m_currentPageIndex;
		}
		updatePageView();
	}
	
	/**
	 * 显示上一页
	 */
	public void setLastPage(){
		m_lastPageIndex = m_currentPageIndex;
		// 当前是否是第一页
		if(m_currentPageIndex == 0){
			// 页面容器循环的话，则回到最后一页
			if(m_isCycle){
				m_currentPageIndex = m_pageCount - 1;
			}
		} else {
			--m_currentPageIndex;
		}
		updatePageView();
	}

	/**
	 * 添加子页面
	 * @param page
	 */
	public void addPage(Page page){
		// 默认显示第一页，其他的页面根据是否裁剪选择可见或者不可见
		if(m_pageCount == 0){
			page.setVisible(true);
		} else {
			page.setVisible(!m_isClipping);
		}
		m_pages.add(page);
		this.addChild(page);
		// 子页面根据它的索引放置到指定位置
		page.setPosition(new Vec2((getWidth() + m_pageInterval) * m_pageCount, 0));
		++m_pageCount;
	}
	
	public void removePage(int pageIndex){
		this.removeChild(m_pages.get(pageIndex));
		m_pages.remove(pageIndex);
		--m_pageCount;
	}
	
	public void removeAllPage(){
		for(Page page : m_pages){
			this.removeChild(page);
		}
		m_pages.clear();
		m_pageCount = 0;
	}
	
	public int getPageCount(){
		return m_pageCount;
	}
	
	public int getCurrentPageIndex(){
		return m_currentPageIndex;
	}
	
	// 当容器的页面改变时，应该更新容器的显示内容
	private void updatePageView(){
		int delay = 1;
		if(m_animationEffect){
			delay = 30;
		}
		for(Page page : m_pages){
			Vector<Action> actions = new Vector<Action>();
			// 移动效果
			actions.add(new MoveBy(
					new Vec2((getWidth() + m_pageInterval) * (m_lastPageIndex - m_currentPageIndex), 0),
					delay));
			// 回弹效果
			if(m_isSpringBack){
				int spring = 0;
				if((m_lastPageIndex - m_currentPageIndex) > 0){
					spring = 1;
				} else if((m_lastPageIndex - m_currentPageIndex) < 0){
					spring = -1;
				}
				
				actions.add(new MoveBy(
						new Vec2(10 * spring, 0),
						5));
				actions.add(new MoveBy(
						new Vec2(-15 * spring, 0),
						8));
				actions.add(new MoveBy(
						new Vec2(5 * spring, 0),
						10));
			}
			page.getActionManger().addAction(new CombinAtions(actions, false));
		}
	}
}
