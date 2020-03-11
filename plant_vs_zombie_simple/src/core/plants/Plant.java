package core.plants;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import core.game.Background;
import core.game.GamePlay;

public abstract class Plant {
	
	// 植物的超类
	// 加载图片
	public static BufferedImage loadImage(String fileName) {
		try {
			BufferedImage img = ImageIO.read(Plant.class.getResource(fileName));
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
		
	// 植物的状态
	// 在滚轮机上的状态
	public static final int WAIT = 0;
	// 在滚轮机上停止的状态
	public static final int STOP = 1;
	// 被鼠标点击后随着鼠标移动
	public static final int MOVE = 2;
	public static final int LIFE = 3;
	public static final int DEAD = 4;
	protected int state = WAIT;
	
	// 设置植物的状态
	public void setState(int state) {
		this.state = state;
	}
	
	// 判断植物的状态
	public boolean isWait() {
		return state == WAIT;
	}
	public boolean isStop() {
		return state == STOP;
	}
	public boolean isMove() {
		return state == MOVE;
	}
	public boolean isLife() {
		return state == LIFE;
	}
	public boolean isDead() {
		return state == DEAD;
	}
	
	// 植物状态改变
	public void goMove() {
		state = MOVE;
	}
	public void goLife() {
		state = LIFE;
	}
	public void goStop() {
		state = STOP;
	}
	public void goWait() {
		state = WAIT;
	}
	public void goDead() {
		state = DEAD;
	}
	
	// 设置属性
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	protected int live;
	protected int ySpeed;
	
	// 构造器
	public Plant(int width,int height) {
		// 初始的x,y为固定值，在滚轮机初始位置
		this.width = width;
		this.height = height;
		// 初始在屏幕的左下角
		this.x = 0;
		this.y = GamePlay.HEIGHT;
		ySpeed = 1;
	}
	
	// 获取宽高,x,y
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	
	// 设置x,y
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	// 获取植物的血量
	public int getLive() {
		return this.live;
	}
	
	// 植物被攻击扣血
	public void loseLive() {
		this.live--;
	}
	
	// 植物被选中后移动  x,y为鼠标的坐标
	public void moveTo(int x,int y) {
		this.x = x-this.width/2;
		this.y = y-this.height/2;
	}
	
	// 植物在等待状态时移动
	public void step() {
		y-=ySpeed;
		if(y<=0) {
			state = STOP;
		}
	}
	
}
