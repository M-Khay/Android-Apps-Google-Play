package com.rainmaker.android.batterysaver.ap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AppManager extends Activity {
  
  private ListView mainListView ;
  private AppInfo[] planets ;
  private ArrayAdapter<AppInfo> listAdapter ;
  
  ArrayList<AppInfo> appsArrayList;
  
  private PackageManager mPackManager;
  
  ProgressDialog dialog;
  
  DBHelper dbHelper;
  
  private Button setAppsConfig;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.app_manager);
    
    dialog = new ProgressDialog(AppManager.this);
    dialog = ProgressDialog.show(AppManager.this,null,null);
 	dialog.setContentView(R.layout.loader);
 	
    dbHelper = new DBHelper(this);
    
    setAppsConfig = (Button)findViewById(R.id.setbutton);
    
    setAppsConfig.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {

			dbHelper.deleteAll();
			
			for (int i = 0; i < listAdapter.getCount(); i++)
            {
                AppInfo item = listAdapter.getItem(i);
                if (item.isChecked())
                {
                	AppDetail app=new AppDetail(item.getName(),item.getPackage());
					
					dbHelper.AddApp(app);
                }
                
            }
			
			dbHelper.close();
			Toast.makeText(AppManager.this,
					getString(R.string.setchg),
                    Toast.LENGTH_SHORT).show();
			finish();
		}
	});
    
    
    mPackManager = getPackageManager();
    
    // Find the ListView resource. 
    mainListView = (ListView) findViewById( R.id.mainListView );
    mainListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE); 
    
    // When item is tapped, toggle checked properties of CheckBox and Planet.
    mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick( AdapterView<?> parent, View item, 
                               int position, long id) {
        AppInfo planet = listAdapter.getItem( position );
        planet.toggleChecked();
        AppViewHolder viewHolder = (AppViewHolder) item.getTag();
        viewHolder.getCheckBox().setChecked( planet.isChecked() );
      }
    });

    
    ArrayList<String> existingApps = dbHelper.getAppPackages();
    
//    Bundle extras = getIntent().getExtras();
    
//    ArrayList<String> existingApps = extras.getStringArrayList("apps_values");
    
    // //Log.v("TESST 0", " "+existingApps);
    
    List<ApplicationInfo> appInfoList = Utilities.getInstalledApplication(this);
    
     appsArrayList = new ArrayList<AppInfo>();
    
    for (int i = 0; i < appInfoList.size(); i++) {
		ApplicationInfo aInfo = appInfoList.get(i);
		
		// //Log.v("TESST 1", " CONTAINS "+aInfo.packageName);
		
		AppInfo appItem;
		
		if(aInfo.loadIcon(mPackManager) != null){
		
		 appItem = new AppInfo(aInfo.loadLabel(mPackManager).toString(), aInfo.packageName, aInfo.loadIcon(mPackManager));
		}else{
			
		 appItem = new AppInfo(aInfo.loadLabel(mPackManager).toString(), aInfo.packageName);
		}

		 if(existingApps.contains(aInfo.packageName)){
				
				// //Log.v("TESST 3", " CONTAINS "+aInfo.packageName);
				appItem.setChecked(true);
			}
		 
		appsArrayList.add(appItem);
		
	}
    
    
//    appsArrayList.addAll( Arrays.asList(planets) );
    
    // Set our custom array adapter as the ListView's adapter.
    listAdapter = new AppArrayAdapter(this, appsArrayList);
    mainListView.setAdapter( listAdapter );      
   
    if(dialog != null && dialog.isShowing()){dialog.dismiss();}
  }
  
  /** Holds planet data. */
  private static class AppInfo {
    private String name = "" ;
    
    private String appPackage = "";
    
    private Drawable imgIcon;
    
    private boolean checked = false ;
    public AppInfo() {}
    public AppInfo( String name, String pack ) {
      this.name = name ;
      this.appPackage = pack ;
    }
    public AppInfo( String name, boolean checked ) {
      this.name = name ;
      this.checked = checked ;
    }
    
    public AppInfo( String name, String pack, boolean checked ) {
        this.name = name ;
        this.appPackage = pack ;
        this.checked = checked ;
      }
    
    public AppInfo( String name, String pack, boolean checked, Drawable img ) {
        this.name = name ;
        this.appPackage = pack ;
        this.checked = checked ;
        this.imgIcon = img;
      }
    
    public AppInfo( String name, String pack, Drawable img ) {
        this.name = name ;
        this.appPackage = pack ;
        this.imgIcon = img;
      }
    
    public Drawable getIcon() {
        return imgIcon;
      }

    public void setIcon(Drawable ic) {
        imgIcon = ic;
      }

    public String getName() {
      return name;
    }
    
    public String getPackage() {
        return appPackage;
      }
    
    public void setName(String name) {
      this.name = name;
    }
    public boolean isChecked() {
      return checked;
    }
    public void setChecked(boolean checked) {
      this.checked = checked;
    }
    public String toString() {
      return name ; 
    }
    public void toggleChecked() {
      checked = !checked ;
    }
  }
  
  /** Holds child views for one row. */
  private static class AppViewHolder {
    private CheckBox checkBox ;
    private TextView textView ;
    private ImageView imageView ;
    
    
    public AppViewHolder() {}
    public AppViewHolder( TextView textView, CheckBox checkBox ) {
      this.checkBox = checkBox ;
      this.textView = textView ;
    }
    
    public AppViewHolder( TextView textView, CheckBox checkBox, ImageView imgV ) {
        this.checkBox = checkBox ;
        this.textView = textView ;
        this.imageView = imgV;
      }
    
    public CheckBox getCheckBox() {
      return checkBox;
    }
    public void setCheckBox(CheckBox checkBox) {
      this.checkBox = checkBox;
    }
    public TextView getTextView() {
      return textView;
    }
    public void setTextView(ImageView imgView) {
      this.imageView = imgView;
    }
    public ImageView getImageView() {
        return imageView;
      }
      public void setImageView(ImageView imgView) {
        this.imageView = imgView;
      }
  }
  
  
  
  /** Custom adapter for displaying an array of Planet objects. */
  private  class AppArrayAdapter extends ArrayAdapter<AppInfo> {
	  int temp;
    private LayoutInflater inflater;
    private int counter=0;
    public AppArrayAdapter( Context context, List<AppInfo> planetList ) {
      super( context, R.layout.simplerow, R.id.rowTextView, planetList );
      // Cache the LayoutInflate to avoid asking for a new one each time.
      inflater = LayoutInflater.from(context) ;
      counter=0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      // Planet to display
      AppInfo planet = (AppInfo) this.getItem( position ); 

      // The child views in each row.
      CheckBox checkBox ; 
      TextView textView ; 
      ImageView icon;
      Drawable d;
      // Create a new row view
      if ( convertView == null ) {
        convertView = inflater.inflate(R.layout.simplerow, null);
        
        // Find the child views.
        textView = (TextView) convertView.findViewById( R.id.rowTextView );
        checkBox = (CheckBox) convertView.findViewById( R.id.CheckBox01 );
        icon = (ImageView) convertView.findViewById(R.id.ivIcon);
       // Optimization: Tag the row with it's child views, so we don't have to 
        // call findViewById() later when we reuse the row.
      
        if(counter==0)
      {
    	   temp= planet.getIcon().getMinimumWidth();
    	  System.out.println(temp);
    	  counter++;
        
      } 
      if(planet.getIcon().getMinimumWidth()>temp )
      {	
      	d = getResources().getDrawable(R.drawable.ic);
      System.out.println("heeel");
      planet.setIcon(d);
      }
        icon.setImageDrawable(planet.getIcon()); //p
        
        convertView.setTag( new AppViewHolder(textView,checkBox, icon) );

        // If CheckBox is toggled, update the planet it is tagged with.
        checkBox.setOnClickListener( new View.OnClickListener() {
          public void onClick(View v) {
       
        	  CheckBox cb = (CheckBox) v ;
            AppInfo planet = (AppInfo) cb.getTag();
       
            System.out.println(planet.getName());     
            planet.setChecked( cb.isChecked() );
          }
        });        
      }
      // Reuse existing row view
      else {
        // Because we use a ViewHolder, we avoid having to call findViewById().
        AppViewHolder viewHolder = (AppViewHolder) convertView.getTag();
        if(planet.getIcon().getMinimumWidth()>temp )
        {	d = getResources().getDrawable(R.drawable.ic);
        System.out.println("heeel");
        planet.setIcon(d);
        }
        checkBox = viewHolder.getCheckBox() ;
        textView = viewHolder.getTextView() ;
        icon = viewHolder.getImageView();
      }

      // Tag the CheckBox with the Planet it is displaying, so that we can
      // access the planet in onClick() when the CheckBox is toggled.
      checkBox.setTag( planet ); 
      
      // Display planet data
      checkBox.setChecked( planet.isChecked() );
      textView.setText( planet.getName() );    
      icon.setImageDrawable(planet.getIcon()); 
      
      return convertView;
    }
    
  }
  
  public Object onRetainNonConfigurationInstance() {
    return planets ;
  }
}