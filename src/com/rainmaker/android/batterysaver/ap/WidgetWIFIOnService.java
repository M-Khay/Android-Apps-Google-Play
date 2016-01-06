package com.rainmaker.android.batterysaver.ap;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class WidgetWIFIOnService extends Service{
	
	RemoteViews remoteViews;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);		
				
//		if(isPro()){
			
		 remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.batt_widget_layout);				
						
		WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
		 if (!wm.isWifiEnabled()) {
			 
			 setMobileDataEnabled(getApplicationContext(), false);
			 
            wm.setWifiEnabled(true);       
            
            //Log.v("WidgetWIFIOnService", "rnm WIFI ON");
            
            remoteViews.setImageViewResource(R.id.wifi_toggle, R.drawable.wifi_on); 
            remoteViews.setImageViewResource(R.id.data_toggle, R.drawable.data_off); 
            
		    Toast.makeText(getApplicationContext(),
		    		getString(R.string.wifion), Toast.LENGTH_SHORT)
                    .show();
		  }		
		 
		 else if(wm.isWifiEnabled()) { 
	            wm.setWifiEnabled(false);
	        
	            //Log.v("WidgetWIFIOnService", "rnm WIFI OFF");
	            
	            remoteViews.setImageViewResource(R.id.wifi_toggle, R.drawable.wifi_off); 
	            
			    Toast.makeText(getApplicationContext(),
			    		getString(R.string.wifioff), Toast.LENGTH_SHORT)
	                    .show();
	            
		 }		 		
		        
		    ComponentName thisWidget = new ComponentName(this,BattWidgetProvider.class);
			AppWidgetManager manager = AppWidgetManager.getInstance(this.getApplicationContext());
			manager.updateAppWidget(thisWidget, remoteViews);
			
//		}else{
//			
//			Toast.makeText(getApplicationContext(),
//                    "Please install the PRO version to enable Widget functionality", Toast.LENGTH_LONG)
//                    .show();
//			
//		}
		 stopSelf();
		 
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
