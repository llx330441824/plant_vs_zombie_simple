package core.zombies;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.imageio.ImageIO;
import core.game.GamePlay;
import core.plants.Plant;



public abstract class Zombie {
	
	// 僵尸的超类
	// 加载img图片
	public static BufferedImage loadImage(String fileName) {
		try {
			BufferedImage img = ImageIO.read(Zombie.class.getResource(fileName));
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
	
	// 对象的状态
	public static final int LIFE = 0;
	public static final int ATTACK = 1;
	public static final int DEAD = 2;
	protected int state = LIFE;
	
	// 判断僵尸的状态
	public boolean isLife() {
		return state == LIFE;
	}
	public boolean isAttack() {
		return state == ATTACK;
	}
	public boolean isDead() {
		return state == DEAD;
	}

	// 僵尸状态改变
	public void goDead() {
		state = DEAD;
	}
	public void goAttack() {
		state = ATTACK;
	}
	public void goLife() {
		state = LIFE;
	}
	
	// 僵尸速度改为0
	public void goStop() {
		xSpeed = 0;
	}
	public int getSpeed() {
		return xSpeed;
	}
	
	// 僵尸的停止状态结束
	public abstract void goRun();
	
	
	// 设置属性
	protected int width;
	protected int height;
	protected int live;
	protected int xSpeed;
	protected int x;
	protected int y;
	
	// 构造器
	public Zombie(int width,int height) {
		this.width = width;
		this.height = height;
		Random rand = new Random();
		this.x = GamePlay.WIDTH-100;
		this.y = rand.nextInt(GamePlay.HEIGHT-this.height);
	}
	
	// 获取宽高和x,y
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	
	// 获取僵尸的生命
	public int getLive() {
		return live;
	}
	
	// 被子弹击中掉血
	public void loseLive() {
		live--;
	}
	
	// 移动方式
	public abstract void step();
	
	// 僵尸被寒冰豌豆集中后减速
	public void goSlowDown() {
		xSpeed = 1;
	}
	
	// 僵尸被吹风草吹走
	public void goOut() {
		xSpeed-=2;
	}
	
	// 僵尸攻击植物
	public boolean zombieHit(Plant p) {
		int x1 = this.x-p.getWidth();
		int x2 = this.x+this.width;
		int y1 = this.y-p.getHeight();
		int y2 = this.y+this.width;
		int x = p.getX();
		int y = p.getY();
		return x>=x1 && x<=x2 && y>=y1 && y<=y2;
	}
	
	// 僵尸是否越界
	public boolean OutOfBound() {
		return this.x<=0;
	}
	
}
