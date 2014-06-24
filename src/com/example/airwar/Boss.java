package com.example.airwar;

import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.UpdateAppearance;

public class Boss
{
	//Boss图片
	private Bitmap boss;
	//Boss血量
	public int boosHealth=5000;
	//Boss移动速度
	private int speed=1;
	//Boss的坐标
	public int x,y;
	//速度状态
	private final int plus=1,reduce=0;
	//Boss的移动方向
	private int xDirection=reduce,yDirection=plus;
	//Boss状态
	public boolean isDead=false;
	
	public Boss(Bitmap boss,int x,int y)
	{
		this.boss=boss;
		this.x=x;
		this.y=y;
	}
	
	//用于改变Boss运行状态
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
			//判断Boss运行速度的状态
			if(x<0)
			{
				//改变状态
				xDirection=change(xDirection);
			}
			else if(x>WarView.screenW-boss.getWidth())
			{
				xDirection=change(xDirection);
			}
			if(y<0)
			{			
				//改变状态
				yDirection=change(yDirection);
			}
			else if(y>WarView.screenH-boss.getHeight())
			{
				//改变状态
				yDirection=change(yDirection);
			}
			//Boss在上下左右乱动
			if(xDirection==reduce)
			{
				//越界则返回
				x-=speed;
			}
			else if(xDirection==plus)
			{
				//越界则返回
				x+=speed;
			}
			if(yDirection==reduce)
			{
				//越界则返回
				y-=speed;
			}
			else if(yDirection==plus)
			{
				//越界则返回
				y+=speed;
			}
		}
	}
	public void draw(Canvas canvas,Paint paint)
	{
		canvas.drawBitmap(boss, x, y, paint);
	}
}
