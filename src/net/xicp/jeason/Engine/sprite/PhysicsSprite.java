/*************************************************
 * Copyright (c) 2015, JiaYing University
 * All rights reserved.
/*************************************************
 
 
/**
 * @ Project : EasyEngine
 * @ File Name : PhysicsSprite.java
 * @ Created : 2015/6/3
 * @ Updated : 2015/6/10
 * @ Author : Jeason1997
 */

package net.xicp.jeason.Engine.sprite;

import javax.swing.ImageIcon;

import static net.xicp.jeason.Engine.utils.Constant.*;

import net.xicp.jeason.Engine.base.Node;
import net.xicp.jeason.Engine.utils.Constant;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.collision.shapes.ShapeType;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;

public class PhysicsSprite extends Sprite {

	protected Body m_body = null;
	protected Vec2[] m_oldVertices;			// 保存Body最初的顶点，Polygon类型的刚体进行缩放时用到的

	public PhysicsSprite(int order, ImageIcon img, Vec2 position) {
		super(order, img, position);
	}

	public void setPhysicsBody(Body body) {
		m_body = body;
		// 如果是多边形，则保存它们的顶点，用于缩放
		Shape shape = m_body.getFixtureList().getShape();
		if (shape.getType() == ShapeType.POLYGON) {
			m_oldVertices = new Vec2[((PolygonShape)shape).m_count];
			for(int i = 0; i < m_oldVertices.length; ++i){
				m_oldVertices[i] = new Vec2();
				m_oldVertices[i].x = ((PolygonShape)shape).m_vertices[i].x;
				m_oldVertices[i].y = ((PolygonShape)shape).m_vertices[i].y;
			}
		}
	}

	public Body getPhysicsBody() {
		return m_body;
	}

	

	@Override
	public void run() {

		// 同步Body与Sprite的状态
		if (m_body != null) {
			if (m_actionManger.isThereAnyActions()) {
				m_actionManger.runAction();
				// 先让Boyd禁止模拟
				m_body.setActive(false);
				// 位置与角度
				m_body.setTransform(new Vec2(getPosition().x / RATE,
						getPosition().y / RATE), getAngle());
				// 缩放
				scaleBody();
			} else {
				// Action结束后或者没Action时，应该让Boyd启动模拟
				m_body.setActive(true);
				this.setPosition(new Vec2(m_body.getPosition().x * RATE,
						m_body.getPosition().y * RATE ));
				this.setAngle(m_body.getAngle());
			}

		} else {
			m_actionManger.runAction();
		}
	}

	// 对刚体进行缩放
	private void scaleBody() {

		for (Fixture fixture = m_body.getFixtureList(); fixture != null; fixture = fixture
				.getNext()) {
			Shape shape = fixture.getShape();
			if (shape.getType() == ShapeType.CIRCLE) {
				shape.setRadius(getWidth() / 2 / Constant.RATE);
			} else if (shape.getType() == ShapeType.POLYGON) {
				for(int i = 0; i < ((PolygonShape)shape).m_count; ++i){
					((PolygonShape)shape).m_vertices[i].x = m_oldVertices[i].x * getScale();
					((PolygonShape)shape).m_vertices[i].y = m_oldVertices[i].y *getScale();
				}
			}
		
		}
	}

	
	/**
	 * PhysicsSpirte类不应该有父节点,此方法隐藏
	 */
	@Override
	public Node getFatehr() {
		return null;
	}

	/**
	 * PhysicsSpirte类不应该有父节点,此方法隐藏
	 */
	@Override
	public Boolean addToFather(Node father) {
		return false;
	}

	/**
	 * PhysicsSpirte类不应该有父节点,此方法隐藏
	 */
	@Override
	public void removeFromFather() {
	}

	/**
	 * PhysicsSpirte类不应该有父节点,此方法隐藏
	 */
	@Override
	public void updateWithFather() {
	}
	

}
