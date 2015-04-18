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
	
	public double testData(MatrixPac p, float lambda){
		double accuracy = 0.0;
		double right = 0.0;
		Matrix x = new Matrix( p.x_values);
		
		x = x.transpose();
		
		w = new Matrix(p.x_values[0].length,1); //Comment out once training is complete
		
		for(int i = 0; i < p.x_values.length; i++){
			//Compute prediction
			//Y = 1/(1+e^(-wTx[i])
			double predY = Math.round(1/(1+ Math.pow(Math.E, ( w.transpose().times(-1).times( x )).get(0, 0) ) )) ;
			
			//Test prediction
			if(p.y_values[i] == predY){
				right++;
			}
			
		}
		accuracy = right/(p.x_values.length);
		
		return accuracy;
	}
}
