package com.example.digitalclock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;



import android.app.Activity;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

public class BigClock extends Activity{
	ArrayList<TextView> _fields = new ArrayList<TextView>();
	int sc =0;
	goInd mt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_big_clock);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		Typeface face=Typeface.createFromAsset(getAssets(),
                "Electron.ttf");
		
		List<Integer> fldId = Arrays.asList(R.id.bh1,R.id.brr1,R.id.bmm1,
											R.id.bday,R.id.bmount,R.id.byear,
											R.id.brr2,R.id.bss1,R.id.btemp);
		for (int i=0;i<fldId.size();i++){
			TextView fff = (TextView)findViewById(fldId.get(i));
			fff.setTypeface(face);
			_fields.add(fff);
		}
		
		Thread t = new Thread() {
      	  @Override
      	  public void run() {
      	    try {
      	      while (!isInterrupted()) {
      	        Thread.sleep(1000);
      	        runOnUiThread(new Runnable() {
      	          @Override
      	          public void run() {
      	        	  sc ++;
      	        	  	Calendar currentTime = Calendar.getInstance();
      	        	  	_fields.get(0).setText(((currentTime.get(11)) >=10 ? String.valueOf(currentTime.get(11))  : "0"+String.valueOf(currentTime.get(11))));
      	        	  	_fields.get(2).setText(((currentTime.get(12)) >=10 ? String.valueOf(currentTime.get(12))  : "0"+String.valueOf(currentTime.get(12))));
      	      	    	String[] xx = gisFromSite.getCurrData();
      	      	    	
      	      	    	_fields.get(4).setText(xx[2]);
      	      	    	_fields.get(5).setText(xx[1]);
      	      	    	_fields.get(3).setText(xx[7]);
      	      	    	_fields.get(7).setText(xx[5]);
      	      	      
      	      	    	if (sc>300) {
      	      	    		sc = 0;
      	      	    		mt = new goInd();
      	      	    		mt.execute();
      	      	    	}
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
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.big_clock, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	class goInd extends AsyncTask<Void, Void, Void> {
		List<String> _stringData = Arrays.asList("-","err","-","err","-","err","-","err","err","err","err","err","err","err");
		/////////////////////////////////////////////////////////////////////////////////////
		
		@Override
	    protected void onPreExecute() {
	      super.onPreExecute();
	    }//protected void onPreExecute()

		/////////////////////////////////////////////////////////////////////////////////////
	    
		@Override
	    protected Void doInBackground(Void... params) {
			List<Integer> ind = Arrays.asList(0,2,6,4);
			
	    	try {
	    		//**мой датчик
	    		String[] x = gisFromSite.readMy();
	    		if (x.length>6) {
	    			for(int i=0;i<ind.size();i++){
	    				_stringData.set(i, x[ind.get(i)].indexOf(" ")>0 
	    						? x[ind.get(i)] : "".concat(x[ind.get(i)])) ;
	    				Log.i("m",x[ind.get(i)].indexOf(" ")>0 
	    						? x[ind.get(i)] : "".concat(x[ind.get(i)]));
	    			}
	    		}
	    	} catch (IOException e) {
	    		Log.i("m","error read my weather");
			}
	    	return null;
	    }//protected Void doInBackground(Void... params)

		/////////////////////////////////////////////////////////////////////////////////////
	    
		@Override
	    protected void onPostExecute(Void result) {
	      super.onPostExecute(result);
	      _fields.get(8).setText(_stringData.get(0));
		}//protected void onPostExecute(Void result)
	}
}
