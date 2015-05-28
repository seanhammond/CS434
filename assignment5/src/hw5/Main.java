package hw5;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;




import hw5.MatrixPac;
@SuppressWarnings("unused")
public class Main {

	public static void main(String[] args) throws Exception, FileNotFoundException, UnsupportedEncodingException {
		Path pathTrain = Paths.get(System.getProperty("user.dir")+ "/src/usps-4-9-train.csv"); // Get path
		
		MatrixPac training = new MatrixPac();
		
		
		
		try {
			
			//Parse data files
			training.parseCSV_void(pathTrain.toString());
			
			//K values of 2, 4, 6 and 8
			long startTime1 = System.currentTimeMillis();
			for(int i = 2; i <= 8; i+=2){	
				Kmeans bestCluster = new Kmeans(training.x_values, i);
				bestCluster.compute();
				double bestSSE = bestCluster.clusterSSE();
				
				for(int j = 0; j < 10; j++){
					Kmeans cluster = new Kmeans(training.x_values, i);
					
					cluster.compute();
					double clusterSSE = cluster.clusterSSE();
					if(clusterSSE < bestSSE){
						bestCluster = cluster;
						bestSSE = clusterSSE;
					}
				}
				
				printCluster(bestCluster);
				printConfusionMatrix(i,bestCluster.confusionMatrix(training.y_values));
				System.out.println("Purity: " + bestCluster.clusterPurity(training.y_values) + "\n\n");
			}
			long endTime1 = System.currentTimeMillis();
			long elapsedTime1 = endTime1 - startTime1;
			
			training.x_values = Kmeans.reducePCAData(training.x_values);
			//Reduced
			long startTime2 = System.currentTimeMillis();
			for(int i = 2; i <= 8; i+=2){	
				Kmeans bestCluster = new Kmeans(training.x_values, i);
				bestCluster.compute();
				double bestSSE = bestCluster.clusterSSE();
				
				for(int j = 0; j < 10; j++){
					Kmeans cluster = new Kmeans(training.x_values, i);
					
					cluster.compute();
					double clusterSSE = cluster.clusterSSE();
					if(clusterSSE < bestSSE){
						bestCluster = cluster;
						bestSSE = clusterSSE;
					}
				}
				
				printCluster(bestCluster);
				printConfusionMatrix(i,bestCluster.confusionMatrix(training.y_values));
				System.out.println("Purity: " + bestCluster.clusterPurity(training.y_values) + "\n\n");
			}
			long endTime2 = System.currentTimeMillis();
			long elapsedTime2 = endTime2 - startTime2;
			
			System.out.println("Run time of full dataset: " + elapsedTime1);
			System.out.println("Run time of reduced dataset: " + elapsedTime2);
			
		}catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void printConfusionMatrix(int k, int[][] matrix)throws Exception, FileNotFoundException, UnsupportedEncodingException{
		PrintWriter writer = new PrintWriter("kmeans_printConfusionMatrix"+k+".txt", "UTF-8");
		System.out.println("k: "+k+"\n----------------------\n");
		writer.println("k: "+k+"\n----------------------\n");
		System.out.println("class\t0\t1\n");
		writer.println("class\t0\t1\n");
		for(int i = 0; i < matrix.length; i++){
			System.out.println(i+"\t"+matrix[i][0]+"\t"+matrix[i][1]);
			writer.println(i+"\t"+matrix[i][0]+"\t"+matrix[i][1]);
		}
		writer.close();
		
	}
	
	
	public static void printCluster(Kmeans kmeans) throws Exception, FileNotFoundException,
			UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter("kmeans"+kmeans.clusters.size()+".txt", "UTF-8");
		for (int n = 0; n < kmeans.clusters.size(); n++) {
			
			writer.printf("Cluster %d has %d instances\n", n, kmeans.clusters.get(n).size());
		
		}
		writer.close();
	
	}

}
