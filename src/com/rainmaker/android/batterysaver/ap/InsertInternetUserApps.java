package com.rainmaker.android.batterysaver.ap;

import java.util.ArrayList;
import java.util.List;

import com.rainmaker.android.batterysaver.ap.BatterySaver.GetAppListAsync;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class InsertInternetUserApps extends AsyncTask<Object, Void, Void>{
	
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(dialog != null && dialog.isShowing()){dialog.dismiss();}
		
		Toast toast = Toast.makeText(ctx, ctx.getString(R.string.welc),
                Toast.LENGTH_SHORT);
        toast.show();
		
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		
		Toast toast = Toast.makeText(ctx, ctx.getString(R.string.wait),
                Toast.LENGTH_LONG);
        toast.show();
		
		dialog = ProgressDialog.show(ctx,null,null);
		 	dialog.setContentView(R.layout.loader);
		
	}

	private static final String TAG = "InsertInternetUserApps";
	Context ctx;
	
	ProgressDialog dialog;
	
	DBHelper dbHelper;
	
	public InsertInternetUserApps(Context ctxx) {
	
		this.ctx = ctxx;
		
		dialog = new ProgressDialog(ctxx);
		
		dbHelper=new DBHelper(ctx);
	}
	
	@Override
	protected Void doInBackground(Object... params) {
		
		PackageManager pm = ctx.getPackageManager();
		List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
		
		prefs.edit().putInt(BatterySaver.APP_COUNT, packages.size()).commit();

		for (ApplicationInfo applicationInfo : packages) {
			
			
			try {

				PackageInfo packageInfo = pm.getPackageInfo(applicationInfo.packageName, PackageManager.GET_PERMISSIONS);
				PackageManager mPackManager = ctx.getPackageManager();
				
//				dbHelper.deleteAll();
				
				//Get Permissions
				String[] requestedPermissions = packageInfo.requestedPermissions;
				
				ArrayList<AppDetail> appDetailArray = new ArrayList<AppDetail>();

				if(requestedPermissions != null)
				{
				  for (int i = 0; i < requestedPermissions.length; i++) {
					  
					  if(requestedPermissions[i].equalsIgnoreCase("android.permission.INTERNET")){
						  
						  AppDetail app=new AppDetail(applicationInfo.loadLabel(mPackManager).toString(),applicationInfo.packageName);
							
							dbHelper.AddApp(app);
							
							//Log.d(TAG, "rnm ADDED App:"+applicationInfo.loadLabel(mPackManager)+" Package :" + applicationInfo.packageName);
					  }
					  
					  dbHelper.close();
					  
				  }
				}


			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		
		return null;
	}

	

}
