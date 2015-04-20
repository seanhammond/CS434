package assignment2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
	
	public static void main(String args[]){
		
		//Create paths for data files
		Path pathTrain = Paths.get(System.getProperty("user.dir")+ "/src/usps-4-9-train.csv"); // Get path
		Path pathTest = Paths.get(System.getProperty("user.dir")+ "/src/usps-4-9-test.csv"); // Get path
		
		MatrixPac training = new MatrixPac();
		MatrixPac testing = new MatrixPac();
		
		NumRec rec = new NumRec();
		
		try {
			
			//Parse data files
			training = parseCSV(pathTrain.toString());
			testing = parseCSV(pathTest.toString());
			
			//Record iteration accuracy for both training and testing data for different learning rates
			for(double i = 0.0001; i <= 1; i*=10){
				System.out.println("Recording accuracy for learning rate: " + i);
				rec.recordIterationAccuracy(training, testing, 0.001, 0, ("iteration-accuracy-"+i+".tsv"));
			}
			
			//Record different lambda trends
			for(double i = Math.pow(10, -3); i <= Math.pow(10, 2); i *=10){
				System.out.println("Recording accuracy for lambda: " + i);
			    rec.recordIterationAccuracy(training, testing, 0.0001,i, ("lambda-"+i+".tsv"));
			}
			
			System.out.println("Done!");
			
		}catch (IOException e) {
			e.printStackTrace();
		}
		
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
