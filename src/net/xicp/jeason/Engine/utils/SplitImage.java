/*************************************************
 * Copyright (c) 2015, JiaYing University
 * All rights reserved.
/*************************************************
 
 
/**
 * @ Project : EasyEngine
 * @ File Name : SplitImage.java
 * @ Created : 2015/6/3
 * @ Updated : 2015/6/10
 * @ Author : Jeason1997
 */

package net.xicp.jeason.Engine.utils;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public class SplitImage {

	private static SplitImage mSplitImage;

	public static SplitImage getInstance() {
		if (mSplitImage == null) {
			mSplitImage = new SplitImage();
		}
		return mSplitImage;
	}

	private SplitImage() {
	}

	public ImageIcon splitImage(ImageIcon img, int x, int y, int width,
			int height) {
		BufferedImage sourceImg = new BufferedImage(img.getIconWidth(), img.getIconHeight(),
				BufferedImage.TYPE_INT_ARGB);
		sourceImg.getGraphics().drawImage(img.getImage(), 0, 0, null);
		if (x + width > sourceImg.getWidth() || y + height > sourceImg.getHeight()) {
			return null;
		} else {
			BufferedImage newImg = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_ARGB);
			newImg.createGraphics().drawImage(sourceImg, 0, 0, width, height,
					x, y, x + width, y + height, null);
			return new ImageIcon(newImg);
		}
	}
}
