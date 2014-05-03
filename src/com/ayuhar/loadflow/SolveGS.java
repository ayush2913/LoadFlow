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
	String maximumQ,minimumQ,tolerance;
	double maxQ,minQ, maxDeltaV;
	
	public void getld(ArrayList<ArrayList<String>> a, ArrayList<ArrayList<String>> b, int x, int y, int z, String c, String d, String e){
		busdet = new ArrayList<ArrayList<String>>(a);
		li = new ArrayList<ArrayList<String>>();
		li = b;
		nol = y;
		noi = z;
		nob = x;
		maximumQ = c;
		minimumQ = d;
		tolerance = e;
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
			maxDeltaV = 0.000;
			//Log.d("Bus Type", busdet.get(0).get(0));
			for (int i = 0; i<nob; i++){
				Log.d("Bus Type", busdet.get(i).get(0));
				if(busdet.get(i).get(0).equalsIgnoreCase("Slack")){ 
					Log.d("Slack Check", "Slack");
					continue;
				}
				else if(busdet.get(i).get(0).equalsIgnoreCase("P-V")){
					calculateQi(i);
					calculateV(i);
				}
				else if(busdet.get(i).get(0).equalsIgnoreCase("P-Q")){
					Log.d("P-Q Check", "P-Q");
					calculateV(i);
				}
			}
			if(tolerance.equalsIgnoreCase("")==false){
				if(maxDeltaV < Double.parseDouble(tolerance)) break;
				else continue;
			}
		}
	}
	public void calculateV(int i){
		Log.d("Check", String.valueOf(i));
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
		if(maxDeltaV < Math.abs(magV-Double.parseDouble(busdet.get(i).get(3)))){
			maxDeltaV = Math.abs(magV-Double.parseDouble(busdet.get(i).get(3)));
		}
		busdet.get(i).set(3,magV.toString());
		busdet.get(i).set(4,thetaV.toString());
		Log.d("Answer V", busdet.get(i).get(3));
		Log.d("Answer theta", busdet.get(i).get(4));
	}
	public void calculateQi(int i){
		double multiplier = 0.000;
		for(int p=0; p<nob; p++){
			double gsin = reY[i][p]*(Math.sin(Double.parseDouble(busdet.get(i).get(4))-Double.parseDouble(busdet.get(p).get(4))));
			double bcos = imY[i][p]*(Math.cos(Double.parseDouble(busdet.get(i).get(4))-Double.parseDouble(busdet.get(p).get(4))));
			multiplier += (gsin-bcos)*Double.parseDouble(busdet.get(p).get(3));
		}
		double qi = multiplier*Double.parseDouble(busdet.get(i).get(3));
		Log.d("Qi", String.valueOf(qi));
		if(maximumQ.equalsIgnoreCase("")==false & minimumQ.equalsIgnoreCase("")==false){
			maxQ = Double.parseDouble(maximumQ);
			minQ = Double.parseDouble(minimumQ);
			if(qi< maxQ & qi>minQ) busdet.get(i).set(2, String.valueOf(qi));
			else if(qi > maxQ) busdet.get(i).set(2, String.valueOf(maxQ));
			else busdet.get(i).set(2, String.valueOf(minQ));
		}
		else if(maximumQ.equalsIgnoreCase("")==true & minimumQ.equalsIgnoreCase("")==false){
			minQ = Double.parseDouble(minimumQ);
			if(qi>minQ) busdet.get(i).set(2, String.valueOf(qi));
			else busdet.get(i).set(2, String.valueOf(minQ));
		}
		else if(minimumQ.equalsIgnoreCase("")==true & maximumQ.equalsIgnoreCase("")==false){
			maxQ = Double.parseDouble(maximumQ);
			if(qi<maxQ) busdet.get(i).set(2, String.valueOf(qi));
			else busdet.get(i).set(2, String.valueOf(maxQ));
		}
		else busdet.get(i).set(2, String.valueOf(qi));
	}
}