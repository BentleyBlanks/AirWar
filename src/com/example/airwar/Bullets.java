package com.example.airwar;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Bullets
{
    //子弹的xy坐标
	public int x,y;
	//子弹的宽高
	public int bulletWidth,bulletHeight;
	//子弹图片
	public Bitmap bullet;
	//子弹状态
	public boolean isDead=false;
	
	public Bullets(Bitmap bullet,int x,int y)
	{
		this.bullet=bullet;
		this.x=x;
		this.y=y;
		bulletWidth=bullet.getWidth();
		bulletHeight=bullet.getHeight();
	}

	public void logic()
	{
		y-=20;
	}
	
	public void draw(Canvas canvas,Paint paint)
	{
		canvas.drawBitmap(bullet, x, y, paint);
	}
}
