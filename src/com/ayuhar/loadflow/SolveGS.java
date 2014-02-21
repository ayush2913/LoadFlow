package com.ayuhar.loadflow;

import java.util.ArrayList;
import java.lang.Math.*;

import android.util.Log;

public class SolveGS {
	ArrayList<ArrayList<String>> li;
	ArrayList<ArrayList<String>> busdet;
	int nob, noi, nol;
	double[][]  reY;
	double[][]  imY;
	double[][] thetaY;
	double[][] magY;
	
	public void getld(ArrayList<ArrayList<String>> a, ArrayList<ArrayList<String>> b, int x, int y, int z){
		busdet = new ArrayList<ArrayList<String>>(a);
		li = new ArrayList<ArrayList<String>>();
		li = b;
		nol = y;
		noi = z;
		nob = x;
		
	}
	public void formY(){
		reY = new double[nob][nob];
		imY = new double[nob][nob];
		magY = new double[nob][nob];
		thetaY = new double[nob][nob];
		for(int i =0; i<nol; i++){
			
			reY[Integer.parseInt(li.get(i).get(0))-1][Integer.parseInt(li.get(i).get(1))-1] += Double.parseDouble(li.get(i).get(2))/(Double.parseDouble(li.get(i).get(2))*Double.parseDouble(li.get(i).get(2))+Double.parseDouble(li.get(i).get(3))*Double.parseDouble(li.get(i).get(3)));
			imY[Integer.parseInt(li.get(i).get(0))-1][Integer.parseInt(li.get(i).get(1))-1] += Double.parseDouble(li.get(i).get(3))/(Double.parseDouble(li.get(i).get(2))*Double.parseDouble(li.get(i).get(2))+Double.parseDouble(li.get(i).get(3))*Double.parseDouble(li.get(i).get(3)));
			reY[Integer.parseInt(li.get(i).get(1))-1][Integer.parseInt(li.get(i).get(0))-1] += Double.parseDouble(li.get(i).get(2))/(Double.parseDouble(li.get(i).get(2))*Double.parseDouble(li.get(i).get(2))+Double.parseDouble(li.get(i).get(3))*Double.parseDouble(li.get(i).get(3)));
			imY[Integer.parseInt(li.get(i).get(1))-1][Integer.parseInt(li.get(i).get(0))-1] += Double.parseDouble(li.get(i).get(3))/(Double.parseDouble(li.get(i).get(2))*Double.parseDouble(li.get(i).get(2))+Double.parseDouble(li.get(i).get(3))*Double.parseDouble(li.get(i).get(3)));
			reY[Integer.parseInt(li.get(i).get(0))-1][Integer.parseInt(li.get(i).get(0))-1] += Double.parseDouble(li.get(i).get(2))/(Double.parseDouble(li.get(i).get(2))*Double.parseDouble(li.get(i).get(2))+Double.parseDouble(li.get(i).get(3))*Double.parseDouble(li.get(i).get(3)));
			reY[Integer.parseInt(li.get(i).get(1))-1][Integer.parseInt(li.get(i).get(1))-1] += Double.parseDouble(li.get(i).get(2))/(Double.parseDouble(li.get(i).get(2))*Double.parseDouble(li.get(i).get(2))+Double.parseDouble(li.get(i).get(3))*Double.parseDouble(li.get(i).get(3)));
			imY[Integer.parseInt(li.get(i).get(0))-1][Integer.parseInt(li.get(i).get(0))-1] += Double.parseDouble(li.get(i).get(3))/(Double.parseDouble(li.get(i).get(2))*Double.parseDouble(li.get(i).get(2))+Double.parseDouble(li.get(i).get(3))*Double.parseDouble(li.get(i).get(3)));
			imY[Integer.parseInt(li.get(i).get(1))-1][Integer.parseInt(li.get(i).get(1))-1] += Double.parseDouble(li.get(i).get(3))/(Double.parseDouble(li.get(i).get(2))*Double.parseDouble(li.get(i).get(2))+Double.parseDouble(li.get(i).get(3))*Double.parseDouble(li.get(i).get(3)));
		}
		for(int i =0; i<nob; i++){
			for(int j =0; j<nob; j++){
				if(i==j){
					reY[i][j] = reY[i][j];
					imY[i][j] = -imY[i][j];
				}
				else{
					reY[i][j] = -reY[i][j];
					imY[i][j] = imY[i][j];
				}
				magY[i][j] = Math.sqrt(reY[i][j]*reY[i][j]+imY[i][j]*imY[i][j]);
				thetaY[i][j] = Math.atan2(imY[i][j], reY[i][j]);
			}
		}
	}
	public void evaluate(){
		Log.d("number of Bus",String.valueOf(nob) );
		Log.d("number of line",String.valueOf(nol) );
		Log.d("number of iteration",String.valueOf(noi) );
		//Log.d("Bus Type", busdet.get(0).get(0));
		formY();
		for(int k = 0; k<noi; k++){
			//Log.d("Bus Type", busdet.get(0).get(0));
			for (int i = 0; i<nob; i++){
				Log.d("Bus Type", busdet.get(i).get(0));
				if(busdet.get(i).get(0).equalsIgnoreCase("Slack")){ 
					Log.d("Slack Check", "Slack");
					continue;
				}
				else if(busdet.get(i).get(0).equalsIgnoreCase("P-V")){
					Log.d("P-V Check", "P-V");
					Double multiplier = 0.000;
					for(int p=0; p<nob; p++){
						Double gsin = reY[i][p]*(Math.sin(Double.parseDouble(busdet.get(i).get(4))-Double.parseDouble(busdet.get(p).get(4))));
						Double bcos = imY[i][p]*(Math.cos(Double.parseDouble(busdet.get(i).get(4))-Double.parseDouble(busdet.get(p).get(4))));
						multiplier += (gsin-bcos)*Double.parseDouble(busdet.get(p).get(3));
					}
					Double qi = multiplier*Double.parseDouble(busdet.get(i).get(3));
					Log.d("Qi", qi.toString());
					busdet.get(i).set(2, qi.toString());
					calculateV(i);
				}
				else if(busdet.get(i).get(0).equalsIgnoreCase("P-Q")){
					Log.d("P-Q Check", "P-Q");
					calculateV(i);
				}
			}
		}
	}
	public void calculateV(int i){
		Log.d("Check", "Ok");
		Double magPQ, thetaPQ, thetaterm1,magterm1;
		Double reterm2,imterm2,reterm3,imterm3, reterm1, imterm1;
		reterm1 = imterm1 = reterm2 = imterm2 = reterm3 = imterm3 = 0.000;
		magPQ = Math.sqrt(Double.parseDouble(busdet.get(i).get(1))
				* Double.parseDouble(busdet.get(i).get(1))
				+ Double.parseDouble(busdet.get(i).get(2))
				* Double.parseDouble(busdet.get(i).get(2)));
		thetaPQ = Math.atan2(-Double.parseDouble(busdet.get(i).get(2)), Double.parseDouble(busdet.get(i).get(1)));
		thetaterm1 = thetaPQ + Double.parseDouble(busdet.get(i).get(4));
		magterm1 = magPQ/Double.parseDouble(busdet.get(i).get(3));
		reterm1 = magterm1*Math.cos(thetaterm1);
		imterm1 = magterm1*Math.sin(thetaterm1);
		for(int j=0;j<=i-1 ;j++){
			reterm2 += reY[i][j] * Double.parseDouble(busdet.get(j).get(3))
					* Math.cos(Double.parseDouble(busdet.get(j).get(4)))
					- imY[i][j] * Double.parseDouble(busdet.get(j).get(3))
					* Math.sin(Double.parseDouble(busdet.get(j).get(4)));
			imterm2 += reY[i][j] * Double.parseDouble(busdet.get(j).get(3))
					* Math.sin(Double.parseDouble(busdet.get(j).get(4)))
					+ imY[i][j] * Double.parseDouble(busdet.get(j).get(3))
					* Math.cos(Double.parseDouble(busdet.get(j).get(4)));
		}
		for(int j=i+1; j<nob ; j++){
			reterm3 += reY[i][j] * Double.parseDouble(busdet.get(j).get(3))
					* Math.cos(Double.parseDouble(busdet.get(j).get(4)))
					- imY[i][j] * Double.parseDouble(busdet.get(j).get(3))
					* Math.sin(Double.parseDouble(busdet.get(j).get(4)));
			imterm3 += reY[i][j] * Double.parseDouble(busdet.get(j).get(3))
					* Math.sin(Double.parseDouble(busdet.get(j).get(4)))
					+ imY[i][j] * Double.parseDouble(busdet.get(j).get(3))
					* Math.cos(Double.parseDouble(busdet.get(j).get(4)));
		}
		Double magV = Math.sqrt((reterm1 - reterm2 - reterm3)
				* (reterm1 - reterm2 - reterm3) + (imterm1 - imterm2 - imterm3)
				* (imterm1 - imterm2 - imterm3))
				/ magY[i][i];
		Log.d("Real", String.valueOf(reterm1 - reterm2 - reterm3));
		Log.d("Imaginary", String.valueOf(imterm1 - imterm2 - imterm3));
		Log.d("Real Yii", String.valueOf(reY[i][i]));
		Log.d("Im Yii", String.valueOf(imY[i][i]));
		Double thetaV = Math.atan2((imterm1-imterm2-imterm3),(reterm1-reterm2-reterm3))-thetaY[i][i];
		busdet.get(i).set(3,magV.toString());
		busdet.get(i).set(4,thetaV.toString());
		Log.d("Answer V", busdet.get(i).get(3));
		Log.d("Answer theta", busdet.get(i).get(4));
	}
}