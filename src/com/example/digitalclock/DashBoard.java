package com.example.digitalclock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
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
	goInd mt;
	goUsd mt1;
	goCurrT mt2;
	int sc =0;
	String this_marker = "DashBoard"; //** ������� ��� ������� ��� �����
	
	public static String dyn_brent_usd = ""; //**������������� ������ �������� �����/������
	
	
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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myscreen);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		Typeface face=Typeface.createFromAsset(getAssets(), "Electron.ttf");
		
		Log.i(this_marker," welcome dashboard");
		
		List<Integer> fldId = Arrays.asList(R.id.curT,R.id.minT,R.id.trend,R.id.maxT,
				R.id.brent,R.id.usd,R.id.bch1,R.id.brr1,
				R.id.m1,R.id.weekDay,R.id.day,R.id.mont,R.id.prg,R.id.astr);
		
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
        						Calendar currentTime = Calendar.getInstance();
        						_fields.get(6).setText(((currentTime.get(11)) >=10 ? String.valueOf(currentTime.get(11))  : "0"+String.valueOf(currentTime.get(11))));
        						_fields.get(8).setText(((currentTime.get(12)) >=10 ? String.valueOf(currentTime.get(12))  : "0"+String.valueOf(currentTime.get(12))));
        						
        						String[] xx = gisFromSite.getCurrData();
        						
        						_fields.get(9).setText(xx[7]);
        						_fields.get(10).setText(xx[2]);
        						_fields.get(11).setText(xx[6]);
        	      	      
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
        			Log.e(this_marker," error in t = new Thread()- public void run()");
        		}
        	}//public void run()
       };//Thread t = new Thread()
       t.start();
    }//protected void onCreate(Bundle savedInstanceState)

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
			Log.i(this_marker," take USD");
			dyn_brent_usd = "usd";
			Intent intent = new Intent(this, DynUsd.class);
		    startActivity(intent);
		}
		
		if (v.getId()==R.id.brent) {
			Log.i(this_marker," take R.id.brent");
			dyn_brent_usd = "brent";
			Intent intent = new Intent(this, DynUsd.class);
		    startActivity(intent);
		}
		
		if (v.getId()==R.id.curT) {
			Log.i(this_marker," R.id.curT");
			mt2 = new goCurrT();
			mt2.execute();
		}
		
		if (v.getId()==R.id.prg) {
			Log.i(this_marker," R.id.prg");
			Intent intent = new Intent(this, FromGis.class);
		    startActivity(intent);
		}
		
		if (v.getId()==R.id.bch1 | v.getId()==R.id.m1 | v.getId()==R.id.brr1) {
			Log.i(this_marker," take R.id.bch1 | v.getId()==R.id.m1 | v.getId()==R.id.brr1");
			Intent intent = new Intent(this, ClockForHome.class);
		    startActivity(intent);
		}
		
		if (v.getId()==R.id.mont) {
			Log.i(this_marker," take R.id.mont");
			Intent intent = new Intent(this, MainActivity.class);
		    startActivity(intent);
		}
	}//public void onClick(View v)
	
	/////////////////////////////////////////////////////////////////////////////////////

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
	    		//**��� ������
	    		String[] x = gisFromSite.readMy();
	    		
	    		if (x.length>6) {
	    			
	    			for(int i=0;i<ind.size();i++){
	    				
	    				_stringData.set(i, x[ind.get(i)].indexOf(" ")>0 
	    						? x[ind.get(i)] : "".concat(x[ind.get(i)])) ;
	    				
	    				Log.i(this_marker,x[ind.get(i)].indexOf(" ")>0 
	    						? x[ind.get(i)] : "".concat(x[ind.get(i)]));
	    				
	    			}
	    		}
	    		else {
	    			Log.e(this_marker,"error _stringData in class goInd less 7 ");
	    		}
	    	} catch (IOException e) {
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
	    	
	    	//**������� ������
	    	try {
	    		ArrayList<String> h = gisFromSite.getPrognozV2();
	    		if (h.size()>3) {
	    			_stringData.set(12,String.valueOf(h.get(0)));
	    		}	
	    	}catch (IOException e) {
	    		Log.e(this_marker,"error read gisFromSite.getPrognozV2() in class goInd");
			}
	    	
	    	//**������� ���
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
	    	return null;
	    }//protected Void doInBackground(Void... params)

		/////////////////////////////////////////////////////////////////////////////////////
	    
		@Override
	    protected void onPostExecute(Void result) {
	      super.onPostExecute(result);
	      
	      List<Integer> ind = Arrays.asList(0,1,2,3,4,5,12,13);
	      
	      for(int i=0 ;i<ind.size();i++) {
	    	  _fields.get(ind.get(i)).setText(_stringData.get(ind.get(i)));
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
	    	_stringData.set(5,"USD: "+_stringData.get(5));
	    	_stringData.set(4,"brent: "+_stringData.get(4));
	    }//protected void onPostExecute(Void result)
		
	}//class goUsd extends AsyncTask<Void, Void, Void>
	
	class goCurrT extends AsyncTask<Void, Void, Void> {
		List<String> _stringData = Arrays.asList("","","","","","","","","","","","","","");
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
	    		//**��� ������
	    		String[] x = gisFromSite.readMy();
	    		if (x.length>6) {
	    			for(int i=0;i<ind.size();i++){
	    				_stringData.set(i, x[ind.get(i)].indexOf(" ")>0 
	    						? x[ind.get(i)] : "".concat(x[ind.get(i)])) ;
	    				Log.i(this_marker,x[ind.get(i)].indexOf(" ")>0 
	    						? x[ind.get(i)] : "".concat(x[ind.get(i)]));
	    			}
	    		}
	    	} catch (IOException e) {
	    		Log.e(this_marker,"error read gisFromSite.readMy() in  class goCurrT");
			}
		    return null;
	    }//protected Void doInBackground(Void... params)
		
		/////////////////////////////////////////////////////////////////////////////////////
	    
		@Override
	    protected void onPostExecute(Void result) {
	    	super.onPostExecute(result);
	    	List<Integer> ind = Arrays.asList(0,1,2,3);
		      for(int i=0 ;i<ind.size();i++) {
		    	  _fields.get(ind.get(i)).setText(_stringData.get(ind.get(i)));
		      }
	    }//protected void onPostExecute(Void result)
		
	}//class goCurrT extends AsyncTask<Void, Void, Void>
	
}//public class DashBoard extends Activity implements OnClickListener
