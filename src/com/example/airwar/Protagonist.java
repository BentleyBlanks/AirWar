package com.example.airwar;

import android.R.drawable;
import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Protagonist
{
	//飞机xy坐标
	private int x,y;
	//飞机的图片
	private Bitmap plane;
	//飞机图片的宽高
	public int planeHeight,planeWidth;
	//飞机的血量
	public int planeHealth=2500;
	//积分
	public int score=0;
	//飞机状态
	private boolean isDead=false;
	
	public Protagonist(Bitmap plane,int x,int y)
	{
		// TODO Auto-generated constructor stub
		this.x=x;
		this.y=y;
		this.plane=plane;
		planeHeight=plane.getHeight();
		planeWidth=plane.getWidth();
	}

	public void update(int x,int y)
	{
		//更新飞机在屏幕上的位置
		this.x=x;
		this.y=y;
	}
	
	public void draw(Canvas canvas,Paint paint)
	{
		canvas.drawBitmap(plane, x, y, paint);
	}
	
	public void drawText(Canvas canvas,Paint paint)
	{
		canvas.drawText("血量为"+planeHealth, WarView.screenW-200, 20, paint);
		canvas.drawText("积分为"+score, 0, 20, paint);
	}
}
