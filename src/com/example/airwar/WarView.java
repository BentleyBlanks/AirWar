package com.example.airwar;

import java.util.Vector;

import org.apache.http.auth.NTCredentials;

import android.R.drawable;
import android.R.integer;
import android.R.plurals;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class WarView extends SurfaceView implements Callback,Runnable
{
	//画笔
	private Paint paint;
    //画布
	private Canvas canvas;
	//屏幕宽高
	public static int  screenH,screenW;
	//线程开始与否标记物
	private boolean flag=false;
	//得到监视SufaceView的控制器
	private SurfaceHolder holder;

	//飞机时时位置
	private int mapX,mapY=900;
	//敌机与子弹计数器
	private long count_1,count_2,count_3,count_4, countBullet;
	//移动物件线程
	private Thread th;
	//游戏状态参数
	public final static int GAME_MENU=0;
	public final static int GAMING=1;
	public final static int GAME_OVER=2;
	public final static int GAME_WIN=3;
	//计时器
	public long timeStart,now;
	//游戏难度
	private int difficulty=1;
	//游戏状态
	public static int gameState=GAME_MENU;
	//得到资源文件res去初始化位图文件	
	private Resources res=this.getResources();	
	
	private Bitmap plane;	//飞机图片
	private Bitmap general;  //敌机1图片
	private Bitmap ordinary;  //敌机2图片
	private Bitmap particular;  //敌机3图片
	private Bitmap boss;  //Boss图片
	private Bitmap bullet;  //子弹图片
	private Bitmap button;  //按钮图片
	private Bitmap buttonpressed;  //按钮被按下的图片
	private Bitmap menu;  //菜单的背景图片
	private Bitmap backgroundGame;  //菜单的背景图片
	private Bitmap backgroundGameOver;  //游戏结束的背景图片
	
	//三组向量组敌人
	private Vector<Enermy> vectorEnermy;
	//敌机随机坐标
	private int enermyX,enermyY;
	//向量组子弹
	private Vector<Bullets> vectorBullet;
	//子弹坐标
	private int bulletX,bulletY;
	//记录歼灭敌人的数量
	private int deadNumberOfEnermy=0;
	//开始菜单
	public Menu startMenu,winMenu;
	//游戏背景 游戏背景 游戏结束背景
	private BackGround background,gameover;  
	//主角飞机
	private Protagonist player;
	//Boss
	private Boss bossEnermy;
	//Boss是否出现
	private boolean isBoss=false;
	
	public WarView(Context context)
	{
		super(context);
		//设置画笔
		paint=new Paint();
		paint.setColor(Color.WHITE);
		paint.setTextSize(20);
		//画笔设置为抗锯齿
		paint.setAntiAlias(true);
		//实例监视器
		holder=this.getHolder();
		//添加必要的回调函数以确定SurfaceView何时销毁
		holder.addCallback(this);
		//SurfaceView获得焦点
		setFocusable(true);	
		//线程可以开始运作
	    flag=true;
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0)
	{
		// TODO Auto-generated method stub
		//获得当前屏幕宽高
		screenW=this.getWidth();
		screenH=this.getHeight();
		//初始化敌机和子弹 
		init();
		//拿this对象实例化物件移动线程
		th=new Thread(this);
		th.start();
	}

	public void init()
	{		
		//当游戏状态处于菜单时 才重置游戏
		if(gameState==GAME_MENU)
		{
			//将png图片文件解码转为bmp位图显示
			plane=BitmapFactory.decodeResource(res, R.drawable.airplane);
			general=BitmapFactory.decodeResource(res, R.drawable.general);
			ordinary=BitmapFactory.decodeResource(res, R.drawable.ordinary);
			particular=BitmapFactory.decodeResource(res, R.drawable.particular);
			bullet=BitmapFactory.decodeResource(res, R.drawable.bullet);
			menu=BitmapFactory.decodeResource(res, R.drawable.menu);
			button=BitmapFactory.decodeResource(res, R.drawable.button);
			buttonpressed=BitmapFactory.decodeResource(res, R.drawable.buttonpressed);
			backgroundGame=BitmapFactory.decodeResource(res, R.drawable.background);
			boss=BitmapFactory.decodeResource(res, R.drawable.boss);
			backgroundGameOver=BitmapFactory.decodeResource(res, R.drawable.gameover);
			
			//为主角添加实例 并设定初值
			player=new Protagonist(plane, 0, 800);
			//为游戏背景添加实例
			background=new BackGround(backgroundGame);
			//为菜单添加实例
			startMenu=new Menu(menu, button, buttonpressed);
			//为赢得游戏添加实例
			//winMenu=new Menu(menu, button, buttonpressed);
			//为敌机向量组添加实例
			vectorEnermy=new Vector<Enermy>();
			//为子弹向量组添加实例
			vectorBullet=new Vector<Bullets>();
			//为游戏结束背景添加实例
			gameover=new BackGround(backgroundGameOver);
			//为Boss添加实例
			bossEnermy=new Boss(boss, screenW/2, 0);
		}
	}
	
	//绘制方法
	private void drawGame()
	{
		try
		{		
			//等到一次绘制结束才能进行下一次
			canvas=holder.lockCanvas();
			if (canvas != null) 
			{
				switch (gameState)
				{
				//绘制菜单
				case GAME_MENU:
					//绘制菜单背景
					startMenu.draw(canvas,paint);
					break;
				//游戏更新为开始
				case GAMING:
					//绘制游戏背景
					background.draw(canvas, paint);
					//绘制主角
					player.draw(canvas,paint);
					//绘制主角血量和积分
					player.drawText(canvas, paint);
					//绘制难度系数
					canvas.drawText("难度"+difficulty, 0, 50, paint);
					//绘制敌机
					for(int i=0;i<vectorEnermy.size();i++)
					{
						vectorEnermy.elementAt(i).draw(canvas, paint);
					}
					//绘制主角的子弹
					for(int i=0;i<vectorBullet.size();i++)
					{
						vectorBullet.elementAt(i).draw(canvas, paint);
					}

					if(isBoss)
					{
						//绘制Boss
						bossEnermy.draw(canvas, paint);
						canvas.drawText("Boss生成！", screenW/2, 50, paint);
					}
					break;
				case GAME_OVER:
					//绘制游戏结束画面
					gameover.draw(canvas, paint);
					//绘制Gameover
					canvas.drawText("Game Over", screenW/2, screenH/2, paint);
					break;
				case GAME_WIN:
					//winMenu.draw(canvas, paint);
					//游戏结束
					gameover.draw(canvas, paint);
					Paint paint_2=new Paint();
					paint_2.setTextSize(40);
					paint_2.setColor(Color.YELLOW);
					paint_2.setAntiAlias(true);
					canvas.drawText("你赢啦!!", screenW/2, screenH/2, paint_2);
					canvas.drawText("得分"+player.score,screenW/2,screenH/2-50,paint_2);
					break;
				}
			}
		}
		catch (Exception e){}
		finally
		{
			if (canvas != null) 
			{
				//在绘制结束后进行解锁画布
				holder.unlockCanvasAndPost(canvas);
			}
		}
	}
	
	//游戏逻辑
	private void logic()
    {
		switch (gameState)
		{
		case GAME_MENU:
			break;
		case GAMING:
			//若Boss已经生成
			if(isBoss)
			{
				//为Boss检测碰撞
				if(Collision(bossEnermy.x, bossEnermy.y, boss.getWidth(), boss.getHeight(), 
						mapX-plane.getWidth()/2, mapY-plane.getHeight()/2, plane.getWidth(), plane.getHeight()))
				{
					//若发生飞机与Boss碰撞就掉血
					if(player.planeHealth>50)
					{
						player.planeHealth-=50;
						//bossEnermy.boosHealth-=50;
					}
					else 
					{
						//若飞机的血量不足以扣除20
						player.planeHealth=0;
						//游戏结束
						gameState=GAME_OVER;
					}
				}
				//Boss逻辑
				bossEnermy.logic();
			}

			if(difficulty>=1&&difficulty<3)
			{
				count_1++;
				if(count_1%30==0)
				{		
					//生成随机坐标
					enermyX=(int)(Math.random()*screenW*0.9);
					enermyY=0;
					//生成随机敌机
					int index=(int)(Math.random()*3);
					//为敌机向量组添加随机1 2号敌人
					switch (index)
					{
					case Enermy.GENERAL:
						vectorEnermy.addElement(new Enermy(general, enermyX, enermyY, index));
						break;
					case Enermy.ORDINARY:
						vectorEnermy.addElement(new Enermy(ordinary, enermyX, enermyY, index));
						break;
					}
				}
			}
			else if(difficulty>=3&&difficulty<5)
			{
				count_2++;
				if(count_2%20==0)
				{		
					//生成随机坐标
					enermyX=(int)(Math.random()*screenW*0.9);
					enermyY=0;
					//生成随机敌机
					int index=(int)(Math.random()*3+1);
					//为敌机向量组添加随机1 2号敌人
					switch (index)
					{
					case Enermy.GENERAL:
						vectorEnermy.addElement(new Enermy(general, enermyX, enermyY, index));
						break;
					case Enermy.ORDINARY:
						vectorEnermy.addElement(new Enermy(ordinary, enermyX, enermyY, index));
						break;
					case Enermy.PARTICULAR:
						vectorEnermy.addElement(new Enermy(particular, enermyX, enermyY, index));
						break;
					}
				}
			}
			else if(difficulty>=5&&difficulty<8)
			{
				count_3++;
				if(count_3%25==0)
				{		
					//生成随机坐标
					enermyX=(int)(Math.random()*screenW*0.9);
					enermyY=0;
					//生成随机敌机
					int index=(int)(Math.random()*2+2);
					//为敌机向量组添加随机1 2号敌人
					switch (index)
					{
					case Enermy.GENERAL:
						vectorEnermy.addElement(new Enermy(general, enermyX, enermyY, index));
						break;
					case Enermy.ORDINARY:
						vectorEnermy.addElement(new Enermy(ordinary, enermyX, enermyY, index));
						break;
					case Enermy.PARTICULAR:
						vectorEnermy.addElement(new Enermy(particular, enermyX, enermyY, index));
						break;
					}
				}
				//生成Boss
				isBoss=true;
			}
			else if(difficulty>=8)
			{
				count_4++;
				if(count_4%17==0)
				{
					//生成随机坐标
					enermyX=(int)(Math.random()*screenW*0.93);
					enermyY=0;
					int index=(int)(Math.random()*3+1);
					switch (index)
					{
					case Enermy.ORDINARY:
						vectorEnermy.addElement(new Enermy(ordinary, enermyX, enermyY, index));
						break;
					case Enermy.PARTICULAR:
						vectorEnermy.addElement(new Enermy(particular, enermyX, enermyY, index));
						break;
					default:
						break;
					}
				}
			}
			
			//敌机逻辑
			for(int i=0;i<vectorEnermy.size();i++)
			{
				Enermy e=vectorEnermy.elementAt(i);
				//若判定已死亡则从向量组中移除
				if(e.isDead)
				{
					vectorEnermy.remove(e);
				}
				else if(e.y>=screenH)
				{
					//超过了边界就消除
					vectorEnermy.remove(e);
				}
				else if(Collision(e.x,e.y, e.enermyWidth, e.enermyHeight,mapX-plane.getWidth()/2, mapY-plane.getHeight()/2, player.planeWidth, player.planeHeight))
				{
					//若发生飞机与敌机碰撞就掉血
					if(player.planeHealth>20)
					{
						player.planeHealth-=20;
					}
					else 
					{
						//若飞机的血量不足以扣除20
						player.planeHealth=0;
						//游戏结束
						gameState=GAME_OVER;
					}
					//敌机掉血
					e.enermyHealth-=20;
				}
				else 
				{
					e.logic();
				}
			}
			
			countBullet++;
			if(countBullet%10==0)
			{
				bulletX=mapX;
				bulletY=mapY-player.planeHeight/2;
				//添加子弹
				vectorBullet.addElement(new Bullets(bullet, bulletX, bulletY));
			}
			
			//子弹逻辑
			for(int i=0;i<vectorBullet.size();i++)
			{
				Bullets b=vectorBullet.elementAt(i);
				//若判定已死亡则从向量组中移除
				if(b.isDead)
				{
					//若此处是i则会发生崩溃
					vectorEnermy.remove(b);
				}
				else if(b.y<0)
				{
					vectorBullet.remove(b);
				}
				else 
				{
					b.logic();
				}
			}
			
			//两重循环检测碰撞
			for(int i=0;i<vectorBullet.size();i++)
			{
				Bullets b=vectorBullet.elementAt(i);
				//子弹碰撞检测
				for(int j=0;j<vectorEnermy.size();j++)
				{
					Enermy e=vectorEnermy.elementAt(j);
					if(Collision(b.x, b.y, b.bulletWidth, b.bulletHeight, e.x, e.y, e.enermyWidth, e.enermyHeight))
					{
						b.isDead=true;
						vectorBullet.remove(b);
						//一颗子弹扣除一百点血量
						if(e.enermyHealth>100)
						{
							e.enermyHealth-=100;
						}
						else 
						{
							e.enermyHealth=0;
							//更新敌人状态			
							e.isDead=true;
							//积分增加20
							player.score+=20;	
							deadNumberOfEnermy++;
						}
					}
				}
				//Boss出现检测与子弹的碰撞
				if(isBoss)
				{
					//若子弹射击到了Boss上
					if(Collision(bossEnermy.x, bossEnermy.y, boss.getWidth(), boss.getHeight(), b.x, b.y, b.bulletWidth, b.bulletHeight))
					{
						if(bossEnermy.boosHealth<50)
						{
							//更新Boss状态为死亡
							bossEnermy.boosHealth=0;
							bossEnermy.isDead=true;
							//更新游戏状态
							gameState=GAME_WIN;
						}
						else 
						{
							bossEnermy.boosHealth-=50;
						}
						
						b.isDead=true;
						//将子弹直接消除
						vectorBullet.remove(b);
					}
				}
			}
			//如果歼灭了七个以上的敌人就难度升级
			if(deadNumberOfEnermy>10)
			{
				//难度升高
				difficulty++;
				//置为默认0
				deadNumberOfEnermy=0;
			}
			break;
		case GAME_OVER:
			break;
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0)
	{
		// TODO Auto-generated method stub
		flag=false;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{			
		mapX=(int) event.getX();
		mapY=(int) event.getY();	
		bulletX=mapX;
	    bulletY=mapY-25;
	    //更新飞机位置
	    player.update(mapX-plane.getWidth()/2,mapY-plane.getHeight()/2);
	    
		switch (gameState)
		{
		case GAME_MENU:
			//为菜单的触摸传递事件
			startMenu.onTouchEvent(event);
			break;
		case GAMING:
			break;
		case GAME_OVER:
			break;
		}
		return true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		// TODO Auto-generated method stub
		//如是按下了返回按键
		if(keyCode==KeyEvent.KEYCODE_BACK)
		{
			if(gameState==GAMING||gameState==GAME_OVER)
			{
				//无论是游戏菜单界面或者是游戏中界面或者游戏结束都回到菜单
				gameState=GAME_MENU;
				//重置游戏
				init();
			}
			else if(gameState==GAME_MENU)
			{
				//结束当前
				MainActivity.instance.finish();
				System.exit(0);
			}
			//返回按键已处理 不再交给系统处理 避免游戏被切入后台
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}


	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event)
	{
		// TODO Auto-generated method stub
		//如是按下了返回按键
		if(keyCode==KeyEvent.KEYCODE_BACK)
		{
			if(gameState==GAMING||gameState==GAME_OVER)
			{
				//无论是游戏菜单界面或者是游戏中界面或者游戏结束都回到菜单
				gameState=GAME_MENU;
			}
			return true;
		}

		//return super.onKeyDown(keyCode, event);
		return super.onKeyUp(keyCode, event);
	}

	//XY为坐标 WT为宽高
	public boolean Collision(int x1,int y1,int w1,int t1,int x2,int y2,int w2,int t2)
	{
		if(x1>=x2&&x1>=x2+w2)
		{
			return false;
		}
		else if(x1<x2&&x1+w1<x2)
		{
			return false;
		}
		else if(y1<y2&&y1+t1<y2)
		{
			return false;
		}
		else if(y1>y2&&y1>y2+t2)
		{
			return false;
		}
		else 
		{
			//大多数情况为未碰撞 因此可以节约判别时间
			return true;
		}
	}
	
	@Override
	public void run()
	{
		long timestart=System.currentTimeMillis();
		while(flag)
		{			    	
			drawGame();
			logic();
		}
	}
}
