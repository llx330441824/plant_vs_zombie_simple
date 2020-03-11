package core.bullets;

import java.awt.image.BufferedImage;

public class PeaBullet extends Bullet{
	
	// 豌豆子弹
	// 加载图片
	private static BufferedImage imgs[];
	static {
		imgs = new BufferedImage[4];
		for(int i=0;i<imgs.length;i++) {
			imgs[i] = loadImage("PeaBullet"+i+".png");
		}
	}
	
	// 获取图片,0-3为子弹旋转移动
	int index = 0;
	public BufferedImage getImage() {
		if(isLife()) {
			return imgs[index++%4];//0-3
		}else {
			return null;
		}
	}
	
	// 构造器
	public PeaBullet(int x,int y) {
		super(x,y,24,24);
		xSpeed = 3;
	}

}
