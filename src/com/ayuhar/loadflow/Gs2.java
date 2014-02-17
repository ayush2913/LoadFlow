package com.ayuhar.loadflow;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

public class Gs2 extends Activity{

	Button gof,ok;
	TextView enol;
	EditText lineno;
	TableLayout linedetails;
	int l;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gsnxt);
		gof = (Button) findViewById(R.id.gogs2);
		enol = (TextView) findViewById(R.id.tVgs2);
		lineno = (EditText) findViewById(R.id.nolgs);
		ok = (Button) findViewById(R.id.okgs);
		linedetails = (TableLayout)findViewById(R.id.linedata);
		gof.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				String line = lineno.getText().toString();
				l = Integer.parseInt(line);
				generateLtable();
			}
		});
	}
	public void generateLtable(){
		
	}
}
