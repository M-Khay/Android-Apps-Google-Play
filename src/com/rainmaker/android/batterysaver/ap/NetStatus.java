package com.rainmaker.android.batterysaver.ap;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * @author prashantmavinkurve
 * USAGE: 
 * if (NetStatus.getInstance(this).isOnline(this)) {
*
 *   Toast t = Toast.makeText(this,"You are online!!!!",8000).show();
*
* 		}
 *
 */

public class NetStatus {

    private static NetStatus instance = new NetStatus();
    static Context context;
    ConnectivityManager connectivityManager;
    NetworkInfo wifiInfo, mobileInfo;
    boolean connected = false;

    public static NetStatus getInstance(Context ctx) {
        context = ctx;
        return instance;
    }

    public boolean isOnline(Context con) {
        try {
            connectivityManager = (ConnectivityManager) con
                        .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        connected = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnectedOrConnecting();
//                networkInfo.isConnected();
        return connected;


        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
//            MyLogger.d("connectivity", e.toString());
        }
        return connected;
    }
}