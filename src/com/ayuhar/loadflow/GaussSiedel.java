package com.ayuhar.loadflow;

import java.util.ArrayList;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class GaussSiedel extends Activity{
	
	Button go,next;
	TextView enter;
	EditText busno;
	TableLayout busdetails;
	ArrayList<ArrayList<String>> bd = new ArrayList<ArrayList<String>>();
	int n;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gs);
		go = (Button) findViewById(R.id.gogs);
		enter = (TextView) findViewById(R.id.tVgs1);
		busno = (EditText) findViewById(R.id.nobgs);
		next = (Button) findViewById(R.id.nextgs);
		busdetails = (TableLayout)findViewById(R.id.busdata);
		go.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String bus = busno.getText().toString();
				n = Integer.parseInt(bus);
				
				generateTable(n);
			}
		});
		next.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setMatrix();
				Intent openGS2 = new Intent("com.ayuhar.loadflow.GS2");
				startActivity(openGS2);
			}
		});
	}
	public void setMatrix(){
		for(int i=1;i<=n;i++)
		{ 
		
			bd.add(i-1, new ArrayList<String>());
			TableRow R = (TableRow) busdetails.getChildAt(i);
				Spinner bustype =(Spinner) R.getChildAt(1);
				//String bt = bustype.getSelectedItem().toString();
				//Log.d("gg-s",bt);
				bd.get(i-1).add(0, bustype.getSelectedItem().toString());
			 
				
				if(bustype.getSelectedItem().toString()=="Slack")
				{
					EditText V = (EditText) R.getChildAt(2);
					EditText theta = (EditText) R.getChildAt(3);
					bd.get(i-1).add(1, "0");
					bd.get(i-1).add(2, "0");
					bd.get(i-1).add(3, V.getText().toString());
					bd.get(i-1).add(4, theta.getText().toString());
				}
				if(bustype.getSelectedItem().toString()=="P-V")
				{
					EditText P = (EditText) R.getChildAt(2);
					EditText V = (EditText) R.getChildAt(3);
					bd.get(i-1).add(1, P.getText().toString());
					bd.get(i-1).add(2, "0");
					bd.get(i-1).add(3, V.getText().toString());
					bd.get(i-1).add(4, "0");
				}
				if(bustype.getSelectedItem().toString()=="P-Q")
				{
					EditText P = (EditText) R.getChildAt(2);
					EditText Q = (EditText) R.getChildAt(3);
					bd.get(i-1).add(1, P.getText().toString());
					bd.get(i-1).add(2, Q.getText().toString());
					bd.get(i-1).add(3, "1");
					bd.get(i-1).add(4, "0");
				}
		}
	}
	public void generateTable(int n){
		busdetails.removeAllViews();
		TableRow row1, row;
		TextView t1, t2, t3;
		Spinner spinner;
		row1 = new TableRow(this);
		t1 = new TextView(this);
		t1.setText("Sr. No.");
		t1.setGravity(Gravity.CENTER);
		t2 = new TextView(this);
		t2.setText("Bus Type");
		t2.setGravity(Gravity.CENTER);
		t3 = new TextView(this);
		t3.setText("Bus Details");
		row1.addView(t1);
		row1.addView(t2);
		row1.addView(t3);
		busdetails.addView(row1,new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT) );
		for(int i=1; i<=n; i++){
			final EditText et2,et3;
			row = new TableRow(this);
			t1 = new TextView(this);
			String a = String.valueOf(i); 
			t1.setText(a);
			spinner = new Spinner(this);
			ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this,
			        android.R.layout.simple_spinner_dropdown_item,
			            new String[] { "Slack", "P-V", "P-Q" });
			spinner.setAdapter(spinnerArrayAdapter);
			spinner.setId(i);
			et2 = new EditText(this);
			et2.setId(1000+i);
			et3 = new EditText(this);
			et3.setId(10000+i);
			
			spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
	            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	                // TODO Auto-generated method stub
	                Object item = arg0.getItemAtPosition(arg2);
	                if (item=="Slack") {
	                	et2.setHint("Enter V");
	                	et3.setHint("Enter <");
	                }
	                if (item=="P-V") {
	                	et2.setHint("Enter P");
	                	et3.setHint("Enter V");
	                }
	                if (item=="P-Q") {
	                	et2.setHint("Enter P");
	                	et3.setHint("Enter Q");
	                }

	            }

	            @Override
	            public void onNothingSelected(AdapterView<?> arg0) {
	                // TODO Auto-generated method stub

	            }
	        });
			
			row.addView(t1);
			row.addView(spinner);
			row.addView(et2);
			row.addView(et3);
			busdetails.addView(row , new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT) );
		}
	}
}
