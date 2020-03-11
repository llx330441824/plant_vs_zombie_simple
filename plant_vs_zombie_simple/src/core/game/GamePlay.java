package core.game;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;

import core.bullets.Bullet;
import core.bullets.SnowBullet;
import core.plants.Blover;
import core.plants.Plant;
import core.plants.Repeater;
import core.plants.Shoot;
import core.plants.SnowPea;
import core.plants.Spikerock;
import core.plants.ThreePeater;
import core.plants.WallNut;
import core.zombies.Award;
import core.zombies.Zombie;
import core.zombies.Zombie0;
import core.zombies.Zombie1;
import core.zombies.Zombie2;
import core.zombies.Zombie3;

/**
 * 植物大战僵尸
 * 游戏内容：
 * 		左边的滚轮机会自动生成植物的卡牌，鼠标单击可以选中植物，
 * 被选中的植物将跟随鼠标移动，再次点击后，可在草地中放置植物。
 * 豌豆射手可以发射豌豆，寒冰射手发射的冰冻豌豆可以使僵尸减速，
 * 三头豌豆射手可以发射三颗豌豆，坚果可以阻止僵尸前进，吹风草
 * 可以将所有的僵尸吹到屏幕外。地刺会持续对僵尸进行攻击。若对
 * 放置的植物不满意，可以使用铲子，将草地上的植物移除。
 * 		不同的僵尸血量不同，移动速度不同。击杀足球僵尸后将获得
 * 随机奖励，奖励类型有：全屏僵尸死亡，全屏僵尸静止两秒等。
 * 		当有僵尸闯入房间，游戏结束。点击图标可重新开始游戏。
 * 
 * @author Lychee
 *
 */


public class GamePlay extends JPanel{
	
	// 游戏窗口大小，游戏状态
	public static final int WIDTH = 1400;
	public static final int HEIGHT = 600;
	public static final int START = 0;
	public static final int RUNNING =1;
	public static final int GAME_OVER =2;
	// 游戏的初始状态
	public static int state = RUNNING;

	// 背景
	private Background start = new Background(800,533,300,50);
	private Background running = new Background(WIDTH,HEIGHT,0,0);
	private Background gameOver = new Background(WIDTH,HEIGHT,0,0);

	// 游戏对象
	// 僵尸集合
	private List<Zombie> zombies = new ArrayList<Zombie>();
	// 植物集合
	// 滚轮机上的植物，状态为stop和wait
	private List<Plant> plants = new ArrayList<Plant>();
	// 战场上的植物，状态为life和move
	private List<Plant> plantsLife = new ArrayList<Plant>();
	// 子弹集合
	private List<Bullet> bullets = new ArrayList<Bullet>();
	// 草地集合
	private List<Glass> glasses = new ArrayList<Glass>();
	// 铲子
	private List<Shovel> shovels = new ArrayList<Shovel>();

	// 铲子入场
	public void shovelEnterAction() {
		// 铲子只有一把
		if(shovels.size()==0) {
			shovels.add(new Shovel());
		}
	}

	// 草地入场
	// 第一块草地的坐标
	int glassX = 260;
	int glassY = 80;
	public void glassEnterAction() {
		for(int i=0;i<9;i++) {
			int x = glassX + i*Glass.WIDTH;
			for(int j=0;j<5;j++) {
				int y = glassY + j*Glass.HEIGHT;
				glasses.add(new Glass(x,y));
			}
		}
	}

	// 检测草地状态
	public void glassCheckAction() {
		// 先遍历所有草地，将状态改为空
		for(Glass g:glasses) {
			g.goEmpty();
			// 遍历所有植物，如果草地上有植物，将草地状态改为被占有
			for(Plant p:plantsLife) {
				if(p.isLife()) {
					int x1 = g.getX();
					int y1 = g.getY();
					int x = p.getX();
					int y = p.getY();
					// 共点问题
					if(x==x1&&y==y1) {						
						g.goHold();						
						break;
					}
				}
			}
		}
	}

	// 生成僵尸
	public Zombie nextOneZombie() {
		Random rand = new Random();
		// 控制不同种类僵尸出现的概率
		int type = rand.nextInt(20);
		if(type<5) {
			return new Zombie0();
		}else if(type<10) {
			return new Zombie1();
		}else if(type<15) {
			return new Zombie2();
		}else {
			return new Zombie3();
		}
	}

	// 僵尸入场
	// 设置进场间隔
	int zombieEnterTime = 0;
	public void zombieEnterAction() {
		zombieEnterTime++;
		if(zombieEnterTime%300==0) {
			zombies.add(nextOneZombie());
		}
	}

	//僵尸移动
	//设置移动间隔
	int zombieStepTime = 0;
	public void zombieStepAction() {
		if(zombieStepTime++%3==0) {
			for(Zombie z:zombies) {
				//只有活着的僵尸会移动
				if(z.isLife()) {
					z.step();
				}
			}
		}
	}

	// 僵尸走到地刺上扣血
	// 设置地刺攻击间隔
	int spikerockHitTime = 0;
	public void zombieMoveToSpikerockAction() {
		if(spikerockHitTime++%20==0) {
			for(Plant p :plantsLife) {
				// 如果植物是地刺类型就去遍历僵尸集合
				if(p instanceof Spikerock) {
					for(Zombie z: zombies) {
						int x1 = p.getX();
						int x2 = p.getX()+p.getWidth();
						int y1 = p.getY();
						int y2 = p.getY()+p.getHeight();
						int x = z.getX();
						int y = z.getY();
						// 如果僵尸在地刺上就扣血
						if(x>x1&&x<x2&&y>y1&&y<y2&&p.isLife()&&(z.isLife()||z.isAttack())) {
							z.loseLive();
						}
					}
				}
			}
		}
	}

	// 僵尸攻击
	// 设置攻击间隔
	int zombieHitTime = 0;
	public void zombieHitAction() {
		if(zombieHitTime++%100==0) {
			for(Zombie z:zombies) {
				// 如果战场上没有植物，则把所有僵尸的状态改为life
				if(!z.isDead()) {
					z.goLife();
				}
				for(Plant p:plantsLife) {
					// 如果僵尸是活的，并且植物是活的，并且僵尸进入攻击植物的范围
					if(z.isLife()&&!p.isDead()&&z.zombieHit(p)&&!(p instanceof Spikerock)) {
						// 僵尸状态改为攻击状态
						z.goAttack();
						// 植物掉血
						p.loseLive();
					}
				}
			}
		}
	}

	// 检测僵尸状态
	public void checkZombieAction() {
		// 迭代器
		Iterator<Zombie> it = zombies.iterator();
		while(it.hasNext()) {
			Zombie z = it.next();
			// 僵尸血量小于0则死亡,死亡的僵尸从集合中删除
			if(z.getLive()<=0) {
				// 判断僵尸是否有奖励的接口
				if(z instanceof Award) {
					Award a = (Award)z;
					int type = a.getAwardType();
					switch(type) {
					case Award.CLEAR:
						for(Zombie zo:zombies) {
							zo.goDead();
						}
						break;
					case Award.STOP:
						for(Zombie zom:zombies) {
							zom.goStop();
							timeStop = 1;
							//zombieGoLife();
						}
						break;
					}
				}
				z.goDead();
				it.remove();
			}
			// 僵尸跑进房子，而游戏生命减一，并删除僵尸
			if(z.OutOfBound()) {
				gameLife--;
				it.remove();
			}
		}
	}

	// 僵尸静止2秒后继续移动
	int timeStop = 1;
	public void zombieGoLife() {
		if(timeStop++%200==0) {
			for(Zombie z:zombies) {
				z.goRun();
			}
		}
	}

	// 检测游戏状态
	// 初始游戏生命值
	int gameLife = 1;
	public void checkGameAction() {
		if(gameLife<=0) {
			state = GAME_OVER;
			// 游戏结束清空所有集合
			plants.clear();
			plantsLife.clear();
			zombies.clear();
			bullets.clear();
			shovels.clear();
		}
	}

	// 生成植物
	public Plant nextOnePlant() {
		Random rand = new Random();
		int type = rand.nextInt(30);
		// 控制植物的出场概率
		if(type<5) {
			return new Repeater();
		}else if(type<10) {
			return new SnowPea();
		}else if(type<15) {
			return new ThreePeater();
		}else if(type<20) {
			return new Spikerock();
		}else if(type<25) {
			return new Blover();
		}else {
			return new WallNut();
		}
	}

	// 植物入场
	// 设置进场间隔
	int plantTime = 0;
	public void plantEnterAction() {
		plantTime++;
		if(plantTime%300==0) {
			// 添加到滚轮机集合中
			plants.add(nextOnePlant());
		}
	}

	// 植物移动
	public void plantStepAction() {
		for(Plant p:plants) {
			// 只有滚轮机集合中的wait状态的植物会移动
			if(p.isWait()) {
				p.step();
			}
		}
	}

	// 植物在滚轮机上的碰撞判定
	public void plantBangAction() {
		// 遍历滚轮机上植物集合，从第二个开始
		for(int i=1;i<plants.size();i++) {
			// 如果第一个植物的y大于0，并且是stop状态，则状态改为wait
			if(plants.get(0).getY()>0&&plants.get(0).isStop()) {
				plants.get(0).goWait();
			}
			// 如果第i个植物y小于i-1个植物的y+height，则说明碰到了，改变i的状态为stop
			if((plants.get(i).isStop()||plants.get(i).isWait())&&
					(plants.get(i-1).isStop()||plants.get(i-1).isWait())&&
					plants.get(i).getY()<=plants.get(i-1).getY()+plants.get(i-1).getHeight()
					) {
				plants.get(i).goStop();
			}
			/*
			 * 如果第i个植物y大于于i-1个植物的y+height，则说明还没碰到或者第i-1个
			 * 植物被移走了，改变i的状态为wait，可以继续往上走
			 */
			if(plants.get(i).isStop()&& 
					plants.get(i).getY()>plants.get(i-1).getY()+plants.get(i-1).getHeight()) {
				plants.get(i).goWait();
			}
		}
	}

	// 检测滚轮机上的植物状态
	public void checkPlantAction1() {
		// 迭代器
		Iterator<Plant> it = plants.iterator();
		while(it.hasNext()) {
			Plant p = it.next();
			/*
			 * 如果滚轮机集合里有move或者life状态的植物
			 * 则添加到战场植物的集合中，并从原数组中删除
			 */
			if(p.isMove()||p.isLife()) {
				plantsLife.add(p);
				it.remove();
			}
		}
	}
	// 检测战场上的植物状态
	public void checkPlantAction2() {
		// 迭代器
		Iterator<Plant> it = plantsLife.iterator();
		while(it.hasNext()) {
			Plant p = it.next();
			// 植物生命小于0死亡，死亡状态的植物从集合中移出
			if(p.getLive()<=0) {
				p.goDead();
				it.remove();
			}
		}
	}
	// 检测吹风草的状态
	int bloverTime = 1;
	public void checkBloverAction() {
		if(bloverTime++%200==0) {
			for(Plant p :plantsLife) {
				if(p instanceof Blover &&p.isLife()) {
					((Blover) p).goClick();
				}
			}
		}
	}

	// 子弹入场
	// 控制子弹进场的间隔时间
	int BulletTime = 0;
	public void BulletShootAction() {
		if(BulletTime++%50==0) {
			for(Plant p : plantsLife) {
				if(p.isLife()) {
					if(p instanceof Shoot) {
						// 如果植物有射击的接口
						Shoot s = (Shoot)p;
						// 射击
						//s.shoot();
						// 把射击的子弹装进子弹数组
						bullets.addAll(Arrays.asList(s.shoot()));
					}
				}
			}
		}
	}

	// 子弹移动
	public void BulletStepAction() {
		for(Bullet b:bullets) {
			b.step();
		}
	}

	// 子弹与僵尸的碰撞
	public void hitAction() {
		// 遍历僵尸和子弹数组
		for(Zombie z:zombies) {
			for(Bullet b:bullets) {
				// 满足条件则僵尸扣血，子弹去死
				if((z.isAttack()||z.isLife())&&b.isLife()&&b.hit(z)&&z.getX()<GamePlay.WIDTH) {
					if(b instanceof SnowBullet) {
						z.goSlowDown();
					}
					z.loseLive();
					b.goDead();
				}
			}
		}
	}

	// 检测子弹状态
	public void bulletCheckAction() {
		Iterator<Bullet> it = bullets.iterator();
		while(it.hasNext()) {
			Bullet b = it.next();
			// 如果子弹死亡或者越界则删除
			if(b.isDead()||b.isOutOfBound()) {
				it.remove();
			}
		}
	}

	// 运行代码
	// 鼠标上是否有植物和铲子的判定
	boolean plantCheck = false;
	boolean shovelCheck = false;
	public void action() {
		// 生成草地
		glassEnterAction();
		// 鼠标的相关操作
		MouseAdapter l = new MouseAdapter() {
			// 鼠标点击事件
			public void mouseClicked(MouseEvent e) {
				// 获得鼠标的坐标
				int Mx = e.getX();
				int My = e.getY();
				//System.out.println(Mx+"-"+My);

				if(state==RUNNING) {
					// 放置植物
					f:for(Plant p:plantsLife) {
						if(p.isMove()&&plantCheck) {
							for(Glass g:glasses) {
								int x1 = g.getX();
								int x2 = g.getX()+g.getWidth();
								int y1 = g.getY();
								int y2 = g.getY()+g.getHeight();	
								if(Mx>x1&&Mx<x2&&My>y1&&My<y2&&g.isEmpty()) {
									p.setX(x1);
									p.setY(y1);
									g.goHold();
									p.goLife();
									plantCheck = false;
									if(p instanceof Blover) {
										bloverTime = 0;
									}
									break f;
								}
							}			
						}
					}
				// 使用铲子
				Iterator<Shovel> it = shovels.iterator();
				Iterator<Plant> it2 = plantsLife.iterator();
				while(it.hasNext()) {
					Shovel s = it.next();
					// 如果铲子是移动状态，就遍历植物集合
					if(s.isMove()) {
						while(it2.hasNext()) {
							Plant p  = it2.next();
							int x1 = p.getX();
							int x2 = p.getX()+p.getWidth();
							int y1 = p.getY();
							int y2 = p.getY()+p.getHeight();
							if((p.isLife()||((Blover) p).isClick())&&Mx>x1&&Mx<x2&&My>y1&&My<y2&&shovelCheck) {
								// 移除植物
								it2.remove();
								// 移除铲子
								it.remove();
								shovelCheck = false;
							}
						}
					}
				}
				// 鼠标单击后，植物将改变状态，随鼠标移动
				for(Plant p:plants) {
					if((p.isStop()||p.isWait())&&!plantCheck&&!shovelCheck) {
						int x1 = p.getX();
						int x2 = p.getX()+p.getWidth();
						int y1 = p.getY();
						int y2 = p.getY()+p.getHeight();						
						if(Mx>=x1&&Mx<=x2&&My>=y1&&My<=y2) {
							p.goMove();
							plantCheck = true;
							break;
						}
					}
				}
				// 铲子被选中后随鼠标移动
				Iterator<Shovel> it3 = shovels.iterator();
				if(plantsLife.size()>0) {
					while(it3.hasNext()) {
						Shovel s = it3.next();
						int x1 = s.getX();
						int x2 = s.getX()+s.getWidth();
						int y1 = s.getY();
						int y2 = s.getY()+s.getHeight();
						if(s.isWait()&&Mx>x1&&Mx<x2&&My>y1&&My<y2&&!plantCheck) {
							s.goMove();
							shovelCheck = true;
						}
					}
				}
				// 点击吹风草吹走僵尸
				for(Plant p:plantsLife) {
					if(p instanceof Blover) {
						int x1 = p.getX();
						int x2 = p.getX()+p.getWidth();
						int y1 = p.getY();
						int y2 = p.getY()+p.getHeight();
						if(((Blover) p).isClick()&&Mx>x1&&Mx<x2&&My>y1&&My<y2
					&&!plantCheck&&!shovelCheck) {
							p.goDead();
							for(Zombie z:zombies) {
								if(z.isAttack()) {
									z.goLife();
								}
								z:for(int i=0;i<10;i++) {
									z.goOut();
									if(z.getX()>=GamePlay.WIDTH-z.getWidth()) {
										z.goRun();
										break z;
									}
								}
							}
						}
					}
				}

				}	
				// 点击按钮开始游戏
				if(state==START) {
					int x1 = 720;
					int x2 = 990;
					int y1 = 210;
					int y2 = 320;
					if(Mx>=x1&&Mx<=x2&&My>=y1&&My<=y2) {
						state = RUNNING;
					}
				}
				// 点击按钮重新开始游戏
				if(state==GAME_OVER) {
					int x1 = 480;
					int x2 = 950;
					int y1 = 100;
					int y2 = 540;
					if(Mx>=x1&&Mx<=x2&&My>=y1&&My<=y2) {
						// 重新开始游戏
						state = START;
						gameLife = 1;
					}
				}
			}

			// 鼠标移动事件
			public void mouseMoved(MouseEvent e) {
				if(state==RUNNING) {
					// 被选中的植物随鼠标移动
					for(Plant p:plantsLife) {	
						if(p.isMove()) {
							int x = e.getX();
							int y = e.getY();
							p.moveTo(x, y);
							break;
						}
					}
					// 被选中的铲子随鼠标移动
					for(Shovel s:shovels) {
						if(s.isMove()) {
							int x = e.getX();
							int y = e.getY();
							s.moveTo(x, y);
							break;
						}
					}
				}
			}
		};
		this.addMouseListener(l);
		this.addMouseMotionListener(l);

		// 定时器
		Timer timer = new Timer();
		int interval = 10;
		timer.schedule(new TimerTask() {
			public void run() {
				if(state==RUNNING) {
					shovelEnterAction();
					zombieEnterAction();
					zombieStepAction();
					zombieMoveToSpikerockAction();
					zombieHitAction();
					plantEnterAction();
					plantStepAction();
					plantBangAction();
					zombieGoLife();
					BulletShootAction();
					BulletStepAction();
					hitAction();
					checkBloverAction();
					checkPlantAction1();
					checkPlantAction2();
					checkZombieAction();
					bulletCheckAction();
					glassCheckAction();
					checkGameAction();
				}
				repaint();
			}
		},interval,interval);
	}

	// 画
	public void paint(Graphics g) {
		// 画背景
		if(state==START) {
			start.paintObject(g);
		}else if(state==RUNNING) {
			running.paintObject(g);
		}else if(state==GAME_OVER) {
			gameOver.paintObject(g);
		}
		// 画植物
		for(Plant p:plants) {
			p.paintObject(g);
		}
		for(Plant p:plantsLife) {
			p.paintObject(g);
		}
		// 画僵尸
		for(Zombie z:zombies) {
			z.paintObject(g);
		}
		// 画子弹
		for(Bullet b:bullets) {
			b.paintObject(g);
		}		
		// 画铲子
		for(Shovel s:shovels) {
			s.paintObject(g);
		}
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		GamePlay game= new GamePlay();
		frame.add(game);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH, HEIGHT);
		frame.setLocationRelativeTo(null); 
		frame.setVisible(true); 

		game.action();
		
		// 启动线程加载音乐
		Runnable r = new zombieAubio("bgm.wav");
		Thread t = new Thread(r);
		t.start();
	}
	
}
