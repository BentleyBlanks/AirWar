package com.example.airwar;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity
{
	//���ڱ��浱ǰ�û��
	public static MainActivity instance;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		//����this�
		instance=this;
		super.onCreate(savedInstanceState);
		//�ڵ�ǰactivity�еõ���崰���ô��ڹ���������Ϊȫ��
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//�ޱ��ⴰ��
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(new WarView(this));
	}

}
