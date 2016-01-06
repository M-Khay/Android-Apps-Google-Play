package com.rainmaker.android.batterysaver.ap;


import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class UpdateWidget extends Service{

	
	RemoteViews remoteViews;
	
	SharedPreferences prefs;
	protected Handler mHandler = new Handler();
	Context ctx;
	
	
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		
//		if(isPro()){
			
		prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		
		
		//Log.v("UpdateWidget", "rnm ON UPDATE");
		
		
		//prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		
		//remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.batt_widget_layout);	
			        	
		remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.batt_widget_layout);
			        
		
		/**
		 * For WIFI
		 */
		
		WifiManager wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
		 if (wm.isWifiEnabled()) {

//			 mHandler.post(new Runnable() {
//	                @Override
//	                public void run() {
			 
	                	remoteViews.setImageViewResource(R.id.wifi_toggle, R.drawable.wifi_on);
	                	
//	                }
//	            });
			 
			 //Log.v("UpdateWidget", "rnm WIFI ON");
			 
		 }else{
			 
			 //Log.v("UpdateWidget", "rnm WIFI OFF");
			 
//			 mHandler.post(new Runnable() {
//	                @Override
//	                public void run() {
	                	remoteViews.setImageViewResource(R.id.wifi_toggle, R.drawable.wifi_off);
//	                }
//	            });
			 
		 }
		
		
		/**
		 * 
		 * for MODE
		 */
		int mode = prefs.getInt(BatterySaver.MODE, 1);
		
		if(mode == 1){
			
			//Log.v("UpdateWidget", "MODE ON");
			
			remoteViews.setImageViewResource(R.id.mode_toggle, R.drawable.mode_on);
			
//			ComponentName thisWidget = new ComponentName(getApplicationContext(),BattWidgetProvider.class);
//			appWidgetManager.updateAppWidget(thisWidget, remoteViews);
			
		}else{
			//Log.v("UpdateWidget", "rnm MODE OFF");
			remoteViews.setImageViewResource(R.id.mode_toggle, R.drawable.mode_off);
			
//			ComponentName thisWidget = new ComponentName(getApplicationContext(),BattWidgetProvider.class);
//			appWidgetManager.updateAppWidget(thisWidget, remoteViews);
		}
		
		 
		
		
		/**
		 * for DATA
		 */
		
		 if(isDATANetworkON()){
			 
			 //Log.v("UpdateWidget", "rnm DATA ON");
			 remoteViews.setImageViewResource(R.id.data_toggle, R.drawable.data_on);
//			 ComponentName thisWidget = new ComponentName(getApplicationContext(),BattWidgetProvider.class);
//			 appWidgetManager.updateAppWidget(thisWidget, remoteViews);
			 
		 }else{
			 //Log.v("UpdateWidget", "rnm DATA OFF");
			 remoteViews.setImageViewResource(R.id.data_toggle, R.drawable.data_off);
//			 ComponentName thisWidget = new ComponentName(getApplicationContext(),BattWidgetProvider.class);
//			 appWidgetManager.updateAppWidget(thisWidget, remoteViews);
			 
		 }
		 
		 
		// ComponentName thisWidget = new ComponentName(getApplicationContext(),BattWidgetProvider.class);
		 
//		 Toast.makeText(getApplicationContext(),
//                 "Widget Refreshed", Toast.LENGTH_SHORT)
//                 .show();

		 
		 AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
		ComponentName thisWidget = new ComponentName(getApplicationContext(),BattWidgetProvider.class);
		 appWidgetManager.updateAppWidget(thisWidget, remoteViews);
		 
		 super.onStart(intent, startId);
		 
//		}else{
//			Toast.makeText(getApplicationContext(),
//                    "Please install the PRO version to enable Widget functionality", Toast.LENGTH_LONG)
//                    .show();
//		}
		 stopSelf();

	}

	
	//check of DATA net is on - UPDATE ALL COPIES OF METHOD
	boolean isDATANetworkON(){
	
	boolean HaveConnectedMobile = false;
	ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
	NetworkInfo ni = cm.getActiveNetworkInfo();
	if ( ni != null )
	{
	    
	    if (ni.getType() == ConnectivityManager.TYPE_MOBILE)
	        if (ni.isConnectedOrConnecting())
	            HaveConnectedMobile = true;
	}
	return HaveConnectedMobile;
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
