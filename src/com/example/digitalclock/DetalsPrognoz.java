package com.example.digitalclock;

import java.io.IOException;
import java.util.ArrayList;


import android.app.Activity;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

/////////////////////////////////////////////////////////////////////////////////////////////////////////
//	описание:
//		не используется . . .

public class DetalsPrognoz extends Activity {
	TextView pr1,pr2,pr3,pr4;
	goPr mt;
	String this_marker = "DetalsPrognoz"; //** зададим имя маркера для логов
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detals_prognoz);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		Typeface face=Typeface.createFromAsset(getAssets(),"digital-7mi.ttf");
		
		pr1 = (TextView)findViewById(R.id.pr1);
		pr1.setTypeface(face);
		
		pr2 = (TextView)findViewById(R.id.pr2);
		pr2.setTypeface(face);
		
		pr3 = (TextView)findViewById(R.id.pr3);
		pr3.setTypeface(face);
		
		pr4 = (TextView)findViewById(R.id.pr4);
		pr4.setTypeface(face);
		mt = new goPr();
        mt.execute();
        
	}//protected void onCreate(Bundle savedInstanceState)

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.detals_prognoz, menu);
		return true;
	}//public boolean onCreateOptionsMenu(Menu menu)

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}//public boolean onOptionsItemSelected(MenuItem item)
	
	class goPr extends AsyncTask<Void, Void, Void> {
		String _pr1,_pr2,_pr3,_pr4 ="";
		
		/////////////////////////////////////////////////////////////////////////////////////		
		
		@Override
		protected void onPreExecute() {
		      super.onPreExecute();
		}//protected void onPreExecute()

		/////////////////////////////////////////////////////////////////////////////////////
		
		@Override
		protected Void doInBackground(Void... params) {
	    	try {
	    		ArrayList<String> h1 = gisFromSite.getPrognozV2();
	    		if (h1.size()>3) {
	    			_pr1= String.valueOf(h1.get(0));
	    			
	    			_pr2= String.valueOf(h1.get(1));
	    			_pr3= String.valueOf(h1.get(2));
	    			_pr4= String.valueOf(h1.get(3));
	    			
	    		}
			} catch (IOException e) {
				_pr1 ="err";
				_pr2 ="err";
				_pr3 ="err";
				_pr4 ="err";
			}
		    	return null;
	    }//protected Void doInBackground(Void... params)
		
		/////////////////////////////////////////////////////////////////////////////////////
	    
		@Override
	    protected void onPostExecute(Void result) {
	    	super.onPostExecute(result);
	    	
	    	pr1.setText(_pr1);
	    	pr2.setText(_pr2);
	    	pr3.setText(_pr3);
	    	pr4.setText(_pr4);
	    	
	    }//protected void onPostExecute(Void result)
	}//class goPr extends AsyncTask<Void, Void, Void>
}
