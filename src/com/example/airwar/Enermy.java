package com.example.airwar;

import android.R.integer;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Enermy
{
    //敌人左上角XY坐标
	public int x,y;
	//敌人的宽高
	public int enermyWidth,enermyHeight;
	//以位图形式显示敌人
	public Bitmap enermy;
	//敌人的生存状态
	public boolean isDead=false;
	//敌人的血量
	public int enermyHealth;
	//敌人移动的速度 默认为1
	public int speed=1;	
	//敌人的类型序列号
	public int index;
	//三种敌人的名称
	public final static int GENERAL=1;
	public final static int ORDINARY=2;
	public final static int PARTICULAR=3;
	
	public Enermy(Bitmap enermy,int x,int y,int index)
	{
		this.enermy=enermy;
		this.x=x;
		this.y=y;
		enermyWidth=enermy.getWidth();
		enermyHeight=enermy.getHeight();
		
		this.index=index;
		//敌机有三种类型
		switch (index)
		{
		case GENERAL:
			speed=2;
			enermyHealth=100;
			break;
		case ORDINARY:
			speed=4;
			enermyHealth=200;
			break;
		case PARTICULAR:
			speed=6;
			enermyHealth=300;
			break;
		}
	}
	
	public void logic()
	{
		//让敌机向下飞翔
		y+=speed;
	}
	
	public void draw(Canvas canvas,Paint paint)
	{
		canvas.drawBitmap(enermy, x, y, paint);
	}

}
