package com.rainmaker.android.batterysaver.ap;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.rainmaker.android.batterysaver.ap.WifiONService.WifiWorkerON;

import android.app.ActivityManager;
import android.app.Service;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.util.Log;

public class WifiOFFService extends Service{

	static String TAG = "WifiOFFService";
	SharedPreferences prefs; 
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		prefs = PreferenceManager.getDefaultSharedPreferences(WifiOFFService.this);
		//Log.v(TAG, "rnm WifiOFFService Started");
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		
		new WifiWorkerOFF().execute();
	 
	}
	
	
	boolean isSleeping(){
		
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		
		if(pm.isScreenOn()){
			return false;
		}else{
			return true;
		}
		
	}
	
	boolean sleepValue(){
		
		int sleep_option_selected = prefs.getInt(BatterySaver.SLEEP, 3);
		
		
		if(sleep_option_selected == 1){
			
			return false;
		
		}else{
			return true;
		}
		
		
	}
	
	
	class WifiWorkerOFF extends AsyncTask<Void, Void, Void>{

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			stopSelf();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			if(isSleeping()){
				
				//Log.v(TAG, "rnm IS SLEEPING so check Sleep VALUE");
				
						if(sleepValue()){
							//Log.v(TAG, "rnm User wants INT ON in SLEEP so DO NOTHING");
							
							turnNetworkOn();
							
						}else{
							
							//Log.v(TAG, "rnm User wants INT OFF in SLEEP so OFF NOW");
							
							turnNetworkOff();
						}
					
					
				}else{ //isSleeping - phone is awake
					
					//Log.v(TAG, "rnm PHONE AWAKE, so check APP-TEST");
					
					if(appTest() == true){
						
						//Log.v(TAG, "rnm NO MARKED APPS FOUND SO WIFI OFF");
						
						turnNetworkOff();
						
					}else{
						//Log.v(TAG, "rnm MARKED APPS FOUND SO WIFI STAYS ON");
						
					}
					
				}
				
			return null;
		}
		
	
		
	void turnNetworkOn(){
			
			WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE); 
			
			int networkType = prefs.getInt(BatterySaver.NETWORK, 1);
			
			if(networkType == 1){
				
				  if (!wm.isWifiEnabled()) { 
		              wm.setWifiEnabled(true); 
		              //Log.v(TAG, "rnm WIFI ON SUCCESS");
				  }
				
			}else if(networkType == 0){
				
				setMobileDataEnabled(getApplicationContext(), true);
				
			}else if(networkType == 2){
				
			//	wm.startScan();	
				if (!wm.isWifiEnabled()) { 
		              wm.setWifiEnabled(true); 
		              //Log.v(TAG, "rnm WIFI ON SUCCESS");
				  }
			   	setMobileDataEnabled(getApplicationContext(), true);
			}
	}
		
		
		
		void turnNetworkOff(){
			
			WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE); 
		
			int networkType = prefs.getInt(BatterySaver.NETWORK, 1);
			
				if(networkType == 1){
					
					  if (wm.isWifiEnabled()) { 
			              wm.setWifiEnabled(false); 
			              //Log.v(TAG, "rnm WIFI OFF SUCCESS");
					  }
					  
					
				}else if(networkType == 0){
					
					setMobileDataEnabled(getApplicationContext(), false);
					
				}else if(networkType == 2){
					
						if (wm.isWifiEnabled()) { 
				              wm.setWifiEnabled(false); 
				              //Log.v(TAG, "rnm WIFI OFF SUCCESS");
						  }
						setMobileDataEnabled(getApplicationContext(), false);
				}
			
		} //close turnNetworkOff
		
		
		
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
//				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			}
		    
		    
		}
		
		
		
		boolean appTest(){
			
			boolean tester = true;
			
			DBHelper dbHelper=new DBHelper(WifiOFFService.this);
			ArrayList<String> apps = dbHelper.getAppPackages();
			
			//Log.v("DBApps : rnm", apps.toString());
			
			ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService( Context.ACTIVITY_SERVICE );
			List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
			
			for(RunningAppProcessInfo appProcess : appProcesses){
				
				//Log.i("APP TEST rnm", appProcess.importance +" - "+RunningAppProcessInfo.IMPORTANCE_VISIBLE);
		    	
		    	//Log.i("APP TEST2 FOREGRD rnm ",appProcess.processName+" - "+ appProcess.importance +" - "+RunningAppProcessInfo.IMPORTANCE_FOREGROUND);
		    	
			    if(appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND){
			    	
			    	
			        //Log.i("rnm Current FOREGROUND App", appProcess.processName);
			        
			        	if(apps.contains(appProcess.processName)){
			        		
			        		//Log.i("rnm Foreground App FOUND", appProcess.processName);
			        		tester =  false;
			        		return false;
			        		
			        	}else{
			        		tester = true;
			        	}
			    }else {
			    	
			    	tester = true;
			    	
			    }
			}
			
			return tester;
			
		} //appTest
		
		
		boolean isModeOn(){
			
			int ison = prefs.getInt(BatterySaver.MODE, 3);
				
				if(ison == 1){
					return true;
				}else{
					return false;
				}
			
		} //ismodeon
		
	}
	

//	public int getWifiOrData(){
//		
//		WifiManager wifi = (WifiManager) getSystemService(WIFI_SERVICE);
//		
//		wifi.startScan();
//		
//		List<ScanResult> scannedNets = wifi.getScanResults();
//		
//		List<WifiConfiguration> savedNets = wifi.getConfiguredNetworks();
//		
//		// 0 = wifi, 1 = data net
//		int result = 1;
//		
//		Log.v(TAG, "Lists: "+scannedNets.size()+" - "+savedNets.size() );
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
//									Log.v(TAG, "Network found: "+ sc.SSID);
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
//	}	

}
