/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rainmaker.android.batterysaver.ap;

import com.google.android.gcm.GCMRegistrar;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

/**
 * Helper class used to communicate with the demo server.
 */
public final class ServerUtilities {

    private static final int MAX_ATTEMPTS = 5;
    private static final int BACKOFF_MILLI_SECONDS = 2000;
    private static final Random random = new Random();
    
    private static final String TAG = "GCM SERVER Util: "; 

    /**
     * Register this account/device pair within the server.
     *
     * @return whether the registration succeeded or not.
     */
    static boolean register(final Context context, final String regId, final String email) {
        Log.i(TAG, "registering device (regId = " + regId + ")");
        String serverUrl = BatterySaver.DATA_URL;
        Map<String, String> params = new HashMap<String, String>();
        
        //Log.v(TAG, "UPDATING GCM: "+regId+" - "+email);
        
        params.put("regId", regId);
        params.put("action", "reg");
        params.put("email", email);
        
        
        long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
        // Once GCM returns a registration id, we need to register it in the
        // demo server. As the server might be down, we will retry it a couple
        // times.
        for (int i = 1; i <= MAX_ATTEMPTS; i++) {
            Log.d(TAG, "Attempt #" + i + " to register");
            try {
//                displayMessage(context, context.getString(
//                        R.string.server_registering, i, MAX_ATTEMPTS));
                
            	////Log.v(TAG, "Trying to register GCM");
            	
                post(serverUrl, params, context);
                updateLocalGcm(regId, context);
                GCMRegistrar.setRegisteredOnServer(context, true);
                
                
                ////Log.v(TAG, "GCM Registered ");
                
                return true;
            } catch (IOException e) {
                // Here we are simplifying and retrying on any error; in a real
                // application, it should retry only on unrecoverable errors
                // (like HTTP error code 503).
                Log.e(TAG, "Failed to register on attempt " + i, e);
                if (i == MAX_ATTEMPTS) {
                    break;
                }
                try {
                    Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
                    Thread.sleep(backoff);
                } catch (InterruptedException e1) {
                    // Activity finished before we complete - exit.
                    Log.d(TAG, "Thread interrupted: abort remaining retries!");
                    Thread.currentThread().interrupt();
                    return false;
                }
                // increase backoff exponentially
                backoff *= 2;
            }
        }
        
        ////Log.v(TAG, "ERROR register GCM");
//        String message = context.getString(R.string.server_register_error,
//                MAX_ATTEMPTS);
//        CommonUtilities.displayMessage(context, message);
        return false;
    }

    /**
     * Unregister this account/device pair within the server.
     */
    static void unregister(final Context context, final String regId) {
        Log.i(TAG, "unregistering device (regId = " + regId + ")");
        
        String serverUrl = BatterySaver.DATA_URL;
        
        Map<String, String> params = new HashMap<String, String>();
        params.put("regId", regId);
        params.put("acton", "unreg");
        
        try {
            post(serverUrl, params, context); 
            GCMRegistrar.setRegisteredOnServer(context, false);
            
//            String message = context.getString(R.string.server_unregistered);
//            
//            CommonUtilities.displayMessage(context, message);
            
            ////Log.v(TAG, "GCM Unregistered");
            
        } catch (IOException e) {
            // At this point the device is unregistered from GCM, but still
            // registered in the server.
            // We could try to unregister again, but it is not necessary:
            // if the server tries to send a message to the device, it will get
            // a "NotRegistered" error message and should unregister the device.
            
        	////Log.v(TAG, "GCM Unregistered ERROR : "+e);
        	
        }
    }

    /**
     * Issue a POST request to the server.
     *
     * @param endpoint POST address.
     * @param params request parameters.
     *
     * @throws IOException propagated from POST.
     */

		    
		    private static void post(String endpoint, Map<String, String> params, Context ctx)
		    throws IOException {
		
		    	////Log.v("ServerUtil : ", "post");
		    	
		    	
		    	final String gcmId = params.get("regId");
		    	final String action = params.get("action");
		    	final String email = params.get("email");

		    	if(action.equalsIgnoreCase("reg")){
		    		
		    		//Log.v(TAG, "Calling GcmOn");
		    		
		    		new GcmOn(gcmId, email, ctx).execute();
		    		
		    		
		    	}else {
//		    		new GcmOff(fbid).execute();
		    	}
		    	
//		    	new GcmOn(gcmId, fbid).execute();
		    	
		    	
    
		    }
		    
		    
		    public static void updateLocalGcm(String gcmID, Context cont){
		    	
		    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(cont);
		    	
		    	prefs.edit().putString(BatterySaver.GCMSAVE, gcmID).commit();
		    	
		    }
		    
}