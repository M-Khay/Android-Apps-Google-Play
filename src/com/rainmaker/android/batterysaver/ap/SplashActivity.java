package com.rainmaker.android.batterysaver.ap;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash);
		System.out.println("This is splash");
		Handler handler = new Handler();
		Runnable runnable = new Runnable() {
	
			@Override
			public void run() {
				// TODO Auto-generated method stub
			Intent i= new Intent(SplashActivity.this,BatterySaver.class);
			startActivity(i);
			finish();
			}
		};
		handler.postDelayed(runnable, 3000);
		System.out.println("This is splash ending");
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}

}
