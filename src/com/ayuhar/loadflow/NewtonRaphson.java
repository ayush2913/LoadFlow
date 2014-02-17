package com.ayuhar.loadflow;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class NewtonRaphson extends Activity {
	
	Button go;
	TextView enter;
	EditText busno;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nr);
		go = (Button) findViewById(R.id.gonr);
		enter = (TextView) findViewById(R.id.tVnr1);
		busno = (EditText) findViewById(R.id.nobnr);
		
	}

}
