package assignment2;

import Jama.Matrix;

public class NumRec {

	public Matrix w;
	
	public static double sigmoid(double x){
		return 1 / (1 + Math.exp(-x));
	}
	
	//updates matrix w after it finishes training
	//follows slide 15 from logistic regression set
	public void trainData(MatrixPac p, float lr, float lambda){
		//set number of iterations
		//number needs to be changed until convergence is reached
		int num_iter = 1000;
		double sigma_y = 0;
		double err = 0;
		double d = 0;
		for (int i = 0; i < num_iter; i++){
			for(int j = 0; j < (p.x_values.length) - 1; j++){
				for(int k = 0; k < p.x_values[j].length; k++){
					//todo: fix this
					//sigma_y = sigmoid(-w*p.x_values[j][k]);
					err = p.y_values[j] - sigma_y;
					d = d + err*p.x_values[j][k];
				}
				
			}
			//fix this
			//w = w + lr*d;
		}
	}
	
	public static double testData(MatrixPac p, float lambda){
		double accuracy = 0.0;
		
		return accuracy;
	}
}
