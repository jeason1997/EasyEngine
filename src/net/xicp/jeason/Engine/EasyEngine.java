/*************************************************
 * Copyright (c) 2015, JiaYing University
 * All rights reserved.
 * 
 * EasyEngine是一个轻量级JAVA游戏引擎，引擎结构模仿COCOS2D-X，
 * 其中物理引擎用的是BOX2D 2.2.1.1,图像界面用的是JAVA SWING，
 * 暂时只支持WIN32版的游戏开发（未来会增加对Android及Linux的支
 * 持）。本引擎遵循MIT开源协议。
/*************************************************
 
 
/**
 * @ Project : EasyEngine
 * @ File Name : EasyEngine.java
 * @ Created : 2015/6/3
 * @ Updated : 2015/6/11
 * @ Author : Jeason1997
 * @ Version : 1.0.0.6_11_Alpha
 */



package net.xicp.jeason.Engine;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Stack;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.jbox2d.common.Vec2;

import net.xicp.jeason.Engine.base.Layer;
import net.xicp.jeason.Engine.base.Node;
import net.xicp.jeason.Engine.base.Scene;
import net.xicp.jeason.Engine.utils.Constant;


public class EasyEngine extends JFrame{
	
	private static final long serialVersionUID = 1L;

	private static EasyEngine m_EasyEngine;
	
	private static Boolean m_isEngineStart = false;		// 引擎是否已经启动
	
	private static Boolean m_isNoBorder = true;			// 屏幕是否无边框
	
	// 是否显示调试信心
	private Boolean displayStats = false;
	
	// 场景栈
	private Stack<Scene> sceneStack = new Stack<Scene>();
	
	// 当前场景
	private Scene m_activeScene;
	
	public static EasyEngine getInstance(){
		if(m_isEngineStart && m_EasyEngine == null){
			m_EasyEngine = new EasyEngine();
			m_EasyEngine.showLogo();
		}
		return m_EasyEngine;
	}
	
	/**
	 * 
	 * @param gameName 游戏名字
	 * @param screenSize 屏幕尺寸
	 * @param isNoBorder 屏幕是否无边框
	 */
	public static void startEngine(String gameName, Dimension screenSize, Boolean isNoBorder){
		if(m_isEngineStart){
			return;
		}
		Constant.GAME_NAME = gameName;
		Constant.SCREEN_WIDTH = screenSize.width;
		Constant.SCREEN_HEIGHT = screenSize.height;
		m_isNoBorder = isNoBorder;
		m_isEngineStart = true;
	}
	
	private void showLogo(){
		ImageIcon logo = new ImageIcon(getClass().getResource("/res/LOGO.png"));
		Scene start = new Scene();
		if(!m_isNoBorder){
			start.setBackground(Color.BLACK);
		}
		
		Layer layer = new Layer(0);
		layer.addNode(new Node(0, logo,
				new Vec2(Constant.SCREEN_WIDTH / 2, Constant.SCREEN_HEIGHT / 2)));
		start.addLayer(layer);
		m_EasyEngine.replaceScene(start);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			m_EasyEngine.replaceScene(new Scene());
		}
	}
	
	private EasyEngine() {
		super(Constant.GAME_NAME);
		if(m_isNoBorder){
			// 设置无边框
		    setUndecorated(true);
		    // 设置透明度
		    setBackground(new Color(0,0,0,0.0f));
		} else {
			setBackground(Color.BLACK);
		}
	    setSize(new Dimension(Constant.SCREEN_WIDTH, Constant.SCREEN_HEIGHT));
	    setResizable(false);
	    // 设置窗口在屏幕中心
	    setLocationRelativeTo(null);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    // 必须放在最后，否则以上设置无法立刻看到。相当于刷新。
        setVisible(true);
	}
	
	// 显示调试信息
	public void setDisplayStats(Boolean bool){
		displayStats = bool;
	}
	
	// 获取当前场景
	public Scene getCurrentScene(){
		if(m_activeScene != null){
			return m_activeScene;
		}
		return null;
	}
	
	// 开启新场景，并将此场景入栈
	public Boolean pushScene(Scene scene) {
		sceneStack.push(scene);
		m_activeScene = scene;
		updateDisplayScene();
		return true;
	}
	
	// 直接开启新场景
	public Boolean replaceScene(Scene scene) {
		m_activeScene = scene;
		updateDisplayScene();
		return true;
	}
	
	// 将栈顶部的场景出栈，作为开启的新场景
	public Boolean popScene() {
		if(!sceneStack.isEmpty()){
			m_activeScene = sceneStack.pop();
			updateDisplayScene();
			return true;
		}
		else {
			return false;
		}
	}
	
	// 设置当前显示场景
	private void updateDisplayScene() {
		// 为了效率考虑，只显示当前场景，其他的不显示，但实际存在于栈中。
		this.getContentPane().removeAll();
		if(m_activeScene != null)
			this.getContentPane().add(m_activeScene);
		// 每次更新组件，必须重新调用，才会显示出来
		this.setVisible(true);
	}
	
	// 模拟的线程
	public void run(){
		
		// 显示调试信息
		if(displayStats){
			
		}
		
		// 如果当前存在场景，则进行场景运算
		if(m_activeScene != null){
			m_activeScene.run();
		}
		
		// 如果JFrame是透明的，则必须不断repaint，不然JFrame上面的容器留下的轨迹会一直存在。
		repaint();
	}
}
