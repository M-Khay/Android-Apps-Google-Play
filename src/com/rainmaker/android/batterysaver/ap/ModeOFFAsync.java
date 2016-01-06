package com.rainmaker.android.batterysaver.ap;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

public class ModeOFFAsync extends AsyncTask<Void, Void, Void>{

	Context ctx;
	
	SharedPreferences prefs;
	
	ModeOFFAsync(Context ctxx){
		
		this.ctx = ctxx;
		
	}
	
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
	}
	
	@Override
	protected Void doInBackground(Void... params) {

		// first cancel all alarms, the set new with values
		
		prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
		
		Intent intent_0 = new Intent("com.rainmaker.android.batterysaver.ALARM1");
		PendingIntent sender = PendingIntent.getBroadcast(ctx,
		               1, intent_0, 0);
		AlarmManager alarmManager0 = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);

		alarmManager0.cancel(sender);
		
		Intent intent2 = new Intent("com.rainmaker.android.batterysaver.ALARM2");
		PendingIntent sender2 = PendingIntent.getBroadcast(ctx,
		               2, intent2, 0);
		AlarmManager alarmManager2 = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);

		alarmManager2.cancel(sender2);
		
		prefs.edit().putInt(BatterySaver.MODE, 0).commit();
		
		return null;
		
}
}