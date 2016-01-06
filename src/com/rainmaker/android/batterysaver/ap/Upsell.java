package com.rainmaker.android.batterysaver.ap;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Upsell extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.upsell_pro);
			 		
		Button goMkt = (Button) findViewById(R.id.gotomkt);
		
		Button back_to_simple = (Button) findViewById(R.id.button1);
		
		back_to_simple.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				final Intent bat_saver = new Intent(getApplicationContext(), BatterySaver.class);
				startActivity(bat_saver);
			}
		});
		
		goMkt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("market://details?id=com.rainmaker.android.batterysaver.pro"));
				startActivity(intent);
				
			}
		});
	}

}
