package assignment3;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.commons.lang3.ArrayUtils;

public class KNN {
	
	private final int maxK = 15;
	public int K = 1;
	
	private class ErrorK {
		public int k;
		public double error;
		
		public ErrorK(int k, double error){
			this.k = k;
			this.error = error;
		}
	}

	//Test K from 1-15 using leave-one-out cross-validation
	//Return best K
	public int determineBestK(MatrixPac p) throws FileNotFoundException, UnsupportedEncodingException{
		
		MatrixPac trainSet = new MatrixPac();
		trainSet.y_values = p.y_values;
		
		ArrayList<ErrorK> errors = new ArrayList<ErrorK>();
		
		double[][] xs = p.x_values;
		
		for(int k = 1; k <= 205; k +=2){
			K = k;
			float wrong = 0;
			
			for(int i = 0; i < xs.length; i++){
				double[] validator = xs[i];
				trainSet.x_values = ArrayUtils.remove(xs, i);
				
				
				//Get validation error
				int y = getNearestValue(validator, trainSet);
				if(y != p.y_values[i]){
					wrong++;
				}
			}
			double error = ((double)wrong/(double)xs.length );
			
			errors.add(new ErrorK(k,error));
		}
		PrintWriter writer = new PrintWriter("allErrors.tsv", "UTF-8");
		ErrorK bestK = errors.get(0);
		
		for(ErrorK error : errors){
			writer.println(error.error);
			if(error.error < bestK.error){
				bestK = error;
			}
		}
		writer.close();
		//TODO Produce a graph of all the errors off cross-validation to show why we chose that k value
		
		return bestK.k;
	}
	
	public double getValidationError(MatrixPac train){
		
		MatrixPac trainSet = new MatrixPac();
		trainSet.y_values = train.y_values;
		
		float wrong = 0;
		double[][] xs = train.x_values;
		
		for(int i = 0; i < xs.length; i++){
			double[] validator = xs[i];
			trainSet.x_values = ArrayUtils.remove(xs, i);
			
			
			//Get validation error
			int y = getNearestValue(validator, trainSet);
			if(y != train.y_values[i]){
				wrong++;
			}
		}
		return ((double)wrong/(double)xs.length );
	}
	
	public double getError(MatrixPac train, MatrixPac test){
		//For every vector in the test set
		
		double[][] testX = test.x_values;
		int wrong = 0;
		for(int i = 0; i < testX.length; i++){
			
			int value = getNearestValue(testX[i],train);
			if(value != test.y_values[i]){
				wrong++;
			}
		}
		
		return ((double)wrong/(double)testX.length);
	}
	
	
	public int getNearestValue(double[] x, MatrixPac p){
		
		double[][] xs = p.x_values;
		double[] distances = new double[xs.length];
		
		//For each point
		for(int i = 0; i < xs.length; i++){
			
			double distanceSum = 0;
			
			//For each attribute
			for(int j = 0; j < x.length; j++){
				distanceSum += Math.pow(x[j] - xs[i][j], 2);
			}
			
			double sum = Math.sqrt(distanceSum);
			distances[i] = sum;
		}
		
		int[] closetIndices = new int[K];
		for(int i = 0; i < K; i++){
			closetIndices[i] = i;
		}
		
		for(int i = K; i < distances.length; i++){
			
			int largestSmallestIndex = -1;
			for(int j = 0; j < K; j++){
				if(distances[i] < distances[closetIndices[j]]){
					if(largestSmallestIndex != -1){
						if(distances[closetIndices[largestSmallestIndex]] < distances[closetIndices[j]]){
							largestSmallestIndex = j;
						}
					} else {
						largestSmallestIndex = j;
					}
				}
			}
			
			if(largestSmallestIndex != -1){
				closetIndices[largestSmallestIndex] = i;
			}
			
		}
		
		int average = 0;
		for(int i = 0; i < K; i++){
			average += p.y_values[closetIndices[i]];
		}
		
		
		return (average > 0) ? 1 : -1;
	}

}
