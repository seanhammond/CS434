package assignment3;

import org.apache.commons.lang3.ArrayUtils;

public class KNN {
	
	private final int maxK = 15;
	public int K = 1;

	//Test K from 1-15 using leave-one-out cross-validation
	//Return best K
	public int determineBestK(MatrixPac p){
		
		
		MatrixPac trainSet = new MatrixPac();
		trainSet.y_values = p.y_values;
		
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
			
			System.out.println(K + " Error: " + (wrong/xs.length ));
		}
		
		return 0;
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
