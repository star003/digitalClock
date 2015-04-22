package com.example.digitalclock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

////////////////////////////////////////////////////////////////////////////////////////////
//	описание:
//		не используется

public class MainActivity extends Activity  implements OnClickListener {
	////////////////////////////////////////////////////////////////////////////////
	
	ArrayList<TextView> _fields = new ArrayList<TextView>();
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ArrayList<String> sp = new ArrayList<String>();
        List<Integer> fldId = Arrays.asList(R.id.k1,R.id.k2,R.id.k3,R.id.k4,R.id.k5,R.id.k6,R.id.k7
                ,R.id.k8,R.id.k9,R.id.k10,R.id.k11,R.id.k12,R.id.k13,R.id.k14
                ,R.id.k15,R.id.k16,R.id.k17,R.id.k18,R.id.k19,R.id.k20,R.id.k21
                ,R.id.k22,R.id.k23,R.id.k24,R.id.k25,R.id.k26,R.id.k27,R.id.k28
                ,R.id.k29,R.id.k30,R.id.k31,R.id.k32,R.id.k33,R.id.k34,R.id.k35);
        
        sp = gisFromSite.buildCalendar();
        
        for (int i=0;i<fldId.size();i++){
			TextView fff = (TextView)findViewById(fldId.get(i));
			if (i <= sp.size()-1){
				fff.setText(sp.get(i));
			}	
			_fields.add(fff);
		}
        
       //Typeface face=Typeface.createFromAsset(getAssets(),
       //       "digital-7mi.ttf");

    }//protected void onCreate(Bundle savedInstanceState)

    ////////////////////////////////////////////////////////////////////////////////
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }//public boolean onCreateOptionsMenu(Menu menu)
    
    ////////////////////////////////////////////////////////////////////////////////
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.brent) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }//public boolean onOptionsItemSelected(MenuItem item)

    ////////////////////////////////////////////////////////////////////////////////
	
    @Override
	public void onClick(View v) {
    	
	} //public void onClick(View v)
	
//}


}//public class MainActivity extends Activity  implements OnClickListener