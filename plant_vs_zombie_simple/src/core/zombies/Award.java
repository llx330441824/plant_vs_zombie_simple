package core.zombies;

public interface Award {
	
	// 奖励接口
	public static final int CLEAR = 0;
	public static final int STOP = 1;
	public abstract int getAwardType();
}
