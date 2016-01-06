package com.rainmaker.android.batterysaver.ap;

import static com.google.android.gcm.GCMConstants.DEFAULT_INTENT_SERVICE_CLASS_NAME;

import java.util.Iterator;
import java.util.List;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMBroadcastReceiver;
import com.google.android.gcm.GCMRegistrar;
import com.rainmaker.android.batterysaver.ap.BatterySaver.GetAppListAsync;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.util.Log;


public class AllBroadcastReceiver extends BroadcastReceiver {
	
	static String TAG = "AlarmReceiver";
	private static boolean mReceiverSet = false;
	
	BatterySaver batSave;
	
	SharedPreferences prefs;

//	public AllBroadcastReceiver(BatterySaver batSave){
//		
//		this.batSave = batSave;
//		
//	}
	

@Override
public void onReceive(Context context, Intent intent) {

	 String action = intent.getAction();
	 
	 prefs = PreferenceManager.getDefaultSharedPreferences(context);
	 
	 int modeOnOff = prefs.getInt(BatterySaver.MODE, 0);
	 
	 //Log.v(TAG, "rnm ACTION : "+action);
	 
	    if(action.equalsIgnoreCase("android.intent.action.BOOT_COMPLETED")){
	    
	    	Intent i = new Intent("com.rainmaker.android.batterysaver.AlarmCreator");
	    	  i.setClass(context, AlarmCreator.class);
	    	  context.startService(i);
	       
	    }else if(action.equalsIgnoreCase("com.rainmaker.android.batterysaver.ALARM1") && modeOnOff == 1){
	    	   
	    	   //Log.v(TAG, "rnm GOT ALARM BROADCAST 1");
	   		Intent i = new Intent("com.rainmaker.android.batterysaver.WifiONService");
	   		i.setClass(context, WifiONService.class);
	   		context.startService(i);
	    	   
	       }else if(action.equalsIgnoreCase("com.rainmaker.android.batterysaver.ALARM2") && modeOnOff == 1){
	    	
	    	   //Log.v(TAG, "rnm GOT ALARM BROADCAST 2");
	   		Intent i = new Intent("com.rainmaker.android.batterysaver.WifiOFFService");
	   		i.setClass(context, WifiOFFService.class);
	   		context.startService(i);
	   		
	       }else if(action.equalsIgnoreCase(Intent.ACTION_SCREEN_ON) && modeOnOff == 1){
	    	   
	    	   //Log.v(TAG, "rnm GOT ALARM BROADCAST SCREEN ON");
		   		Intent i = new Intent("com.rainmaker.android.batterysaver.NetworkScreenOnHelper");
		   		i.setClass(context, NetworkScreenOnHelper.class);
		   		context.startService(i);
	    	
	       }
//	       else if(action.equalsIgnoreCase("android.net.wifi.SCAN_RESULTS") && modeOnOff == 1){
//	    	   
//	    	   //Log.v(TAG, "rnm GOT android.net.wifi.SCAN_RESULTS");
//	    	   
//	    	   int wifiOrData = getWifiOrData();
//	    	   
//	    	   		if(wifiOrData == 1){
//	    	   			
//	    	   			batSave.setMobileDataEnabled(batSave, true);
//	    	   			
//	    	   		}else{
//	    	   			
//	    	   			batSave.turnWIFIOn();
//	    	   			
//	    	   		}
	    	   
	    	
	     //  }
	    
	   
}


//		public int getWifiOrData(){
//		
////		WifiManager wifi = (WifiManager) batSave.getSystemService(Context.WIFI_SERVICE);
//		
//		List<ScanResult> scannedNets = batSave.wifiMgr.getScanResults();
//		
//		List<WifiConfiguration> savedNets = batSave.wifiMgr.getConfiguredNetworks();
//		
//		// 0 = wifi, 1 = data net
//		int result = 1;
//		
//		//Log.v(TAG, "Lists: "+scannedNets.size()+" - "+savedNets.size() );
//		
//		Iterator<ScanResult> iteratorScannedNets = scannedNets.iterator();
//		
//		Iterator<WifiConfiguration> iteratorSavedNets = savedNets.iterator();
//		
//			while(iteratorScannedNets.hasNext()){
//				
//				ScanResult sc = iteratorScannedNets.next();
//				
//					while(iteratorSavedNets.hasNext()){
//						
//						WifiConfiguration wc = iteratorSavedNets.next();
//						
//								if(sc.SSID.equalsIgnoreCase(wc.SSID)){
//									
//									result = 0;
//									//Log.v(TAG, "Network found: "+ sc.SSID);
//									break;
//								}
//						
//						
//					}
//				
//			}
//		
//		
//		
//		return result;
//		}

		

/**
 * Gets the class name of the intent service that will handle GCM messages.
 */
protected String getGCMIntentServiceClassName(Context context) {
    return getDefaultIntentServiceClassName(context);
}

/**
 * Gets the default class name of the intent service that will handle GCM
 * messages.
 */
static final String getDefaultIntentServiceClassName(Context context) {
    String className = context.getPackageName() +
            DEFAULT_INTENT_SERVICE_CLASS_NAME;
    return className;
}


}