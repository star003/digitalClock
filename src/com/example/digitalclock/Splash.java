package com.example.digitalclock;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;

public class Splash extends Activity {

	private static final String APP_PREFERENCES = "digitalClock";
	//private static final Boolean APP_PREFERENCES_SMALL_SCREEN_SIZE = false;
    private SharedPreferences mSettings;
    //private static Boolean screenSize = false;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
		
		if(mSettings.contains("APP_PREFERENCES_SMALL_SCREEN_SIZE")) {
			//screenSize = mSettings.getBoolean("APP_PREFERENCES_SMALL_SCREEN_SIZE",false );
			Intent intent = new Intent(this, DashBoard.class);
			startActivity(intent);
			finish();
		}
		
	}//protected void onCreate(Bundle savedInstanceState)
	
	@SuppressLint("NewApi")
	public void onClick(View v) {
		if (v.getId()==R.id.imageSmall | v.getId()==R.id.textImageSmall) {
			
			Editor editor = mSettings.edit();
			editor.putBoolean("APP_PREFERENCES_SMALL_SCREEN_SIZE", true);
			editor.apply();
			
			Intent intent = new Intent(this, DashBoard.class);
		    startActivity(intent);
		    finish();
		}
		
		if (v.getId()==R.id.imageBig | v.getId()==R.id.textImageBig) {
			
			Editor editor = mSettings.edit();
			editor.putBoolean("APP_PREFERENCES_SMALL_SCREEN_SIZE", false);
			editor.apply();
			
			Intent intent = new Intent(this, DashBoard.class);
		    startActivity(intent);
		    finish();
		}
		
	}//public void onClick(View v)
}
