package com.rainmaker.android.batterysaver.ap;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
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

public class WifiONService extends Service {

	static String TAG="WifiONService";
	SharedPreferences prefs; 
	
	/* (non-Javadoc)
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		prefs = PreferenceManager.getDefaultSharedPreferences(WifiONService.this);
		
		//Log.v(TAG, "rnm WifiONService Started");
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		
		new WifiWorkerON().execute();
	 
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

			
			if(isModeOn()){
				
				if(isSleeping()){
					//Log.v(TAG, "Phone is SLEEPING rnm");
						
						if(sleepValue()){
							
							//Log.v(TAG, "rnm user wants Internet OFF in SLEEP so DO NOTHING");
							turnNetworkOff();
							
						}else{
							//Log.v(TAG, "user wants Internet in SLEEP so Internet ON rnm");
							
							turnNetworkOn();
						}
					
					
				}else{
					
					//Log.v(TAG, "rnm NOT SLEEPING.. SO TURN ON");
					turnNetworkOn();
					
					
				}
				
				
			}else{
				//mode off so do nothing
				//Log.v(TAG, "rnm mode OFF so do nothing");	
			}
			
			
			
			return null;
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
					
					setMobileDataEnabled(getApplicationContext(), false);
					
					if (wm.isWifiEnabled()) { 
			              wm.setWifiEnabled(false); 
			              //Log.v(TAG, "rnm WIFI OFF SUCCESS");
					  }
					
				}
			
		} //close turnNetworkOff
		
		
	
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
				
				if (!wm.isWifiEnabled()) { 
		              wm.setWifiEnabled(true); 
		              //Log.v(TAG, "rnm WIFI ON SUCCESS");
				  }
				setMobileDataEnabled(getApplicationContext(), true);
			}
	              

		} // close turnWIFIOn
		
		
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
				
				return true;
			
			}else{
				return false;
			}
			
			
		}
		
		
		boolean isModeOn(){
			
			int ison = prefs.getInt(BatterySaver.MODE, 3);
				
				if(ison == 1){
					return true;
				}else{
					return false;
				}
			
		} //ismodeon
		
		
		
		
		
	} // Async Ends
	
	

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
