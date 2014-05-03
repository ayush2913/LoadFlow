package com.ayuhar.loadflow;

import java.util.ArrayList;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class GaussSiedel extends Activity{
	
	Button go,next;
	EditText busno;
	TableLayout busdetails;
	ArrayList<ArrayList<String>> bd;
	int n,key;
	String qmax,qmin;
	boolean dialogboxq = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Intent intent = this.getIntent();
		key = intent.getIntExtra("Key", 0);
		setContentView(R.layout.gs);
		go = (Button) findViewById(R.id.gogs);
		//enter = (TextView) findViewById(R.id.tVgs1);
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
				Intent openGS = new Intent(GaussSiedel.this , Gs2.class);
//				Bundle bundle = new Bundle();
//				bundle.putSerializable("Bus Details", bd);
//				openGS.putExtras(bundle);
				for(int i = 0; i<n;i++){
					openGS.putStringArrayListExtra(String.valueOf(i), bd.get(i));
				}
				openGS.putExtra("Qmax", qmax);
				openGS.putExtra("Qmin", qmin);
				openGS.putExtra("Key", key);
				openGS.putExtra("bus", n);
				startActivity(openGS);
			}
		});
	}
	
	
	
	public void setMatrix(){
		bd = new ArrayList<ArrayList<String>>();
		
		
		for(int i=1;i<=n;i++)
		{ 
		
			bd.add(i-1, new ArrayList<String>());
			TableRow R = (TableRow) busdetails.getChildAt(i);
				Spinner bustype =(Spinner) R.getChildAt(1);
				//String bt = bustype.getSelectedItem().toString();
				//Log.d("gg-s",bt);
				bd.get(i-1).add(0, bustype.getSelectedItem().toString());
				Log.d("gg-s",bd.get(i-1).get(0));
				
				
				
				if(bustype.getSelectedItem().toString()=="Slack")
				{
					EditText V = (EditText) R.getChildAt(2);
					EditText theta = (EditText) R.getChildAt(3);
					bd.get(i-1).add(1, "0");
					Log.d("gg-s",bd.get(i-1).get(1));
					bd.get(i-1).add(2, "0");
					Log.d("gg-s",bd.get(i-1).get(2));
					bd.get(i-1).add(3, V.getText().toString());
					Log.d("gg-s",bd.get(i-1).get(3));
					bd.get(i-1).add(4, theta.getText().toString());
					Log.d("gg-s",bd.get(i-1).get(4));
				}
				
				
				if(bustype.getSelectedItem().toString()=="P-V")
				{
					EditText P = (EditText) R.getChildAt(2);
					EditText V = (EditText) R.getChildAt(3);
					bd.get(i-1).add(1, P.getText().toString());
					Log.d("gg-s",bd.get(i-1).get(1));
					bd.get(i-1).add(2, "0");
					Log.d("gg-s",bd.get(i-1).get(2));
					bd.get(i-1).add(3, V.getText().toString());
					Log.d("gg-s",bd.get(i-1).get(3));
					bd.get(i-1).add(4, "0");
					Log.d("gg-s",bd.get(i-1).get(4));
				}
				
				
				if(bustype.getSelectedItem().toString()=="P-Q")
				{
					EditText P = (EditText) R.getChildAt(2);
					EditText Q = (EditText) R.getChildAt(3);
					bd.get(i-1).add(1, P.getText().toString());
					Log.d("gg-s",bd.get(i-1).get(1));
					bd.get(i-1).add(2, Q.getText().toString());
					Log.d("gg-s",bd.get(i-1).get(2));
					bd.get(i-1).add(3, "1");
					Log.d("gg-s",bd.get(i-1).get(3));
					bd.get(i-1).add(4, "0");
					Log.d("gg-s",bd.get(i-1).get(4));
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
			et2.setInputType(3);
			et2.setId(1000+i);
			et3 = new EditText(this);
			et3.setInputType(3);
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
	                	if(dialogboxq==false){
	                		dialogboxq =true;
	                	AlertDialog EnterQmaxQmin = new AlertDialog.Builder(GaussSiedel.this).create();
	                	EnterQmaxQmin.setTitle("Enter Permissible Q");
	                	EnterQmaxQmin.setMessage("Leave Blank if Not Available");
	                	LinearLayout lila1= new LinearLayout(GaussSiedel.this);
	                	lila1.setOrientation(1);
	                	final EditText Qmax = new EditText(GaussSiedel.this);
	                	Qmax.setHint("Enter Maximum Q");
	                	final EditText Qmin = new EditText(GaussSiedel.this);
	                	Qmin.setHint("Enter Minimum Q");
	                	lila1.addView(Qmax);
	                	lila1.addView(Qmin);
	                	EnterQmaxQmin.setView(lila1);
	                	EnterQmaxQmin.setButton("Ok", new DialogInterface.OnClickListener() {
								
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								qmax  = Qmax.getText().toString();
								qmin = Qmin.getText().toString();
							}
						});
	                	EnterQmaxQmin.show();	
	                }
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
			dialogboxq = false;
		}
	}

}