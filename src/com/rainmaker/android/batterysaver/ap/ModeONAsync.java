package com.rainmaker.android.batterysaver.ap;

import java.util.Calendar;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class ModeONAsync extends AsyncTask<Void, Void, Void>{

	Context ctx;
	static String TAG = "CreateAlarms";
	
	public ModeONAsync(Context ctxx) {

		this.ctx = ctxx;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		Toast.makeText(ctx,
				ctx.getString(R.string.setsave), Toast.LENGTH_SHORT)
                .show();
	}
	
	@Override
	protected Void doInBackground(Void... params) {

		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
		/**
		 * Alarm to turn WIFI ON
		 */
		Intent myIntent = new Intent("com.rainmaker.android.batterysaver.ALARM1");
		
		myIntent.putExtra("alarm", 1);

      	    PendingIntent pendingIntent
      	     = PendingIntent.getBroadcast(ctx,
      	       1, myIntent, 0);
      
      AlarmManager alarmManager = (AlarmManager)ctx.getSystemService(Context.ALARM_SERVICE);
      
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(System.currentTimeMillis());
    calendar.add(Calendar.SECOND, 10);

    
    int interval = prefs.getInt(BatterySaver.STOP_FOR, 3);
    int run_for = prefs.getInt(BatterySaver.RUN_FOR, 1);
    
    alarmManager.setRepeating(AlarmManager.RTC,
      calendar.getTimeInMillis(), interval*60*1000, pendingIntent);
    
    //Log.v(TAG, "rnm Alarm 1 Created with runFOR: "+run_for+" INTERVAL: "+interval);
		
    
    /**
     * Alarm 2 to set WIFI OFF based on conditions
     */
    
    
//    Intent myIntent2 = new Intent(getBaseContext(),
//    	      AlarmReceiver.class);
    
    Intent myIntent2 = new Intent("com.rainmaker.android.batterysaver.ALARM2");
    
		myIntent2.putExtra("alarm", 2);

    	    PendingIntent pendingIntent2
    	     = PendingIntent.getBroadcast(ctx,
    	       2, myIntent2, 0);
    

  alarmManager.setRepeating(AlarmManager.RTC,
    calendar.getTimeInMillis()+run_for*60*1000, interval*60*1000, pendingIntent2);
  
  
  //Log.v(TAG, "rnm Alarm 2 Created with runFOR: "+run_for+" INTERVAL: "+interval);
    
  prefs.edit().putInt(BatterySaver.MODE, 1).commit();
		
		return null;
	}
	
}