/*************************************************
 * 这是一个EasyEngine引擎的通用模板，利用它可以快速创建你的项目
/*************************************************
 
 
/**
 * @ Project : EasyEngine
 * @ File Name : Main.java
 * @ Created : 2015/6/10
 * @ Updated : 2015/6/11
 * @ Author : Jeason1997
 */

package template;

import java.awt.Dimension;

import net.xicp.jeason.Engine.EasyEngine;

public class Main {
	
	
	public static void main(String[] arvg){
		
		EasyEngine.startEngine("HelloWorld", new Dimension(640, 480), false);
		HelloWorldScene scene = new HelloWorldScene();
		EasyEngine.getInstance().replaceScene(scene);
		
		while(true){
			EasyEngine.getInstance().run();
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
