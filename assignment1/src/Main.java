import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Main {
	public static void main(String args[]){
		
		List<ArrayList<Float>> dataset = new ArrayList<ArrayList<Float>>();
		
		//Parse housing_train.txt into List
		try {
			Path path = Paths.get(System.getProperty("user.dir")+"/src/housing_train.txt"); //Get path
			Object[] lines = Files.lines(path).toArray();
			
			for(Object line : lines){
				
				String[] stringData = ((String)line).split(" "); //Convert line into list
				ArrayList<Float> data = new ArrayList<Float>();
				
				for( int i = 0; i < stringData.length; i++){ //Convert every entry into Float
					
					if(stringData[i].equals(" ") || stringData[i].equals("")){
						continue;
					}
					
					//Parse string into Float
					try {
						data.add(Float.parseFloat(stringData[i]));
					} catch (NumberFormatException nfe) {
						nfe.printStackTrace();
					}
					
				}
				
				dataset.add(data);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		System.out.println(dataset);
		
		BostonSuburbs suburbs = new BostonSuburbs();
		
		suburbs.train(dataset);
	}
}
