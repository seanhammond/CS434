package assignment2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Main {
	
	public static void main(String args[]){
		Path pathTrain = Paths.get(System.getProperty("user.dir")+ "/src/usps-4-9-train.csv"); // Get path
		Path pathTest = Paths.get(System.getProperty("user.dir")+ "/src/usps-4-9-test.csv"); // Get path
		//Parse training data
		MatrixPac training = new MatrixPac();
		MatrixPac testing = new MatrixPac();
		
		NumRec rec = new NumRec();
		
		try {
			training = parseCSV(pathTrain.toString());
			testing = parseCSV(pathTest.toString());
			
			rec.recordIterationAccuracy(training, testing, 0.001, 0);
			
			/*
			
			for(double i = 0.01; i >= 0.001; i-=0.001){
			      rec.trainData(training, i, 0);
			      System.out.println(Arrays.toString(rec.w.getArray()[0]));
			      double accuracy = rec.testData(training, 0); 
			      System.out.println("Training: " + accuracy*100 + "%");
			      
			      accuracy = rec.testData(testing, 0); 
			      System.out.println("Training: " + accuracy*100 + "%");
			}
			*/
		}catch (IOException e) {
			e.printStackTrace();
		}
		//Pass results into the training data function
		//Parse Testing data
		//Pass results into the testing data function
		
		
	}
	
	public static MatrixPac parseCSV(String path) throws IOException{
		MatrixPac data = new MatrixPac();
		int numberOfLines = 0;
		//need to read through and find out number of lines to initialize 2D array.
		try {
			FileReader file_to_read = new FileReader(path);
			BufferedReader bf = new BufferedReader(file_to_read);
		
			String line;
		
			while((line = bf.readLine()) != null){
				line.length();
				numberOfLines++;
			}
			bf.close();
		
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		data.x_values = new double[numberOfLines][256];
		data.y_values = new double[numberOfLines];
		String line = "";
		String splitBy = ",";
		
		try{
			BufferedReader br = new BufferedReader(new FileReader(path));
			int j = 0;
			while((line = br.readLine()) != null){
				String[] oneLine = line.split(splitBy);
				for(int i = 0; i< 256; i++){
					data.x_values[j][i] = Double.parseDouble(oneLine[i]);
				}
				data.y_values[j] = Double.parseDouble(oneLine[256]);
				j++;
			}
			br.close();
			
		}catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return data;
	}

}
