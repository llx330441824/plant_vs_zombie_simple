package core.plants;

import java.awt.image.BufferedImage;

public class Spikerock extends Plant{

	// 地刺
	// 加载图片 
	private static BufferedImage[] imgs;
	static {
		imgs = new BufferedImage[6];
		for(int i=0;i<imgs.length;i++) {
			imgs[i] = loadImage("Spikerock"+i+".png");
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
	public Spikerock(){
		super(84,80);
		live = 5;
	}




}
