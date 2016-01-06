package com.rainmaker.android.batterysaver.ap;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;

import com.xtcxorfr.athvsupf175270.Universal;

import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
//import android.content.Context;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.ToggleButton;

public class Preferences extends Activity {

	static String TAG="MainActivity";
	//MySpinner interval_options;
//	private RevMob revmob;
	//int changes = 0;
	private Universal airsdk; //Declare SDK here

	Button moreApps;
	//ToggleButton mode;
	RadioGroup wifiOrData;
	ToggleButton sleepMode;
	RadioButton wifi_radio;
	RadioButton data_radio;
	RadioButton auto_radio;
	protected Handler mHandler;
	//Button confirmSettings;
	Button app_settings;
	Button time_intervals;

	static String MY_AD_UNIT_ID = "a1509a603ba98aa";
	
	Button simple_mode;
	
	ToggleButton netOnWakeup;
	
	TextView sleep_mode_txt;
	String sleep_mode_msg = "";
	
	String[] timeSettings = {"DEFAULT","1 minute every 3 minutes","1 minute every 5 minutes"
			,"1 minute every 10 minutes","1 minute every 15 minutes",
			"1 minute every 30 minutes","2 minutes every 10 minutes",
			"2 minutes every 15 minutes","2 minutes every 30 minutes"};
	
	protected int[] run_for_values = {1,1,1,1,1,2,2,2}; 
	protected int[] stop_for_values = {4,6,11,16,31,11,16,31}; 
	
	SharedPreferences prefs;
	
	
	@Override
	protected void onRestart() {

		//Log.v(TAG, "onRESTART");
		if(!isPro()){

	   		RelativeLayout layout = (RelativeLayout) findViewById(R.id.relpref);	
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			
			params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			params.setMargins(0, 0, 0, 2);	
			
			}
		
			
		super.onRestart();
		
	}

	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		//Log.v(TAG, "DESTROYED");
		
	}
	
	public void onStart() {
	    super.onStart();
//	    if(!isPro()){
//	    revmob.showFullscreenAd(this);
//	    }			adView =  new AdView(this, AdSize.BANNER, MY_AD_UNIT_ID);
		

	  }  
	
	
//	public void revMobOpenAdLink(View v) {
//	    revmob.openAdLink(this);
//	  }
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
	
		super.onBackPressed();
	}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.prefs_maon);
    	try{
       airsdk.callLandingPageAd();
    	}catch(NullPointerException e)
    	{
    		Log.e("Null Pointer","null pointer at landing page");
    	}
        mHandler = new Handler();
        
//        revmob = RevMob.start(this, BatterySaver.APPLICATION_ID);
        
       
 
        sleep_mode_txt = (TextView)findViewById(R.id.sleep_mode_txt);
        wifi_radio = (RadioButton)findViewById(R.id.wifi);
        data_radio = (RadioButton)findViewById(R.id.mob_net);
        auto_radio = (RadioButton)findViewById(R.id.autonet);
        
        app_settings = (Button)findViewById(R.id.app_set);
        
        simple_mode = (Button) findViewById(R.id.simple_mode);
        
       time_intervals=(Button)findViewById(R.id.button1);
        
      //  time_intervals=(MySpinner)findViewById(R.id.spinner1);
       
       
       if(!isPro()){

   		
   		RelativeLayout layout = (RelativeLayout) findViewById(R.id.relpref);	
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			
			params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			params.setMargins(0, 0, 0, 2);	

			

   		}
       
        registerForContextMenu(time_intervals);//Register a button for context events
        time_intervals.setOnClickListener(new View.OnClickListener() {		
    		@Override
    		public void onClick(View v) {
    			openContextMenu(v);			
    		}
    	});
        
        
        simple_mode.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
try{
	
			airsdk.callOverlayAd();
			}catch(NullPointerException e)
			{
				
			}
				final Intent bat_saver = new Intent(getApplicationContext(), BatterySaver.class);
				startActivity(bat_saver);
	
		
			}
		});
        
        prefs = PreferenceManager.getDefaultSharedPreferences(Preferences.this);
        
        
        app_settings.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(),
						getString(R.string.wait), Toast.LENGTH_LONG)
	                    .show();
			
		final Intent preferences = new Intent(getApplicationContext(), AppManager.class);
			startActivity(preferences);
				
			}
		});
        
        
        netOnWakeup = (ToggleButton)findViewById(R.id.netonwakeup);
        
        netOnWakeup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				if(netOnWakeup.isChecked()){

					prefs.edit().putInt(BatterySaver.WAKE_UP_NET, 1).commit();
//					changes = 1;
					
				}else{
					
					prefs.edit().putInt(BatterySaver.WAKE_UP_NET, 0).commit();
					
//					changes = 1;
					
				}
				
			}
		});
        
        
                
        wifiOrData = (RadioGroup) findViewById(R.id.radioGroup1);
        
        wifiOrData.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				
				if(checkedId == R.id.wifi){
					
					//Log.v(TAG, "WIFI SELECTED rnm");
					prefs.edit().putInt(BatterySaver.NETWORK, 1).commit();
					on_offWifi(true);
					setMobileDataEnabled(getApplicationContext(), false);
//					changes = 1;
					
					
				}else if (checkedId == R.id.mob_net){
					
					//Log.v(TAG, "DATA SELECTED rnm");
					prefs.edit().putInt(BatterySaver.NETWORK, 0).commit();
					on_offWifi(false);
					setMobileDataEnabled(getApplicationContext(), true);
//					changes = 1;
					
				}
                else if (checkedId == R.id.autonet){
					
                	prefs.edit().putInt(BatterySaver.NETWORK, 2).commit();
					
//					WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
					
					//wifiMgr.startScan();
					on_offWifi(true);
					setMobileDataEnabled(getApplicationContext(), true);
					
				}
				
			}

			
		});
        
        
        sleepMode = (ToggleButton)findViewById(R.id.sleep_mode);
        
        sleepMode.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				changes = 1;
				if(sleepMode.isChecked() == true){
					try{
						airsdk.callVideoAd();
					}catch(NullPointerException e)
					{
						
					}
					prefs.edit().putInt(BatterySaver.SLEEP, 1).commit();
					
					sleep_mode_txt.setText(getResources().getString(R.string.slp_on));
					//Log.v(TAG, "rnm SLEEP EVENT 0");
					// RECREATE ALARMS
					
					Toast.makeText(getApplicationContext(),
							getString(R.string.psslpon), Toast.LENGTH_SHORT)
	                        .show();
					
					
				}else{
					prefs.edit().putInt(BatterySaver.SLEEP, 0).commit();
					sleep_mode_txt.setText(getResources().getString(R.string.slp_off));
					//Log.v(TAG, "rnm SLEEP EVENT 0");
					// RECREATE ALARMS
					try{
					airsdk.callAppWall();
					}catch(NullPointerException e)
					{
						
					}
					Toast.makeText(getApplicationContext(),
							getString(R.string.psslpoff), Toast.LENGTH_SHORT)
	                        .show();
					
				}
				
			}
		});
       
        
        new SetPreviousValuesAsync().execute();
        
//        }else{
//        	Toast.makeText(getApplicationContext(),
//                    "Please install the PRO version to enable Advanced Settings", Toast.LENGTH_LONG)
//                    .show();
//        	finish();
//        }
        
    } // onCreate END
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
    		ContextMenuInfo menuInfo) {
    	// TODO Auto-generated method stub
    	super.onCreateContextMenu(menu, v, menuInfo);
    	
    	getMenuInflater().inflate(R.menu.time_interval, menu);
    	menu.setHeaderTitle("Select an Option");    	
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	// TODO Auto-generated method stub
    	
    	switch(item.getItemId()) {
    	
    	case R.id.time1:
    		prefs.edit().putInt(BatterySaver.RUN_FOR, 1).commit();
			prefs.edit().putInt(BatterySaver.STOP_FOR, 4).commit();
			time_intervals.setText(timeSettings[1]);
			
			new ModeONAsync(Preferences.this).execute();
	    	
	    	Toast.makeText(getApplicationContext(),
	                "Settings Changed Successfully", Toast.LENGTH_SHORT)
	                .show();
		//	changes = 1;
    		return true;
    		
    	case R.id.time2:
    		prefs.edit().putInt(BatterySaver.RUN_FOR, 1).commit();
			prefs.edit().putInt(BatterySaver.STOP_FOR, 6).commit();
			//changes = 1;
			time_intervals.setText(timeSettings[2]);
			
			new ModeONAsync(Preferences.this).execute();
	    	
	    	Toast.makeText(getApplicationContext(),
	                "Settings Changed Successfully", Toast.LENGTH_SHORT)
	                .show();
    		return true;
    		
    	case R.id.time3:
    		prefs.edit().putInt(BatterySaver.RUN_FOR, 1).commit();
			prefs.edit().putInt(BatterySaver.STOP_FOR, 11).commit();
			time_intervals.setText(timeSettings[3]);
			new ModeONAsync(Preferences.this).execute();
	    	
	    	Toast.makeText(getApplicationContext(),
	                "Settings Changed Successfully", Toast.LENGTH_SHORT)
	                .show();
			//changes = 1;
    		return true;
    		
    	case R.id.time4:
    		prefs.edit().putInt(BatterySaver.RUN_FOR, 1).commit();
			prefs.edit().putInt(BatterySaver.STOP_FOR, 16).commit();
			time_intervals.setText(timeSettings[4]);
			
			new ModeONAsync(Preferences.this).execute();
	    	
	    	Toast.makeText(getApplicationContext(),
	                "Settings Changed Successfully", Toast.LENGTH_SHORT)
	                .show();
		//	changes = 1;
    		return true;
    		
    	case R.id.time5:
    		prefs.edit().putInt(BatterySaver.RUN_FOR, 1).commit();
			prefs.edit().putInt(BatterySaver.STOP_FOR, 31).commit();
			time_intervals.setText(timeSettings[5]);
			
			new ModeONAsync(Preferences.this).execute();
	    	
	    	Toast.makeText(getApplicationContext(),
	                "Settings Changed Successfully", Toast.LENGTH_SHORT)
	                .show();
			//changes = 1;
    		return true;
    		
    	case R.id.time6:
    		prefs.edit().putInt(BatterySaver.RUN_FOR, 2).commit();
			prefs.edit().putInt(BatterySaver.STOP_FOR, 11).commit();
			time_intervals.setText(timeSettings[6]);
			
			new ModeONAsync(Preferences.this).execute();
	    	
	    	Toast.makeText(getApplicationContext(),
	                "Settings Changed Successfully", Toast.LENGTH_SHORT)
	                .show();
		//	changes = 1;
    		return true;
    		
    	case R.id.time7:
    		prefs.edit().putInt(BatterySaver.RUN_FOR, 2).commit();
			prefs.edit().putInt(BatterySaver.STOP_FOR, 16).commit();
			time_intervals.setText(timeSettings[7]);
		//	changes = 1;
			
			new ModeONAsync(Preferences.this).execute();
	    	
	    	Toast.makeText(getApplicationContext(),
	                "Settings Changed Successfully", Toast.LENGTH_SHORT)
	                .show();
    		return true;
    		
    	case R.id.time8:
    		prefs.edit().putInt(BatterySaver.RUN_FOR, 2).commit();
			prefs.edit().putInt(BatterySaver.STOP_FOR, 31).commit();
			time_intervals.setText(timeSettings[8]);
		//	changes = 1;
			
			new ModeONAsync(Preferences.this).execute();
	    	
	    	Toast.makeText(getApplicationContext(),
	                "Settings Changed Successfully", Toast.LENGTH_SHORT)
	                .show();
	    	
    		return true;
    	case R.id.Default:
    	
    	prefs.edit().putInt(BatterySaver.RUN_FOR, 1).commit();
		prefs.edit().putInt(BatterySaver.STOP_FOR, 5).commit();
		time_intervals.setText(timeSettings[0]);
		new ModeONAsync(Preferences.this).execute();
    	
    	Toast.makeText(getApplicationContext(),
                "Settings Changed Successfully", Toast.LENGTH_SHORT)
                .show();
    	
		return true;
    	}
    	
    	
    	
    	return super.onContextItemSelected(item);    	    	
    	
    }
    
    
    class SetPreviousValuesAsync extends AsyncTask<Void,Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {

	    	
	    	// for internet on when phone wakes up
	    	
	    	int wakeupInternet = prefs.getInt(BatterySaver.WAKE_UP_NET, 1);
	    	
	    	if(wakeupInternet == 1){
	    		 
	    		mHandler.post(new Runnable() {
	                @Override
	                public void run() {
	                	netOnWakeup.setChecked(true);
	                }
	            });
	    		
	    	}else{
	    		
	    		mHandler.post(new Runnable() {
	                @Override
	                public void run() {
	                	netOnWakeup.setChecked(false);
	                }
	            });
	    		
	    	}
	    	
	    	//for alarm timers
	    	
	    	int runFor = prefs.getInt(BatterySaver.RUN_FOR, 0);
	    	
	    	int stopFor = prefs.getInt(BatterySaver.STOP_FOR, 0);
	    	
	    	//Log.v("SPINNER rnm", "RUN-FOR "+runFor+" STOP FOR "+stopFor	);
	    	
	    	if(runFor == 1){
	    		if(stopFor==5)
	    		{
	    			
	    			mHandler.post(new Runnable() {
		                @Override
		                public void run() {
		                //	interval_options.setSelection(0);
		                	time_intervals.setText(timeSettings[0]);
		                }
		            });
	    			}
	    		
	    		if(stopFor == 4){
	    			
	    			mHandler.post(new Runnable() {
		                @Override
		                public void run() {
		                //	interval_options.setSelection(0);
		                	time_intervals.setText(timeSettings[1]);
		                }
		            });
	    			
	    		}else if(stopFor == 6){
	    			
	    			mHandler.post(new Runnable() {
		                @Override
		                public void run() {
		                	//interval_options.setSelection(1);
		                	time_intervals.setText(timeSettings[2]);
		                }
		            });
	    			
	    		}else if(stopFor == 11){
	    			
	    			mHandler.post(new Runnable() {
		                @Override
		                public void run() {
		                	//interval_options.setSelection(2);
		                	time_intervals.setText(timeSettings[3]);
		                }
		            });
	    			
	    		}else if(stopFor == 16){
	    			
	    			mHandler.post(new Runnable() {
		                @Override
		                public void run() {
		                	//interval_options.setSelection(3);
		                	time_intervals.setText(timeSettings[4]);
		                }
		            });
	    			
	    		}else if(stopFor == 31){
	    			
	    			mHandler.post(new Runnable() {
		                @Override
		                public void run() {
		                	//interval_options.setSelection(4);
		                	time_intervals.setText(timeSettings[5]);
		                }
		            });
	    			
	    		}
	    		
	    		
	    		
	    	}else if(runFor == 2){ // main else
	    		
	    		if(stopFor == 11){
	    			mHandler.post(new Runnable() {
		                @Override
		                public void run() {
		                	//interval_options.setSelection(5);
		                	time_intervals.setText(timeSettings[6]);
		                }
		            });
	    			
	    		}else if(stopFor == 16){
	    			
	    			mHandler.post(new Runnable() {
		                @Override
		                public void run() {
		                	//interval_options.setSelection(6);
		                	time_intervals.setText(timeSettings[7]);
		                }
		            });
	    			
	    		}else if(stopFor == 31){
	    			
	    			mHandler.post(new Runnable() {
		                @Override
		                public void run() {
		                	//interval_options.setSelection(7);
		                	time_intervals.setText(timeSettings[8]);
		                }
		            });
	    			
	    		}
	    		
	    		
	    	}else{
	    		//nothing
	    	}
	    	
	    	
	    	//for NETWORKS 
	    	int networkSelected = prefs.getInt(BatterySaver.NETWORK, 1);
	    	
	    	if(networkSelected == 1){
	    		
	    		mHandler.post(new Runnable() {
	                @Override
	                public void run() {
	                	wifiOrData.check(R.id.wifi);
	                }
	            });
	    		
//	    		setMobileDataEnabled(Preferences.this, false);
	    		
	    		
	    	}else if(networkSelected == 0){
	    		
	    		mHandler.post(new Runnable() {
	                @Override
	                public void run() {
	                	wifiOrData.check(R.id.mob_net);
	                }
	            });
	    		
//	    		WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE); 
//	    		  if (wm.isWifiEnabled()) { 
//		              wm.setWifiEnabled(false); 
//		              //Log.v(TAG, "rnm WIFI OFF SUCCESS");
//				  }
	    		
	    	}else{
	    		
	    		mHandler.post(new Runnable() {
	                @Override
	                public void run() {
	                	wifiOrData.check(R.id.autonet);
	                }
	            });
	    		
	    		
	    	}
	    	
	    	
	    	//for SLEEP mode
	    	int sleepVal = prefs.getInt(BatterySaver.SLEEP, 3);
	    	
	    	//Log.v(TAG, "rnm SLEEP VAL: "+sleepVal);
	    	if(sleepVal == 1){
	    		
	    		
	    		mHandler.post(new Runnable() {
	                @Override
	                public void run() {
	                	sleepMode.setChecked(true);
	                	sleep_mode_txt.setText(getResources().getString(R.string.slp_on));
	                }
	            });
	    		
	    	}else{
	    		
	    		mHandler.post(new Runnable() {
	                @Override
	                public void run() {
	                	sleepMode.setChecked(false);
	                	sleep_mode_txt.setText(getResources().getString(R.string.slp_off));
	                }
	            });
	    		
	    	}
	    	
	    
			
			return null;
		}
    	
    }
    
    void on_offWifi(boolean offon){
    
    	WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
    	
    	if(offon == true){

    		if (!wm.isWifiEnabled()) { 
    	        wm.setWifiEnabled(true); 
    	        //Log.v(TAG, "rnm WIFI ON SUCCESS");
    		  }
    		
    	}else{
    		
    		if (wm.isWifiEnabled()) { 
    	        wm.setWifiEnabled(false); 
    	        //Log.v(TAG, "rnm WIFI OFF SUCCESS");
    		  }
    		
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

    
    
 void turnModeON(){
		
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		/**
		 * Alarm to turn WIFI ON
		 */
		Intent myIntent = new Intent("com.rainmaker.android.batterysaver.ALARM1");
		
		myIntent.putExtra("alarm", 1);

      	    PendingIntent pendingIntent
      	     = PendingIntent.getBroadcast(getBaseContext(),
      	       1, myIntent, 0);
      
      AlarmManager alarmManager = (AlarmManager)getBaseContext().getSystemService(Context.ALARM_SERVICE);
      
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
	
	
	
	void turnModeOFF(){
		
		Intent intent = new Intent("com.rainmaker.android.batterysaver.ALARM1");
		PendingIntent sender = PendingIntent.getBroadcast(getBaseContext(),
		               1, intent, 0);
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

		alarmManager.cancel(sender);
		
		Intent intent2 = new Intent("com.rainmaker.android.batterysaver.ALARM2");
		PendingIntent sender2 = PendingIntent.getBroadcast(getBaseContext(),
		               2, intent2, 0);
		AlarmManager alarmManager2 = (AlarmManager) getSystemService(ALARM_SERVICE);

		alarmManager2.cancel(sender2);
		prefs.edit().putInt(BatterySaver.MODE, 0).commit();
	}
    
    
	
	boolean isPro(){
	    
    	PackageManager manager = getPackageManager();
        if (manager.checkSignatures("com.rainmaker.android.batterysaver", "com.rainmaker.android.batterysaverpro")
            == PackageManager.SIGNATURE_MATCH) {
           return true; //full version
        }else{
        	return false;
        }
    	
    }	
	
	
}
