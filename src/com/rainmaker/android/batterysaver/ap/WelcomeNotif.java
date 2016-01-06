package com.rainmaker.android.batterysaver.ap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class WelcomeNotif extends Activity{

	Handler mHandler;
	TextView title;
	TextView desc;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notif_view);
	
		mHandler = new Handler();
		
		String api_response = getIntent().getStringExtra("API_RESPONSE");
		
		title = (TextView) findViewById(R.id.title);
		desc = (TextView) findViewById(R.id.desc);
		
		try {
			JSONArray notifDetail = new JSONArray(api_response);
			
			JSONObject record = notifDetail.getJSONObject(0);
			
			if(record.has("tit")){
//				MyLogger.d("WishlistDetail", "TTILE - " + record.getString("tit"));
				
				title.setText(record.getString("tit"));
				
			}
			
			if(record.has("des")){
				
//				MyLogger.d("WishlistDetail", "DESC - " + record.getString("des"));
				desc.setText(Html.fromHtml(record.getString("des")));
				desc.setMovementMethod(LinkMovementMethod.getInstance());
				
			}
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
		}
		
		
	}// oncreate

}
