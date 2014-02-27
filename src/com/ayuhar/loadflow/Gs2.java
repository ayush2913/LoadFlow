package com.ayuhar.loadflow;

import java.util.ArrayList;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Gs2 extends Activity{

	Button gof,ok;
	//TextView enol;
	EditText lineno, itergs;
	TableLayout linedetails;
	ArrayList<ArrayList<String>> ld, busdetailsmatrix;
	int l, iter, numberofbus;
	SolveGS gld ;
	Double maxQ, minQ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gsnxt);
		busdetailsmatrix = new ArrayList<ArrayList<String>>();
		Intent intent = this.getIntent();
		numberofbus = intent.getIntExtra("bus", 100);
		maxQ = intent.getDoubleExtra("Qmax", 0);
		minQ = intent.getDoubleExtra("Qmin", 0);
		for(int i = 0;i<numberofbus;i++){
			busdetailsmatrix.add(i, intent.getStringArrayListExtra(String.valueOf(i)));
		}
		gof = (Button) findViewById(R.id.gogs2);
		//enol = (TextView) findViewById(R.id.tVgs2);
		lineno = (EditText) findViewById(R.id.nolgs);
		itergs = (EditText) findViewById(R.id.noigs);
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
		ok.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String iteration = itergs.getText().toString();
				iter = Integer.parseInt(iteration);
				lineImpedanceMatrix(l);
				Log.d("Bus Type", busdetailsmatrix.get(0).get(0));
				gld = new SolveGS();
				gld.getld(busdetailsmatrix, ld ,numberofbus, l, iter);
				gld.evaluate();
			}
		});
	}
	public void lineImpedanceMatrix(int l){
		ld = new ArrayList<ArrayList<String>>();
		for(int i=1;i<=l;i++)
		{ 
		
			ld.add(i-1, new ArrayList<String>());
			TableRow R = (TableRow) linedetails.getChildAt(i);
			Spinner from =(Spinner) R.getChildAt(0);
			Spinner to =(Spinner) R.getChildAt(1);
			EditText rez = (EditText) R.getChildAt(2);
			EditText imz = (EditText) R.getChildAt(3);
			//String bt = bustype.getSelectedItem().toString();
			//Log.d("gg-s",bt);
			ld.get(i-1).add(0, from.getSelectedItem().toString());
			Log.d("gg-s",ld.get(i-1).get(0));
			ld.get(i-1).add(1, to.getSelectedItem().toString());
			Log.d("gg-s",ld.get(i-1).get(1));
			ld.get(i-1).add(2, rez.getText().toString());
			Log.d("gg-s",ld.get(i-1).get(2));
			ld.get(i-1).add(3, imz.getText().toString());
			Log.d("gg-s",ld.get(i-1).get(3));
		}
	}
	public void generateLtable(){
		ArrayList<String> spinner = new ArrayList<String>();
		linedetails.removeAllViews();
		TableRow row1, row;
		TextView t1, t2, t3,t4;
		Spinner spinner1, spinner2;
		row1 = new TableRow(this);
		
		t1 = new TextView(this);
		t1.setText("From");
		t1.setGravity(Gravity.CENTER);
		t2 = new TextView(this);
		t2.setText("To");
		t2.setGravity(Gravity.CENTER);
		t3 = new TextView(this);
		t3.setText("Re(Z)");
		t4 = new TextView(this);
		t4.setText("Im(Z)");
		
		row1.addView(t1);
		row1.addView(t2);
		row1.addView(t3);
		row1.addView(t4);
		linedetails.addView(row1,new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT) );
		
		for(int i=1; i<=numberofbus; i++){
			String q = String.valueOf(i) ;
			spinner.add(q);
		}
		for(int i=1; i<=l; i++){
			final EditText et1,et2;
			row = new TableRow(this);
			
			spinner1 = new Spinner(this);
			ArrayAdapter spinner1ArrayAdapter = new ArrayAdapter(this,
			        android.R.layout.simple_spinner_dropdown_item,
			            spinner);
			spinner1.setAdapter(spinner1ArrayAdapter);
			
			spinner2 = new Spinner(this);
			ArrayAdapter spinner2ArrayAdapter = new ArrayAdapter(this,
			        android.R.layout.simple_spinner_dropdown_item,
			        spinner);
			spinner2.setAdapter(spinner2ArrayAdapter);
			//spinner1.setId(i);
			
			et1 = new EditText(this);
			//et1.setInputType(InputType.TYPE_CLASS_NUMBER);
			//et1.setId(1000+i);
			
			et2 = new EditText(this);
			//et2.setInputType(InputType.TYPE_CLASS_NUMBER);
			//et2.setId(10000+i);
			
//			spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//				@Override
//	            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//	                // TODO Auto-generated method stub
//	                Object item = arg0.getItemAtPosition(arg2);
//	                if (item=="Slack") {
//	                	et2.setHint("Enter V");
//	                	et3.setHint("Enter <");
//	                }
//	                if (item=="P-V") {
//	                	et2.setHint("Enter P");
//	                	et3.setHint("Enter V");
//	                }
//	                if (item=="P-Q") {
//	                	et2.setHint("Enter P");
//	                	et3.setHint("Enter Q");
//	                }
//
//	            }
//
//	            @Override
//	            public void onNothingSelected(AdapterView<?> arg0) {
//	                // TODO Auto-generated method stub
//
//	            }
//	        });
			
			row.addView(spinner1);
			row.addView(spinner2);
			row.addView(et1);
			row.addView(et2);
			linedetails.addView(row , new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT) );
		}
	}
//	public void getbd(ArrayList<ArrayList<String>> a){
//		busdetailsmatrix = new ArrayList<ArrayList<String>>(a);
//		Log.d("Bus Type",a.get(0).get(0));
//		Log.d("Bus Type",busdetailsmatrix.get(0).get(0));
//	}
}
