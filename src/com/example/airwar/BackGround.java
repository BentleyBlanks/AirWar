package com.example.airwar;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class BackGround
{
	//±³¾°Í¼Æ¬
	private Bitmap background;
	
	public BackGround(Bitmap background)
	{
		this.background=background;
	}
	
	public void draw(Canvas canvas,Paint paint)
	{
		canvas.drawBitmap(background, 0, 0, paint);
	}
}
