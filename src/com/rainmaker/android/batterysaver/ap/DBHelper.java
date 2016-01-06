package com.rainmaker.android.batterysaver.ap;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.sax.StartElementListener;
import android.util.Log;


public class DBHelper extends SQLiteOpenHelper{

	static final String dbName="batdb";
	static final String appTable="appwifi";
	
	static final String col_name="appname";
	static final String col_pack="apppack";
	public static String Lock = "dblock";
	
	
	
	public DBHelper(Context context) {
		super(context, dbName, null, 33);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL("CREATE TABLE IF NOT EXISTS "+appTable+" ("+col_name+" TEXT, "+col_pack+" TEXT)");
		
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("DROP TABLE IF EXISTS "+appTable);
		onCreate(db);
	}
	
	
	
	//my methods
	
	int getDBAppCount(){
		
		SQLiteDatabase db = getReadableDatabase();
		Cursor mCount= db.rawQuery("select count(*) from "+appTable, null);
		mCount.moveToFirst();
		int count= mCount.getInt(0);
		mCount.close();
		return count;
	}
	
	
	
	void AddApp(AppDetail emp)
	{
		
		synchronized(Lock) {
			
			SQLiteDatabase db= this.getWritableDatabase();
			 
			ContentValues cv=new ContentValues();
			
			cv.put(col_name, emp.getName());
			cv.put(col_pack, emp.getPackage());
			
			db.insert(appTable, col_pack, cv);
		 }
		
//		db.close(); check
		
		
	}
	
	
	void AddAllApps(ArrayList<AppDetail> emp)
	{
		
		SQLiteDatabase db;
		ContentValues cv=new ContentValues();
		
		synchronized(Lock) {
			
			db= this.getWritableDatabase();
			
			for (AppDetail s : emp)
	        {
	            
				cv.put(col_name, s.getName());
				cv.put(col_pack, s.getPackage());				
				db.insert(appTable, col_pack, cv);	
	        }
			
			
		 }
		
		db.close(); 
		
		
	}
	

	
	ArrayList<String> getAppPackages()
	 {
		 SQLiteDatabase db=this.getReadableDatabase();
		 Cursor cur=db.rawQuery("SELECT "+col_pack+" from "+appTable,new String [] {});
		 
		 ArrayList<String> items = new ArrayList<String>();
		 
		 for(cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
			    // The Cursor is now set to the right position
			 	//Log.v("rnm DBHelper ", cur.getString(0));
			    items.add(cur.getString(0));
			    
			}
		 cur.close();
		 db.close();
		 
		 return items;
	 }
	
	
	void deleteAll()
	{
	    
		SQLiteDatabase db= this.getWritableDatabase();
		
		db.execSQL("DELETE FROM "+appTable);
		
//	    db.delete(appTable, null, null);
	    db.close();

	}

}
