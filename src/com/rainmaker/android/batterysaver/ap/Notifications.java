package com.rainmaker.android.batterysaver.ap;


import java.util.ArrayList;
import java.util.List;

import org.alexd.jsonrpc.JSONRPCClient;
import org.alexd.jsonrpc.JSONRPCException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Notifications extends Activity{
	
	protected ListView notifList;
	
	Handler mHandler;
	
	ProgressDialog dialog;
	
	protected ArrayList<NotifItem> itemsArray = new ArrayList<NotifItem>();
	
	NotifAdapter wl;
	
	JSONArray jsonArray;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notif_list);
		
		mHandler = new Handler();
		dialog = new ProgressDialog(getApplicationContext());
		
		notifList = (ListView) findViewById(R.id.notif_list);
		
		new GetNotificationsAsync().execute();
		
		notifList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				
				
				 try {
					 
					 JSONObject wlItem = jsonArray.getJSONObject(position);
					
					if(wlItem.has("id")){
						
						String dbId = wlItem.getString("id");
						
						if(NetStatus.getInstance(Notifications.this).isOnline(Notifications.this)){
						
						new GetNotifItemAsync(dbId).execute();
						
						}else{            		
							showToast(getString(R.string.nonet));
						}
						            		

						
					//	Intent WishlistView = new Intent();
						
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
//					 MyLogger.d("WishlistList",e.getMessage());
					
				} 
				
				
			}
		});
		
		
	}

	
	
	public class NotifAdapter extends ArrayAdapter<NotifItem> {

		private LayoutInflater mInflater;
		
	public NotifAdapter(Context context, int resource,
			 List<NotifItem> objects) {
		super(context, resource, objects);
		
	}
	
	@Override
	public NotifItem getItem(int position) {
		// TODO Auto-generated method stub
//		return super.getItem(position);
	return itemsArray.get(position);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
//		return super.getCount();
		return itemsArray.size();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
//		return super.getView(position, convertView, parent);
	    ViewHolder holder1 = new ViewHolder();				
		 View hView = convertView;
            
            if (convertView == null) { 
            	
            	mInflater = getLayoutInflater();
                hView = mInflater.inflate(R.layout.wl_item, null);
            
                holder1.name = (TextView) hView.findViewById(R.id.name);
//                holder1.info = (TextView) hView.findViewById(R.id.info);
                
                hView.setTag(holder1);
            }
            else
            {
             holder1 = (ViewHolder) hView.getTag();
            }
            holder1.populate(itemsArray.get(position));
            
            
            return hView;
		
	}
	
	
	
	}

	class ViewHolder {
	    TextView name;
//	    TextView info;
	    String id;
	    
	    void populate(NotifItem w){
	    	
	    	name.setText(w.name);
//	    	info.setText(w.info);
	    	id = w.id;
	    }
	}
	 
	 class NotifItem {
		    String name;
//		    String info;
		    String id;
		}
	 

	 class GetNotificationsAsync extends AsyncTask<Object, Integer, Object>{
		 
//		 private ProgressDialog progD; 
			
	    	private static final String NAME = "name"; 
	    	private static final String DESC = "desc"; 
	    	private static final String EID = "eid";
	    	private String m_notifId;

		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(dialog != null && dialog.isShowing()){dialog.dismiss();}
			JSONArray jsonArr = (JSONArray) result;

//			MyLogger.d("NOTIF RESP", result.toString());
			
			if(result.toString().equalsIgnoreCase("[]")){
				
				mHandler.post(new Runnable() {
					
					@Override
					public void run() {
						
					showToast(getString(R.string.nonotif));
					finish();
						
					}
				});
				
			}else{
			
			for (int i = 0; i < jsonArr.length(); i++) {
				
	    		 JSONObject jsonObj;
				try {
					jsonObj = jsonArr.getJSONObject(i);
				
	    		 
	    		 NotifItem wlItem = new NotifItem();
	    		 wlItem.id = jsonObj.getString("id");  
	    		 wlItem.name = jsonObj.getString("tit");
//	    		 wlItem.info = jsonObj.getString("des");
	    		 
//	    		 MyLogger.d("Notif added");
	    		 
	    		 itemsArray.add(wlItem);
	    		 
				} catch (JSONException e) {
					// TODO Auto-generated catch block
				}
	    		 
	    	 }
			
			wl = new NotifAdapter(getApplicationContext(),R.layout.notif_view, itemsArray);
			notifList.setAdapter(wl);
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog = ProgressDialog.show(Notifications.this,null,null);
			dialog.setContentView(R.layout.loader);
		}

		
		@Override
		protected Object doInBackground(Object... params) {
			 try {
				 
//				 MyLogger.d("WishlistList", "3 - in doInBackground");
				 ArrayList<JSONObject> eventIdArray = new ArrayList<JSONObject>();
				              
            // JSONRPCClient client = JSONRPCClient.create("http://rainmaker.com.co/xmlrpc-test2/public/server/json", JSONRPCClient.Versions.VERSION_1);
         //  JSONRPCClient client = JSONRPCClient.create("http://rainmaker.com.co/xmlrpc-test2/public/server/json", null);
           JSONRPCClient client = JSONRPCClient.create(BatterySaver.DATA_URL, null);
         
         	jsonArray = client.callJSONArray("cf.getnotifbatt", eventIdArray);
         	   
//         	MyLogger.d("notif 2", jsonArray.toString());

         		 
			 } catch (JSONRPCException e) {
					
//					MyLogger.d("WishlistList-JSONRPCException", e.getMessage());
					
				}
				return jsonArray;
		}
		 
	 }
	 

	 
		class GetNotifItemAsync extends AsyncTask<Object, Integer, String>{

			
//	    	private ProgressDialog progD; 
			
	    	String itemId;
	    	
	    //	final JSONObject dataToSend;
	    	
	    	private JSONArray resultArray;
//	    	Activity m_activity;
	    	
	    	private static final String NAME = "name"; 
	    	private static final String DESC = "desc"; 
	    	private static final String EID = "eid";
	    	private String m_eventId;
	    	
	    	protected GetNotifItemAsync (String id) {
	    	    
	    		itemId = id;
	    		
	    	}
	   	
	    	
			@Override
			protected String doInBackground(Object... arg0) {
				 try {
					 
//					 MyLogger.d("WishlistList", "3 - in doInBackground");
					 ArrayList<JSONObject> notifIdArray = new ArrayList<JSONObject>();
					              
	            // JSONRPCClient client = JSONRPCClient.create("http://rainmaker.com.co/xmlrpc-test2/public/server/json", JSONRPCClient.Versions.VERSION_1);
	       //    JSONRPCClient client = JSONRPCClient.create("http://rainmaker.com.co/xmlrpc-test2/public/server/json", null);
	           JSONRPCClient client = JSONRPCClient.create(BatterySaver.DATA_URL, null);
	         
	           final JSONObject dataToSend = new JSONObject();
	           
	           dataToSend.put("i", itemId);
	         	
//	         	MyLogger.d("WishlistListItem 1", dataToSend.toString());
	         	
	         	notifIdArray.add(dataToSend);
	         	
	         	resultArray = client.callJSONArray("cf.getnotifitembat",notifIdArray );
	         	   
	         		 
					} catch (JSONRPCException e) {
						
//						MyLogger.d("WishlistList-JSONRPCException", e.getMessage());						
						mHandler.post(new Runnable() {
	                        public void run() {

	                        	showToast(getString(R.string.unerr));
	                        }
	                    });						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
//						MyLogger.d("GetWishlistItemAsync",e.getMessage());
						mHandler.post(new Runnable() {
	                        public void run() {
	                       
	                        	showToast(getString(R.string.unerr));
	                        }
	                    });
					}
					
					return resultArray.toString();
			
	 }	
			
			@Override
			protected void onPostExecute(String result) {
				
				super.onPostExecute(result);
				if(dialog != null && dialog.isShowing()){dialog.dismiss();}
				
				if(result.length() > 5){
					
				Intent notif = new Intent(Notifications.this, NotifView.class);
				notif.putExtra("API_RESPONSE", result);
//				notif.putExtra("nid", m_eventId);
				
				
				startActivity(notif);
				
				}else{
				
					mHandler.post(new Runnable() {
                        public void run() {
                       
//                        	Util.showAlert(Notifications.this, "Error", "This item does not exist");
                        	showToast(getString(R.string.unerr));
                        }
                    });
					
				}
				
			} // onPostExecute
			
			@Override
			  protected void onPreExecute() {
				 super.onPreExecute(); 
				 // progD.setMessage("Creating profile");
				 
				 dialog = ProgressDialog.show(Notifications.this,null,null);
    			 dialog.setContentView(R.layout.loader);

				// progD.show();
			    }

			
			@Override
			protected void onProgressUpdate(Integer... values)  {
				super.onProgressUpdate(values); 
				//progD.setProgress((int) ((values[0] / (float) values[1]) * 100));
			}
	

	}			
	   
	 private void showToast(String message){
			Toast.makeText(Notifications.this, message, Toast.LENGTH_SHORT).show();
		}
	 
	 
	 
	 
}
