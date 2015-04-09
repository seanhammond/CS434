import java.util.ArrayList;
import java.util.List;

import Jama.Matrix;

public class BostonSuburbs {
	public void train(List<ArrayList<Double>> dataset){
		//Linear Regression algorithm here
		Matrix x,y,w;
		
		//Get X Matrices nx13 // The first 13 elements are the X matrix
		//Get Y Matrices nx1 // The 14th element of the dataset is are the Y values
		ArrayList<ArrayList<Double>> xElements = new ArrayList<ArrayList<Double>>();
		ArrayList<Double> yElements = new ArrayList<Double>();
		
		for(ArrayList<Double> e : dataset){
			//e is a list of 14 elements
			ArrayList<Double> firstSet = new ArrayList<Double>();
			for(int i = 0; i < e.size()-1; i++){
				firstSet.add(e.get(i));
			}
			xElements.add(firstSet);
			yElements.add(e.get(e.size()-1));
		}
		
		//Translate Double into double
		double[][] xMatrix = new double[xElements.size()][xElements.get(0).size()];
		for(int i = 0; i < xElements.size(); i++){
			xMatrix[i] = toDoubleArray(xElements.get(i));
		}
		
		double[] yMatrix = toDoubleArray(yElements);
		
		x = new Matrix(xMatrix); //nx13
		y = new Matrix(yMatrix, 1); //1xn
		y = y.transpose(); //nx1
		w = new Matrix(dataset.size(), 1); //nx1
		
		//x.print(0, 5);
		//y.print(0, 2);
		//w.print(0, 2);
		
		System.out.println("X is " + x.getRowDimension() + "x" + x.getColumnDimension());
		System.out.println("Y is " + y.getRowDimension() + "x" + y.getColumnDimension());
		System.out.println("W is " + w.getRowDimension() + "x" + w.getColumnDimension());
		
		
		
		
	}
	
	private double[] toDoubleArray(List<Double> list){
		double[] array = new double[list.size()];
		for(int i = 0; i < array.length; i++){
			array[i] = list.get(i);
		}
		return array;
	}
}
