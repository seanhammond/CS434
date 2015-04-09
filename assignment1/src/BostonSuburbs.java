import java.util.ArrayList;
import java.util.List;

import Jama.Matrix;

public class BostonSuburbs {
	
	private Matrix w;
	
	public void train(List<ArrayList<Double>> dataset, double sigma){
		//Linear Regression algorithm here
		Matrix x,y;
		
		
		//Get X Matrices nx13 // The first 13 elements are the X matrix
		//Get Y Matrices nx1 // The 14th element of the dataset is are the Y values
		ArrayList<ArrayList<Double>> xElements = new ArrayList<ArrayList<Double>>();
		ArrayList<Double> yElements = new ArrayList<Double>();
		
		for(ArrayList<Double> e : dataset){
			//e is a list of 14 elements
			ArrayList<Double> firstSet = new ArrayList<Double>();
			
			firstSet.add(1.0); //Dummy Variable
			
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
		
		x = new Matrix(xMatrix); //nx14
		y = new Matrix(yMatrix, 1); //1xn
		y = y.transpose(); //nx1
		
		Matrix I = (x.transpose().times(x));
		I = Matrix.identity(I.getRowDimension(), I.getColumnDimension());
		
		//w = (XT * X + sigmaI)^−1 * XT * Y
		w = (x.transpose().times(x).plus(I.times(sigma))).inverse().times(x.transpose()).times(y); //14x1 //Learned weight vector
		
		//Print dimensions for debugging
		//System.out.println("X is " + x.getRowDimension() + "x" + x.getColumnDimension());
		//System.out.println("Y is " + y.getRowDimension() + "x" + y.getColumnDimension());
		//System.out.println("W is " + w.getRowDimension() + "x" + w.getColumnDimension());
		
		//Print Matrices
		//x.print(0, 5);
		//y.print(1, 2);
		//w.print(0, 10);

	}
	
	public double evaluateSSE(List<ArrayList<Double>> dataset){
		
		Matrix x,y;
		
		ArrayList<ArrayList<Double>> xElements = new ArrayList<ArrayList<Double>>();
		ArrayList<Double> yElements = new ArrayList<Double>();
		
		for(ArrayList<Double> e : dataset){
			//e is a list of 14 elements
			ArrayList<Double> firstSet = new ArrayList<Double>();
			
			firstSet.add(1.0); //Dummy Variable
			
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
		
		x = new Matrix(xMatrix); //nx14
		y = new Matrix(yMatrix, 1); //1xn
		y = y.transpose(); //nx1
		
		//Compute SSE vlaue
		// (y - Xw)T * (y - Xw)
		Matrix sse = ((y.minus(x.times(w))).transpose()).times(y.minus(x.times(w))); //1x1
						
		return sse.get(0, 0);
		
	}
	
	
	private double[] toDoubleArray(List<Double> list){
		double[] array = new double[list.size()];
		for(int i = 0; i < array.length; i++){
			array[i] = list.get(i);
		}
		return array;
	}
}
