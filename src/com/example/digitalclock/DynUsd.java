package com.example.digitalclock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

public class DynUsd extends Activity {
	ArrayList<TextView> _fields = new ArrayList<TextView>();
	int sc =0;
	goMCEX mt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dyn_usd);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		List<Integer> fldId = Arrays.asList(R.id.usLast,R.id.usOpen,R.id.usVal,R.id.usTime);
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
	      	        Thread.sleep(360000);
	      	        runOnUiThread(new Runnable() {
	      	          @Override
	      	          public void run() {
	      	        	mt = new goMCEX();
  	      	    		mt.execute();
	      	          }
	      	        });
	      	      }
	      	    } catch (InterruptedException e) {
	      	    }
	      	  }
	      	};
	      	t.start();
	      	
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.dyn_usd, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
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
	    	try {
	    		_stringData = priceBRENT.getUsdDay();
	    	} catch (IOException e) {
	    		Log.i("m","error read priceBRENT.getUsdDay()");
			}
	    	return null;
	    }//protected Void doInBackground(Void... params)

		/////////////////////////////////////////////////////////////////////////////////////
	    
		@Override
	    protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (_stringData.size()==4) {
				for (int i=0;i<4;i++){
					_fields.get(i).setText(_stringData.get(i));
				}
			}	
		}//protected void onPostExecute(Void result)
	}
}
