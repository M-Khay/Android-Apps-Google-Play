package com.rainmaker.android.batterysaver.ap;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class AlarmCreator extends Service{

	SharedPreferences prefs;
	static String TAG = "AlarmCreator";
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		prefs = PreferenceManager.getDefaultSharedPreferences(AlarmCreator.this);
	}

	
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		
		
		/**
		 * Alarm to turn WIFI ON
		 */
		
		if(prefs.contains(BatterySaver.MODE) && prefs.getInt(BatterySaver.MODE, 3)== 1){
			
		
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
	    
	    //Log.v(TAG, "rnm Alarm 1 Created");
		
    
    /**
     * Alarm 2 to set WIFI OFF based on conditions
     */
    
    
//	    Intent myIntent2 = new Intent(getBaseContext(),
//	      AlarmReceiver.class);

		Intent myIntent2 = new Intent("com.rainmaker.android.batterysaver.ALARM2");
		
			myIntent2.putExtra("alarm", 2);
		
			    PendingIntent pendingIntent2
			     = PendingIntent.getBroadcast(getBaseContext(),
			       2, myIntent2, 0);
		
		
		alarmManager.setRepeating(AlarmManager.RTC,
		calendar.getTimeInMillis()+run_for*60*1000, interval*60*1000, pendingIntent2);
		
		
		//Log.v(TAG, "rnm Alarm 2 Created");
		
		prefs.edit().putInt(BatterySaver.MODE, 1).commit();
  
		} else if(!prefs.contains(BatterySaver.MODE)){

			createDefaultAlarms();
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	//creates default alarms for first time
	// copy all changes to AlarmCreator
	protected void createDefaultAlarms(){
		
		/**
		 * Alarm to turn WIFI ON
		 */
//		Intent myIntent = new Intent(getBaseContext(),
//      	      AlarmReceiver.class);
		
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

    
    int interval = 3;
    int run_for = 1;
    
    alarmManager.setRepeating(AlarmManager.RTC,
      calendar.getTimeInMillis(), interval*60*1000, pendingIntent);
    
    //Log.v(TAG, "rnm Alarm 1 Created");
		
    
    /**
     * Alarm 2 to set WIFI OFF based on conditions
     */
    
    
//    Intent myIntent2 = new Intent(getBaseContext(),
//    	      AlarmReceiver.class);
    
    Intent myIntent2 = new Intent("com.rainmaker.android.batterysaver.ALARM2");
    
		myIntent2.putExtra("alarm", 2);

    	    PendingIntent pendingIntent2
    	     = PendingIntent.getBroadcast(getBaseContext(),
    	       2, myIntent2, 0);
    

  alarmManager.setRepeating(AlarmManager.RTC,
    calendar.getTimeInMillis()+run_for*60*1000, interval*60*1000, pendingIntent2);
  
  
  //Log.v(TAG, "rnm Alarm 2 Created");
    
  prefs.edit().putInt(BatterySaver.MODE, 1).commit();
    
	}

	
	

}
