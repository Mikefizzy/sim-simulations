package logic.net;

import java.util.Arrays;
import java.util.Random;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import console.core.Console;
import logic.console.NetConsole;
public class LogicNet extends Net{
	private Console console;
	private double[][][] y;
	private int index = 0;
	public LogicNet(double[][][] w, double[][] x, double[][][] y) {
		super(w, x, y[0]);
		this.y = y;
		console = new NetConsole(System.in, this);
		new Thread(console).start();
	}
	public void switchDesired(){
		System.out.println("switched");
		index++;
		if(index>=y.length)
			index = 0;
		super.setY(y[index]);
	}
	public double[][] binaryDataSet(int start, int end){
		double[][] binarySet = new double[end-start][];
		for(int k = start; k<end; k++){
			char[] raw = Integer.toBinaryString(k).toCharArray();
			double[] toAdd = new double[raw.length+1];
			for(int i = 0; i<toAdd.length; i++)
				toAdd[i] = Character.getNumericValue(raw[i]);
			binarySet[k-start] = toAdd;
		}
		return binarySet;
	}
	
	public static void main(String[] args) {
		int[] layers  = {2,4,6};
		double[][][] w = Net.generateWeightsArray(layers);
		double[][] x = {{1,1,1},
						{1,0,0},
						{1,1,0},
						{1,0,1}};
		double[][][] y = {{{1,0,1,0,0,1},
							{0,1,0,1,0,1},
							{0,1,1,0,1,0},
							{0,1,1,0,1,0}},
								{{0,1,0,1,1,0},
								{1,0,1,0,1,0},
								{1,0,0,1,0,1},
								{1,0,0,1,0,1}}};
		
		LogicNet net = new LogicNet(w, x, y);
		
//		File dataFile = new File("home//michael//Desktop//abalone.data");
//		try {
//			System.out.println(dataFile.exists());
//			BufferedReader reader = new BufferedReader(new FileReader(dataFile));
//			String line  = "";
//			int counter = 0;
//			double[][] xArray = new double[4177][];
//			double[][] yArray = new double[4177][];
//			while((line = reader.readLine())!=null){
//				String[] params = line.split(",");
//				double[] y = new double[3];
//				switch(params[0]){
//				case "M":
//					y[0] = 1;
//					break;
//				case "F":
//					y[1] = 1;
//					break;
//				case "I":
//					y[2] = 1;
//					break;
//				}
//				params[0] = "1.0";
//				double[] x = new double[params.length];
//				for(int k = 0; k<x.length; k++)
//					x[k] = Double.parseDouble(params[k]);
//				xArray[counter] = x;
//				yArray[counter] = y;
//				counter++;
//			}
//			int[] layers  = {xArray[0].length-1, 6, 3};
//			double[][][] weights = LogicNet.generateWeightsArray(layers);
//			double[][][] toPass = {yArray};
//			LogicNet net = new LogicNet(weights, xArray, toPass);
//			reader.close();
//			
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
}
