package com.example.digitalclock;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TextView;

public class DashBoard extends Activity implements OnClickListener {
	
	ArrayList<TextView> _fields = new ArrayList<TextView>();
	ArrayList<TextView> _fieldsMg = new ArrayList<TextView>();
	
	List<Integer> _color = Arrays.asList(R.color.gm1,R.color.gm2,R.color.gm3,R.color.gm4,
			R.color.gm5,R.color.gm6,R.color.gm7,R.color.gm8);
	goInd mt;
	goUsd mt1;
	goCurrT mt2;
	int sc 				= 0;
	String this_marker 	= "DashBoard"; //** зададим имя маркера для логов
	public static String dyn_brent_usd = ""; //**переключатель показа динамики нефть/доллар
	
	boolean this_small 	= true;
	private static final String APP_PREFERENCES = "digitalClock";
    private SharedPreferences mSettings;
	
	/////////////////////////////////////////////////////////////////////////////////////
	/*
	_fields.add((TextView)findViewById(R.id.curT));//0
	_fields.add((TextView)findViewById(R.id.minT));//1
	_fields.add((TextView)findViewById(R.id.trend));//2
	_fields.add((TextView)findViewById(R.id.maxT));//3
	_fields.add((TextView)findViewById(R.id.brent));//4
	_fields.add((TextView)findViewById(R.id.usd));//5
	_fields.add((TextView)findViewById(R.id.h1));//6
	_fields.add((TextView)findViewById(R.id.rr1));//7
	_fields.add((TextView)findViewById(R.id.m1));//8
	_fields.add((TextView)findViewById(R.id.weekDay));//9
	_fields.add((TextView)findViewById(R.id.day));//10
	_fields.add((TextView)findViewById(R.id.mont));//11
	_fields.add((TextView)findViewById(R.id.prg));//12
	_fields.add((TextView)findViewById(R.id.astr));//13
	*/
	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
		
		if(mSettings.contains("APP_PREFERENCES_SMALL_SCREEN_SIZE")) {
			
			this_small = mSettings.getBoolean("APP_PREFERENCES_SMALL_SCREEN_SIZE",false );
			
		}
		
		if (this_small) {
			
			setContentView(R.layout.new_small_myscreen);
			
		}
		else {
			
			setContentView(R.layout.myscreen);
			
		}
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		Typeface face=Typeface.createFromAsset(getAssets(), "Electron.ttf");
		
		Log.i(this_marker,"----welcome dashboard----");
		
		List<Integer> fldId = Arrays.asList(R.id.curT	,R.id.minT		,R.id.trend	,R.id.maxT,
											R.id.brent	,R.id.usd		,R.id.bch1	,R.id.brr1,
											R.id.m1		,R.id.weekDay	,R.id.day	,R.id.mont,
											R.id.prg	,R.id.astr , 
											R.id.curT1	,R.id.minT1		,R.id.trend1,R.id.maxT1,
											R.id.curT2	,R.id.minT2		,R.id.trend2,R.id.maxT2);
		
		for (int i=0;i<fldId.size();i++){
			
			TextView fff = (TextView)findViewById(fldId.get(i));
			fff.setTypeface(face);
			_fields.add(fff);
			
		}
		
		if (this_small!= true) { 
			
			List<Integer> fldMg = Arrays.asList(R.id.mg11,R.id.mg12,R.id.mg13,R.id.mg14,R.id.mg15,R.id.mg16,R.id.mg17,R.id.mg18,
											R.id.mg21,R.id.mg22,R.id.mg23,R.id.mg24,R.id.mg25,R.id.mg26,R.id.mg27,R.id.mg28,
											R.id.mg31,R.id.mg32,R.id.mg33,R.id.mg34,R.id.mg35,R.id.mg36,R.id.mg37,R.id.mg38);
		
			for (int i = 0; i < fldMg.size(); i++) {
				
				TextView fff = (TextView)findViewById(fldMg.get(i));
				fff.setText("-");
				fff.setGravity(17);
				fff.setTextColor(R.color.black);
				_fieldsMg.add(fff);
				
			} 
			
		}
		
		mt = new goInd();
       	mt.execute();
       	
        Thread t = new Thread() {
        	@Override
        	public void run() {
        		try {
        			//**при первом пуске , заполним поля дата - время
        			refreshTime();
					
        			while (!isInterrupted()) {
        				Thread.sleep(60000); //**обновимся раз в 60 сек
        				runOnUiThread(new Runnable() {
        					@Override
        					public void run() {
        						
        						sc ++;
        						refreshTime();
        	      	      
        						if (sc>5) {
        							
        							//**раз в 5 минут обновим показания
        							sc = 0;
        							mt = new goInd();
        							mt.execute();
        							
        						}
        						
        					}//public void run()
        					
        				});//runOnUiThread(new Runnable()
        				
        			}
        			
        		}
        		
        		catch (InterruptedException e) {
        			
        			Log.e(this_marker,"error in t = new Thread()- public void run()");
        			
        		}
        		
        	}//public void run()
        	
       };//Thread t = new Thread()
       
       t.start();
       
    }//protected void onCreate(Bundle savedInstanceState)

	/////////////////////////////////////////////////////////////////////////////////////
	
	void refreshTime() {
		
		Calendar currentTime = Calendar.getInstance();
		_fields.get(6).setText(((currentTime.get(11)) >=10 ? String.valueOf(currentTime.get(11))  : "0"+String.valueOf(currentTime.get(11))));
		_fields.get(8).setText(((currentTime.get(12)) >=10 ? String.valueOf(currentTime.get(12))  : "0"+String.valueOf(currentTime.get(12))));
		
		String[] xx = gisFromSite.getCurrData();
		
		_fields.get(9).setText(xx[7]);
		_fields.get(10).setText(xx[2]);
		_fields.get(11).setText(xx[6]);
		
	}//void refreshTime()

	/////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.dash_board, menu);
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

	/////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void onClick(View v) {
		
		if (v.getId()==R.id.usd) {
			
			Log.i(this_marker,"take USD");
			dyn_brent_usd = "usd";
			Intent intent = new Intent(this, DynUsd.class);
		    startActivity(intent);
		    
		}
		
		if (v.getId()==R.id.brent) {
			
			Log.i(this_marker,"take R.id.brent");
			dyn_brent_usd = "brent";
			Intent intent = new Intent(this, DynUsd.class);
		    startActivity(intent);
		    
		}
		
		if (v.getId()==R.id.curT) {
			
			Log.i(this_marker,"R.id.curT");
			mt2 = new goCurrT();
			mt2.execute();
			
		}
		
		if (v.getId()==R.id.prg) {
			
			Log.i(this_marker,"R.id.prg");
			Intent intent = new Intent(this, FromGis.class);
		    startActivity(intent);
		    
		}
		
		if (v.getId()==R.id.bch1 | v.getId()==R.id.m1 | v.getId()==R.id.brr1) {
			
			Log.i(this_marker,"take R.id.bch1 | v.getId()==R.id.m1 | v.getId()==R.id.brr1");
			Intent intent = new Intent(this, ClockForHome.class);
		    startActivity(intent);
		    
		}
		
		if (v.getId()==R.id.mont) {
			
			Log.i(this_marker,"take R.id.mont");
			Intent intent = new Intent(this, MainActivity.class);
		    startActivity(intent);
		    
		}
		
	}//public void onClick(View v)
	
	/////////////////////////////////////////////////////////////////////////////////////

	class goInd extends AsyncTask<Void, Void, Void> {
		
		List<String> _stringData 	= Arrays.asList("","","","","","","","","","","","","","");
		List<String> _stringData1 	= Arrays.asList("","","","","","","","","","","","","","");
		List<String> _stringData2 	= Arrays.asList("","","","","","","","","","","","","","");
		List<String> _stringDataMG 	= Arrays.asList("","","","","","","","",
													"","","","","","","","",
													"","","","","","","","");
		
		/////////////////////////////////////////////////////////////////////////////////////
		
		@Override
	    protected void onPreExecute() {
			
	      super.onPreExecute();
	      
	    }//protected void onPreExecute()

		/////////////////////////////////////////////////////////////////////////////////////
	    
		@Override
	    protected Void doInBackground(Void... params) {
			
			List<Integer> ind = Arrays.asList(0,2,6,4);
			//**нужно предусмотреть сохранение прошлых показаний на случай , если не прочитает новые
			
	    	try {
	    		
	    		//**мой датчик
	    		String[] x = gisFromSite.readMy();
	    		
	    		if (x.length>6) {
	    			
	    			if (x[6].contains("u") | x[6].contains("d") | x[6].contains("u") ) {
	    				//**все ок , заполним графы
	    				Log.i(this_marker,"data in my weather...ок " + x[6]);
	    				
	    				for(int i=0;i<ind.size();i++){
		    				
		    				_stringData.set(i, x[ind.get(i)].indexOf(" ")>0 
		    						? x[ind.get(i)] : "".concat(x[ind.get(i)])) ;
		    				
		    				Log.i(this_marker,x[ind.get(i)].indexOf(" ")>0 
		    						? x[ind.get(i)] : "".concat(x[ind.get(i)]));
		    				
		    			}
	    				
	    			}
	    			
	    			else {
	    				//**вернет старые данные
	    				Log.e(this_marker,"data in my weather...fail " + x[6]);
	    				
	    				for (int i = 0 ;i<4;i++){
	    					
	    					_stringData.set(i, _fields.get(i).getText().toString()); 
	    					
	    				}
	    				
	    			}//if (x[4].equals("n") | x[4].equals("d") | x[4].equals("u"))
	    			
	    			
	    			String[] x1 = gisFromSite.readThingSpeak("180093", "1", "field1", "1440");
	    			Log.i(this_marker,"data in readThingSpeak art " + x1[6]);
	    			
		    		if (x1.length>6) {
		    			
		    			if (x1[6].contains("u") | x1[6].contains("d") | x1[6].contains("u") ) {
		    				//**все ок , заполним графы
		    				Log.i(this_marker,"data in my weather...ок " + x1[6]);
		    				
		    				for(int i=0;i<ind.size();i++){
			    				
			    				_stringData1.set(i, x1[ind.get(i)].indexOf(" ")>0 
			    						? x1[ind.get(i)] : "".concat(x1[ind.get(i)])) ;
			    				
			    				Log.i(this_marker,x1[ind.get(i)].indexOf(" ")>0 
			    						? x1[ind.get(i)] : "".concat(x1[ind.get(i)]));
			    				
			    			}
		    				
		    			}
		    			
		    			else {
		    				
		    				//**вернет старые данные
		    				Log.e(this_marker,"data in my weather...fail " + x[6]);
		    				
		    				for (int i = 0 ;i<4;i++){
		    					
		    					_stringData1.set(i, _fields.get(i).getText().toString()); 
		    					
		    				}
		    				
		    			}//if (x[4].equals("n") | x[4].equals("d") | x[4].equals("u"))
	    			
	    		}//if (x.length>6)
		    		
		    		String[] x2 = gisFromSite.readThingSpeak("180657", "2", "field2", "80");
	    			Log.i(this_marker,"data in readThingSpeak art " + x2[6]);
	    			
		    		if (x2.length>6) {
		    			
		    			if (x2[6].contains("u") | x2[6].contains("d") | x2[6].contains("u") ) {
		    				//**все ок , заполним графы
		    				Log.i(this_marker,"data in my weather...ок " + x2[6]);
		    				
		    				for(int i=0;i<ind.size();i++){
			    				
			    				_stringData2.set(i, x2[ind.get(i)].indexOf(" ")>0 
			    						? x2[ind.get(i)] : "".concat(x2[ind.get(i)])) ;
			    				
			    				Log.i(this_marker,x2[ind.get(i)].indexOf(" ")>0 
			    						? x2[ind.get(i)] : "".concat(x2[ind.get(i)]));
			    				
			    			}
		    				
		    			}
		    			
		    			else {
		    				
		    				//**вернет старые данные
		    				Log.e(this_marker,"data in my weather...fail " + x[6]);
		    				
		    				for (int i = 0 ;i<4;i++){
		    					
		    					_stringData2.set(i, _fields.get(i).getText().toString()); 
		    					
		    				}
		    				
		    			}//if (x[4].equals("n") | x[4].equals("d") | x[4].equals("u"))
	    			
	    		}//if (x.length>6)
		    		
	    		else {
	    			
	    			Log.e(this_marker,"error _stringData in class goInd less 7 ");
	    			
	    		}
	    		
	    	}
	    		
	    	} catch (IOException e) {
	    		
	    		Log.e(this_marker,"error read gisFromSite.readMy() in class goInd");
	    		
			} catch (ParseException e) {
				
				Log.e(this_marker,"error read gisFromSite.readMy() in class goInd");
				
			}
	    	
	    	try {
	    		
				_stringData.set(4,"Brent: ".concat(priceBRENT.investing()));
				
			} catch (IOException e1) {
				
				Log.e(this_marker,"error priceBRENT.investing() in class goInd");
				
			}
	    	
	    	try {
	    		
	    		_stringData.set(5, "USD: ".concat(priceBRENT.usd()));
	    		
			} catch (IOException e) {
				
				Log.e(this_marker,"error read priceBRENT.usd() in class goInd");
				
			}
	    	
	    	//**прогноз погоды (не нужен , убрал)
	    	_stringData.set(12,"<прогноз на неделю>");
	    	/*
	    	try {
	    		ArrayList<String> h = gisFromSite.getPrognozV2();
	    		if (h.size()>3) {
	    			_stringData.set(12,String.valueOf(h.get(0)));
	    		}	
	    	}catch (IOException e) {
	    		Log.e(this_marker,"error read gisFromSite.getPrognozV2() in class goInd");
			}
			*/
	    	
	    	//**Долгота дня (не нужен , убрал)
	    	_stringData.set(13,"");
	    	/*
	    	String[] ast;
			try {
				ast = gisFromSite.getAstronomy();
				
				if (ast.length>4) {
					
					_stringData.set(13,ast[0] +":"+ast[1]+" "
						+ast[2] +":"+ast[3]+" "
						+ast[4] +":"+ast[5]);
					
				}
			} catch (IOException e) {
				Log.e(this_marker,"error read gisFromSite.getAstronomy() in class goInd");
			}
			*/
	    	
	    	//**геомагнитная обстановка
	    	if (this_small!= true) {
	    		try {
	    			
	    			ArrayList<String> x= gisFromSite.getMagnetic();
	    			
	    			if (x.size()>23) { 
	    				
	    				for(int img = 0;img < x.size();img++) {
	    					
	    					_stringDataMG.set(img, x.get(img));
	    					
	    				}
	    				
	    				Log.i(this_marker,"data magnetic ... ok");
	    				
	    			}	
	    			else {
	    				
	    				Log.e(this_marker,"error read gisFromSite.getMagnetic()  x.size()<23 in task goInd");
	    				
	    			}
				
	    		} catch (IOException e) {
	    			
	    			Log.e(this_marker,"error read gisFromSite.getMagnetic() in class goInd");
	    			
	    		}
	    	}	
	    	
	    	return null;
	    }//protected Void doInBackground(Void... params)

		/////////////////////////////////////////////////////////////////////////////////////
	    
		@SuppressLint("ResourceAsColor")
		@Override
	    protected void onPostExecute(Void result) {
	      super.onPostExecute(result);
	      
	      List<Integer> ind = Arrays.asList(0,1,2,3,4,5,12,13);
	      
	      try {
	    	  
	    	 for(int i=0 ;i<ind.size();i++) {
	    		 
	    		 _fields.get(ind.get(i)).setText(_stringData.get(ind.get(i)));
	    		 
	    	 }
	    	 
	    	 for(int i=0 ;i<4;i++) {
	    		 
	    		 _fields.get(ind.get(i)+14).setText(_stringData1.get(ind.get(i)));
	    		 _fields.get(ind.get(i)+18).setText(_stringData2.get(ind.get(i)));
	    		 
	    		 
	    	 }
	    	 
	      
	    	 if (this_small!= true) {
	    		 
	    		  for(int i = 0 ; i<_stringDataMG.size();i++){
	    			  
	    			  _fieldsMg.get(i).setText(_stringDataMG.get(i));
	    			  _fieldsMg.get(i).setBackgroundResource(_color.get(Integer.valueOf(_stringDataMG.get(i))-1));
	    			  
	    		  }
	    		  
	    	  }
	    	  
	      }
	      catch (NumberFormatException e3){
	    	  
	    	  Log.e(this_marker,"ошибка вывода в форму, представление строка - число (class goInd) ");
	    	  
	      }
	      
    }//protected void onPostExecute(Void result)
		
	    
	}//class goInd extends AsyncTask<Void, Void, Void>

	class goUsd extends AsyncTask<Void, Void, Void> {
		
		List<String> _stringData = Arrays.asList("","","","","","","","","","","","","","");
		
		@Override
		protected void onPreExecute() {
			
		      super.onPreExecute();
		      
		}//protected void onPreExecute()

		/////////////////////////////////////////////////////////////////////////////////////
		
		@Override
		protected Void doInBackground(Void... params) {
			try {
				
				_stringData.set(4,  priceBRENT.investing());
				
			} catch (IOException e1) {
				
				Log.e(this_marker,"error read priceBRENT.investing() in  class goUsd");
				
			}
	    	try {
	    		
	    		_stringData.set(5, priceBRENT.usd());
	    		
			} catch (IOException e) {
				
				Log.e(this_marker,"error read priceBRENT.usd() in  class goUsd");
				
			}
		    return null;
	    }//protected Void doInBackground(Void... params)
		
		/////////////////////////////////////////////////////////////////////////////////////
	    
		@Override
	    protected void onPostExecute(Void result) {
			
	    	super.onPostExecute(result);
	    	
	    	try {
	    		
	    		_stringData.set(5,"USD: "	+_stringData.get(5));
	    		_stringData.set(4,"brent: "	+_stringData.get(4));
	    		
	    	}
	    	
	    	catch (NumberFormatException e3){
	    		
		    	  Log.e(this_marker,"ошибка вывода в форму, представление строка - число (class goUsd) ");
		    	  
		    }
	    }//protected void onPostExecute(Void result)
		
	}//class goUsd extends AsyncTask<Void, Void, Void>
	
	class goCurrT extends AsyncTask<Void, Void, Void> {
		
		List<String> _stringData = Arrays.asList("","","","","","","","","","","","","","");
		List<String> _stringData1 = Arrays.asList("","","","","","","","","","","","","","");
		List<String> _stringData2 = Arrays.asList("","","","","","","","","","","","","","");
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
	    			
	    			Log.i(this_marker," data x[6] : "+x[6]+" ok ");
	    			
	    			if (x[6].contains("n") | x[6].contains("d") | x[6].contains("u") ) {
	    				
	    				for(int i=0;i<ind.size();i++){
	    				
	    					_stringData.set(i, x[ind.get(i)].indexOf(" ")>0 
	    						? x[ind.get(i)] : "".concat(x[ind.get(i)])) ;
	    					
	    					Log.i(this_marker,x[ind.get(i)].indexOf(" ")>0 
	    						? x[ind.get(i)] : "".concat(x[ind.get(i)]));
	    				}
	    				
	    			}
	    			else {
	    				//**вернет старые данные
	    				Log.i(this_marker,"data in my weather...fail " + x[6]);
	    				
	    				for (int i = 0 ;i<4;i++){
	    					
	    					_stringData.set(i, _fields.get(i).getText().toString()); 
	    					
	    				}
	    				
	    			}//if (x[4].equals("n") | x[4].equals("d") | x[4].equals("u"))
	    			
	    			
	    			String[] x1 = gisFromSite.readThingSpeak("180093", "1", "field1", "1440");
		    		
		    		if (x1.length>6) {
		    			
		    			Log.i(this_marker," data x[6] : "+x1[6]+" ok ");
		    			
		    			if (x1[6].contains("n") | x1[6].contains("d") | x1[6].contains("u") ) {
		    				
		    				for(int i=0;i<ind.size();i++){
		    				
		    					_stringData1.set(i, x1[ind.get(i)].indexOf(" ")>0 
		    						? x[ind.get(i)] : "".concat(x1[ind.get(i)])) ;
		    					
		    					Log.i(this_marker,x1[ind.get(i)].indexOf(" ")>0 
		    						? x1[ind.get(i)] : "".concat(x1[ind.get(i)]));
		    				}//for
		    				
		    			}//if
		    			
		    			else {
		    				//**вернет старые данные
		    				Log.i(this_marker,"data in my weather...fail " + x[6]);
		    				
		    				for (int i = 0 ;i<4;i++){
		    					
		    					_stringData1.set(i, _fields.get(i).getText().toString()); 
		    					
		    				}//for
		    			}
		    			
		    		}//if (x[4].equals("n") | x[4].equals("d") | x[4].equals("u"))
	    			
		    		
	    			
		    	}
		    		
	    		String[] x2 = gisFromSite.readThingSpeak("180657", "2", "field2", "80");
    			Log.i(this_marker,"data in readThingSpeak art " + x2[6]);
    			
	    		if (x2.length>6) {
	    			
	    			if (x2[6].contains("u") | x2[6].contains("d") | x2[6].contains("u") ) {
	    				//**все ок , заполним графы
	    				Log.i(this_marker,"data in my weather...ок " + x2[6]);
	    				
	    				for(int i=0;i<ind.size();i++){
		    				
		    				_stringData2.set(i, x2[ind.get(i)].indexOf(" ")>0 
		    						? x2[ind.get(i)] : "".concat(x2[ind.get(i)])) ;
		    				
		    				Log.i(this_marker,x2[ind.get(i)].indexOf(" ")>0 
		    						? x2[ind.get(i)] : "".concat(x2[ind.get(i)]));
		    				
		    			}
	    				
	    			}
	    			
	    			else {
	    				
	    				//**вернет старые данные
	    				Log.e(this_marker,"data in my weather...fail " + x[6]);
	    				
	    				for (int i = 0 ;i<4;i++){
	    					
	    					_stringData2.set(i, _fields.get(i).getText().toString()); 
	    					
	    				}
	    				
	    			}//if (x[4].equals("n") | x[4].equals("d") | x[4].equals("u"))
    			
    		}//if (x.length>6)
	    		
	    	} catch (IOException e) {
	    		
	    		Log.e(this_marker,"error read gisFromSite.readMy() in  class goCurrT");
	    		
			} catch (ParseException e) {
				
				Log.e(this_marker,"error read gisFromSite.readMy() in  class goCurrT");
				
			}
			
		    return null;
		    
	    }//protected Void doInBackground(Void... params)
		
		/////////////////////////////////////////////////////////////////////////////////////
	    
		@Override
	    protected void onPostExecute(Void result) {
			
	    	super.onPostExecute(result);
	    	
	    	try {
	    		
	    		List<Integer> ind = Arrays.asList(0,1,2,3);
	    		
	    		for(int i=0 ;i<ind.size();i++) {
	    			
	    			_fields.get(ind.get(i)).setText(_stringData.get(ind.get(i)));
	    			_fields.get(ind.get(i)+12).setText(_stringData1.get(ind.get(i)));
	    			_fields.get(ind.get(i)+16).setText(_stringData2.get(ind.get(i)));
	    			
	    			Log.i(this_marker,"_fields.get(ind.get(i+14)).setText(_stringData.get(ind.get(i)))"+_stringData.get(ind.get(i)));
	    			
	    		}
	    		
	    	}
	    	
	    	catch (NumberFormatException e3){
	    		
	    		Log.e(this_marker,"ошибка вывода в форму, представление строка - число (class goCurrT) ");
	    		
		    }
	    	
	    }//protected void onPostExecute(Void result)
		
	}//class goCurrT extends AsyncTask<Void, Void, Void>
	
}//public class DashBoard extends Activity implements OnClickListener
