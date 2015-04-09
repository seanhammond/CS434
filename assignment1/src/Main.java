import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class Main {
	
	
	public static void main(String args[]){
		
		//Parse housing_train.txt into List
		Path pathTrain = Paths.get(System.getProperty("user.dir")+"/src/housing_train.txt"); //Get path
		Path pathTest = Paths.get(System.getProperty("user.dir")+"/src/housing_test.txt"); //Get path
		
		List<ArrayList<Double>> dataset = loadFile(pathTrain);
		List<ArrayList<Double>> testSet = loadFile(pathTest);
		
		//System.out.println(dataset);
		
		BostonSuburbs suburbs = new BostonSuburbs();
		
		try {
			PrintWriter writer = new PrintWriter("SuburbData.txt", "UTF-8");
			
			System.out.println("SSE:");
			for(float i = 0; i < 5; i+=0.01){
				suburbs.train(dataset, i);
				writer.println(i+ " " + suburbs.evaluateSSE(testSet));
			}
			
			writer.close();
			
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public static List<ArrayList<Double>> loadFile(Path path){
		
		List<ArrayList<Double>> dataset = new ArrayList<ArrayList<Double>>();
		
		try {
			
			Object[] lines = Files.lines(path).toArray();
			
			for(Object line : lines){
				
				String[] stringData = ((String)line).split(" "); //Convert line into list
				ArrayList<Double> data = new ArrayList<Double>();
				
				for( int i = 0; i < stringData.length; i++){ //Convert every entry into Double
					
					if(stringData[i].equals(" ") || stringData[i].equals("")){
						continue;
					}
					
					//Parse string into Double
					try {
						data.add(Double.parseDouble(stringData[i]));
					} catch (NumberFormatException nfe) {
						nfe.printStackTrace();
					}
					
				}
				
				dataset.add(data);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return dataset;
	
	}
}
