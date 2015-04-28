package assignment3;

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

public class Main {
	
	
	public static void main(String args[]) throws FileNotFoundException, UnsupportedEncodingException{
		
		Path pathTrain = Paths.get(System.getProperty("user.dir")+ "/src/knn_train.csv"); // Get path
		Path pathTest = Paths.get(System.getProperty("user.dir")+ "/src/knn_test.csv"); // Get path
		
		Path pathTrainD = Paths.get(System.getProperty("user.dir")+ "/src/monks-1-train.csv"); // Get path
		Path pathTestD = Paths.get(System.getProperty("user.dir")+ "/src/monks-1-test.csv"); // Get path
		
		KNN knn = new KNN();
		DecisionTree stump = new DecisionTree();
		DecisionTree dt = new DecisionTree();
		PrintWriter writer = new PrintWriter("PartTwoErrors.txt", "UTF-8");
		
		
		try {
			MatrixPac trainingData = parseCSVKNN(pathTrain.toString());
			MatrixPac testingData = parseCSVKNN(pathTest.toString());
			
			MatrixPac trainingDData = parseCSVD(pathTrainD.toString());
			MatrixPac testingDData = parseCSVD(pathTestD.toString());
			
			// KNN
			int bestK = knn.determineBestK(trainingData);
			
			
			writer = new PrintWriter("errorAnalysis.tsv", "UTF-8");
			writer.println("K\tValidationError\tTrain Error\tTest Error");
			for(int k = 1; k <= 205; k+=2){
				
				
				System.out.println("K: " + k);
				
				knn.K = k;
				System.out.println("Validation Erro: " + knn.getValidationError(trainingData));
				System.out.println("Train Error: " + knn.getError(trainingData, trainingData));
				System.out.println("Test Error: " + knn.getError(trainingData, testingData));
				writer.println(k + "\t" + knn.getValidationError(trainingData) + "\t" + knn.getError(trainingData, trainingData) + "\t" + knn.getError(trainingData, testingData));
			}
			
			System.out.println("\nBest K value: " + bestK);
			
			writer.close();
			
			
			
			//Decision Tree
			stump.growStump(trainingDData);
			System.out.println("\nStump:");
			stump.printTree(stump.root, 0);
			
			writer.println("Stump Training Error: " + stump.dataError(trainingDData));
			writer.println("Stump Testing Error: " + stump.dataError(testingDData));
			
			
			dt.growTree(trainingDData);
			writer.println("Tree Training Error: " + dt.dataError(trainingDData));
			writer.println("Tree Testing Error: " + dt.dataError(testingDData));
			
			
			System.out.println("\nDecision Tree:");
			dt.printTree(dt.root, 0);
			
			
			
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static MatrixPac parseCSVKNN(String path) throws IOException{
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
	
	public static MatrixPac parseCSVD(String path) throws IOException{
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
		
		data.x_values = new double[numberOfLines][6];
		data.y_values = new double[numberOfLines];
		String line = "";
		String splitBy = ",";
		
		try{
			BufferedReader br = new BufferedReader(new FileReader(path));
			int j = 0;
			while((line = br.readLine()) != null){
				String[] oneLine = line.split(splitBy);
				for(int i = 1; i< 7; i++){
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
