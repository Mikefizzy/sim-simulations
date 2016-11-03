package logic.net;
import java.util.Random;

import logic.engine.NetEngine;
import sim.tools.Calculations;

public class Net {
	private double[][][] w;
	private double[][]x;
	private double[][]y;
	private NetEngine e;
	public Net(double[][][] w, double[][]x, double[][]y){
		 e = new NetEngine(w,x);
		new Thread(e).start();
		this.w = w;
		this.x = x;
		this.y = y;
	}
	public double[] test(double[]x){
		return test(x,false);
	}
	public double[] test(double[]x, boolean render){
		double[] alpha = new double[w[1].length];
		double[] gb = new double[w[0].length + 1];
		gb[0] = 1;
		for(int i = 0; i<w[0].length; i++){
			gb[i+1] = Calculations.sigmoid(Calculations.dotProduct(w[0][i], x));
		}
		for(int k  = 0; k<alpha.length; k++)
			alpha[k] = Calculations.sigmoid(Calculations.dotProduct(gb, w[1][k]));
		if(render)
		e.setTrailView(x, alpha);
		return alpha;
	}
	public static double[][][] generateWeightsArray(int[] layers){ //Make sure you don't include bias for the input layer
		double[][][] w = new double[layers.length-1][][];
		Random rand  = new Random();
		for(int k = 0; k<w.length; k++)
			w[k] = new double[layers[k+1]][layers[k]+1];
		
		for(int k = 0; k<w.length; k++)
			for(int i = 0; i<w[k].length; i++)
				for(int j = 0; j<w[k][i].length; j++)
					w[k][i][j] = rand.nextDouble();
		return w;
	}
	public void train(int n, double alpha){
		for(int f = 0; f<n; f++){
		for(int h = 0; h<w.length; h++)
			for(int i = 0; i<w[h].length; i++)
				for(int j = 0; j<w[h][i].length; j++){
					for(int k = 0; k<w[1].length;k++){
						double grad = gradient(h, i,j, k);
					w[h][i][j] -= alpha*grad;
					}
				}
		
		double sum = 0;
		for(int k = 0; k<w[1].length;k++){
			double esum = 0;
			for(int i = 0; i<x.length;i++){
				esum+= Calculations.crossEntropy(test(x[i])[k], y[i][k]);
			}
			sum+=esum/x.length;
		}
		e.updateCost(sum);
		}
	}
	public void sleep(int millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public double gradient(int layer, int unit, int index, int k){
		double sum = 0;
		for(int i = 0; i<x.length; i++) {
			double hAlpha = Calculations.sigmoidPrime(alpha(k,x[i]));
			double costPrime = Calculations.crossEntropyPrime(Calculations.sigmoid(alpha(k, x[i])),
					y[i][k]);
		switch(layer){
		case 0:
			sum+= hAlpha * 
			w[1][k][unit+1] *
			Calculations.sigmoidPrime(Calculations.dotProduct(w[0][unit], x[i])) *
			x[i][index] * 
			costPrime;
			break;
		case 1:
			double b = 1;
			if(index>0)
				b =  Calculations.sigmoid(Calculations.dotProduct(w[0][index-1], x[i]));
			sum+= hAlpha *b * costPrime;
			break;
		}
		}
		return sum/x.length;
	}
	public double alpha(int k, double[] x){
		double[] gb = new double[w[0].length + 1];
		gb[0] = 1;
		for(int i = 0; i<w[0].length; i++){
			gb[i+1] = Calculations.sigmoid(Calculations.dotProduct(w[0][i], x));
		}
		return Calculations.dotProduct(gb, w[1][k]);
	}
	public void setY(double[][] y ){
		this.y = y;
	}public double[][][] getWeights(){
		return w;
	}
	public double[][] getDesired(){
		return y;
	}
	public double[][] getInputs(){
		return x;
	}
	public NetEngine getNetEngine(){
		return e;
	}
}