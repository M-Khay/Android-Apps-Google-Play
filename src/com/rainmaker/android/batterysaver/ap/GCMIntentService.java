package com.rainmaker.android.batterysaver.ap;

import static com.rainmaker.android.batterysaver.ap.BatterySaver.DISPLAY_MESSAGE_ACTION;
import static com.rainmaker.android.batterysaver.ap.BatterySaver.SENDER_ID;

import java.util.ArrayList;
import java.util.Locale;

import org.alexd.jsonrpc.JSONRPCClient;
import org.alexd.jsonrpc.JSONRPCException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;
import com.rainmaker.android.batterysaver.ap.Notifications;


public class GCMIntentService extends GCMBaseIntentService {

	
	String message = "";
    String title = "";
  //  AsyncTask<Void, Integer, Integer> notifOFF;
   // AsyncTask<String, Integer, Integer> notifON;
//    private final String GCM = "gcm";
	
    @SuppressWarnings("hiding")
//    private static final String TAG = "GCMIntentService";

    public GCMIntentService() {
        super(SENDER_ID);
        
//        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        
        try {
			Class.forName("android.os.AsyncTask");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
		}
    }

    @Override
    protected void onRegistered(Context context, String registrationId) {
//        //Log.v(TAG, "Device registered: regId = " + registrationId);
      //  displayMessage(context, message, title);
        //Log.v("ONREG", "On registered "+registrationId);
     //   notifON = new NotificationOn(registrationId);
      //  notifON.execute();
    	
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//      displayMessage(context, "Gcm msg 2");
      ServerUtilities.register(context, registrationId, prefs.getString(BatterySaver.USER_EMAIL, "na"));

    }

    @Override
    protected void onUnregistered(Context context, String registrationId) {
//        //Log.v(TAG, "Device unregistered");
    //    displayMessage(context, message, title);
//        if (GCMRegistrar.isRegisteredOnServer(context)) {
//        
//        	 // This callback results from the call to unregister made on
//            // ServerUtilities when the registration to the server failed.
////            //Log.v(TAG, "Ignoring unregister callback");
//        	
//        } else {
////        	//Log.v(TAG, "PRASH calling NotificationOff");
//        	notifOFF = new NotificationOff();
//        	notifOFF.execute();
//           
//        }
        if (GCMRegistrar.isRegisteredOnServer(context)) {
            ServerUtilities.unregister(context, registrationId);
        } else {
            // This callback results from the call to unregister made on
            // ServerUtilities when the registration to the server failed.
            //Log.i(TAG, "Ignoring unregister callback");
        }
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        //Log.i(TAG, "Received message");
        
        String action = intent.getStringExtra("act");
        
        if(action.equalsIgnoreCase("ad")){
        	
        	String  url = intent.getStringExtra("u"); // URL to ad or page
        	
        	final String  msg = intent.getStringExtra("m");	// message in GCM
        	
        	final String  title = intent.getStringExtra("t"); // title
        	
        	final String  embed_email = intent.getStringExtra("ee"); // append email to AD url? 
        	
			        	if(embed_email.equalsIgnoreCase("1")){
			        		
			        		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			        		
			        		url = url+"?email="+prefs.getString(BatterySaver.USER_EMAIL, ""); 
			        		
			        	}
        	
        	generateNotification(context, msg, title, url);
        	
        }
        
    }


    @Override
    protected void onDeletedMessages(Context context, int total) {
//        //Log.v(TAG, "Received deleted messages notification");
//        String message = getString(R.string.all_events, total);
        displayMessage(context, message, title);
        // notifies user
        generateNotification(context, message, "", "");
    }

    @Override
    public void onError(Context context, String errorId) {
//        //Log.v(TAG, "Received error: " + errorId);
        displayMessage(context, message, title);
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
//        //Log.v(TAG, "Received recoverable error: " + errorId);
        displayMessage(context, message, title);
        return super.onRecoverableError(context, errorId);
    }

    
    
    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private static void generateNotification(Context context, String message, String title, String strURL) {
    	
        int icon = R.drawable.icon_notif;
        
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        
        Notification notification = new Notification(icon, message, when);
        
//        String title = context.getString(R.string.app_name);
        
//        Intent notificationIntent = new Intent(context, MainActivity.class);
        
        Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(strURL));
        
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        
        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, 0);
        
        notification.setLatestEventInfo(context, title, message, intent);
        
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        
        notificationManager.notify(0, notification);
    }
    
    
    

    static void displayMessage(Context context, String message, String title) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra("message", message);
        intent.putExtra("title", title);
        
        context.sendBroadcast(intent);
    }

    
    
//    class NotificationOn extends AsyncTask<String, Integer, Integer>{
//    	
//    	@Override
//		protected void onPostExecute(Integer result) {
//			// TODO Auto-generated method stub
//			super.onPostExecute(result);
//			notifON = null;
//		}
//
//		String regId = "";
//    	
//    	public NotificationOn(String rId) {
//			// TODO Auto-generated constructor stub
//    		
//    		regId = rId;
//    		//Log.v("GCM ", "NotificationOn created");
//		}
//
//		@Override
//		protected Integer doInBackground(String... params) {
//			Integer status = 0;
//			
//			String PhoneModel = android.os.Build.DEVICE;
//			 JSONObject data = new JSONObject();
//			 
////			 String uid = Utility.userUID;
//			 
//			 //Log.v(TAG, "rnm adding GCM record ");
//			 
//			 TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//			 String countryCode = tm.getSimCountryIso();
//			 String lang = Locale.getDefault().getDisplayLanguage();
//          	String carrier = tm.getNetworkOperatorName();
//          	
//				 if(countryCode == null || countryCode == ""){
//					 countryCode = tm.getNetworkCountryIso();
//				 }
//			 
//			 		try {
//			 			
//						data.put("r", regId);
//						data.put("co", countryCode);
//			          	data.put("cr", carrier);
//			          	data.put("dv", PhoneModel);
//			          	data.put("l", lang);
//						
//					} catch (JSONException e) {
//						// TODO Auto-generated catch block
//						//Log.v("rnm Preferences ON", e.getMessage());
//
//					}
//					
//					
//					if(regId != ""){
//						
//						//Log.v("Preferences ON", "rnm regid null");
//					
//					ArrayList<JSONObject> prefDB = new ArrayList<JSONObject>();
//					
//					prefDB.add(data);
//					
//					JSONRPCClient client = JSONRPCClient.create(BatterySaver.DATA_URL, null);
//					
//					try {
//						JSONArray responseArray = client.callJSONArray("cf.batnotifOn",prefDB );
//						
//							if(!responseArray.isNull(0)){
//								
//								JSONObject response = responseArray.getJSONObject(0);
//								
//								//Log.v("rnm Preferences resp ON", response.toString());
//								
//								String sts = response.getString("s");
//								
//								if(sts.length() != 0 && sts != null && sts != "0"){
//						         	
//						          status = Integer.parseInt(response.getString("s"));
//						         	
//								
//						         	} else{
//						         		
//						         		status = 0;
//						         	}
//								
//								
//							}else{
//								
//								status = 0;
//								
//							}
//						
//					} catch (JSONRPCException e) {
//						status = 0;
//						//Log.v("rnm Preferences ON", e.getMessage());
//						
//					} catch (JSONException e) {
//						status = 0;
//						//Log.v("rnm Preferences ON", e.getMessage());
//						
//					}
//			 		
//			 	}
//					
//			
//			
//			return status;
//		} // doinbackground
//
//    	
//    }
//    
//    
//
//	class NotificationOff extends AsyncTask<Void, Integer, Integer>{
//
//
//		@Override
//		protected void onPostExecute(Integer result) {
//			// TODO Auto-generated method stub
//			super.onPostExecute(result);
//			notifOFF = null;
//		}
//
//		@Override
//		protected Integer doInBackground(Void... params) {
//			// TODO Auto-generated method stub
//			
//			Integer status = 0;
////			MyLogger.v("GCM ", "NotificationOFF doInBackground");
//			 JSONObject data = new JSONObject();
//			 
//			 
////			 		MyLogger.d("Preferences resp OFF uid", uid);
//			 		
////			 		try {
////						data.put("i", uid);
////						
////					} catch (JSONException e) {
////						
////						//errr
//////						MyLogger.d("Preferences", e.getMessage());
////
////					}
//					
//					ArrayList<JSONObject> prefDB = new ArrayList<JSONObject>();
//					prefDB.add(data);
//					
//					JSONRPCClient client = JSONRPCClient.create(BatterySaver.DATA_URL, null);
//					
//					try {
//						JSONArray responseArray = client.callJSONArray("cf.notifOff",prefDB );
//						
//							if(!responseArray.isNull(0)){
//								
//								JSONObject response = responseArray.getJSONObject(0);
//								
////								MyLogger.d("Preferences resp OFF", response.toString());
//								
//								String sts = response.getString("s");
//								
//								if(sts.length() != 0 && sts != null && sts != "0"){
//						         	
//						          status = Integer.parseInt(response.getString("s"));
//						         	
//								
//						         	} else{
//						         		
//						         		status = 0;
//						         	}
//								
//								
//							}else{
//								
//								status = 0;
//								
//							}
//						
//					} catch (JSONRPCException e) {
//						status = 0;
////						MyLogger.d("Preferences RPC OFF", e.getMessage());
//						
//					} catch (JSONException e) {
//						status = 0;
////						MyLogger.d("Preferences resp OFF", e.getMessage());
//						
//					}
//			 		
//			return status;
//		} // doinbackground
//
//}

    
}
