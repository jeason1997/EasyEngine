/*************************************************
 * Copyright (c) 2015, JiaYing University
 * All rights reserved.
/*************************************************
 
 
/**
 * @ Project : EasyEngine
 * @ File Name : WorldFactory.java
 * @ Created : 2015/6/3
 * @ Updated : 2015/6/10
 * @ Author : Jeason1997
 */

package net.xicp.jeason.Engine.physics;

import net.xicp.jeason.Engine.base.Scene;
import net.xicp.jeason.Engine.sprite.PhysicsSprite;
import net.xicp.jeason.Engine.utils.Constant;
import static net.xicp.jeason.Engine.utils.Constant.*;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

public class WorldFactory {
	
	private static WorldFactory m_factory;
	private static World m_world;
	
	private WorldFactory(){
		
	}
	
	public static WorldFactory getInstance(Scene scene){
		if(m_factory == null){
			m_factory = new WorldFactory();
		}
		
		if(scene != null){
			if(scene.isAllowPhysics()){
				m_world = scene.getWorldManager().getWorld();
			}else{
				return null;
			}
		}else{
			return null;
		}
		return m_factory;
	}
	
	public WorldFactory createCircle(PhysicsSprite sprite, BodyType type, float density,
			float friction, float restitution) {
	
		BodyDef bd = new BodyDef();
		bd.position.set(sprite.getPosition().x / RATE, sprite.getPosition().y / RATE);
		//bd.linearVelocity = speed;
		bd.angle = (float) (sprite.getAngle() / 180 * Math.PI);
		bd.type = type;
		Body body = m_world.createBody(bd);

		CircleShape circle = new CircleShape();
		circle.m_radius = sprite.getWidth() / 2 / RATE;

		FixtureDef bodyDef = new FixtureDef();
		bodyDef.density = density;
		bodyDef.friction = friction;
		bodyDef.restitution = restitution;
		bodyDef.shape = circle;
		body.createFixture(bodyDef);

		// 互相绑定
		sprite.setPhysicsBody(body);
		body.setUserData(sprite);

		return m_factory;
	}
	
	public WorldFactory createRect(PhysicsSprite sprite, BodyType type, float density,
			float friction, float restitution) {
		BodyDef bd = new BodyDef();
		bd.position.set(sprite.getPosition().x / RATE, sprite.getPosition().y / RATE);
		//bd.linearVelocity = speed;
		bd.angle = (float) (sprite.getAngle() / 180 * Math.PI);
		bd.type = type;
		Body body = m_world.createBody(bd);

		PolygonShape box = new PolygonShape();
		box.setAsBox(sprite.getWidth() / 2 / RATE, sprite.getHeight() / 2 / RATE);

		FixtureDef bodyDef = new FixtureDef();
		bodyDef.density = density;
		bodyDef.friction = friction;
		bodyDef.restitution = restitution;
		bodyDef.shape = box;
		body.createFixture(bodyDef);

		// 互相绑定
		sprite.setPhysicsBody(body);
		body.setUserData(sprite);

		return m_factory;
	}
	
	public WorldFactory createPolygon(PhysicsSprite sprite, BodyType type, Vec2 vertices[],
			float density, float friction, float restitution) {
		BodyDef bd = new BodyDef();
		bd.position.set(sprite.getPosition().x / RATE, sprite.getPosition().y / RATE);
		//bd.linearVelocity = speed;
		bd.angle = (float) (sprite.getAngle() / 180 * Math.PI);
		bd.type = type;
		Body body = m_world.createBody(bd);

		PolygonShape shape = new PolygonShape();
		Vec2 points[] = new Vec2[vertices.length];
		for (int i = 0; i < vertices.length; i++) {
			points[i] = new Vec2();
			// 不知道为啥，要反过来才会正常显示
			points[i].x = vertices[i].x / RATE;
			points[i].y = -vertices[i].y / RATE;
		}
		shape.set(points, vertices.length);

		FixtureDef bodyDef = new FixtureDef();
		bodyDef.density = density;
		bodyDef.friction = friction;
		bodyDef.restitution = restitution;
		bodyDef.shape = shape;
		body.createFixture(bodyDef);

		// 互相绑定
		sprite.setPhysicsBody(body);
		body.setUserData(sprite);

		return m_factory;
	}
	
	/**
	 * 创建一个包围盒
	 * @param x 盒子的左上角X坐标
	 * @param y 盒子的左上角Y坐标
	 * @param width 盒子的宽度
	 * @param height 盒子的长度
	 * @return
	 */
	public WorldFactory createWorldBox(float x, float y, float width, float height){
		
		float thick = 10.0f;	// Box边框砖块的厚度
		
		BodyDef bd = new BodyDef();
		bd.type = BodyType.STATIC;
		
		Body up = m_world.createBody(bd);
		Body down = m_world.createBody(bd);
		Body left = m_world.createBody(bd);
		Body right = m_world.createBody(bd);
		
		up.setTransform(new Vec2((x + width / 2) / RATE, (y - thick / 2) / RATE), 0);
		down.setTransform(new Vec2((x + width / 2) / RATE, (y + height + thick / 2) / RATE), 0);
		left.setTransform(new Vec2((x - thick / 2) / RATE, (y + height / 2) / RATE), Constant.PI / 2);
		right.setTransform(new Vec2((x + width + thick / 2) / RATE, (y + height / 2) / RATE), Constant.PI / 2);
		
		PolygonShape box_up_down = new PolygonShape();
		box_up_down.setAsBox(width / 2 / RATE, thick / 2 / RATE);
		PolygonShape box_left_right = new PolygonShape();
		box_left_right.setAsBox(height / 2 / RATE, thick / 2 / RATE);
		
		up.createFixture(box_up_down, 0);
		down.createFixture(box_up_down, 0);
		left.createFixture(box_left_right, 0);
		right.createFixture(box_left_right, 0);
		
		return m_factory;
	}

}
