package assignment2;

import Jama.Matrix;

public class NumRec {

	public Matrix w;
		
	//updates matrix w after it finishes training
	//follows slide 15 from logistic regression set
	public void trainData(MatrixPac p, float lr, float lambda){
		//set number of iterations
		//number needs to be changed until convergence is reached
		int num_iter = 1000;
		double sigma_y = 0;
		double err = 0;
		Matrix d = null;
		Matrix x = new Matrix(p.x_values);
		w = new Matrix(p.x_values[0].length,1); //Comment out once training is complete
		
		for (int i = 0; i < num_iter; i++){
			for(int j = 0; j < (p.x_values.length) - 1; j++){
				sigma_y = Math.round(1/(1+ Math.pow(Math.E, ( w.transpose().times(-1).times( x )).get(0, 0) ) )) ;
				err = p.y_values[j] - sigma_y;
				d = d.plus(x.times(err));	
			}
			w = w.plus(d.times(lr));
		}
	}
	
	public double testData(MatrixPac p, float lambda){
		double accuracy = 0.0;
		double right = 0.0;
		Matrix x = new Matrix( p.x_values);
		x = x.transpose();
		
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
