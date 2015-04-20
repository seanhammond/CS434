package assignment2;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import Jama.Matrix;

public class NumRec {
	
	public final int num_iter = 200; //optimal iterations for full coverage

	public Matrix w;
		
	/*
	 * Applies linear regression algorithm and updates w with the learned vector.
	 */
	public void trainData(MatrixPac p, double lr, double lambda){
		
		double sigma_y = 0;
		double err = 0;
		Matrix d = new Matrix(p.x_values[0].length,1); 	//256x1
		w = new Matrix(1,p.x_values[0].length); 		//1x256
		
		for (int i = 0; i < num_iter; i++){
			d = d.times(0); //Reset d to (0,0,0,...,0);
			for(int j = 0; j < (p.x_values.length) - 1; j++){
				//Get x_i
				Matrix x = new Matrix(p.x_values[j],1); //1x256
				x = x.transpose(); 						//256x1
			
				//Calculate sigma y
				sigma_y = 1.0/(1.0+ Math.exp(w.times(-1.0).times( x ).get(0, 0) ) ) ;
				
				//Add error to d
				err = p.y_values[j] - sigma_y;
				d = d.plus(x.times(err));	
			}
			
			//Adjust w from batched error of d
			w = w.plus(d.times(lr).transpose());
		}
	}
	
	/*
	 * Records tsv file for training and testing data accuracy per iteration
	 */
	public void recordIterationAccuracy(MatrixPac train, MatrixPac test, double lr, double lambda, String filename) throws FileNotFoundException, UnsupportedEncodingException{
		
		PrintWriter writer = new PrintWriter(filename, "UTF-8");
		writer.println("\tTrain Data\tTest Data"); //Heading

		double sigma_y = 0;
		double err = 0;
		Matrix d = new Matrix(train.x_values[0].length,1); 	//256x1
		w = new Matrix(1,train.x_values[0].length); 		//1x256
		
		for (int i = 0; i < num_iter; i++){
			d = new Matrix(train.x_values[0].length,1);
			for(int j = 0; j < (train.x_values.length) - 1; j++){
				//Get x_i
				Matrix x = new Matrix(train.x_values[j],1); //1x256
				x = x.transpose(); 						//256x1
			
				//Calculate sigma y
				sigma_y = 1.0/(1.0+ Math.exp(w.times(-1.0).times( x ).get(0, 0) ) ) ;
				
				//Add error to d
				err = train.y_values[j] - sigma_y;
				d = d.plus(x.times(err)).plus(w.transpose().times(lambda));	
			}
			
			//Record accuracy
			writer.println(i + "\t" + this.testData(train)*100 + "\t" + this.testData(test)*100);
			
			//Adjust w from batched error of d
			w = w.plus(d.times(lr).transpose());
		}
		
		writer.close();
	}
	
	/*
	 * Applies training algorithm with lambda component
	 */
	public void trainDataLambda(MatrixPac p, double lr, double lambda){
		
		double sigma_y = 0;
		double err = 0;
		Matrix d = new Matrix(p.x_values[0].length,1); 	//256x1
		w = new Matrix(1,p.x_values[0].length); 		//1x256
		
		for (int i = 0; i < num_iter; i++){
			d = d.times(0);
			for(int j = 0; j < (p.x_values.length) - 1; j++){
				//Get x_i
				Matrix x = new Matrix(p.x_values[j],1); //1x256
				x = x.transpose(); 						//256x1
			
				//Calculate sigma y
				sigma_y = 1.0/(1.0+ Math.exp(w.times(-1.0).times( x ).get(0, 0) ) ) ;
				
				//Add error to d
				err = p.y_values[j] - sigma_y;
				d = d.plus(x.times(err)).plus(w.transpose().times(lambda));	//Apply lambda
			}
			
			//Adjust w from batched error of d
			w = w.plus(d.times(lr).transpose());
		}
	}
	
	/*
	 * Return predicted y value from given x vector
	 */
	public double getNumber(double[] x){
		return Math.round(1/(1+ Math.exp(w.times(-1).times( (new Matrix(x,1)).transpose() ).get(0, 0)) ));
	}
	
	/*
	 * Returns the accuracy of a given w vector
	 */
	public double testData(MatrixPac p){
		double accuracy = 0.0;
		double right = 0.0;
		
		for(int i = 0; i < p.x_values.length; i++){
			Matrix x = new Matrix(p.x_values[i],1);
			x = x.transpose();
			
			//Compute prediction
			//Y = 1/(1+e^(-wTx[i])
			double predY = Math.round(1/(1+ Math.exp(w.times(-1).times( x ).get(0, 0)) )) ;
			
			//Test prediction
			if(p.y_values[i] == predY){
				right++;
			}
			
		}
		
		//Compute accuracy
		accuracy = right/(p.x_values.length);
		
		return accuracy;
	}
}
