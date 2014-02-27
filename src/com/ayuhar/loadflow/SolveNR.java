package com.ayuhar.loadflow;

import java.util.ArrayList;
import java.lang.Math.*;
import android.util.Log;
import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.commons.math.linear.ArrayRealVector;
import org.apache.commons.math.linear.DecompositionSolver;
import org.apache.commons.math.linear.LUDecompositionImpl;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealVector;

public class SolveNR {

	ArrayList<ArrayList<String>> li;
	ArrayList<ArrayList<String>> busdet;
	int nob, noi, nol, numberofPV,  numberofPQ;
	double maxQ, minQ, qi, pi, multiplier, maxDeltaP, maxDeltaQ;
	double[][] reY,imY,magY,thetaY,jacobian;
	double[] deltaPi , deltaQi, deltaPQ, deltaVtheta;
	String tolerance;
	
	public void getld(ArrayList<ArrayList<String>> a, ArrayList<ArrayList<String>> b, int x, int y, int z, double c, double d, String e){
		busdet = new ArrayList<ArrayList<String>>(a);
		li = new ArrayList<ArrayList<String>>();
		li = b;
		nol = y;
		noi = z;
		nob = x;
		maxQ = c;
		minQ = d;
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
		deltaPi = new double[nob];
		deltaQi = new double[nob];
		//Log.d("number of Bus",String.valueOf(nob) );
		//Log.d("number of line",String.valueOf(nol) );
		//Log.d("number of iteration",String.valueOf(noi) );
		//Log.d("Bus Type", busdet.get(0).get(0));
		formY();
		for(int k = 0; k<noi; k++){
			maxDeltaP = 0.00;
			maxDeltaQ = 0.00;
			numberofPV = 0;
			numberofPQ = 0;
			//Log.d("Bus Type", busdet.get(0).get(0));
			for (int i = 0; i<nob; i++){
				Log.d("Bus Type", busdet.get(i).get(0));
				if(busdet.get(i).get(0).equalsIgnoreCase("Slack") == true){ 
					Log.d("Slack Check", "Slack");
					busdet.get(i).add(5, "Slack");
					continue;
				}
				else {
					Log.d("P-V Check", "P-V");
					calculatedeltaPi(i);
					calculatedeltaQi(i);
				}
			}
			if(tolerance.equalsIgnoreCase("")==false & maxDeltaQ<Double.parseDouble(tolerance) & maxDeltaP<Double.parseDouble(tolerance)){
				formJacobian();
				formDeltaPQ();
				solve();
				updateVtheta();
				break;
			}
			formJacobian();
			formDeltaPQ();
			solve();
			updateVtheta();
		}
	}
	public void calculatedeltaPi(int i){
		multiplier = 0.000;
		for(int p=0; p<nob; p++){
			double gcos = reY[i][p]*(Math.cos(Double.parseDouble(busdet.get(i).get(4))-Double.parseDouble(busdet.get(p).get(4))));
			double bsin = imY[i][p]*(Math.sin(Double.parseDouble(busdet.get(i).get(4))-Double.parseDouble(busdet.get(p).get(4))));
			multiplier += (gcos+bsin)*Double.parseDouble(busdet.get(p).get(3));
		}
		double pi = multiplier*Double.parseDouble(busdet.get(i).get(3));
		deltaPi[i] = Double.parseDouble(busdet.get(i).get(1)) - pi;
		if(maxDeltaP < Math.abs(deltaPi[i])) maxDeltaP = Math.abs(deltaPi[i]);
	}
	public void calculatedeltaQi(int i){
		multiplier = 0.000;
		for(int p=0; p<nob; p++){
			double gsin = reY[i][p]*(Math.sin(Double.parseDouble(busdet.get(i).get(4))-Double.parseDouble(busdet.get(p).get(4))));
			double bcos = imY[i][p]*(Math.cos(Double.parseDouble(busdet.get(i).get(4))-Double.parseDouble(busdet.get(p).get(4))));
			multiplier += (gsin-bcos)*Double.parseDouble(busdet.get(p).get(3));
		}
		double qi = multiplier*Double.parseDouble(busdet.get(i).get(3));
		if(busdet.get(i).get(0).equalsIgnoreCase("P-Q") == true){
			numberofPQ++;
			busdet.get(i).add(5, "P-Q");
			deltaQi[i] = Double.parseDouble(busdet.get(i).get(2)) - qi;
		}
		else if(busdet.get(i).get(0).equalsIgnoreCase("P-V") == true){
			if(qi< maxQ & qi>minQ){
				numberofPV++;
				busdet.get(i).add(5, "P-V");
				busdet.get(i).set(2, String.valueOf(qi));
				deltaQi[i] = 0.0000;
			}
			else if(qi > maxQ){
				numberofPQ++;
				busdet.get(i).add(5, "P-V-Q");
				busdet.get(i).set(2, String.valueOf(maxQ));
				deltaQi[i] = maxQ - qi;
			}
			else{
				numberofPQ++;
				busdet.get(i).add(5, "P-V-Q");
				busdet.get(i).set(2, String.valueOf(minQ));
				deltaQi[i] = minQ - qi;
			}
		}
		if(maxDeltaQ < Math.abs(deltaQi[i])) maxDeltaQ = Math.abs(deltaQi[i]);
	}
	public void formJacobian(){
		jacobian = new double[2*numberofPQ+numberofPV][2*numberofPQ+numberofPV];
		double H,M,N,L;
		int jcol = 0;
		int jrow = 0;
		for(int i=0;i<nob;i++){
			for(int j=0;j<nob;j++){
				if(busdet.get(i).get(5).equalsIgnoreCase("Slack") == true || busdet.get(j).get(5).equalsIgnoreCase("Slack") == true) continue;
				else{
					if((busdet.get(i).get(5).equalsIgnoreCase("P-Q")==true || busdet.get(i).get(5).equalsIgnoreCase("P-V-Q")==true) & (busdet.get(j).get(5).equalsIgnoreCase("P-Q")==true || busdet.get(j).get(5).equalsIgnoreCase("P-V-Q")==true)){
						if(i==j){
							H = -Double.parseDouble(busdet.get(i).get(2))-(Double.parseDouble(busdet.get(i).get(2))*Double.parseDouble(busdet.get(i).get(2))*imY[i][i]);
							N = Double.parseDouble(busdet.get(i).get(1))+(Double.parseDouble(busdet.get(i).get(2))*Double.parseDouble(busdet.get(i).get(2))*reY[i][i]);
							M = Double.parseDouble(busdet.get(i).get(1))-(Double.parseDouble(busdet.get(i).get(2))*Double.parseDouble(busdet.get(i).get(2))*reY[i][i]);
							L = Double.parseDouble(busdet.get(i).get(2))-(Double.parseDouble(busdet.get(i).get(2))*Double.parseDouble(busdet.get(i).get(2))*imY[i][i]);							
						}
						else{
							double gcos = reY[i][j]*(Math.cos(Double.parseDouble(busdet.get(i).get(4))-Double.parseDouble(busdet.get(j).get(4))));
							double bsin = imY[i][j]*(Math.sin(Double.parseDouble(busdet.get(i).get(4))-Double.parseDouble(busdet.get(j).get(4))));
							double gsin = reY[i][j]*(Math.sin(Double.parseDouble(busdet.get(i).get(4))-Double.parseDouble(busdet.get(j).get(4))));
							double bcos = imY[i][j]*(Math.cos(Double.parseDouble(busdet.get(i).get(4))-Double.parseDouble(busdet.get(j).get(4))));
							H = L = (gsin-bcos)*Double.parseDouble(busdet.get(i).get(3))*Double.parseDouble(busdet.get(j).get(3));
							N = (gcos+bsin)*Double.parseDouble(busdet.get(i).get(3))*Double.parseDouble(busdet.get(j).get(3));
							M = -N;
						}
						jacobian[jrow][jcol] = H;
						jacobian[jrow][jcol+1] = N;
						jacobian[jrow+1][jcol] = M;
						jacobian[jrow+1][jcol+1] = L;
						jcol = jcol+2;
					}
					else if((busdet.get(i).get(5).equalsIgnoreCase("P-Q")==true || busdet.get(i).get(5).equalsIgnoreCase("P-V-Q")==true) & busdet.get(j).get(5).equalsIgnoreCase("P-V")==true){
						if(i==j){
							H = -Double.parseDouble(busdet.get(i).get(2))-(Double.parseDouble(busdet.get(i).get(2))*Double.parseDouble(busdet.get(i).get(2))*imY[i][i]);
							M = Double.parseDouble(busdet.get(i).get(1))-(Double.parseDouble(busdet.get(i).get(2))*Double.parseDouble(busdet.get(i).get(2))*reY[i][i]);
						}
						else{
							double gcos = reY[i][j]*(Math.cos(Double.parseDouble(busdet.get(i).get(4))-Double.parseDouble(busdet.get(j).get(4))));
							double bsin = imY[i][j]*(Math.sin(Double.parseDouble(busdet.get(i).get(4))-Double.parseDouble(busdet.get(j).get(4))));
							double gsin = reY[i][j]*(Math.sin(Double.parseDouble(busdet.get(i).get(4))-Double.parseDouble(busdet.get(j).get(4))));
							double bcos = imY[i][j]*(Math.cos(Double.parseDouble(busdet.get(i).get(4))-Double.parseDouble(busdet.get(j).get(4))));
							H = (gsin-bcos)*Double.parseDouble(busdet.get(i).get(3))*Double.parseDouble(busdet.get(j).get(3));
							M = -(gcos+bsin)*Double.parseDouble(busdet.get(i).get(3))*Double.parseDouble(busdet.get(j).get(3));
						}
						jacobian[jrow][jcol] = H;
						jacobian[jrow+1][jcol] = M;
						jcol = jcol+1;
					}
					else if((busdet.get(j).get(5).equalsIgnoreCase("P-Q")==true || busdet.get(j).get(5).equalsIgnoreCase("P-V-Q")==true) & busdet.get(i).get(5).equalsIgnoreCase("P-V")==true){
						if(i==j){
							H = -Double.parseDouble(busdet.get(i).get(2))-(Double.parseDouble(busdet.get(i).get(2))*Double.parseDouble(busdet.get(i).get(2))*imY[i][i]);
							N = Double.parseDouble(busdet.get(i).get(1))+(Double.parseDouble(busdet.get(i).get(2))*Double.parseDouble(busdet.get(i).get(2))*reY[i][i]);
						}
						else{
							double gcos = reY[i][j]*(Math.cos(Double.parseDouble(busdet.get(i).get(4))-Double.parseDouble(busdet.get(j).get(4))));
							double bsin = imY[i][j]*(Math.sin(Double.parseDouble(busdet.get(i).get(4))-Double.parseDouble(busdet.get(j).get(4))));
							double gsin = reY[i][j]*(Math.sin(Double.parseDouble(busdet.get(i).get(4))-Double.parseDouble(busdet.get(j).get(4))));
							double bcos = imY[i][j]*(Math.cos(Double.parseDouble(busdet.get(i).get(4))-Double.parseDouble(busdet.get(j).get(4))));
							H = (gsin-bcos)*Double.parseDouble(busdet.get(i).get(3))*Double.parseDouble(busdet.get(j).get(3));
							N = (gcos+bsin)*Double.parseDouble(busdet.get(i).get(3))*Double.parseDouble(busdet.get(j).get(3));
						}
						jacobian[jrow][jcol] = H;
						jacobian[jrow+1][jcol] = N;
						jcol += 2;
					}
					else if(busdet.get(i).get(5).equalsIgnoreCase("P-V")==true & busdet.get(j).get(5).equalsIgnoreCase("P-V")==true){
						if(i==j){H = -Double.parseDouble(busdet.get(i).get(2))-(Double.parseDouble(busdet.get(i).get(2))*Double.parseDouble(busdet.get(i).get(2))*imY[i][i]);}
						else{
							double gsin = reY[i][j]*(Math.sin(Double.parseDouble(busdet.get(i).get(4))-Double.parseDouble(busdet.get(j).get(4))));
							double bcos = imY[i][j]*(Math.cos(Double.parseDouble(busdet.get(i).get(4))-Double.parseDouble(busdet.get(j).get(4))));
							H = (gsin-bcos)*Double.parseDouble(busdet.get(i).get(3))*Double.parseDouble(busdet.get(j).get(3));
						}
						jacobian[jrow][jcol] = H;
						jcol+=1;
					}
				}
			}
						
			if(busdet.get(i).get(5).equalsIgnoreCase("Slack")==true) continue;
			else if(busdet.get(i).get(5).equalsIgnoreCase("P-V")==true) jrow+=1;
			else jrow+=2;
			
		}
	}
	public void formDeltaPQ(){
		deltaPQ = new double[2*numberofPQ+numberofPV];
		int row = 0;
		for(int i = 0; i<nob ; i++){
			if(busdet.get(i).get(5).equalsIgnoreCase("Slack")) continue;
			else if(busdet.get(i).get(5).equalsIgnoreCase("P-Q") || busdet.get(i).get(5).equalsIgnoreCase("P-V-Q")){
				deltaPQ[row] = deltaPi[i];
				deltaPQ[row+1] = deltaQi[i];
				row = row+2;
			}
			else{
				deltaPQ[row] = deltaPi[i];
				row++;
			}
		}
	}
	public void solve(){
		RealMatrix a = new Array2DRowRealMatrix(jacobian);
        System.out.println("a matrix: " + a);
        DecompositionSolver solver = new LUDecompositionImpl(a).getSolver();

        RealVector b = new ArrayRealVector(deltaPQ);
        RealVector x = solver.solve(b);
        deltaVtheta =  new double[2*numberofPQ+numberofPV];
        deltaVtheta = x.toArray();
//        System.out.println("solution x: " + x);
//        RealVector residual = a.operate(x).subtract(b);
//        double rnorm = residual.getLInfNorm();
//        System.out.println("residual: " + rnorm);
	}
	public void updateVtheta(){
		int row = 0;
		double updatedV, updatedT;
		for(int i = 0; i<nob ; i++){
			if(busdet.get(i).get(5).equalsIgnoreCase("Slack")) continue;
			else if(busdet.get(i).get(5).equalsIgnoreCase("P-Q") || busdet.get(i).get(5).equalsIgnoreCase("P-V-Q")){
				updatedT = deltaVtheta[row] + Double.parseDouble(busdet.get(i).get(4));
				busdet.get(i).set(4, String.valueOf(updatedT));
				updatedV = deltaVtheta[row+1]*Double.parseDouble(busdet.get(i).get(3)) + Double.parseDouble(busdet.get(i).get(3));
				busdet.get(i).set(3, String.valueOf(updatedV));
				row = row+2;
			}
			else{
				updatedT = deltaVtheta[row] + Double.parseDouble(busdet.get(i).get(4));
				busdet.get(i).set(4, String.valueOf(updatedT));
				row++;
			}
		}
	}
}