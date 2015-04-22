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
	String this_marker = "BigClock"; //** зададим имя маркера для логов
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_big_clock);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		Typeface face=Typeface.createFromAsset(getAssets(), "Electron.ttf");
		
		/*
		 * 0-час , 1- разделитель , 2- минута
		 * 3- день , 4- месяц ,5- день недели
		 * 6- разделитель 2 (секунды) ,7 - секунды
		 * 8- температура
		 */
		List<Integer> fldId = Arrays.asList(R.id.bh1,R.id.brr1,R.id.bmm1,
											R.id.bday,R.id.bmount,R.id.byear,
											R.id.brr2,R.id.bss1,R.id.btemp);
		for (int i=0;i<fldId.size();i++){
			TextView fff = (TextView)findViewById(fldId.get(i));
			fff.setTypeface(face);
			_fields.add(fff);
		}
		
		mt = new goInd();
		mt.execute();
			
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
      						//**часы, минуты
      						Calendar currentTime = Calendar.getInstance();
      						_fields.get(0).setText(((currentTime.get(11)) >=10 ? String.valueOf(currentTime.get(11))  : "0"+String.valueOf(currentTime.get(11))));
      						_fields.get(2).setText(((currentTime.get(12)) >=10 ? String.valueOf(currentTime.get(12))  : "0"+String.valueOf(currentTime.get(12))));
      						String[] xx = gisFromSite.getCurrData();
      						/*
      						 * хх 
      						 * 	0-год
      						 *	1-месяц
      						 *	2-день
      						 *	3-час
      						 *	4-минута
      						 *	5-секунда
      						 *	6-назв.мес
      						 *	7-день недели
      						 */
      	      	    	
      						//** месяц, секунда,день
      						_fields.get(4).setText(xx[1]);
      						_fields.get(5).setText(xx[7]);
      	      	    		_fields.get(3).setText(xx[2]);
      	      	    		_fields.get(7).setText(xx[5]);
      	      	      
      	      	    		if (sc>300) {
      	      	    			sc = 0;
      	      	    			mt = new goInd();
      	      	    			mt.execute();
      	      	    		}
      					}//public void run()
      				});//runOnUiThread(new Runnable()
      			}
      		}
      		catch (InterruptedException e) {
      			Log.i(this_marker,"error thread t ");
      		}
      	}	//public void run()
		};	//Thread t = new Thread()
      	t.start();
	}//protected void onCreate(Bundle savedInstanceState)
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.big_clock, menu);
		return true;
	}//public boolean onCreateOptionsMenu(Menu menu)

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}//public boolean onOptionsItemSelected(MenuItem item)
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	class goInd extends AsyncTask<Void, Void, Void> {
		ArrayList<String> _stringData = new ArrayList<String>();
		
		@Override
	    protected void onPreExecute() {
	      super.onPreExecute();
	    }//protected void onPreExecute()

		/////////////////////////////////////////////////////////////////////////////////////
	    
		@Override
	    protected Void doInBackground(Void... params) {
	    	try {
	    		_stringData.add(gisFromSite.readMy()[0]);
	    	} catch (IOException e) {
	    		Log.i(this_marker,"error gisFromSite.readMy() in class goInd");
			}
	    	return null;
	    }//protected Void doInBackground(Void... params)

		/////////////////////////////////////////////////////////////////////////////////////
	    
		@Override
	    protected void onPostExecute(Void result) {
	      super.onPostExecute(result);
	      _fields.get(8).setText(_stringData.get(0));
		}//protected void onPostExecute(Void result)
		
	}//class goInd extends AsyncTask<Void, Void, Void>
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
}//public class BigClock extends Activity
