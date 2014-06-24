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
    //�������Ͻ�XY����
	public int x,y;
	//���˵Ŀ��
	public int enermyWidth,enermyHeight;
	//��λͼ��ʽ��ʾ����
	public Bitmap enermy;
	//���˵�����״̬
	public boolean isDead=false;
	//���˵�Ѫ��
	public int enermyHealth;
	//�����ƶ����ٶ� Ĭ��Ϊ1
	public int speed=1;	
	//���˵��������к�
	public int index;
	//���ֵ��˵�����
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
		//�л�����������
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
		//�õл����·���
		y+=speed;
	}
	
	public void draw(Canvas canvas,Paint paint)
	{
		canvas.drawBitmap(enermy, x, y, paint);
	}

}
