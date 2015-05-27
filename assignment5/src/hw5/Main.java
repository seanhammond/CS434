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
		
		//PrintWriter writer = new PrintWriter("storingwright.txt", "UTF-8");
		
		try {
			
			//Parse data files
			training.parseCSV_void(pathTrain.toString());
			
			int size = training.x_values.length;
			double thing = training.x_values[1399][0];
			double thing2 = training.x_values[1399][6];
			System.out.printf("number of lines %d\n", size);
			System.out.printf("value at x_values[1399][0] = %f\n", thing);
			System.out.printf("value at x_values[1399][0] = %f\n", thing2);
			Kmeans tryOne = new Kmeans(training.x_values, 2);
			
			//K values of 2, 4, 6 and 8
			
			for(int i = 2; i <= 8; i+=2){	
				Kmeans bestCluster = new Kmeans(training.x_values, i);
				bestCluster.compute();
				double bestSSE = bestCluster.clusterSSE();
				
				for(int j = 0; j < 100; j++){
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
			}
			/*for(int i = 0; i < 2; i++){
				for(int j = 0; j < (training.x_values.length) - 1; j++){
					writer.printf("%d in x values is %f\n", i, training.x_values[j][i]);
					
				}
			}
			writer.close();
			*/
		}catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void printConfusionMatrix(int k, int[][] matrix){
		System.out.println("k: "+k+"\n----------------------\n");
		System.out.println("class\t0\t1\n");
		for(int i = 0; i < matrix.length; i++){
			System.out.println(i+"\t"+matrix[i][0]+"\t"+matrix[i][1]);
		}
		
	}
	
	
	public static void printCluster(Kmeans kmeans) throws Exception, FileNotFoundException,
			UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter("kmeans"+kmeans.clusters.size()+".txt", "UTF-8");
		PrintWriter writer2 = new PrintWriter("kmeans.tsv", "UTF-8");
		for (int n = 0; n < kmeans.clusters.size(); n++) {
			
			writer.printf("Cluster %d has %d instances\n", n, kmeans.clusters.get(n).size());
			
			/*
			Iterator<Integer> iterator = this.clusters.get(n).iterator();
			while (iterator.hasNext()) {
				// for(int j=0; j < this.clusters[n].size(); j++){
				int instance = iterator.next();
				// int instance = this.clusters[n].get(j);
				writer.printf("Instance %d\tCluster %d\n", instance, n);
				writer2.printf("%d\tCluster %d\n", instance, n);
			}*/
		}
		writer.close();
		writer2.close();
	}

}
