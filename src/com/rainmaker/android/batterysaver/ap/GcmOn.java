package com.rainmaker.android.batterysaver.ap;

import java.util.ArrayList;

import org.alexd.jsonrpc.JSONRPCClient;
import org.alexd.jsonrpc.JSONRPCException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;


public class GcmOn extends AsyncTask<String, Integer, Integer>{

	
	String regId = "";
	String userEmail = "";
	Context ctx;
	
	private static String TAG = "GcmOn: ";
	
	public GcmOn(String reg, String _email, Context _ctx) {

		this.regId = reg;
		this.userEmail = _email;
		this.ctx = _ctx;
		
		
		
		//Log.v(TAG, "IN GcmOn");
	
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		if(result == 1){
			
			//Log.v(TAG, "GCM ADDED OK");
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
			prefs.edit().putInt(BatterySaver.GCM, 1).commit();
		}else{
			
			//Log.v(TAG, "GCM ERROR");
			
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
			prefs.edit().putInt(BatterySaver.GCM, 0).commit();
			
		}
	}
	
	@Override
	protected Integer doInBackground(String... params) {

		JSONObject data = new JSONObject();
		
		//Log.v(TAG, "Setting GCM ID");
		
		Integer status = 0;
		
		try {
			
			data.put("gcm", regId);
			data.put("em", userEmail);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		ArrayList<JSONObject> prefDB = new ArrayList<JSONObject>();
		
		prefDB.add(data);
		
		JSONRPCClient client = JSONRPCClient.create(BatterySaver.DATA_URL, null);
		
		try {
			JSONArray responseArray = client.callJSONArray("cf.pitGcmOn",prefDB );
			
				if(!responseArray.isNull(0)){
					
					JSONObject response = responseArray.getJSONObject(0);
					
					////Log.v("rnm Preferences resp ON", response.toString());
					
					String sts = response.getString("s");
					
					if(sts.length() != 0 && sts != null && sts != "0"){
			         	
			          status = Integer.parseInt(response.getString("s"));
			         	
					
			         	} else{
			         		
			         		status = 0;
			         	}
					
					
				}else{
					
					status = 0;
					
				}
			
		} catch (JSONRPCException e) {
			status = 0;
			////Log.v("rnm Preferences ON", e.getMessage());
			
		} catch (JSONException e) {
			status = 0;
			////Log.v("rnm Preferences ON", e.getMessage());
			
		}
		
		
		
		return status;
	}

	
	
}
