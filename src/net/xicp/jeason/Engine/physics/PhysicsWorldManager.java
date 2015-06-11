/*************************************************
 * Copyright (c) 2015, JiaYing University
 * All rights reserved.
/*************************************************
 
 
/**
 * @ Project : EasyEngine
 * @ File Name : PhysicsWorldManager.java
 * @ Created : 2015/6/3
 * @ Updated : 2015/6/10
 * @ Author : Jeason1997
 */

package net.xicp.jeason.Engine.physics;

import static net.xicp.jeason.Engine.utils.Constant.*;


import net.xicp.jeason.Engine.base.Scene;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

public class PhysicsWorldManager{
	
	private World m_world;
	private Scene m_scene;
	private Boolean m_debugDraw = false;
	
	private Vec2 m_gravity = new Vec2(0, 9.8f);
	
	public PhysicsWorldManager(Scene scene){
		m_scene = scene;
		createWorld();
	}
	
	public void setDebugDraw(Boolean bool){
		m_debugDraw = bool;
		if(m_debugDraw){
			DebugDrawJ2D debugDraw = new DebugDrawJ2D(m_scene);
			debugDraw.appendFlags(DebugDrawJ2D.e_aabbBit
					| DebugDrawJ2D.e_centerOfMassBit
					| DebugDrawJ2D.e_dynamicTreeBit
					| DebugDrawJ2D.e_jointBit
					| DebugDrawJ2D.e_pairBit
					| DebugDrawJ2D.e_shapeBit);
			m_world.setDebugDraw(debugDraw);
		}
	}
	
	public Boolean allowDebugDraw(){
		return m_debugDraw;
	}
	
	private void createWorld(){
		if(m_world == null){
			m_world = new World(m_gravity);
			m_world.setAllowSleep(true);
		}
	}

	public World getWorld() {
		return m_world;
	}

	public void run() {
		m_world.step(TIME_STEP, ITERA, ITERA);
		if(m_debugDraw){
			m_world.drawDebugData();
		}
	}
	
	
}
