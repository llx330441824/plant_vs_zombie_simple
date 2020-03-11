package core.bullets;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import core.game.GamePlay;
import core.zombies.Zombie;


public abstract class Bullet {

	// 子弹超类
	// 加载图片
	public static BufferedImage loadImage(String fileName) {
		try {
			BufferedImage img = ImageIO.read(Bullet.class.getResource(fileName));
			return img;
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	// 获取图片
	public abstract BufferedImage getImage();

	// 画图片
	public void paintObject(Graphics g) {
		g.drawImage(getImage(),x,y,null);                                   
	}

	// 基本属性
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	protected int xSpeed;

	// 获取x、y、宽高
	public int getX() {
		return this.x;
	}
	public int getY() {
		return this.y;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}

	// 构造器
	public Bullet(int x,int y,int width,int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	// 子弹的状态
	public static final int LIFE = 0;
	public static final int DEAD = 1;
	protected int state = LIFE;

	// 判断子弹的状态
	public boolean isLife() {
		return state == LIFE;
	}
	public boolean isDead() {
		return state == DEAD;
	}

	// 子弹的移动
	public void step() {
		x+=xSpeed;
	}

	// 子弹消失
	public void goDead() {
		state = DEAD;
	}

	// 子弹与僵尸碰撞
	public boolean hit(Zombie z) {
		int x1 = this.x-z.getWidth();
		int x2 = this.x+this.width;
		int y1 = this.y-z.getHeight();
		int y2 = this.y+this.height;
		int x = z.getX();
		int y = z.getY();
		return x>=x1 && x<=x2 && y>=y1 && y<=y2;
	}

	// 子弹是否越界
	public boolean isOutOfBound() {
		return this.x>=GamePlay.WIDTH;
	}

}
