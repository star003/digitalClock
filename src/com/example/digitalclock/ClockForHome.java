package com.example.digitalclock;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;



import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class ClockForHome extends Activity {
	ArrayList<TextView> _fields = new ArrayList<TextView>();
	ArrayList<ImageView> _fieldsI 	= new ArrayList<ImageView>();
	int sc =0;
	goData mt;
	String this_marker = "ClockForHome"; //** зададим имя маркера для логов
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clock_for_home);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		Typeface face=Typeface.createFromAsset(getAssets(), "Electron.ttf");
		//**описание текстовых полей
		//**c 8 го идет суточный прогноз
		List<Integer> fldId = Arrays.asList(R.id.hcTime , R.id.hcDate , R.id.hcWeekDay
											,R.id.hcCurrT , R.id.hcFactPrs , R.id.hcFactHmd
											,R.id.hcFactWing , R.id.hcFactCloud,
											
											R.id.hcPrStime1,R.id.hcPrStmp1,R.id.hcPrSWing1,
											R.id.hcPrStime2,R.id.hcPrStmp2,R.id.hcPrSWing2,
											R.id.hcPrStime3,R.id.hcPrStmp3,R.id.hcPrSWing3 );
		//**описание графический полей
		List<Integer> fldIdGr = Arrays.asList(R.id.hcFact0,R.id.hcPrIm1
												,R.id.hcPrIm2,R.id.hcPrIm3);
		
		
		for (int i=0;i<fldId.size();i++){
			TextView fff = (TextView)findViewById(fldId.get(i));
			if(i==3 ){
				fff.setTypeface(face);
			}	
			_fields.add(fff);
		}
		
		for (int i=0;i<fldIdGr.size();i++){
			ImageView fff = (ImageView)findViewById(fldIdGr.get(i));
			_fieldsI.add(fff);
		}
		
		mt = new goData();
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
	      						String currTime = ((currentTime.get(11)) >=10 ? String.valueOf(currentTime.get(11))  : "0"+String.valueOf(currentTime.get(11)));
	      						currTime =currTime.concat(":");
	      						currTime =currTime.concat((currentTime.get(12)) >=10 ? String.valueOf(currentTime.get(12))  : "0"+String.valueOf(currentTime.get(12)));
	      						
	      						_fields.get(0).setText(currTime);
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
	      						 *	8-день недели полн.
	      						 */
	      						String currDate = xx[2];
	      						currDate = currDate.concat(" ");
	      						currDate = currDate.concat(xx[6]);
	      						_fields.get(1).setText(currDate);
	      						_fields.get(2).setText(xx[8]);
	      						
	      	      	    		if (sc>300) {
	      	      	    			sc = 0;
	      	      	    			mt = new goData();
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
	
	class goData extends AsyncTask<Void, Void, Void> {
		ArrayList<String> _stringData = new ArrayList<String>();
		ArrayList<String> _stringDataHP = new ArrayList<String>();
		String _gisPick ="";
				
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
				Log.i(this_marker,"error gisFromSite.readMy() in class goData");
			}
			
			try {
				ArrayList<ArrayList<String>> x = gisFromSite.grabGismeteo();
				if (x.size()>6) {
					//1+2 , 4 ,5+6 
					_stringData.add(x.get(1).get(0));
					_stringData.add(x.get(2).get(0));
					_stringData.add(x.get(4).get(0));
					_stringData.add(x.get(6).get(0) + "m/s, " + x.get(5).get(0));
					_stringData.add(x.get(7).get(0));
				}
				else{
					_stringData.add("err");
					_stringData.add("err");
					_stringData.add("err");
					_stringData.add("err");
					_stringData.add("err");
					
					Log.i(this_marker,"x.size() = "+String.valueOf(x.size()));
				}
				
			} catch (IOException e) {
				Log.i(this_marker,"error gisFromSite.grabGismeteo() in class goData");
				_stringData.add("err");
				_stringData.add("err");
				_stringData.add("err");
				_stringData.add("err");
				_stringData.add("err"); //**4
			}
			//_stringData.add();
			ArrayList<ArrayList<String>> x1;
			try {
				List<Integer> ind = Arrays.asList(0,2,3,5,6);
				//**от 5 до 46
				x1 = gisFromSite.getHourPrognoz();
				for(int i = 0 ;i<3 ;i++){
					for(int j = 0 ;j < ind.size();j++){
						_stringDataHP.add(x1.get(i).get(ind.get(j)));
					}
				}
				
			} catch (IOException e) {
				Log.i(this_marker,"error gisFromSite.getHourPrognoz() in class goData");
			}
			
			try {
				_gisPick = gisFromSite.getCurGismeteoPic();
			} catch (IOException e) {
				Log.i(this_marker,"error gisFromSite.getCurGismeteoPic() in class goData");
			}
			return null;
		}//protected Void doInBackground(Void... params)

		/////////////////////////////////////////////////////////////////////////////////////

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			//String gr = unit.fromBaseUnitsAsString(this.angle);
			_fields.get(3).setText( _stringData.get(0).indexOf("-")>0 
					? _stringData.get(0)+ ((char) 0x00B0)+"C" : "+"+ _stringData.get(0)+ ((char) 0x00B0));
			
			_fields.get(4).setText(_stringData.get(3));
			_fields.get(5).setText(_stringData.get(5));
			_fields.get(6).setText(_stringData.get(4));
			
			ImageView ggg = _fieldsI.get(0); 
			new DownloadImageTask((ImageView) ggg).execute(_gisPick);
			
					
			if(_stringDataHP.size()>14){
				for(int i = 0;i<3;i++){
					_fields.get(8+i*3).setText(_stringDataHP.get(i*5));	
					_fields.get(9+i*3).setText(_stringDataHP.get(i*5+2));
					_fields.get(10+i*3).setText(_stringDataHP.get(i*5+4)+" m/s,"+_stringDataHP.get(i*5+3));
					ImageView ggg1 = _fieldsI.get(i+1); 
					new DownloadImageTask((ImageView) ggg1).execute(_stringDataHP.get(i*5+1));
					Log.i(this_marker,"URL: "+_stringDataHP.get(i*5+2));
				}
			}
			//**вывод прогнозов
			
		}//protected void onPostExecute(Void result)

	}//class goData extends AsyncTask<Void, Void, Void>

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}//public DownloadImageTask(ImageView bmImage)

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e(this_marker, "error protected Bitmap doInBackground in private class DownloadImageTask");
			}
			return mIcon11;
		}//protected Bitmap doInBackground(String... urls)

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}//protected void onPostExecute(Bitmap result)
	}//private class DownloadImageTask extends AsyncTask<String, Void, Bitmap>

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}//public class ClockForHome extends Activity
