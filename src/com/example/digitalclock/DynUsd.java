package com.example.digitalclock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	описание:
//		покажет , что по доллару

public class DynUsd extends Activity {
	ArrayList<TextView> _fields = new ArrayList<TextView>();
	
	int sc =0;
	
	goMCEX mt;
	
	String this_marker = "DynUsd"; //** зададим имя маркера для логов
	
	boolean this_small 	= true;
	private static final String APP_PREFERENCES = "digitalClock";
    private SharedPreferences mSettings;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
		
		if(mSettings.contains("APP_PREFERENCES_SMALL_SCREEN_SIZE")) {
			this_small = mSettings.getBoolean("APP_PREFERENCES_SMALL_SCREEN_SIZE",false );
		}
		
		if (this_small) {
			setContentView(R.layout.activity_small_dyn_usd);
		}
		else {
			setContentView(R.layout.activity_dyn_usd);
		}
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		List<Integer> fldId = Arrays.asList(R.id.usLast,R.id.usLastTrend
											,R.id.usVal ,R.id.usTime);
		
		for (int i=0;i<fldId.size();i++){
			TextView fff = (TextView)findViewById(fldId.get(i));
			_fields.add(fff);
		}
		
		mt = new goMCEX();
    	mt.execute();
    		
		Thread t = new Thread() {
			@Override
			public void run() {
				try {
					while (!isInterrupted()) {
						Thread.sleep(30000);
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								mt = new goMCEX();
								mt.execute();
							}
						});
	      	      	}
	      	    } catch (InterruptedException e) {
	      	    	Log.e(this_marker,"error thread T");
	      	    }
			}//public void run()
		};//Thread t = new Thread()
		t.start();
	      	
	}//protected void onCreate(Bundle savedInstanceState)

	/////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.dyn_usd, menu);
		return true;
	}//public boolean onCreateOptionsMenu(Menu menu)
	
	/////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}//public boolean onOptionsItemSelected(MenuItem item)
	
	class goMCEX extends AsyncTask<Void, Void, Void> {
		ArrayList<String> _stringData = new ArrayList<String>();
		
		/////////////////////////////////////////////////////////////////////////////////////
		
		@Override
	    protected void onPreExecute() {
			super.onPreExecute();
	    }//protected void onPreExecute()

		/////////////////////////////////////////////////////////////////////////////////////
	    
		@Override
	    protected Void doInBackground(Void... params) {
			Log.i(this_marker,"doInBackground(Void... params) DashBoard.dyn_brent_usd = " + DashBoard.dyn_brent_usd);
			if (DashBoard.dyn_brent_usd.equals("usd")) {
				try {
	    			_stringData = priceBRENT.usdFinam();
	    		} catch (IOException e) {
	    			Log.e(this_marker,"error read priceBRENT.usdFinam() in class goMCEX");
	    		}
			}
	    	else if(DashBoard.dyn_brent_usd.equals("brent")) {
	    		try {
	    			_stringData = priceBRENT.dynBrentFinam();
	    		}
	    		catch (IOException e) {
	    			Log.e(this_marker,"error read priceBRENT.dynBrentFinam() in class goMCEX");
	    		}
    		}
	    	return null;
	    }//protected Void doInBackground(Void... params)

		/////////////////////////////////////////////////////////////////////////////////////
	    
		@Override
	    protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			List<Integer> ind = Arrays.asList(0,1,6,7);
			if (_stringData.size()>6) {
				for (int i=0;i<ind.size();i++){
					_fields.get(i).setText(_stringData.get(ind.get(i)));
				}
			}	
			else {
				Log.e(this_marker,"error _stringData < 7 in class goMCEX");
			}
		}//protected void onPostExecute(Void result)
		
	}//class goMCEX extends AsyncTask<Void, Void, Void>
}//public class DynUsd extends Activity
