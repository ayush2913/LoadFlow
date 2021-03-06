package com.ayuhar.loadflow;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	Button nr, gs;
	TextView tv1 ;
	int key;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nr = (Button) findViewById(R.id.nr);
        gs = (Button) findViewById(R.id.gs);
        tv1 = (TextView) findViewById(R.id.tv1);
        nr.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				key = 1;
				Intent openNR = new Intent("com.ayuhar.loadflow.GAUSSSIEDEL");
				openNR.putExtra("Key", key);
				startActivity(openNR);
			}
		});
        
        gs.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				key  = 0;
				Intent openGS = new Intent("com.ayuhar.loadflow.GAUSSSIEDEL");
				openGS.putExtra("Key", key);
				startActivity(openGS);
			}
		});
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
