package com.rainmaker.android.batterysaver.ap;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class NetworkScreenOnHelper extends Service{

	
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
		prefs = PreferenceManager.getDefaultSharedPreferences(NetworkScreenOnHelper.this);
		
		if(prefs.getInt(BatterySaver.WAKE_UP_NET, 1) == 1 && prefs.getInt(BatterySaver.MODE, 0) == 1){

			new WifiWorkerON().execute();
		}
		
	}
	
	
	class WifiWorkerON extends AsyncTask<Void, Void, Void>{

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			stopSelf();
		}
		@Override
		protected Void doInBackground(Void... params) {

			int onoff = prefs.getInt(BatterySaver.WAKE_UP_NET, 1);
			
			int networkType = prefs.getInt(BatterySaver.NETWORK, 1);

			if(onoff ==1){
				
				//Log.v("rnm NetworkScreenOnHelper", "onoff = 1 so net ON");
				
						if(networkType == 1){
							
							WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE); 
							
							  if (!wm.isWifiEnabled()) { 
					              wm.setWifiEnabled(true); 
					              //Log.v("rnm NetworkScreenOnHelper", "WIFI ON FROM SCREEN ON");
							  }
							  
						}else if(networkType == 0){
							
							setMobileDataEnabled(getApplicationContext(), true);
						
						}else if(networkType == 2){
							
							WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE); 
							wm.startScan();
						}
			}
			
			
			return null;
		}


}
	
	
	private void setMobileDataEnabled(Context context, boolean enabled) {
	    final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    
	    Class conmanClass;
		try {
			conmanClass = Class.forName(conman.getClass().getName());
			final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
			
			iConnectivityManagerField.setAccessible(true);
		    final Object iConnectivityManager = iConnectivityManagerField.get(conman);
		    final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
		    final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
		    setMobileDataEnabledMethod.setAccessible(true);

		    setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
	    
	    
	}
	
}