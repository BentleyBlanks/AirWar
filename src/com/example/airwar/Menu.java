package com.example.airwar;

import com.example.airwar.R.drawable;
import android.view.MotionEvent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Menu
{
	//按钮是否被按下
	private boolean isPressed;
	//菜单背景图片 按钮 按钮被按下图片
	private Bitmap menu,button,buttonpressed;
	//记录位置
	private int buttonX,buttonY;
	
	public Menu(Bitmap menu,Bitmap button,Bitmap buttonpressed)
	{
		this.menu=menu;
		this.button=button;
		this.buttonpressed=buttonpressed;
		//按钮在最底下 居中
		buttonX=WarView.screenW/2-button.getWidth()/2;
		buttonY=WarView.screenH-button.getHeight();
		isPressed=false;
	}

	public void draw(Canvas canvas,Paint paint)
	{
		//显示菜单背景图片
		canvas.drawBitmap(menu, 0, 0, paint);
		if(isPressed)
		{
			//若已经按下就显示按下的图片
			canvas.drawBitmap(buttonpressed, buttonX,buttonY, paint);
		}
		else
		{
			//显示默认
			canvas.drawBitmap(button, buttonX, buttonY, paint);
		}
	}

	public void onTouchEvent(MotionEvent event)
	{			
		//得到触摸的开始位置
		int x=(int)event.getX();
		int y=(int)event.getY();
		//若是按下或者移动触碰到了按钮区域范围内
		if(event.getAction()==MotionEvent.ACTION_DOWN||event.getAction()==MotionEvent.ACTION_MOVE)
		{
			//判断用户是否按到按钮
			if(x>buttonX&&x<buttonX+button.getWidth()&&y>buttonY&&y<buttonY+button.getHeight())	
			{
				//更新按钮状态
				isPressed=true;
			}
			else 
			{
				isPressed=false;
			}
		}
		//为方便用户取消开始游戏 在抬起时离开按钮区域也可以取消
		else if(event.getAction()==MotionEvent.ACTION_UP)
		{
			if(x>buttonX&&x<buttonX+button.getWidth()&&y>buttonY&&y<buttonY+button.getHeight())	
			{
				//还原未按下
				isPressed=false;
				//改变当前游戏状态为开始游戏
				WarView.gameState=WarView.GAMING;
			}
		}
	}
}
