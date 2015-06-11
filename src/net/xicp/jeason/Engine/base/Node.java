/*************************************************
 * Copyright (c) 2015, JiaYing University
 * All rights reserved.
/*************************************************
 
 
/**
 * @ Project : EasyEngine
 * @ File Name : Node.java
 * @ Created : 2015/6/3
 * @ Updated : 2015/6/10
 * @ Author : Jeason1997
 */

/**
 * @ 待解决问题 : setVisible后会影响子节点的可视度
 */


package net.xicp.jeason.Engine.base;

import java.awt.Image;
import java.util.Vector;

import javax.swing.ImageIcon;

import net.xicp.jeason.Engine.action.ActionManger;
import net.xicp.jeason.Engine.collision.BoundingBox;
import net.xicp.jeason.Engine.utils.Enum.MouseType;

import org.jbox2d.common.Vec2;


public class Node {
	
	public enum NodeType{
		RECT,
		CIRCLE
	}
	
	protected Layer m_layer;						// 此Node所在的Layer
	protected NodeType m_nodeType = NodeType.RECT;	// Node的类型
	protected Node m_fatherNode = null;				// 父节点
	protected Vector<Node> m_childNodes = new Vector<Node>();
													// 子节点
	protected int m_drawOrder = 0;					// 绘画级别，级别越低，越先绘画
	protected BoundingBox m_bouBoundingBox = null;	// Node的边界包围盒
	protected Boolean m_collisionEnable = false;	// 是否允许碰撞检测
	protected Boolean m_touchEnable = false;		// 是否允许被鼠标点击
	protected Boolean m_isVisible = true;			// 是否可见
	protected Vec2 m_anchor = new Vec2(0.5f, 0.5f);	// 锚点
	
	/*
	 * 锚点、坐标的关系：
	 * 坐标即Node的中心在Scene里的位置，而中心点并不是Node图片的中心，这里的中心就是Node的锚点，可以理解为把Node通过
	 * 这一点钉在了Scene上，此后Node的缩放，旋转都是绕着这一点进行的。
	 */
	// 这几个状态的改变会引起包围盒的变化，因此不允许子类直接修改，必须通过set修改
	private Vec2 m_position = null;					// 坐标
	private Vec2 m_relativePosition = new Vec2(0, 0);
													// 相对于父节点的坐标
	private Vec2 m_fatherPositon = new Vec2(0, 0);	// 父节点的位置
	
	private float m_scale = 1.0f;					// Node的缩放级别
	private float m_relativeScale = 1.0f;			// 相对父节点的缩放级别
	private float m_fatherScale = 1.0f;				// 父节点的缩放级别
	
	private float m_angle = 0;
	private float m_relativeAngle = 0;				// 相对父节点的角度
	private float m_fatherAngle = 0;				// 父节点的角度
	
	private float m_width = 0;						// Node的大小(不是实际图片大小)
	private float m_height = 0;
	
	private ImageIcon m_image = null;				// 与Node绑定的图像
	
	protected ActionManger m_actionManger = null;	// 每一个Node都有一个单独的动作管理器，在Node的线程里调用它
	
	public Node(int order, ImageIcon img, Vec2 position){
		this.m_drawOrder = order;
		this.m_image = img;
		this.m_position = position;
		this.m_actionManger = new ActionManger(this);
		updateState();
	}
	
	/**
	 * 设置Node的能见度，只是关系到是否渲染出来而已。
	 * 父节点不可见后，子节点也不可见（待修复）
	 * @param bool
	 */
	public void setVisible(Boolean bool){
		this.m_isVisible = bool;
	}
	
	public Boolean getVisible(){
		return m_isVisible;
	}
	
	/**
	 * @return 返回此Node所在的Layer
	 */
	public Layer getLayer(){
		return m_layer;
	}
	
	/**
	 * 附加到Layer
	 * @param layer
	 */
	public void addToLayer(Layer layer){
		this.m_layer = layer;
	}
	
	public Node getFatehr(){
		return m_fatherNode;
	}
	
	/**
	 * 附加到父节点
	 * @param father
	 * @return
	 */
	public Boolean addToFather(Node father){
		if(father != null){
			m_fatherNode = father;
			// 附件到父节点后，当前节点相对父节点的位置就是当前节点之前相对于Scene的位置
			m_relativePosition = m_position;
			m_layer = father.getLayer();
			return true;
		}
		return false;
	}
	
	public Vector<Node> getchildren(){
		return m_childNodes;
	}
	
	public void addChild(Node child){
		
		if(child != null){
			int newNodeIndex = compareOrder(child.getDrawOrder());
			m_childNodes.insertElementAt(child, newNodeIndex);
			child.addToFather(this);
		}
	}
	
	public void removeChild(Node child){
		if(m_childNodes.contains(child)){
			child.removeFromFather();
		}
	}
	
	public void removeAllChidren(){
		for(Node child : m_childNodes){
			child.removeFromFather();
		}
		m_childNodes.clear();
	}
	
	public void removeFromFather(){
		m_fatherNode = null;
		m_relativePosition = new Vec2(0, 0);
		m_fatherPositon = new Vec2(0, 0);
		m_relativeScale = 1.0f;
		m_fatherScale = 1.0f;
		m_relativeAngle = 0;
		m_fatherAngle = 0;
	}
	
	public ActionManger getActionManger() {
		return m_actionManger;
	}
	
	public void setNodeType(NodeType type) {
		m_nodeType = type;
	}
	
	public NodeType getNodeType(){
		return m_nodeType;
	}

	public float getWidth() {
		return m_width;
	}
	
	public float getHeight() {
		return m_height;
	}
	
	public void setAnchor(Vec2 anchor){
		this.m_anchor = anchor;
	}
	
	public Vec2 getAnchor(){
		return this.m_anchor;
	}
	
	public float getScale() {
		return m_scale;
	}

	public void setScale(float scale) {
		// 当前节点的缩放级别为本节点的Scale乘父节点的Scale	setScale();
		if(m_fatherNode == null){
			this.m_scale = scale;
		} else {
			this.m_relativeScale = scale;
			this.m_scale = m_relativeScale * m_fatherScale;
		}
		
		updateState();
	}

	public int getDrawOrder() {
		return m_drawOrder;
	}

	public void setDrawOrder(int m_drawOrder) {
		this.m_drawOrder = m_drawOrder;
	}

	public Image getImage() {
		return m_image.getImage();
	}
	
	public ImageIcon getImageIcon(){
		return m_image;
	}

	public void setImageIcon(ImageIcon m_image) {
		this.m_image = m_image;
		updateState();
	}

	public Vec2 getPosition() {
		return m_position;
	}
	
	public Vec2 getRelativePosition() {
		return m_relativePosition;
	}
	
	public float getRelativeScale() {
		return m_relativeScale;
	}
	
	public float getRelativeAngle() {
		return m_relativeAngle;
	}

	public void setPosition(Vec2 position) {
		// 当前节点的位置应该是以父节点的中心点为原点进行变化的
		if(m_fatherNode == null){
			this.m_position = position;
		} else {
			this.m_relativePosition = position;
			this.m_position = new Vec2(m_fatherPositon.x + m_relativePosition.x,
					m_fatherPositon.y + m_relativePosition.y);
		}
		
		updateState();
	}
	
	public float getAngle() {
		return m_angle;
	}

	public void setAngle(float angle) {
		// 当前节点的角度应该是以父节点的角度为基础进行变化的	setAngle();
		if(m_fatherNode == null){
			this.m_angle = angle;
		} else {
			this.m_relativeAngle = angle;
			this.m_angle = m_relativeAngle + m_fatherAngle;
		}
		
		updateState();
	}
	
	public BoundingBox getBouBoundingBox() {
		return m_bouBoundingBox;
	}
	
	/**
	 * 是否允许碰撞检测
	 * @return
	 */
	public Boolean collisionEnable() {
		return m_collisionEnable;
	}
	
	public void setCollisionEnable(Boolean enable) {
		this.m_collisionEnable = enable;
	}
	
	/**
	 * 是否允许响应鼠标事件
	 * @return
	 */
	public Boolean touchEnable(){
		return m_touchEnable;
	}
	
	public void setTouchEnable(Boolean enable){
		this.m_touchEnable = enable;
	}
	
	
	/**
	 * 每当改变Sprite的图片、缩放因子、角度、位置时，应该相对应的更新Sprite的包围盒，以及长宽
	 */
	public void updateState(){
		this.m_height = m_image.getIconHeight() * m_scale;
		this.m_width = m_image.getIconWidth() * m_scale;
		this.m_bouBoundingBox = new BoundingBox(this);
	}
	
	/**
	 * 每一帧中，子节点应该伴随着父节点的状态改变而改变
	 */
	public void updateWithFather(){
		setPosition(m_relativePosition);
		setAngle(m_relativeAngle);
		setScale(m_relativeScale);
	}
	
	public void mouseEvent(MouseType type){
	}
	
	// 比较新增加的Node的层次位置，以决定该Node的插入位置
	private int compareOrder(int order){
		for(int index = 0; index < m_childNodes.size(); ++index){
			if(m_childNodes.get(index).getDrawOrder() > order){
				return index;
			}
		}
		return m_childNodes.size();
	}

	public void run() {
		// 更新父节点的中心点以及角度
		if(m_fatherNode != null){
			m_fatherPositon = m_fatherNode.getPosition();
			m_fatherAngle = m_fatherNode.getAngle();
			m_fatherScale = m_fatherNode.getScale();
			
			updateWithFather();
		}
		// Do action
		m_actionManger.runAction();
	}
}
