package core.game;

public class Glass {
	
	// 草地
	// 基础属性
	private int x;
	private int y;
	public static final int WIDTH = 80;
	public static final int HEIGHT = 100;
	
	// 构造器
	public Glass(int x,int y) {
		this.x = x;
		this.y = y;
	}
	
	// 获取x,y,宽,高
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getWidth() {
		return WIDTH;
	}
	
	public int getHeight() {
		return HEIGHT;
	}

	// 草地的状态
	private static final int EMPTY = 0;
	private static final int HOLD = 1;
	private int state = EMPTY;

	// 判断状态
	public boolean isEmpty() {
		return state==EMPTY;
	}
	public boolean isHold() {
		return state==HOLD;
	}
	
	// 改变状态
	public void goEmpty() {
		state = EMPTY;
	}
	public void goHold() {
		state = HOLD;
	}
	
}
