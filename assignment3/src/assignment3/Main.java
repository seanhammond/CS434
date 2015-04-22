package assignment3;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
	
	
	public static void main(String args[]){
		
		Path pathTrain = Paths.get(System.getProperty("user.dir")+ "/src/knn_train.csv"); // Get path
		Path pathTest = Paths.get(System.getProperty("user.dir")+ "/src/knn_test.csv"); // Get path
		
		
		KNN knn = new KNN();
		
		try {
			MatrixPac trainingData = parseCSV(pathTrain.toString());
			MatrixPac testingData = parseCSV(pathTest.toString());

			knn.determineBestK(trainingData);
			
		} catch (IOException e) {
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
		
		data.x_values = new double[numberOfLines][30];
		data.y_values = new double[numberOfLines];
		String line = "";
		String splitBy = ",";
		
		try{
			BufferedReader br = new BufferedReader(new FileReader(path));
			int j = 0;
			while((line = br.readLine()) != null){
				String[] oneLine = line.split(splitBy);
				for(int i = 1; i< 31; i++){
					data.x_values[j][i-1] = Double.parseDouble(oneLine[i]);
				}
				data.y_values[j] = Double.parseDouble(oneLine[0]);
				j++;
			}
			br.close();
			
		}catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return data;
	}

}
