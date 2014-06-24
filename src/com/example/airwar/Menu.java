package com.example.airwar;

import com.example.airwar.R.drawable;
import android.view.MotionEvent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Menu
{
	//��ť�Ƿ񱻰���
	private boolean isPressed;
	//�˵�����ͼƬ ��ť ��ť������ͼƬ
	private Bitmap menu,button,buttonpressed;
	//��¼λ��
	private int buttonX,buttonY;
	
	public Menu(Bitmap menu,Bitmap button,Bitmap buttonpressed)
	{
		this.menu=menu;
		this.button=button;
		this.buttonpressed=buttonpressed;
		//��ť������� ����
		buttonX=WarView.screenW/2-button.getWidth()/2;
		buttonY=WarView.screenH-button.getHeight();
		isPressed=false;
	}

	public void draw(Canvas canvas,Paint paint)
	{
		//��ʾ�˵�����ͼƬ
		canvas.drawBitmap(menu, 0, 0, paint);
		if(isPressed)
		{
			//���Ѿ����¾���ʾ���µ�ͼƬ
			canvas.drawBitmap(buttonpressed, buttonX,buttonY, paint);
		}
		else
		{
			//��ʾĬ��
			canvas.drawBitmap(button, buttonX, buttonY, paint);
		}
	}

	public void onTouchEvent(MotionEvent event)
	{			
		//�õ������Ŀ�ʼλ��
		int x=(int)event.getX();
		int y=(int)event.getY();
		//���ǰ��»����ƶ��������˰�ť����Χ��
		if(event.getAction()==MotionEvent.ACTION_DOWN||event.getAction()==MotionEvent.ACTION_MOVE)
		{
			//�ж��û��Ƿ񰴵���ť
			if(x>buttonX&&x<buttonX+button.getWidth()&&y>buttonY&&y<buttonY+button.getHeight())	
			{
				//���°�ť״̬
				isPressed=true;
			}
			else 
			{
				isPressed=false;
			}
		}
		//Ϊ�����û�ȡ����ʼ��Ϸ ��̧��ʱ�뿪��ť����Ҳ����ȡ��
		else if(event.getAction()==MotionEvent.ACTION_UP)
		{
			if(x>buttonX&&x<buttonX+button.getWidth()&&y>buttonY&&y<buttonY+button.getHeight())	
			{
				//��ԭδ����
				isPressed=false;
				//�ı䵱ǰ��Ϸ״̬Ϊ��ʼ��Ϸ
				WarView.gameState=WarView.GAMING;
			}
		}
	}
}
