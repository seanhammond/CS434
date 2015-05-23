package test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
//import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instances;

import assignment4.MatrixPac;

@SuppressWarnings("unused")
public class Main {
	
	public static BufferedReader readDataFile(String filename) {
		BufferedReader inputReader = null;
 
		try {
			inputReader = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException ex) {
			System.err.println("File not found: " + filename);
		}
 
		return inputReader;
	}
	
	public static void main(String args[]) throws Exception, FileNotFoundException, UnsupportedEncodingException{
		
		//Create paths for data files
		Path pathTrain = Paths.get(System.getProperty("user.dir")+ "/src/assignment4/breast-cancer.arff"); // Get path
				
		int[] assignments={0};
		int[] best_assign={1};
		try {
					
			//Parse data files
			BufferedReader datafile = readDataFile(pathTrain.toString());
			Instances data = new Instances(datafile);			
				
			double best_SSE = 20000;
			SimpleKMeans kmeans = new SimpleKMeans();
			//int[] assignments;
			kmeans.setSeed(10);
			PrintWriter writer = new PrintWriter("kmeans.txt", "UTF-8");
			
			
			kmeans.setPreserveInstancesOrder(true);
			for(int i = 2; i <= 8; i+=2){
				for(int j = 0; j <= 10; j++){
					kmeans.setNumClusters(i);
					kmeans.buildClusterer(data);
					assignments = kmeans.getAssignments();	
					//calculate sum of squares
					double current_SEE = kmeans.getSquaredError();
					if(current_SEE < best_SSE){
						best_SSE = current_SEE; 
						best_assign = kmeans.getAssignments();
					}
				}
				int k=0;
				writer.printf("Best for cluster of K = %d\n", i);
				for(int clusterNum : best_assign) {
					writer.printf("Instance %d -> Cluster %d \n", k, clusterNum);
					k++;
				}
				writer.printf("---------\n");
			}
			writer.close();
			
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	


}
