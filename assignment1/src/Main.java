import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Main {

	public static void main(String args[]) {

		// Parse housing_train.txt into List
		Path pathTrain = Paths.get(System.getProperty("user.dir")
				+ "/src/housing_train.txt"); // Get path
		Path pathTest = Paths.get(System.getProperty("user.dir")
				+ "/src/housing_test.txt"); // Get path

		List<ArrayList<Double>> dataset = loadFile(pathTrain);
		List<ArrayList<Double>> testSet = loadFile(pathTest);

		// System.out.println(dataset);

		BostonSuburbs suburbs = new BostonSuburbs();
		

		try {

			suburbs.train(dataset, 0,true); // lambda 0
			
			PrintWriter writer = new PrintWriter("W.txt", "UTF-8");
			writer.println(Arrays.toString(suburbs.w.transpose().getArray()[0]));
			writer.close();

			writer = new PrintWriter("SSE.txt", "UTF-8");
			suburbs.train(dataset, 0,true);
			writer.println(suburbs.evaluateSSE(testSet,true));
			writer.close();

			//Without DUmmy
			suburbs.train(dataset, 0, false); // lambda 0
			
			writer = new PrintWriter("W_NoDummy.txt", "UTF-8");
			writer.println(Arrays.toString(suburbs.w.transpose().getArray()[0]));
			writer.close();

			writer = new PrintWriter("SSE_NoDummy.txt", "UTF-8");
			suburbs.train(dataset, 0, false);
			writer.println(suburbs.evaluateSSE(testSet,false));
			writer.close();
			
			
			writer = new PrintWriter("lambda_vs_SSE.txt", "UTF-8");
			for (float i = 0; i <= 5; i+=0.01) {

				suburbs.train(dataset, i,true);
				writer.println(i + " " + suburbs.evaluateSSE(testSet,true));
				
			}
			writer.close();
			
			
			PrintWriter fp = new PrintWriter("W_vs_lambda.txt", "UTF-8");
			//Lambda vs W comparisons
			for (int i = 0; i <= 5; i++) {
				suburbs.train(dataset, i,true);
				fp.println(i + " "
						+ Arrays.toString(suburbs.w.transpose().getArray()[0]));
			}

			fp.close();

		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//Test singular house value return
		suburbs.train(dataset, 0.21, true);
		Double[] testData = {0.84054, 0.00, 8.140, 0.0, 0.5380, 5.5990, 85.70, 4.4546, 4.0, 307.0, 21.00, 303.42, 16.51};
		ArrayList<Double> testHouse = new ArrayList<Double>(Arrays.asList(testData));
		
		
		System.out.println("House value: " + suburbs.getHouseProperty(testHouse));
		System.out.println("Done!");
	}

	public static List<ArrayList<Double>> loadFile(Path path) {

		List<ArrayList<Double>> dataset = new ArrayList<ArrayList<Double>>();

		try {

			Object[] lines = Files.lines(path).toArray();

			for (Object line : lines) {

				String[] stringData = ((String) line).split(" "); // Convert
																	// line into
																	// list
				ArrayList<Double> data = new ArrayList<Double>();

				for (int i = 0; i < stringData.length; i++) { // Convert every
																// entry into
																// Double

					if (stringData[i].equals(" ") || stringData[i].equals("")) {
						continue;
					}

					// Parse string into Double
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
