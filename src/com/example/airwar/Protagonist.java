package com.example.airwar;

import android.R.drawable;
import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Protagonist
{
	//�ɻ�xy����
	private int x,y;
	//�ɻ���ͼƬ
	private Bitmap plane;
	//�ɻ�ͼƬ�Ŀ��
	public int planeHeight,planeWidth;
	//�ɻ���Ѫ��
	public int planeHealth=2500;
	//����
	public int score=0;
	//�ɻ�״̬
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
		//���·ɻ�����Ļ�ϵ�λ��
		this.x=x;
		this.y=y;
	}
	
	public void draw(Canvas canvas,Paint paint)
	{
		canvas.drawBitmap(plane, x, y, paint);
	}
	
	public void drawText(Canvas canvas,Paint paint)
	{
		canvas.drawText("Ѫ��Ϊ"+planeHealth, WarView.screenW-200, 20, paint);
		canvas.drawText("����Ϊ"+score, 0, 20, paint);
	}
}
