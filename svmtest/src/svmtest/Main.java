package svmtest;

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
//import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instances;
import weka.core.Instance;
import weka.core.RevisionUtils;
import weka.core.SparseInstance;
import weka.core.converters.SVMLightLoader;
import java.io.File;



@SuppressWarnings("unused")
public class Main {
	

	public static void main(String args[]) throws Exception, FileNotFoundException, UnsupportedEncodingException{
		
		//Create paths for data files
		Path pathTrain = Paths.get(System.getProperty("user.dir")+ "/src/Day0_30.svm"); // Get path
		
		SVMLightLoader file1 = new SVMLightLoader();
		File fp = new File(pathTrain.toString());
		
		file1.setFile(fp);
		Instances data = new Instances(file1.getDataSet());	
		//file1.getDataSet();
	}
	


}
