 package com.rainmaker.android.batterysaver.ap;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.rainmaker.android.batterysaver.ap.BatterySaver.GetAppListAsync;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.sax.StartElementListener;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.RemoteViews.RemoteView;
import android.widget.Toast;

public class BattWidgetProvider extends AppWidgetProvider{
	
	public static String ACTION_MODE_ONOFF = "modeonoff";
//	public static String ACTION_MODE_OFF = "modeoff";
	
	public static String ACTION_WIFI_ONOFF = "wifionoff";
	
//	public static String ACTION_WIFI_OFF = "wifioff";
//	public static String ACTION_WIFI_OFF = "wifioff";
	
	public static String ACTION_DATA_ONOFF = "dataonoff";
	//public static String ACTION_DATA_OFF = "dataoff";
	
	public static String ACTION_SETTINGS_SHOW = "settings";
	
	public static String ACTION_REFRESH = "refresh";
	
	RemoteViews remoteViews;
	
	SharedPreferences prefs;
	
	Context ctx;
	
	
	private RemoteViews buildUpdate(Context context, int[] appWidgetIds) {
		
		//Log.v("WIDGET ", "rnm onUPDATE from onUpdate method");
		this.ctx=context;
		
//		for (int widgetId : appWidgetIds) {
			
			
		remoteViews = new RemoteViews(context.getPackageName(), R.layout.batt_widget_layout);
	
		
		/**
		 * Set all current values to buttons
		 */
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		
		
		/**
		 * 
		 * for MODE
		 */
		int mode = prefs.getInt(BatterySaver.MODE, 1);
		
		if(mode == 1){
			
			remoteViews.setImageViewResource(R.id.mode_toggle, R.drawable.mode_on);
			
		}else{
			remoteViews.setImageViewResource(R.id.mode_toggle, R.drawable.mode_off);
		}
		
		/**
		 * For WIFI
		 */
		
		WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		 if (wm.isWifiEnabled()) {
			 
			 remoteViews.setImageViewResource(R.id.wifi_toggle, R.drawable.wifi_on);
			 
		 }else{
			 
			 remoteViews.setImageViewResource(R.id.wifi_toggle, R.drawable.wifi_off);
		 }
		
		/**
		 * for DATA
		 */
		
		 if(isDATANetworkON()){
			 
			 remoteViews.setImageViewResource(R.id.data_toggle, R.drawable.data_on);
			 
		 }else{
			 
			 remoteViews.setImageViewResource(R.id.data_toggle, R.drawable.data_off);
			 
		 }
		 
		
		Intent modeONOFFIntent = new Intent(context, WidgetModeChangeServiceON.class);
		modeONOFFIntent.setAction(ACTION_MODE_ONOFF);
		
//		modeONIntent.putExtra("mode", 1);
		
		PendingIntent modeONOFFPendingIntent = PendingIntent.getService(context, 50, modeONOFFIntent, 0);
		
		
		Intent modeWIFIOnOFFIntent = new Intent(context, WidgetWIFIOnService.class);
		modeWIFIOnOFFIntent.setAction(ACTION_WIFI_ONOFF);			
		
		PendingIntent wifiONPendingIntent = PendingIntent.getService(context, 70, modeWIFIOnOFFIntent, 0);
		
		Intent modeDATAOnOffIntent = new Intent(context, WidgetDATAOnService.class);
		modeDATAOnOffIntent.setAction(ACTION_DATA_ONOFF);
		
		PendingIntent dataONOFFPendingIntent = PendingIntent.getService(context, 80, modeDATAOnOffIntent, 0);
		
		Intent settings = new Intent(context, BatterySaver.class);
//		settings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		settings.putExtra("from_widget", true);
		settings.setAction(ACTION_SETTINGS_SHOW);			
		
		PendingIntent settingsPendingIntent = PendingIntent.getActivity(context, 90, settings, 0);
		
		Intent refreshIntent = new Intent(context,UpdateWidget.class);
		
		refreshIntent.setAction(ACTION_REFRESH);
//		refreshIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
		refreshIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
		
		PendingIntent refreshPendingIntent = PendingIntent.getService(context, 100, refreshIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		
//		PendingIntent refreshPendingIntent = PendingIntent.getBroadcast(context, 100, refreshIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		Intent intent = new Intent(context, BattWidgetProvider.class);

	      intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
	      intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

	      PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
	          0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	      
	      remoteViews.setOnClickPendingIntent(R.id.textView1, pendingIntent);


		remoteViews.setOnClickPendingIntent(R.id.refresh, refreshPendingIntent);
		
		remoteViews.setOnClickPendingIntent(R.id.mode_toggle,modeONOFFPendingIntent);
		remoteViews.setOnClickPendingIntent(R.id.wifi_toggle, wifiONPendingIntent);
		
		remoteViews.setOnClickPendingIntent(R.id.settings_btn, settingsPendingIntent);
		
		remoteViews.setOnClickPendingIntent(R.id.data_toggle, dataONOFFPendingIntent);
		
//		}  //for loop
		
	return (remoteViews);
		
		
	}
	
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);				 
		
//		  context.startService(new Intent(context, UpdateService.class));		  
		
		
//		RemoteViews remoteViews2 = new RemoteViews(context.getPackageName(), R.layout.batt_widget_layout);
//		
//		Intent intent = new Intent(context, BattWidgetProvider.class);
//
//	      intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//	      intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
//
//	      PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
//	          0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//	      
//	      remoteViews2.setOnClickPendingIntent(R.id.textView1, pendingIntent);
//		
		  ComponentName me=new ComponentName(context, BattWidgetProvider.class);
		
		appWidgetManager.updateAppWidget(me, buildUpdate(context, appWidgetIds));
		
	}
	
	
//	 public static class UpdateService extends Service {
//		 
//		  SharedPreferences prefs;
//  		
//  		Context ctx;
//	       
//	        @Override
//	        public void onStart(Intent intent, int startId) {
//	            // Build the widget update for today
//	            RemoteViews updateViews = buildUpdate(this);
//
//	            // Push update for this widget to the home screen
//	            ComponentName thisWidget = new ComponentName(this,BattWidgetProvider.class);
//	            AppWidgetManager manager = AppWidgetManager.getInstance(this);
//	            manager.updateAppWidget(thisWidget, updateViews);
//	            
//	            stopSelf();
//	            
//	        }
//	        
//	      public RemoteViews buildUpdate(Context context) {
//	    	  
////	    	  SharedPreferences prefs;
////	    		
////	    		Context ctx;
//	    	  
//	    	  this.ctx=context;
//	           
//	                      
//	        // Build an update that holds the updated widget contents
//	         RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.batt_widget_layout);
//
//	         /**
//	 		 * Set all current values to buttons
//	 		 */
//	 		prefs = PreferenceManager.getDefaultSharedPreferences(context);
//	 		
//	 		
//	 		/**
//	 		 * 
//	 		 * for MODE
//	 		 */
//	 		int mode = prefs.getInt(BatterySaver.MODE, 1);
//	 		
//	 		if(mode == 1){
//	 			
//	 			updateViews.setImageViewResource(R.id.mode_toggle, R.drawable.mode_on);
//	 			
//	 		}else{
//	 			updateViews.setImageViewResource(R.id.mode_toggle, R.drawable.mode_off);
//	 		}
//	 		
//	 		/**
//	 		 * For WIFI
//	 		 */
//	 		
//	 		WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//	 		 if (wm.isWifiEnabled()) {
//	 			 
//	 			updateViews.setImageViewResource(R.id.wifi_toggle, R.drawable.wifi_on);
//	 			 
//	 		 }else{
//	 			 
//	 			updateViews.setImageViewResource(R.id.wifi_toggle, R.drawable.wifi_off);
//	 		 }
//	 		
//	 		/**
//	 		 * for DATA
//	 		 */
//	 		
//	 		 if(isDATANetworkON()){
//	 			 
//	 			updateViews.setImageViewResource(R.id.data_toggle, R.drawable.data_on);
//	 			 
//	 		 }else{
//	 			 
//	 			updateViews.setImageViewResource(R.id.data_toggle, R.drawable.data_off);
//	 			 
//	 		 }
//	 		 
//	 		
//	 		Intent modeONOFFIntent = new Intent(context, WidgetModeChangeServiceON.class);
//	 		//modeONOFFIntent.setAction(ACTION_MODE_ONOFF);
//	 		modeONOFFIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//	 		
////	 		modeONIntent.putExtra("mode", 1);
//	 		
//	 		PendingIntent modeONOFFPendingIntent = PendingIntent.getService(context, 0, modeONOFFIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//	 		
//	 		
//	 		Intent modeWIFIOnOFFIntent = new Intent(context, WidgetWIFIOnService.class);
//	 		//modeWIFIOnOFFIntent.setAction(ACTION_WIFI_ONOFF);	
//	 		modeWIFIOnOFFIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//	 		
//	 		
//	 		PendingIntent wifiONPendingIntent = PendingIntent.getService(context, 10, modeWIFIOnOFFIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//	 		
//	 		Intent modeDATAOnOffIntent = new Intent(context, WidgetDATAOnService.class);
//	 		//modeDATAOnOffIntent.setAction(ACTION_DATA_ONOFF);
//	 		modeDATAOnOffIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//	 		
//	 		PendingIntent dataONOFFPendingIntent = PendingIntent.getService(context, 20, modeDATAOnOffIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//	 		
//	 		Intent settings = new Intent(context, Preferences.class);
//	 		//settings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//	 		//settings.setAction(ACTION_SETTINGS_SHOW);	
//	 		settings.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//	 		
//	 		PendingIntent settingsPendingIntent = PendingIntent.getActivity(context, 30, settings, PendingIntent.FLAG_UPDATE_CURRENT);
//	 		
//	 		Intent refreshIntent = new Intent(context,UpdateWidget.class);
//	 		
//	 		//refreshIntent.setAction(ACTION_REFRESH);
//	 		refreshIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
////	 		refreshIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//	 	//	refreshIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
//	 		
//	 		PendingIntent refreshPendingIntent = PendingIntent.getService(context, 40, refreshIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//	 		
////	 		PendingIntent refreshPendingIntent = PendingIntent.getBroadcast(context, 100, refreshIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//	 		
//	 		//Intent intent = new Intent(context, BattWidgetProvider.class);
//
//	 	   //   intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//	 	   //   intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
//
//	 	   //   PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
//	 	    //      0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//	 	      
//	 	   //  updateViews.setOnClickPendingIntent(R.id.textView1, pendingIntent);
//
//
//	 	    updateViews.setOnClickPendingIntent(R.id.refresh, refreshPendingIntent);
//	 		
//	 	   updateViews.setOnClickPendingIntent(R.id.mode_toggle,modeONOFFPendingIntent);
//	 	  updateViews.setOnClickPendingIntent(R.id.wifi_toggle, wifiONPendingIntent);
//	 		
//	 	 updateViews.setOnClickPendingIntent(R.id.settings_btn, settingsPendingIntent);
//	 		
//	 	updateViews.setOnClickPendingIntent(R.id.data_toggle, dataONOFFPendingIntent);           
////	         Intent intent = new Intent(this,ButtonClickActivity.class);
////	         intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
////	                
////	         PendingIntent pendingIntent = 
////	            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
////	           
////	         updateViews.setOnClickPendingIntent(R.id.button1, pendingIntent);
//	 	
//	         
//	         return updateViews;
//
//	      }

//	       		@Override
//			public IBinder onBind(Intent arg0) {
//				// TODO Auto-generated method stub
//				return null;
//			}
	       		
//	       		@Override
//	       		public void onConfigurationChanged(Configuration newConfig) {
//	       			// TODO Auto-generated method stub
//	       			super.onConfigurationChanged(newConfig);
//	       			int oldOrientation = this.getResources().getConfiguration().orientation;
//
//	                if(newConfig.orientation != oldOrientation)
//	                {
//	                    // Update the widget
//	                    RemoteViews remoteView = buildUpdate(this);
//
//	                    // Push update to homescreen
//	                    pushUpdate(remoteView);
//	                }
//	                else if(newConfig.orientation == oldOrientation)
//	                {
//	                    // Update the widget
//	                    RemoteViews remoteView = buildUpdate(this);
//
//	                    // Push update to homescreen
//	                    pushUpdate(remoteView);
//	                }
//	                
//	       			
//	       		}
//	       		
//	       		private void pushUpdate(RemoteViews remoteView)
//	            {
////	                	       	
//	       			ComponentName myWidget = new ComponentName(this, BattWidgetProvider.class);
//	                AppWidgetManager manager = AppWidgetManager.getInstance(this);
//	                manager.updateAppWidget(myWidget, remoteView);
//	            }
//	       		
	       	 boolean isDATANetworkON(){
					
	     		
	     		boolean HaveConnectedMobile = false;
	     		ConnectivityManager cm = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
	     		NetworkInfo ni = cm.getActiveNetworkInfo();
	     		if ( ni != null )
	     		{
	     		    
	     		    if (ni.getType() == ConnectivityManager.TYPE_MOBILE)
	     		        if (ni.isConnectedOrConnecting())
	     		            HaveConnectedMobile = true;
	     		}
	     		return HaveConnectedMobile;
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
//	     					e.printStackTrace();
	     				} catch (SecurityException e) {
	     					// TODO Auto-generated catch block
//	     					e.printStackTrace();
	     				} catch (NoSuchFieldException e) {
	     					// TODO Auto-generated catch block
//	     					e.printStackTrace();
	     				} catch (IllegalArgumentException e) {
	     					// TODO Auto-generated catch block
//	     					e.printStackTrace();
	     				} catch (IllegalAccessException e) {
	     					// TODO Auto-generated catch block
//	     					e.printStackTrace();
	     				} catch (NoSuchMethodException e) {
	     					// TODO Auto-generated catch block
//	     					e.printStackTrace();
	     				} catch (InvocationTargetException e) {
	     					// TODO Auto-generated catch block
//	     					e.printStackTrace();
	     				}
	     			    
	     			    
	     			}
	     		  	     		 

	        
	    


	
//	@Override
//	public void onEnabled(Context context) {
//		// TODO Auto-generated method stub
//		super.onEnabled(context);
//		
//		ComponentName me=new ComponentName(context, BattWidgetProvider.class);
//
//		AppWidgetManager manager = AppWidgetManager.getInstance(context);
//		
////		appWidgetManager.updateAppWidget(me, buildUpdate(context, appWidgetIds));
//		
//		  manager.updateAppWidget(me, buildUpdate(context, manager.getAppWidgetIds(me)));
//		  
//	}

	@Override
	public void onReceive(Context context, Intent intent) {			
		  super.onReceive(context, intent);
		  
		//  //Log.v("rnm FROM WIDGET ", "in OnReceive "+intent.getAction());
		  

//		  ComponentName me=new ComponentName(context, BattWidgetProvider.class);
//
//		AppWidgetManager manager = AppWidgetManager.getInstance(context);
//		  manager.updateAppWidget(me, buildUpdate(context, manager.getAppWidgetIds(me)));
		  
		//  AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		  
//		    ComponentName thisAppWidget = new ComponentName(context.getPackageName(),  BattWidgetProvider.class.getName());
		    
//		    int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
		    
		 //   int[] appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
		    
		//    onUpdate(context, appWidgetManager, appWidgetIds);
	
	}
	
	
//  boolean isDATANetworkON(){
//				
//		
//		boolean HaveConnectedMobile = false;
//		ConnectivityManager cm = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
//		NetworkInfo ni = cm.getActiveNetworkInfo();
//		if ( ni != null )
//		{
//		    
//		    if (ni.getType() == ConnectivityManager.TYPE_MOBILE)
//		        if (ni.isConnectedOrConnecting())
//		            HaveConnectedMobile = true;
//		}
//		return HaveConnectedMobile;
//		}
//		
//		
//		  private void setMobileDataEnabled(Context context, boolean enabled) {
//			    final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//			    
//			    Class conmanClass;
//			    
//				try {
//					conmanClass = Class.forName(conman.getClass().getName());
//					final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
//					
//					iConnectivityManagerField.setAccessible(true);
//				    final Object iConnectivityManager = iConnectivityManagerField.get(conman);
//				    final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
//				    final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
//				    setMobileDataEnabledMethod.setAccessible(true);
//
//				    setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
//					
//				} catch (ClassNotFoundException e) {
//					// TODO Auto-generated catch block
////					e.printStackTrace();
//				} catch (SecurityException e) {
//					// TODO Auto-generated catch block
////					e.printStackTrace();
//				} catch (NoSuchFieldException e) {
//					// TODO Auto-generated catch block
////					e.printStackTrace();
//				} catch (IllegalArgumentException e) {
//					// TODO Auto-generated catch block
////					e.printStackTrace();
//				} catch (IllegalAccessException e) {
//					// TODO Auto-generated catch block
////					e.printStackTrace();
//				} catch (NoSuchMethodException e) {
//					// TODO Auto-generated catch block
////					e.printStackTrace();
//				} catch (InvocationTargetException e) {
//					// TODO Auto-generated catch block
////					e.printStackTrace();
//				}
//			    
//			    
//			}

}


