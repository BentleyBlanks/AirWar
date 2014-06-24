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
	//����
	private Paint paint;
    //����
	private Canvas canvas;
	//��Ļ���
	public static int  screenH,screenW;
	//�߳̿�ʼ�������
	private boolean flag=false;
	//�õ�����SufaceView�Ŀ�����
	private SurfaceHolder holder;

	//�ɻ�ʱʱλ��
	private int mapX,mapY=900;
	//�л����ӵ�������
	private long count_1,count_2,count_3,count_4, countBullet;
	//�ƶ�����߳�
	private Thread th;
	//��Ϸ״̬����
	public final static int GAME_MENU=0;
	public final static int GAMING=1;
	public final static int GAME_OVER=2;
	public final static int GAME_WIN=3;
	//��ʱ��
	public long timeStart,now;
	//��Ϸ�Ѷ�
	private int difficulty=1;
	//��Ϸ״̬
	public static int gameState=GAME_MENU;
	//�õ���Դ�ļ�resȥ��ʼ��λͼ�ļ�	
	private Resources res=this.getResources();	
	
	private Bitmap plane;	//�ɻ�ͼƬ
	private Bitmap general;  //�л�1ͼƬ
	private Bitmap ordinary;  //�л�2ͼƬ
	private Bitmap particular;  //�л�3ͼƬ
	private Bitmap boss;  //BossͼƬ
	private Bitmap bullet;  //�ӵ�ͼƬ
	private Bitmap button;  //��ťͼƬ
	private Bitmap buttonpressed;  //��ť�����µ�ͼƬ
	private Bitmap menu;  //�˵��ı���ͼƬ
	private Bitmap backgroundGame;  //�˵��ı���ͼƬ
	private Bitmap backgroundGameOver;  //��Ϸ�����ı���ͼƬ
	
	//�������������
	private Vector<Enermy> vectorEnermy;
	//�л��������
	private int enermyX,enermyY;
	//�������ӵ�
	private Vector<Bullets> vectorBullet;
	//�ӵ�����
	private int bulletX,bulletY;
	//��¼������˵�����
	private int deadNumberOfEnermy=0;
	//��ʼ�˵�
	public Menu startMenu,winMenu;
	//��Ϸ���� ��Ϸ���� ��Ϸ��������
	private BackGround background,gameover;  
	//���Ƿɻ�
	private Protagonist player;
	//Boss
	private Boss bossEnermy;
	//Boss�Ƿ����
	private boolean isBoss=false;
	
	public WarView(Context context)
	{
		super(context);
		//���û���
		paint=new Paint();
		paint.setColor(Color.WHITE);
		paint.setTextSize(20);
		//��������Ϊ�����
		paint.setAntiAlias(true);
		//ʵ��������
		holder=this.getHolder();
		//��ӱ�Ҫ�Ļص�������ȷ��SurfaceView��ʱ����
		holder.addCallback(this);
		//SurfaceView��ý���
		setFocusable(true);	
		//�߳̿��Կ�ʼ����
	    flag=true;
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0)
	{
		// TODO Auto-generated method stub
		//��õ�ǰ��Ļ���
		screenW=this.getWidth();
		screenH=this.getHeight();
		//��ʼ���л����ӵ� 
		init();
		//��this����ʵ��������ƶ��߳�
		th=new Thread(this);
		th.start();
	}

	public void init()
	{		
		//����Ϸ״̬���ڲ˵�ʱ ��������Ϸ
		if(gameState==GAME_MENU)
		{
			//��pngͼƬ�ļ�����תΪbmpλͼ��ʾ
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
			
			//Ϊ�������ʵ�� ���趨��ֵ
			player=new Protagonist(plane, 0, 800);
			//Ϊ��Ϸ�������ʵ��
			background=new BackGround(backgroundGame);
			//Ϊ�˵����ʵ��
			startMenu=new Menu(menu, button, buttonpressed);
			//ΪӮ����Ϸ���ʵ��
			//winMenu=new Menu(menu, button, buttonpressed);
			//Ϊ�л����������ʵ��
			vectorEnermy=new Vector<Enermy>();
			//Ϊ�ӵ����������ʵ��
			vectorBullet=new Vector<Bullets>();
			//Ϊ��Ϸ�����������ʵ��
			gameover=new BackGround(backgroundGameOver);
			//ΪBoss���ʵ��
			bossEnermy=new Boss(boss, screenW/2, 0);
		}
	}
	
	//���Ʒ���
	private void drawGame()
	{
		try
		{		
			//�ȵ�һ�λ��ƽ������ܽ�����һ��
			canvas=holder.lockCanvas();
			if (canvas != null) 
			{
				switch (gameState)
				{
				//���Ʋ˵�
				case GAME_MENU:
					//���Ʋ˵�����
					startMenu.draw(canvas,paint);
					break;
				//��Ϸ����Ϊ��ʼ
				case GAMING:
					//������Ϸ����
					background.draw(canvas, paint);
					//��������
					player.draw(canvas,paint);
					//��������Ѫ���ͻ���
					player.drawText(canvas, paint);
					//�����Ѷ�ϵ��
					canvas.drawText("�Ѷ�"+difficulty, 0, 50, paint);
					//���Ƶл�
					for(int i=0;i<vectorEnermy.size();i++)
					{
						vectorEnermy.elementAt(i).draw(canvas, paint);
					}
					//�������ǵ��ӵ�
					for(int i=0;i<vectorBullet.size();i++)
					{
						vectorBullet.elementAt(i).draw(canvas, paint);
					}

					if(isBoss)
					{
						//����Boss
						bossEnermy.draw(canvas, paint);
						canvas.drawText("Boss���ɣ�", screenW/2, 50, paint);
					}
					break;
				case GAME_OVER:
					//������Ϸ��������
					gameover.draw(canvas, paint);
					//����Gameover
					canvas.drawText("Game Over", screenW/2, screenH/2, paint);
					break;
				case GAME_WIN:
					//winMenu.draw(canvas, paint);
					//��Ϸ����
					gameover.draw(canvas, paint);
					Paint paint_2=new Paint();
					paint_2.setTextSize(40);
					paint_2.setColor(Color.YELLOW);
					paint_2.setAntiAlias(true);
					canvas.drawText("��Ӯ��!!", screenW/2, screenH/2, paint_2);
					canvas.drawText("�÷�"+player.score,screenW/2,screenH/2-50,paint_2);
					break;
				}
			}
		}
		catch (Exception e){}
		finally
		{
			if (canvas != null) 
			{
				//�ڻ��ƽ�������н�������
				holder.unlockCanvasAndPost(canvas);
			}
		}
	}
	
	//��Ϸ�߼�
	private void logic()
    {
		switch (gameState)
		{
		case GAME_MENU:
			break;
		case GAMING:
			//��Boss�Ѿ�����
			if(isBoss)
			{
				//ΪBoss�����ײ
				if(Collision(bossEnermy.x, bossEnermy.y, boss.getWidth(), boss.getHeight(), 
						mapX-plane.getWidth()/2, mapY-plane.getHeight()/2, plane.getWidth(), plane.getHeight()))
				{
					//�������ɻ���Boss��ײ�͵�Ѫ
					if(player.planeHealth>50)
					{
						player.planeHealth-=50;
						//bossEnermy.boosHealth-=50;
					}
					else 
					{
						//���ɻ���Ѫ�������Կ۳�20
						player.planeHealth=0;
						//��Ϸ����
						gameState=GAME_OVER;
					}
				}
				//Boss�߼�
				bossEnermy.logic();
			}

			if(difficulty>=1&&difficulty<3)
			{
				count_1++;
				if(count_1%30==0)
				{		
					//�����������
					enermyX=(int)(Math.random()*screenW*0.9);
					enermyY=0;
					//��������л�
					int index=(int)(Math.random()*3);
					//Ϊ�л�������������1 2�ŵ���
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
					//�����������
					enermyX=(int)(Math.random()*screenW*0.9);
					enermyY=0;
					//��������л�
					int index=(int)(Math.random()*3+1);
					//Ϊ�л�������������1 2�ŵ���
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
					//�����������
					enermyX=(int)(Math.random()*screenW*0.9);
					enermyY=0;
					//��������л�
					int index=(int)(Math.random()*2+2);
					//Ϊ�л�������������1 2�ŵ���
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
				//����Boss
				isBoss=true;
			}
			else if(difficulty>=8)
			{
				count_4++;
				if(count_4%17==0)
				{
					//�����������
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
			
			//�л��߼�
			for(int i=0;i<vectorEnermy.size();i++)
			{
				Enermy e=vectorEnermy.elementAt(i);
				//���ж�������������������Ƴ�
				if(e.isDead)
				{
					vectorEnermy.remove(e);
				}
				else if(e.y>=screenH)
				{
					//�����˱߽������
					vectorEnermy.remove(e);
				}
				else if(Collision(e.x,e.y, e.enermyWidth, e.enermyHeight,mapX-plane.getWidth()/2, mapY-plane.getHeight()/2, player.planeWidth, player.planeHeight))
				{
					//�������ɻ���л���ײ�͵�Ѫ
					if(player.planeHealth>20)
					{
						player.planeHealth-=20;
					}
					else 
					{
						//���ɻ���Ѫ�������Կ۳�20
						player.planeHealth=0;
						//��Ϸ����
						gameState=GAME_OVER;
					}
					//�л���Ѫ
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
				//����ӵ�
				vectorBullet.addElement(new Bullets(bullet, bulletX, bulletY));
			}
			
			//�ӵ��߼�
			for(int i=0;i<vectorBullet.size();i++)
			{
				Bullets b=vectorBullet.elementAt(i);
				//���ж�������������������Ƴ�
				if(b.isDead)
				{
					//���˴���i��ᷢ������
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
			
			//����ѭ�������ײ
			for(int i=0;i<vectorBullet.size();i++)
			{
				Bullets b=vectorBullet.elementAt(i);
				//�ӵ���ײ���
				for(int j=0;j<vectorEnermy.size();j++)
				{
					Enermy e=vectorEnermy.elementAt(j);
					if(Collision(b.x, b.y, b.bulletWidth, b.bulletHeight, e.x, e.y, e.enermyWidth, e.enermyHeight))
					{
						b.isDead=true;
						vectorBullet.remove(b);
						//һ���ӵ��۳�һ�ٵ�Ѫ��
						if(e.enermyHealth>100)
						{
							e.enermyHealth-=100;
						}
						else 
						{
							e.enermyHealth=0;
							//���µ���״̬			
							e.isDead=true;
							//��������20
							player.score+=20;	
							deadNumberOfEnermy++;
						}
					}
				}
				//Boss���ּ�����ӵ�����ײ
				if(isBoss)
				{
					//���ӵ��������Boss��
					if(Collision(bossEnermy.x, bossEnermy.y, boss.getWidth(), boss.getHeight(), b.x, b.y, b.bulletWidth, b.bulletHeight))
					{
						if(bossEnermy.boosHealth<50)
						{
							//����Boss״̬Ϊ����
							bossEnermy.boosHealth=0;
							bossEnermy.isDead=true;
							//������Ϸ״̬
							gameState=GAME_WIN;
						}
						else 
						{
							bossEnermy.boosHealth-=50;
						}
						
						b.isDead=true;
						//���ӵ�ֱ������
						vectorBullet.remove(b);
					}
				}
			}
			//����������߸����ϵĵ��˾��Ѷ�����
			if(deadNumberOfEnermy>10)
			{
				//�Ѷ�����
				difficulty++;
				//��ΪĬ��0
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
	    //���·ɻ�λ��
	    player.update(mapX-plane.getWidth()/2,mapY-plane.getHeight()/2);
	    
		switch (gameState)
		{
		case GAME_MENU:
			//Ϊ�˵��Ĵ��������¼�
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
		//���ǰ����˷��ذ���
		if(keyCode==KeyEvent.KEYCODE_BACK)
		{
			if(gameState==GAMING||gameState==GAME_OVER)
			{
				//��������Ϸ�˵������������Ϸ�н��������Ϸ�������ص��˵�
				gameState=GAME_MENU;
				//������Ϸ
				init();
			}
			else if(gameState==GAME_MENU)
			{
				//������ǰ
				MainActivity.instance.finish();
				System.exit(0);
			}
			//���ذ����Ѵ��� ���ٽ���ϵͳ���� ������Ϸ�������̨
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}


	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event)
	{
		// TODO Auto-generated method stub
		//���ǰ����˷��ذ���
		if(keyCode==KeyEvent.KEYCODE_BACK)
		{
			if(gameState==GAMING||gameState==GAME_OVER)
			{
				//��������Ϸ�˵������������Ϸ�н��������Ϸ�������ص��˵�
				gameState=GAME_MENU;
			}
			return true;
		}

		//return super.onKeyDown(keyCode, event);
		return super.onKeyUp(keyCode, event);
	}

	//XYΪ���� WTΪ���
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
			//��������Ϊδ��ײ ��˿��Խ�Լ�б�ʱ��
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
