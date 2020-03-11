package core.plants;

import java.awt.image.BufferedImage;

import core.bullets.Bullet;
import core.bullets.PeaBullet;

public class ThreePeater extends Plant implements Shoot{

	// 三头豌豆射手
	// 加载图片 
	private static BufferedImage[] imgs;
	static {
		imgs = new BufferedImage[6];
		for(int i=0;i<imgs.length;i++) {
			imgs[i] = loadImage("ThreePeater"+i+".png");
		}
	}

	// 获取图片，0-4为生存图片，5为卡片图片
	int index = 1;
	public BufferedImage getImage() {
		if(isWait()|| isStop()||isMove()) {
			return imgs[5];
		}else if(isLife()) {
			return imgs[index++%5];//0-4
		}else {
			return null;
		}
	}

	// 构造器
	public ThreePeater() {
		super(73,80);
		live = 7;
	}

	// 发射子弹
	public Bullet[] shoot() {
		Bullet[] bs = new Bullet[3];
		bs[0] = new PeaBullet(this.x+this.width,this.y+this.height/2-5);
		bs[1] = new PeaBullet(this.x+this.width,this.y+this.height/2-20);
		bs[2] = new PeaBullet(this.x+this.width,this.y+this.height/2-35);
		return bs;
	}
	
}
