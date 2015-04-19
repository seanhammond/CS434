package assignment2;

import java.util.Arrays;

import Jama.Matrix;

public class NumRec {

	public Matrix w;
		
	//updates matrix w after it finishes training
	//follows slide 15 from logistic regression set
	public void trainData(MatrixPac p, double lr, double lambda){
		//set number of iterations
		//number needs to be changed until convergence is reached
		int num_iter = 300;
		double sigma_y = 0;
		double err = 0;
		Matrix d = new Matrix(p.x_values[0].length,1); //256x1
		//Matrix x = new Matrix(p.x_values);
		w = new Matrix(1,p.x_values[0].length); //1x256
		
		for (int i = 0; i < num_iter; i++){
			d = d.times(0);
			for(int j = 0; j < (p.x_values.length) - 1; j++){
				Matrix x = new Matrix(p.x_values[j],1); //1x256
				x = x.transpose(); //256x1
			
				sigma_y = 1.0/(1.0+ Math.exp(w.times(-1.0).times( x ).get(0, 0) ) ) ;
				
				err = p.y_values[j] - sigma_y;
				//System.out.println(err);
				d = d.plus(x.times(err));	
			}
			w = w.plus(d.times(lr).transpose());
		}
	}
	
	public double testData(MatrixPac p, float lambda){
		double accuracy = 0.0;
		double right = 0.0;
		
		for(int i = 0; i < p.x_values.length; i++){
			Matrix x = new Matrix(p.x_values[i],1);
			x = x.transpose();
			//Compute prediction
			//Y = 1/(1+e^(-wTx[i])
			double predY = Math.round(1/(1+ Math.exp(w.times(-1).times( x ).get(0, 0)) )) ;
			
			//System.out.println("PREDY: " + predY);
			//Test prediction
			if(p.y_values[i] == predY){
				right++;
			}
			
		}
		accuracy = right/(p.x_values.length);
		
		return accuracy;
	}
}
