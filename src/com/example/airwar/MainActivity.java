package com.example.airwar;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity
{
	//用于保存当前用户活动
	public static MainActivity instance;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		//保存this活动
		instance=this;
		super.onCreate(savedInstanceState);
		//在当前activity中得到面板窗口用窗口管理器设置为全屏
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//无标题窗口
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(new WarView(this));
	}

}
