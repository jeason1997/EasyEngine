/**
 * @ Project : EasyEngine
 * @ File Name : HelloWorldScene.java
 * @ Created : 2015/6/10
 * @ Updated : 2015/6/11
 * @ Author : Jeason1997
 */

package template;

import javax.swing.ImageIcon;

import org.jbox2d.common.Vec2;

import net.xicp.jeason.Engine.base.Layer;
import net.xicp.jeason.Engine.base.Node;
import net.xicp.jeason.Engine.base.Scene;
import net.xicp.jeason.Engine.utils.Constant;

@SuppressWarnings("serial")
public class HelloWorldScene extends Scene {

	public HelloWorldScene(){
		Layer layer = new Layer(0);
		this.addLayer(layer);
		
		ImageIcon background = new ImageIcon(getClass().getResource("/res/HelloWorld.png"));
		Node node = new Node(0, background,
				new Vec2(Constant.SCREEN_WIDTH / 2, Constant.SCREEN_HEIGHT / 2));
		layer.addNode(node);
	}
}
