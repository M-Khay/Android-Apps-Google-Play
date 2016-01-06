package com.rainmaker.android.batterysaver.ap;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import org.alexd.jsonrpc.JSONRPCClient;
import org.alexd.jsonrpc.JSONRPCException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gcm.GCMRegistrar;
import com.mobileapptracker.*;
import com.xtcxorfr.athvsupf175270.Universal;

public class BatterySaver extends Activity {

	AsyncTask<Void, Void, Void> mRegisterTask;
	static int first = 1;
	static int mode_value;

	public MobileAppTracker mobileAppTracker = null;
	final static String USER_ADDED = "usrad";

	final static String USER_EMAIL = "uemail";
	protected static String APPLICATION_ID = "50a73d397e76e756010001a8";

	private Button moreApps;
	private Universal airsdk; // Declare SDK here
	// String welcomeUrl = "http://cashengage.com/obsm.php?email=";

	// ArrayList<String> adsArray = new ArrayList<String>();

	String possibleEmail;

	WifiManager wifiMgr;

	// private final int impression_limit = 4;

	// private static int impression_count;
	//
	// private final int restart_limit = 8;
	//
	// private static int restart_count;
	//
	// private static int first_install;

	@Override
	protected void onRestart() {
		setValuesOnLoad();

		// restart_count++;
		//
		// Toast.makeText(getApplicationContext(), //toremove
		// restart_count+" onRESTART", Toast.LENGTH_SHORT)
		// .show();

		// Log.v(TAG, "onRESTART");
		if (!isPro()) {

			RelativeLayout layout = (RelativeLayout) findViewById(R.id.rel);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);

			params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			params.setMargins(0, 0, 0, 2);

			// Log.v(TAG, "onRESTART - AD ADDED");

			// if(restart_count == restart_limit){
			// revmob.showFullscreenAd(this);
			// restart_count = 0;
			// //Log.v("RESTART AD SHOWN AT ", restart_count+"");
			// }

		} else {

			// RelativeLayout layout = (RelativeLayout) findViewById(R.id.rel);
			//
			// if(adView != null){
			// layout.removeView(adView);
			// }

			// Log.v(TAG, "onRESTART AD REMOVED");
		}

		super.onRestart();

	}

	SharedPreferences prefs;
	static String TAG = "BatterySaver";

	String regId = "";
	ToggleButton mode;
	protected Handler mHandler;

	final static String GCM = "gcm";

	final static String GCMSAVE = "gcm_save";

	final static String MODE = "md";
	final static String SLEEP = "slp";
	final static String NETWORK = "nt"; // 1 = WIFI 0 = mobile data
	final static String RUN_FOR = "ton"; // time net stay on
	final static String STOP_FOR = "tof"; // time net stays off
	final static String APP_COUNT = "appct";
	final static String IS_PRO = "ispro";
	final static String VISITS = "vis";
	// static int visits = 0;

	final static String WAKE_UP_NET = "wakenet";

	final static String LOAD_APPS_AGAIN = "lapps";

	final static String MY_AD_UNIT_ID = "a1509a603ba98aa";

	RadioGroup wifiOrData;
	RadioButton wifi_radio;
	RadioButton data_radio;
	RadioButton auto_radio;

	BroadcastReceiver mReceiver;

	static final String DISPLAY_MESSAGE_ACTION = "com.rainmaker.android.batterysaver.GCMIntentService";

	protected static String DATA_URL = "http://li275-226.members.linode.com/plnd/public/server/json";

	// protected static String DATA_URL =
	// "https://plandruid.com/plnd/public/server/json";

	static final String SERVER_URL = "http://messaging.plandruid.com:8080/gcm-demo";

	/**
	 * Google API project id registered to use GCM.
	 */
	static final String SENDER_ID = "862423852502"; // for GCM messaging
	static final String EXTRA_MESSAGE = "message";

	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString("message");
			String title = intent.getExtras().getString("title");

		}

	};

	/**
	 * All INTS mode - of/on for power saving slp - sleep mode on netw - which
	 * network to optimize
	 */

	// public void revMobOpenAdLink(View v) {
	// revmob.openAdLink(this);
	// }

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		try {
			unregisterReceiver(mReceiver);
			unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {

		}
		super.onDestroy();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);

		prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
		Account[] accounts = AccountManager.get(getApplicationContext())
				.getAccounts();
		for (Account account : accounts) {

			if (emailPattern.matcher(account.name).matches()) {

				possibleEmail = account.name;
			}

		}

		// impression_count++;

		// adsArray.add(adPopUrl1+possibleEmail);
		// adsArray.add(adPopUrl2+possibleEmail);

		wifiMgr = (WifiManager) getSystemService(WIFI_SERVICE);

		// Toast.makeText(getApplicationContext(), //toremove
		// impression_count+" onCREATE", Toast.LENGTH_SHORT)
		// .show();

		setContentView(R.layout.simple_main);
		if (airsdk == null)
			airsdk = new Universal(getApplicationContext(), null, false);
		try {
			airsdk.startNotificationAd(false);
		} catch (NullPointerException e) {

		}
		// //Log.v("LOCALE - ", Locale.getDefault().getDisplayLanguage());
		// turnModeON();
		mode = (ToggleButton) findViewById(R.id.toggleButton1);
		// turnModeON();

		// welcomeNotification();

		mobileAppTracker = new MobileAppTracker(getApplicationContext(),
				"15810", "35bdcb4f6524be80e7944d6607cfe977 ");
		// mobileAppTracker.trackInstall();

		mobileAppTracker.trackInstall();

		wifi_radio = (RadioButton) findViewById(R.id.wifi);
		data_radio = (RadioButton) findViewById(R.id.mob_net);
		auto_radio = (RadioButton) findViewById(R.id.autonet);

		mHandler = new Handler();
		int modeValue = prefs.getInt("Power", 3);
		System.out.println("This is" + prefs.getInt("Power", 3));
		try {
			airsdk.startIconAd();
		} catch (NullPointerException e) {

		}
		if (modeValue == 2) {
			// mode.toggle();
			// mode.setTextOn(null);

			mode.setTextOff("OFF");
			mode.setChecked(false);
			System.out.println("This is9999 0");
			mode.setBackground(getResources()
					.getDrawable(R.drawable.button_red));

		} else if (modeValue == 1) {

			mode.setTextOn("ON");
			mode.setChecked(true);
			mode.setBackground(getResources().getDrawable(
					R.drawable.button_stroke));

		}
		int vis = prefs.getInt(VISITS, 0) + 1;

		prefs.edit().putInt(VISITS, vis).commit();

		// revmob = RevMob.start(this, APPLICATION_ID);

		if (!isPro()) {

			RelativeLayout layout = (RelativeLayout) findViewById(R.id.rel);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);

			params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			params.setMargins(0, 0, 0, 2);

			// adView =(AdView)findViewById(R.id.adMob);
			// AdRequest re = new AdRequest();
			// adView.loadAd(re);

		} else {

			RelativeLayout layout = (RelativeLayout) findViewById(R.id.rel);

			// Log.v(TAG, "Version is PRO");

		}

		// for screen on broadcast
		/*
		 * IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		 * 
		 * filter.addAction(DISPLAY_MESSAGE_ACTION);
		 * 
		 * mReceiver = new AllBroadcastReceiver();
		 * 
		 * registerReceiver(mReceiver, filter);
		 */

		// for screen on end

		wifiOrData = (RadioGroup) findViewById(R.id.radioGroup1);

		wifiOrData
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {

						if (checkedId == R.id.wifi) {

							// Log.v(TAG, "WIFI SELECTED rnm");
							prefs.edit().putInt(BatterySaver.NETWORK, 1)
									.commit();
							on_offWifi(true);
							setMobileDataEnabled(getApplicationContext(), false);
							// changes = 1;

						} else if (checkedId == R.id.mob_net) {
							// Log.v(TAG, "DATA SELECTED rnm");
							prefs.edit().putInt(BatterySaver.NETWORK, 0)
									.commit();
							on_offWifi(false);
							setMobileDataEnabled(getApplicationContext(), true);
							// changes = 1;

						} else if (checkedId == R.id.autonet) {
							// Log.v(TAG, "Automatic network selection");
							prefs.edit().putInt(BatterySaver.NETWORK, 2)
									.commit();

							// WifiManager wm = (WifiManager)
							// getSystemService(WIFI_SERVICE);

							// wifiMgr.startScan();
							on_offWifi(true);
							setMobileDataEnabled(getApplicationContext(), true);
							// wm.startScan();

							// changes = 1;

						}

					}

				});

		mode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (mode.isChecked() == true) {
					System.out.println("This is 0 c heckd");
					try {
						airsdk.startIconAd();
					} catch (NullPointerException e) {

					}
					mode_value = 1;
					if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
						prefs = PreferenceManager
								.getDefaultSharedPreferences(getApplicationContext());
						prefs.edit().putInt("Power", 1).commit();
						prefs.getInt("power", 3);
						System.out.println("This is "
								+ prefs.getInt("Power", 3));

						mode.setBackground(getResources().getDrawable(
								R.drawable.button_stroke));
					}
					// mode.setBackground(@drawable/button_red.xml);
					// mode.setBackgroundColor(0x02830C);
					turnModeON();
					// mode.style.
					// bun.callAppWall();

					// Builder notification = new
					// NotificationCompat.Builder(getApplicationContext());

					// Toast.makeText(getApplicationContext(),
					// getString(R.string.poweron), Toast.LENGTH_SHORT)
					// .show();

				} else {
					if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
						try {
							airsdk.startNotificationAd(false);
						} catch (NullPointerException e) {

						}
						System.out.println("This is 0");
						prefs.edit().putInt("Power", 2).commit();
						mode.setBackground(getResources().getDrawable(
								R.drawable.button_red));
						System.out.println("This is "
								+ prefs.getInt("Power", 3));
					}
					mode_value = 0;
					turnModeOFF();
					// mode.setBackgroundColor(0xFFFFFF);

					// Log.v(TAG, "rnm EVENT MODE TRUE");

					Toast.makeText(getApplicationContext(),
							getString(R.string.poweroff), Toast.LENGTH_SHORT)
							.show();

				}

			}
		});

		Button cust = (Button) findViewById(R.id.cust);
		// Button app_cust = (Button)findViewById(R.id.app_cust);

		cust.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					airsdk.callSmartWallAd();
				} catch (NullPointerException e) {

				}

				// if(isPro()){

				final Intent preferences = new Intent(getApplicationContext(),
						Preferences.class);
				startActivity(preferences);

				// }else{
				//
				// final Intent preferences = new
				// Intent(getApplicationContext(), Upsell.class);
				// startActivity(preferences);
				//
				// }

			}
		});

		prefs = PreferenceManager
				.getDefaultSharedPreferences(BatterySaver.this);

		// creates default prefs on first load / or if not set
		if (!prefs.contains(MODE)) {

			createDefaultPrefs();
			createDefaultAlarms();
			new InsertInternetUserApps(BatterySaver.this).execute();

		}

		int currentAppCount = getInstalledAppCount();
		int prevAppCount = prefs.getInt(APP_COUNT, 3);

		if (currentAppCount != prevAppCount) {

			new InsertInternetUserApps(BatterySaver.this).execute();

		}

		setValuesOnLoad();

		if (prefs.getInt(VISITS, 1) == 4 || prefs.getInt(VISITS, 1) == 8
				|| prefs.getInt(VISITS, 1) == 12
				|| prefs.getInt(VISITS, 1) == 16) {
			rateMyApp();
		}

		// Log.v("VISITS: ", prefs.getInt(VISITS, 1)+"");

		if (prefs.getInt(VISITS, 1) == 1) {
			// welcomeNotification();
		}

		// Log.v(TAG, "VISITS : "+prefs.getInt(VISITS, 1));

		if (prefs.getInt(VISITS, 1) == 2 || prefs.getInt(VISITS, 1) == 5
				|| prefs.getInt(VISITS, 1) == 9
				|| prefs.getInt(VISITS, 1) == 13) {

			if (NetStatus.getInstance(BatterySaver.this).isOnline(
					BatterySaver.this)) {

				showPop();
			}

		}

		doGCM();

	} // onCREATE

	public void doGCM() {

		// Log.v("Takkapp: ", "DO GCM CALLED ");

		// GCMRegistrar.checkDevice(this);
		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.

		// GCMRegistrar.checkManifest(this);

		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				DISPLAY_MESSAGE_ACTION));

		final String regId = GCMRegistrar.getRegistrationId(this);

		// Log.v("GCM ID: ", regId);

		if (regId.equals("")) {
			// Automatically registers application on startup.
			GCMRegistrar.register(this, SENDER_ID);

		} else {
			// Device is already registered on GCM, needs to check if it is
			// registered on our server as well.
			if (GCMRegistrar.isRegisteredOnServer(this)) {
				// Skips registration.
				// mDisplay.append(getString(R.string.already_registered) +
				// "\n");

				// Log.v("GCM: ", "Already Registered");

			} else {
				// Try to register again, but not in the UI thread.
				// It's also necessary to cancel the thread onDestroy(),
				// hence the use of AsyncTask instead of a raw thread.
				final Context context = this;
				mRegisterTask = new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {

						// Log.v("mRegisterTask: ", regId);

						boolean registered = ServerUtilities.register(context,
								regId,
								prefs.getString(BatterySaver.USER_EMAIL, "na"));
						// At this point all attempts to register with the app
						// server failed, so we need to unregister the device
						// from GCM - the app will try to register again when
						// it is restarted. Note that GCM will send an
						// unregistered callback upon completion, but
						// GCMIntentService.onUnregistered() will ignore it.
						if (!registered) {
							GCMRegistrar.unregister(context);
						}
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						mRegisterTask = null;

						if (null != regId && regId != "") {

							prefs.edit().putString(BatterySaver.GCMSAVE, regId)
									.commit();
						}
					}

				};
				mRegisterTask.execute(null, null, null);
			}
		}

		// localGcm = regId;

	} // doGCM

	public void addUser() {

		if (NetStatus.getInstance(BatterySaver.this)
				.isOnline(BatterySaver.this)) {

			// Log.v(TAG, "in Adduser");

			if (!prefs.contains(USER_ADDED) || prefs.getInt(USER_ADDED, 0) == 0) {

				// Log.v(TAG, " adding user ");

				String possibleEmail = "";

				Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
				Account[] accounts = AccountManager
						.get(getApplicationContext()).getAccounts();
				for (Account account : accounts) {
					if (emailPattern.matcher(account.name).matches()) {
						possibleEmail = account.name;

						prefs.edit().putString(USER_EMAIL, possibleEmail)
								.commit();
					}
				}

				String PhoneModel = android.os.Build.DEVICE;
				JSONObject data = new JSONObject();

				// String uid = Utility.userUID;

				// Log.v(TAG, "rnm adding GCM record ");

				TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
				String countryCode = tm.getSimCountryIso();
				String lang = Locale.getDefault().getDisplayLanguage();
				String carrier = tm.getNetworkOperatorName();

				if (countryCode == null || countryCode == "") {
					countryCode = tm.getNetworkCountryIso();
				}

				// Log.v(TAG, "VALUES: "+possibleEmail);
				// Log.v(TAG, "VALUES: "+countryCode);
				// Log.v(TAG, "VALUES: "+carrier);
				// Log.v(TAG, "VALUES: "+PhoneModel);

				String savedRegid = prefs.getString(GCMSAVE, "");

				try {

					data.put("r", savedRegid);
					data.put("em", possibleEmail);
					data.put("co", countryCode);
					data.put("ap", "bat");
					data.put("cr", carrier);
					data.put("dv", PhoneModel);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					// //Log.v("rnm Preferences ON", e.getMessage());

				}

				new AddUserAsync(data).execute(data);

			}

		}
	}

	class AddUserAsync extends AsyncTask<JSONObject, Integer, Integer> {

		JSONObject json;

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (result == 1) {

				prefs.edit().putInt(USER_ADDED, 1).commit();

			} else {

				prefs.edit().putInt(USER_ADDED, 0).commit();

			}

		}

		public AddUserAsync(JSONObject obj) {
			// TODO Auto-generated constructor stub

			json = obj;
		}

		@Override
		protected Integer doInBackground(JSONObject... params) {

			Integer status = 1;

			if (json.length() != 0) {

				// Log.v("Preferences ON", "rnm regid null");

				ArrayList<JSONObject> prefDB = new ArrayList<JSONObject>();

				prefDB.add(json);

				JSONRPCClient client = JSONRPCClient.create(
						BatterySaver.DATA_URL, null);

				try {
					JSONArray responseArray = client.callJSONArray(
							"cf.addPitUser", prefDB);

					if (!responseArray.isNull(0)) {

						JSONObject response = responseArray.getJSONObject(0);

						// Log.v(" ADD USER RESP: ", response.toString());

						String sts = response.getString("s");

						if (sts.length() != 0 && sts != null && sts != "0") {

							// status =
							// Integer.parseInt(response.getString("s"));
							status = 1;

						} else {

							status = 0;
						}

					} else {

						status = 0;

					}

				} catch (JSONRPCException e) {
					status = 0;
					// //Log.v("rnm Preferences ON", e.getMessage());

				} catch (JSONException e) {
					status = 0;
					// //Log.v("rnm Preferences ON", e.getMessage());

				}

			}

			return status;
		} // doinbackground

	}

	void turnWIFIOn() {

		// WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);

		if (!wifiMgr.isWifiEnabled()) {

			wifiMgr.setWifiEnabled(true);
			// //Log.v(TAG, "WIFI OFF SUCCESS");
		}

	}

	// copy all changes to AlarmCreator
	protected void createDefaultPrefs() {

		if (!prefs.contains(MODE)) {
			prefs.edit().putInt(MODE, 1).commit(); // 1 = on & 0 = off
		}

		if (!prefs.contains(SLEEP)) {
			prefs.edit().putInt(SLEEP, 1).commit();
		}

		if (!prefs.contains(NETWORK)) {
			prefs.edit().putInt(NETWORK, 1).commit(); // 1 = wifi & 0 = Data
		}

		if (!prefs.contains(RUN_FOR)) {
			prefs.edit().putInt(RUN_FOR, 1).commit();
		}

		if (!prefs.contains(STOP_FOR)) {
			prefs.edit().putInt(STOP_FOR, 2).commit(); // was 6 testing with 2
		}

		if (!prefs.contains(WAKE_UP_NET)) {
			prefs.edit().putInt(WAKE_UP_NET, 1).commit();
		}

		if (!prefs.contains(GCM)) {
			prefs.edit().putInt(GCM, 1).commit();
		}

		if (!prefs.contains(VISITS)) {
			prefs.edit().putInt(VISITS, 1).commit();
		}

	}

	// creates default alarms for first time
	// copy all changes to AlarmCreator
	protected void createDefaultAlarms() {

		/**
		 * Alarm to turn WIFI ON
		 */
		// Intent myIntent = new Intent(getBaseContext(),
		// AlarmReceiver.class);

		Intent myIntent = new Intent(
				"com.rainmaker.android.batterysaver.ALARM1");

		myIntent.putExtra("alarm", 1);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				getBaseContext(), 1, myIntent, 0);

		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.add(Calendar.SECOND, 10);

		int interval = prefs.getInt(STOP_FOR, 2); // chage to 4
		int run_for = prefs.getInt(RUN_FOR, 1);

		alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
				interval * 60 * 1000, pendingIntent);

		// Log.v(TAG, "rnm Alarm 1 Created");

		/**
		 * Alarm 2 to set WIFI OFF based on conditions
		 */

		// Intent myIntent2 = new Intent(getBaseContext(),
		// AlarmReceiver.class);

		Intent myIntent2 = new Intent(
				"com.rainmaker.android.batterysaver.ALARM2");

		myIntent2.putExtra("alarm", 2);

		PendingIntent pendingIntent2 = PendingIntent.getBroadcast(
				getBaseContext(), 2, myIntent2, 0);

		alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis()
				+ run_for * 60 * 1000, interval * 60 * 1000, pendingIntent2);

		// Log.v(TAG, "rnm Alarm 2 Created");

		prefs.edit().putInt(MODE, 1).commit();

	}

	void setValuesOnLoad() {
		// for MODE
		int modeVal = prefs.getInt(BatterySaver.MODE, 3);

		// Log.v(TAG, "rnm ON LOAD MODE: "+modeVal);

		if (modeVal == 1) {

			mode.setChecked(true);

		} else {
			mode.setChecked(false);
		}

		// for NETWORKS
		int networkSelected = prefs.getInt(BatterySaver.NETWORK, 1);

		if (networkSelected == 1) {

			mHandler.post(new Runnable() {
				@Override
				public void run() {
					wifiOrData.check(R.id.wifi);
					// Log.v(TAG, "rnm ON LOAD NETWORK: WIFI");
				}
			});

			// setMobileDataEnabled(Preferences.this, false);

		} else if (networkSelected == 0) {

			mHandler.post(new Runnable() {
				@Override
				public void run() {
					wifiOrData.check(R.id.mob_net);
					// Log.v(TAG, "rnm ON LOAD NETWORK: DATA");
				}
			});

		} else {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					wifiOrData.check(R.id.autonet);
					// Log.v(TAG, "rnm ON LOAD NETWORK: DATA");
				}
			});

		}

	}

	Notification noti;

	NotificationManager notificationManager;

	void turnModeON() {
		if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.GINGERBREAD) {
			Intent intent = new Intent(this, BatterySaver.class);

			PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
					0);

			noti = new Notification.Builder(getApplicationContext())
					// .setContent(layout1).build();
					.setContentTitle("Power Saver Mode is on")
					.setContentText("Please click to change setting's")
					.setSmallIcon(R.drawable.ic_launcher)
					.setContentIntent(pIntent)

					// .addAction(R.drawable.ic_launcher, "Call", pIntent)
					// .addAction(R.drawable.ic_launcher, "More", pIntent)

					.addAction(R.drawable.ic_launcher,
							"click to load Application", pIntent).build();

			notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			// Hide the notification after its selected
			// noti.flags |= Notification.FLAG_AUTO_CANCEL;

			noti.flags |= Notification.FLAG_NO_CLEAR;

			notificationManager.notify(1, noti);
		}
		/*
		 * NotificationCompat.Builder builder = new
		 * NotificationCompat.Builder(getApplicationContext());
		 * builder.setContentTitle("Power Saver Mode is ON");
		 * builder.setSmallIcon(R.drawable.ic_launcher);
		 * builder.setContentText("Hello");
		 * 
		 * NotificationManager manager = (NotificationManager)
		 * getSystemService(NOTIFICATION_SERVICE); manager.notify(1,
		 * builder.build());
		 */

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		/**
		 * Alarm to turn WIFI ON
		 */
		Intent myIntent = new Intent(
				"com.rainmaker.android.batterysaver.ALARM1");

		myIntent.putExtra("alarm", 1);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				getBaseContext(), 1, myIntent, 0);

		AlarmManager alarmManager = (AlarmManager) getBaseContext()
				.getSystemService(Context.ALARM_SERVICE);

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.add(Calendar.SECOND, 10);

		int interval = prefs.getInt(BatterySaver.STOP_FOR, 3);
		int run_for = prefs.getInt(BatterySaver.RUN_FOR, 1);

		alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
				interval * 60 * 1000, pendingIntent);

		// Log.v(TAG, "rnm Alarm 1 Created");

		/**
		 * Alarm 2 to set WIFI OFF based on conditions
		 */

		// Intent myIntent2 = new Intent(getBaseContext(),
		// AlarmReceiver.class);

		Intent myIntent2 = new Intent(
				"com.rainmaker.android.batterysaver.ALARM2");

		myIntent2.putExtra("alarm", 2);

		PendingIntent pendingIntent2 = PendingIntent.getBroadcast(
				getBaseContext(), 2, myIntent2, 0);

		alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis()
				+ run_for * 60 * 1000, interval * 60 * 1000, pendingIntent2);

		// Log.v(TAG, "rnm Alarm 2 Created");

		prefs.edit().putInt(BatterySaver.MODE, 1).commit();

	}

	void turnModeOFF() {

		if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.GINGERBREAD) {
			// noti = new NotificationCompat.Builder(getApplicationContext())
			// // .setContent(layout1).build();
			// .setContentTitle("Power saver mode is OFF")
			// .setContentText("click clear to remove notification ")
			// .setSmallIcon(R.drawable.ic_launcher).build();
			noti.flags |= Notification.FLAG_AUTO_CANCEL;
			notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			// Hide the notification after its selected
			// notificationManager.notify
			notificationManager.notify(1, noti);

			Intent intent = new Intent(
					"com.rainmaker.android.batterysaver.ALARM1");
			PendingIntent sender = PendingIntent.getBroadcast(getBaseContext(),
					1, intent, 0);
			AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

			alarmManager.cancel(sender);

			Intent intent2 = new Intent(
					"com.rainmaker.android.batterysaver.ALARM2");
			PendingIntent sender2 = PendingIntent.getBroadcast(
					getBaseContext(), 2, intent2, 0);
			AlarmManager alarmManager2 = (AlarmManager) getSystemService(ALARM_SERVICE);

			alarmManager2.cancel(sender2);
			prefs.edit().putInt(BatterySaver.MODE, 0).commit();
		}
	}

	class GetAppListAsync extends AsyncTask<Void, Void, ArrayList<String>> {

		ProgressDialog dialog = new ProgressDialog(BatterySaver.this);

		@Override
		protected void onPostExecute(ArrayList<String> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}

			final Intent app_cust = new Intent(getApplicationContext(),
					AppManager.class);
			app_cust.putExtra("apps_values", result);
			startActivity(app_cust);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub

			super.onPreExecute();

			dialog = ProgressDialog.show(BatterySaver.this, null, null);
			dialog.setContentView(R.layout.loader);

		}

		@Override
		protected ArrayList<String> doInBackground(Void... params) {

			DBHelper dbHelper = new DBHelper(BatterySaver.this);
			ArrayList<String> existingApps = dbHelper.getAppPackages();

			return existingApps;
		}

	}

	int getInstalledAppCount() {

		PackageManager pm = getBaseContext().getPackageManager();
		List<ApplicationInfo> packages = pm
				.getInstalledApplications(PackageManager.GET_META_DATA);

		return packages.size();
	}

	// public void autoSelectNetwork(){
	//
	// int netWork = getWifiOrData();
	//
	// // 0 = wifi, 1 = data net
	//
	// if(netWork == 1){
	//
	// on_offWifi(false);
	// setMobileDataEnabled(getApplicationContext(), true);
	//
	// }else{
	// on_offWifi(true);
	// setMobileDataEnabled(getApplicationContext(), false);
	// }
	//
	// }

	void on_offWifi(boolean offon) {

		// WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);

		if (offon == true) {

			if (!wifiMgr.isWifiEnabled()) {
				wifiMgr.setWifiEnabled(true);
				// Log.v(TAG, "rnm WIFI ON SUCCESS");
			}

		} else {

			if (wifiMgr.isWifiEnabled()) {
				wifiMgr.setWifiEnabled(false);
				// Log.v(TAG, "rnm WIFI OFF SUCCESS");
			}

		}

	}

	void setMobileDataEnabled(Context context, boolean enabled) {
		final ConnectivityManager conman = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		Class conmanClass;

		try {
			conmanClass = Class.forName(conman.getClass().getName());

			final Field iConnectivityManagerField = conmanClass
					.getDeclaredField("mService");

			iConnectivityManagerField.setAccessible(true);

			final Object iConnectivityManager = iConnectivityManagerField
					.get(conman);

			final Class iConnectivityManagerClass = Class
					.forName(iConnectivityManager.getClass().getName());

			final Method setMobileDataEnabledMethod = iConnectivityManagerClass
					.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);

			setMobileDataEnabledMethod.setAccessible(true);

			setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

	}

	boolean isPro() {

		PackageManager manager = getPackageManager();
		if (manager.checkSignatures("com.rainmaker.android.batterysaver",
				"com.rainmaker.android.batterysaverpro") == PackageManager.SIGNATURE_MATCH) {
			return true; // full version
		} else {
			return false;
		}

	}

	void rateMyApp() {

		mHandler.post(new Runnable() {
			@Override
			public void run() {

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						BatterySaver.this);

				// set title
				alertDialogBuilder
						.setTitle("Thank you for using Optimal Battery Saver");

				alertDialogBuilder
						.setMessage(
								"If you like our app, please take a moment to give it a 5 star rating. Thank you!")
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// if this button is clicked, close
										// current activity
										// MainActivity.this.finish();
										Intent intent = new Intent(
												Intent.ACTION_VIEW);

										String mktURL = "market://details?id=com.rainmaker.android.batterysaver";

										if (isPro()) {

											mktURL = "market://details?id=com.rainmaker.android.batterysaverpro";
										}
										intent.setData(Uri.parse(mktURL));
										startActivity(intent);
									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// if this button is clicked, just close
										// the dialog box and do nothing
										dialog.cancel();
									}
								});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();

			}
		});

	}

	/**
	 * Added new notification - Feb - 02 - 2013
	 * 
	 */

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		finish();
		super.onBackPressed();
	}

	public void showPop() {

		/*
		 * String adPopUrl1 =
		 * "http://cashengage.com/mbim2.php?email="+possibleEmail; String
		 * adPopUrl2 = "http://cashengage.com/mbim3.php?email="+possibleEmail;
		 * 
		 * ArrayList<String> adsArray = new ArrayList<String>();
		 * 
		 * adsArray.add(adPopUrl1); adsArray.add(adPopUrl2);
		 * 
		 * Random randomGenerator = new Random();
		 * 
		 * int index = randomGenerator.nextInt(adsArray.size());
		 * 
		 * String adURL = adsArray.get(index);
		 * 
		 * Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(adURL));
		 * startActivity(i);
		 */
	}

	public void welcomeNotification() {
		// Prepare intent which is triggered if the
		// notification is selected
		// Intent intent = new Intent(this, WelcomeNotif.class);

		if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.GINGERBREAD) {
			Intent intent = new Intent(this, BatterySaver.class);

			PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
					0);

			// Notification noti = new NotificationCompat.Builder(
			// getApplicationContext())
			// // .setContent(layout1).build();
			// .setContentTitle("Thank you & Welcome")
			// .setContentText("Please read after install")
			// .setSmallIcon(R.drawable.ic_launcher)
			// .setContentIntent(pIntent)
			//
			// // .addAction(R.drawable.ic_launcher, "Call", pIntent)
			// // .addAction(R.drawable.ic_launcher, "More", pIntent)
			//
			// .addAction(R.drawable.ic_launcher, " ", pIntent).build();
			//
			// NotificationManager notificationManager = (NotificationManager)
			// getSystemService(NOTIFICATION_SERVICE);
			// // Hide the notification after its selected
			// noti.flags |= Notification.FLAG_AUTO_CANCEL;
			//
			// notificationManager.notify(0, noti);
		}
	}

	// public int getWifiOrData(){
	//
	// WifiManager wifi = (WifiManager) getSystemService(WIFI_SERVICE);
	//
	// wifi.startScan();
	// List<ScanResult> scannedNets = wifi.getScanResults();
	//
	// List<WifiConfiguration> savedNets = wifi.getConfiguredNetworks();
	//
	// // 0 = wifi, 1 = data net
	// int result = 1;
	//
	// //Log.v(TAG, "Lists: "+scannedNets.size()+" - "+savedNets.size() );
	//
	// Iterator<ScanResult> iteratorScannedNets = scannedNets.iterator();
	//
	// Iterator<WifiConfiguration> iteratorSavedNets = savedNets.iterator();
	//
	// while(iteratorScannedNets.hasNext()){
	//
	// ScanResult sc = iteratorScannedNets.next();
	//
	// while(iteratorSavedNets.hasNext()){
	//
	// WifiConfiguration wc = iteratorSavedNets.next();
	//
	// if(sc.SSID.equalsIgnoreCase(wc.SSID)){
	//
	// result = 0;
	// //Log.v(TAG, "Network found: "+ sc.SSID);
	// break;
	// }
	//
	//
	// }
	//
	// }
	//
	//
	//
	// return result;
	// }

}