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
			Kmeans tryOne = new Kmeans(training.x_values, 4);
			tryOne.printCluster();
			
			//K values of 2, 4, 6 and 8
			/*
			for(int i = 2; i <= 8; i+=2){	
				Kmeans cluster = new Kmeans(training.x_values, i);
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

}
