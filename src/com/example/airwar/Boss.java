package com.example.airwar;

import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.UpdateAppearance;

public class Boss
{
	//BossͼƬ
	private Bitmap boss;
	//BossѪ��
	public int boosHealth=5000;
	//Boss�ƶ��ٶ�
	private int speed=1;
	//Boss������
	public int x,y;
	//�ٶ�״̬
	private final int plus=1,reduce=0;
	//Boss���ƶ�����
	private int xDirection=reduce,yDirection=plus;
	//Boss״̬
	public boolean isDead=false;
	
	public Boss(Bitmap boss,int x,int y)
	{
		this.boss=boss;
		this.x=x;
		this.y=y;
	}
	
	//���ڸı�Boss����״̬
	private int change(int direction)
	{
		if(direction==reduce)
		{
			direction=plus;
		}
		else
		{
			direction=reduce;
		}
		return direction;
	}

	public void logic()
	{
		if(isDead==false)
		{
			//�ж�Boss�����ٶȵ�״̬
			if(x<0)
			{
				//�ı�״̬
				xDirection=change(xDirection);
			}
			else if(x>WarView.screenW-boss.getWidth())
			{
				xDirection=change(xDirection);
			}
			if(y<0)
			{			
				//�ı�״̬
				yDirection=change(yDirection);
			}
			else if(y>WarView.screenH-boss.getHeight())
			{
				//�ı�״̬
				yDirection=change(yDirection);
			}
			//Boss�����������Ҷ�
			if(xDirection==reduce)
			{
				//Խ���򷵻�
				x-=speed;
			}
			else if(xDirection==plus)
			{
				//Խ���򷵻�
				x+=speed;
			}
			if(yDirection==reduce)
			{
				//Խ���򷵻�
				y-=speed;
			}
			else if(yDirection==plus)
			{
				//Խ���򷵻�
				y+=speed;
			}
		}
	}
	public void draw(Canvas canvas,Paint paint)
	{
		canvas.drawBitmap(boss, x, y, paint);
	}
}
