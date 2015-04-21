package com.example.digitalclock;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class FromGis extends Activity {
	ArrayList<TextView> _fields 	= new ArrayList<TextView>();
	ArrayList<ImageView> _fieldsI 	= new ArrayList<ImageView>();
	int sc =0;
	goGis mt;
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_from_gis);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		/*
		 * 0-3 заголовки
		 * 4-16  -облака
		 * 17-29 -осадки
		 * 30-42 - температура
		 * 43-55 -давление
		 * 56-68 -влажность
		 * 69-81 - ветер
		 * 82-94 - ощущения 
		 */
		List<Integer> fldId = Arrays.asList(
				R.id.gsDataCurr,R.id.gsDataDay1,R.id.gsDataDay2,R.id.gsDataDay3,
				
				R.id.gsCl1,R.id.gsCl2,R.id.gsCl3,R.id.gsCl4
				,R.id.gsCl5,R.id.gsCl6,R.id.gsCl7,R.id.gsCl8
				,R.id.gsCl9,R.id.gsCl10,R.id.gsCl11,R.id.gsCl12,R.id.gsCl13
				
				,R.id.gsPer1,R.id.gsPer2,R.id.gsPer3,R.id.gsPer4,
				R.id.gsPer5,R.id.gsPer6,R.id.gsPer7,R.id.gsPer8,
				R.id.gsPer9,R.id.gsPer10,R.id.gsPer11,R.id.gsPer12,R.id.gsPer13
				
				,R.id.gsTmp1,R.id.gsTmp2,R.id.gsTmp3,R.id.gsTmp4
				,R.id.gsTmp5,R.id.gsTmp6,R.id.gsTmp7,R.id.gsTmp8
				,R.id.gsTmp9,R.id.gsTmp10,R.id.gsTmp11,R.id.gsTmp12,R.id.gsTmp13
				
				,R.id.gsPrs1,R.id.gsPrs2,R.id.gsPrs3,R.id.gsPrs4
				,R.id.gsPrs5,R.id.gsPrs6,R.id.gsPrs7,R.id.gsPrs8
				,R.id.gsPrs9,R.id.gsPrs10,R.id.gsPrs11,R.id.gsPrs12,R.id.gsPrs13
				
				,R.id.gsWic1,R.id.gsWic2,R.id.gsWic3,R.id.gsWic4
				,R.id.gsWic5,R.id.gsWic6,R.id.gsWic7,R.id.gsWic8
				,R.id.gsWic9,R.id.gsWic10,R.id.gsWic11,R.id.gsWic12,R.id.gsWic13
				
				,R.id.gsWin1,R.id.gsWin2,R.id.gsWin3,R.id.gsWin4
				,R.id.gsWin5,R.id.gsWin6,R.id.gsWin7,R.id.gsWin8
				,R.id.gsWin9,R.id.gsWin10,R.id.gsWin11,R.id.gsWin12,R.id.gsWin13
				
				,R.id.gsRto1,R.id.gsRto2,R.id.gsRto3,R.id.gsRto4
				,R.id.gsRto5,R.id.gsRto6,R.id.gsRto7,R.id.gsRto8
				,R.id.gsRto9,R.id.gsRto10,R.id.gsRto11,R.id.gsRto12,R.id.gsRto13
				
				);
		/*
		 * разберем на два вида полей
		 * текстовое и графическое
		 * с 0 по 3 и 30 до конца - это текст
		 * с 4 по 29 - картинки
		 */
		
		for (int i=0;i<fldId.size();i++){
			if(i<3 & i>29) {
				TextView fff = (TextView)findViewById(fldId.get(i));
				fff.setText("000");
				_fields.add(fff);
				//_fieldsI.add(null);
				
			}
			
			else {
				_fields.add(null);
				//ImageView ggg = (ImageView)findViewById(fldId.get(i));
				//_fieldsI.add(ggg);
			}
			
			
		}
		
		
		//mt = new goGis();
		//mt.execute();
		
	}//protected void onCreate(Bundle savedInstanceState)
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.from_gis, menu);
		return true;
	}//public boolean onCreateOptionsMenu(Menu menu)

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}//public boolean onOptionsItemSelected(MenuItem item)

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	class goGis extends AsyncTask<Void, Void, Void> {
		ArrayList<String> _stringData = new ArrayList<String>();

		@Override
	    protected void onPreExecute() {
	      super.onPreExecute();
	    }//protected void onPreExecute()

		/////////////////////////////////////////////////////////////////////////////////////
	    
		@Override
	    protected Void doInBackground(Void... params) {
			ArrayList<ArrayList<String>> x = new ArrayList<ArrayList<String>>();
			try {
				x = gisFromSite.grabGismeteo();
			} catch (IOException e) {
				Log.i("m","error gisFromSite.grabGismeteo()");
				_stringData.add("err");
			}
			for(ArrayList<String> a:x){
				for(String h:a){
					_stringData.add(h);
				}
			}
	    	return null;
	    }//protected Void doInBackground(Void... params)

		/////////////////////////////////////////////////////////////////////////////////////
	    
		@Override
	    protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			/*
			for (int i = 0;i<_stringData.size();i++){
				if(i<3 & i>29) {
					//**вывод текстовой информации
					//_fields.get(i).setText(_stringData.get(i));
					_fields.get(i).setText("000");
				}
				
				else {
					//**вывод  графической информации
					ImageView ggg = _fieldsI.get(i); 
					//	new DownloadImageTask((ImageView) findViewById(R.id.imageview)).execute(ImageUrl);
					new DownloadImageTask((ImageView) ggg).execute(_stringData.get(i));
				}
				
			}
		*/
			//_fields.get(0).setText("000");	
		}//protected void onPostExecute(Void result)
		
	}//class goGis extends AsyncTask<Void, Void, Void>
	
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
	              Log.e("Error", e.getMessage());
	              e.printStackTrace();
	          }
	          return mIcon11;
	      }//protected Bitmap doInBackground(String... urls)

	      protected void onPostExecute(Bitmap result) {
	      	bmImage.setImageBitmap(result);
	      }//protected void onPostExecute(Bitmap result)
	  }//private class DownloadImageTask extends AsyncTask<String, Void, Bitmap>

	
}//public class FromGis extends Activity
