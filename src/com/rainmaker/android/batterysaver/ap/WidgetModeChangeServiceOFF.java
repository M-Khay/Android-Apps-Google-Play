package com.rainmaker.android.batterysaver.ap;

import java.util.Calendar;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.RemoteViews.RemoteView;
import android.widget.Toast;

public class WidgetModeChangeServiceOFF extends Service{

	SharedPreferences prefs;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		
		if(isPro()){
			
		RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.batt_widget_layout);
		prefs = PreferenceManager.getDefaultSharedPreferences(WidgetModeChangeServiceOFF.this);
		int mode = prefs.getInt(BatterySaver.MODE, 3);
		
			if(mode == 1){
				//turn off
				//Log.v("BUTTON TEST", "PS MODE OFF");
				Intent intent01 = new Intent(this, AllBroadcastReceiver.class);
				PendingIntent sender = PendingIntent.getBroadcast(this,
				               1, intent01, 0);
				AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

				alarmManager.cancel(sender);
				
				Intent intent2 = new Intent(this, AllBroadcastReceiver.class);
				PendingIntent sender2 = PendingIntent.getBroadcast(this,
				               2, intent2, 0);
				AlarmManager alarmManager2 = (AlarmManager) getSystemService(ALARM_SERVICE);

				alarmManager2.cancel(sender2);
				
				prefs.edit().putInt(BatterySaver.MODE, 0).commit();
				
				//remoteViews.setInt(R.id.mode_toggle, "setImageResource", R.drawable.ic_launcher);
				
//				Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),android.R.drawable.btn_star_big_on);
				
				//Bitmap bitmap = ((BitmapDrawable) getApplicationContext().getResources().getDrawable(android.R.drawable.btn_star_big_on)).getBitmap();
//				remoteViews.setBitmap(R.id.mode_toggle, "android:src", bitmap);
				
//				remoteViews.setBitmap(R.id.mode_toggle,"src", bitmap);
//				remoteViews.setTextViewText(R.id.mode_toggle, "OFF");
				//remoteViews.setTextViewText(R.id.mode_toggle, "OFF");
				 remoteViews.setImageViewResource(R.id.mode_toggle, R.drawable.mode_off);
				ComponentName thisWidget = new ComponentName(this,BattWidgetProvider.class);
				AppWidgetManager manager = AppWidgetManager.getInstance(getApplicationContext());
				manager.updateAppWidget(thisWidget, remoteViews);
				    
				Toast.makeText(getApplicationContext(),
						getString(R.string.poweroff), Toast.LENGTH_SHORT)
                        .show();
				
			}else if(mode == 0){
				//turn on
				
				//Log.v("BUTTON TEST", "PS MODE ON");
				/**
				 * Alarm to turn WIFI ON
				 */
				Intent myIntent = new Intent("com.rainmaker.android.batterysaver.ALARM1");
				
				myIntent.putExtra("alarm", 1);

		      	    PendingIntent pendingIntent
		      	     = PendingIntent.getBroadcast(getBaseContext(),
		      	       1, myIntent, 0);
		      
		      AlarmManager alarmManager
		      = (AlarmManager)getSystemService(ALARM_SERVICE);
		    Calendar calendar = Calendar.getInstance();
		    calendar.setTimeInMillis(System.currentTimeMillis());
		    calendar.add(Calendar.SECOND, 10);

		    
		    int interval = prefs.getInt(BatterySaver.STOP_FOR, 3);
		    int run_for = prefs.getInt(BatterySaver.RUN_FOR, 1);
		    
		    alarmManager.setRepeating(AlarmManager.RTC,
		      calendar.getTimeInMillis(), interval*60*1000, pendingIntent);
		    
		    prefs.edit().putInt(BatterySaver.MODE, 1).commit();
		    //Log.v("WidgetModeChangeService", "Alarm 1 Created");
			
		   // remoteViews.setInt(R.id.mode_toggle, "setImageResource", R.drawable.checkbox);
		    
		  //  Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),android.R.drawable.btn_star_big_on);
		    
//		    Bitmap bitmap = ((BitmapDrawable) getApplicationContext().getResources().getDrawable(android.R.drawable.btn_star_big_off)).getBitmap();
//			remoteViews.setBitmap(R.id.mode_toggle, "src", bitmap);
			
//		    remoteViews.setBitmap(R.id.mode_toggle,"src", bitmap);
//		    remoteViews.setCharSequence(R.id.mode_toggle, "text", "ON");
		    remoteViews.setImageViewResource(R.id.mode_toggle, R.drawable.mode_on);
			
		    ComponentName thisWidget = new ComponentName(this,BattWidgetProvider.class);
			AppWidgetManager manager = AppWidgetManager.getInstance(getApplicationContext());
			manager.updateAppWidget(thisWidget, remoteViews);
		    
//		    remoteViews.setInt(R.id.mode_toggle, "ImageButton",android.R.drawable.btn_star_big_on);
		
		    Toast.makeText(getApplicationContext(),
		    		getString(R.string.poweron), Toast.LENGTH_SHORT)
                    .show();
		    
			}
		}else{
			Toast.makeText(getApplicationContext(),
                    "Please install the PRO version to enable Widget functionality", Toast.LENGTH_LONG)
                    .show();
		}
		stopSelf();
	}
	
boolean isPro(){
	    
    	PackageManager manager = getApplicationContext().getPackageManager();
        if (manager.checkSignatures("com.rainmaker.android.batterysaver", "com.rainmaker.android.batterysaverpro")
            == PackageManager.SIGNATURE_MATCH) {
           return true; //full version
        }else{
        	return false;
        }
    	
    }

}
