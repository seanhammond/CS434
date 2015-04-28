package assignment3;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class DecisionTree {
	
	Node root;
	final int LARGEST_ATTRIBUTE = 4;
	final int NUM_ATTR = 6;
	List<Integer> usedAttr = new ArrayList<Integer>();
	
	public class Node {
		public Node(int[] i, double j) {
			this.attr = i[0];
			this.value = i[1];
			this.children = new Node[2];
			this.theGain = j;
		}
		public Node(double yClass){
			this.yClass = yClass;
		}
		double yClass; //If the node is a leaf, it has the associated y value
		boolean isLeaf = false; //used for when selecting root
		int attr; 
		int value;
		double theGain;
		Node[] children;
		
	}
	
	private class SplitReturn{
		MatrixPac positiveBranch;
		MatrixPac negativeBranch;
		
		public SplitReturn(MatrixPac pos, MatrixPac neg){
			positiveBranch = pos;
			negativeBranch = neg;
		}
	}
	
	//takes set of training examples
	public Node chooseRoot(MatrixPac p) throws FileNotFoundException, UnsupportedEncodingException{
		int bestAttr = -1;
		int bestValue = 1;
		double bestGain = -1;
		double[][] count = new double[p.x_values[0].length][LARGEST_ATTRIBUTE]; //[number of attr][3]
		double gain = 0;
		
		PrintWriter writer = new PrintWriter("testGain.txt", "UTF-8");
		
		double baseEntropy = getEntropy(p);
		
		//loop to test each feature
		for(int i = 0;i < p.x_values[0].length;i++){

			//loop to test each example
			for(int j = 0; j < p.x_values.length;j++){
				if(p.x_values[j][i] == 1)
					count[i][0]++;
				if(p.x_values[j][i] == 2)
					count[i][1]++;
				if(p.x_values[j][i] == 3)
					count[i][2]++;
				if(p.x_values[j][i] == 4)
					count[i][3]++;
			}
			
			
			//For each possible choice for the attribute
			for(int k = 0; k < LARGEST_ATTRIBUTE; k ++){
				if(count[i][k] == 0 ){ //This is not a valid option, either a binary property or it has already been assigned
					continue;
				}
				
				//Get count for True
				double trueCount = count[i][k];
				
				//Get count for False
				double falseCount = p.x_values.length - trueCount;

				double p1 = trueCount/p.x_values.length;
				double p2 = falseCount/p.x_values.length;
				
				double positive = 0;
				double negative = 0;
				double otherPositive = 0; //For attributes != k
				double otherNegative = 0;
				for(int l = 0; l < p.x_values.length;l++){
					if(p.x_values[l][i] == k+1){ //if it is the value we are looking for
						if(p.y_values[l] > 0){
							positive++;
						} else {
							negative++;
						}
					} else {
						if(p.y_values[l] > 0){
							otherPositive++;
						} else {
							otherNegative++;
						}
					}
				}
				
				double hs1 = calcEntropy(positive,negative);
				double hs2 = calcEntropy(otherPositive, otherNegative);
				
						//H(S) - (p1 H(S1) + p2 H(S2))
				gain =  baseEntropy - (p1*hs1 + p2*hs2 );
				//i is from 0 to 5, we want 1 to 6
				writer.println("Gain for test x" + (i+1) + ": " + gain + " with value: " + (k+1));
				if (gain > bestGain){
					bestGain = gain;
					bestAttr = i+1;  //i is from 0..5, we want actual value 1..6
					bestValue = k+1; //k is from 0..2, we want actual value 1..3
				}
				
			}
		
		}
		writer.close();
		System.out.println("Best attribute: x" + bestAttr + " with value: " + bestValue + " with information gain: " + bestGain);
		int[] best = new int[2];
		
		best[0] = bestAttr;
		best[1] = bestValue;
		Node best2 = new Node(best, bestGain);
		return best2;
	}
	
	public Node expandTree(MatrixPac p) throws FileNotFoundException, UnsupportedEncodingException{
		Node n = (chooseRoot(p));
		SplitReturn splitR = split(p, n.attr, n.value); //Split the values into True and False groups
		
		if(getEntropy(splitR.positiveBranch) != 0){ //Still some uncertainty, create True branch node
			n.children[0] = expandTree(splitR.positiveBranch);
			
		} else { //Create leaf node to represent the y value if 100% certain
			double yClass = splitR.positiveBranch.y_values[0];
			n.children[0] = new Node(yClass);
			n.children[0].isLeaf = true;
		}
		
		if(getEntropy(splitR.negativeBranch) != 0){ //Still some uncertainty, create False branch node
			n.children[1] = expandTree(splitR.negativeBranch);
			
		} else { //Create leaf node to represent the y value if 100% certain
			double yClass = splitR.negativeBranch.y_values[0];
			n.children[1] = new Node(yClass);
			n.children[1].isLeaf = true;
		}

		return n;
		
	}
	
	//Calculates the given entropy of a set of y values
	public double getEntropy(MatrixPac p){
		double positive = 0;
		double negative = 0;
		for(int i = 0; i < p.y_values.length; i++){
			if(p.y_values[i] > 0){
				positive++;
			} else {
				negative++;
			}
		}
		
		return calcEntropy(positive,negative);

	}
	
	//Builds DT starting with the root node
	public void growTree(MatrixPac p) throws FileNotFoundException, UnsupportedEncodingException{
		root = expandTree(p);
	}
	
	//Splits the MatrixPac into two branches, positive and negative
	public SplitReturn split(MatrixPac p, int attr, int val){
		List<Integer> posRows = new ArrayList<Integer>();
		List<Integer> negRows = new ArrayList<Integer>();
		MatrixPac posBranch = new MatrixPac();
		MatrixPac negBranch = new MatrixPac();

		
		for(int j = 0; j < p.x_values.length;j++){
			
			if(p.x_values[j][attr-1] == val){
				//add to posBranch
				posRows.add(j);
			}else{
				//add to negBranch
				negRows.add(j);
			}
			
		}
		
		posBranch.x_values = new double[posRows.size()][p.x_values[0].length];
		posBranch.y_values = new double[posRows.size()];
		
		for(int i = 0; i < posRows.size(); i++){
			System.arraycopy(p.x_values[posRows.get(i)], 0 , posBranch.x_values[i], 0, p.x_values[0].length);
			posBranch.y_values[i] = p.y_values[posRows.get(i)];
		}
		
		negBranch.x_values = new double[negRows.size()][p.x_values[0].length];
		negBranch.y_values = new double[negRows.size()];
		
		for(int i = 0; i < negRows.size(); i++){
			System.arraycopy(p.x_values[negRows.get(i)], 0 , negBranch.x_values[i], 0, p.x_values[0].length);
			negBranch.y_values[i] = p.y_values[negRows.get(i)];
		}
		
		return new SplitReturn(posBranch,negBranch);
	}
	
	
	public static double calcEntropy(double a, double b){
		double entropy = 0.0;
		
		double aE = 0.0, bE = 0.0;
		
		if(a != 0) {
			aE =  (a/(a+b)) * log2(a/(a+b));
		}
		
		if(b != 0){
			bE =  (b/(a+b)) * log2(b/(a+b));
		}
		
		entropy = -1.0 * (aE + bE);

		return entropy;
	}
	
	static double log2(double x){
		return (Math.log(x) / Math.log(2.));
	}
	
	public double dataError(MatrixPac p){
		double wrong = 0.0;
		for(int i = 0; i < p.x_values.length; i++){
			double testResult = getDecision(root,p.x_values[i]);
			if(testResult != p.y_values[i]){
				wrong++;
			}
		}
		return (double)wrong/(double)p.x_values.length;
	}
	
	public double getDecision(Node node, double[] x_value){
		
		if(node == null){
			System.out.println("ERROR, no node found");
			return 0;
		}
		
		if(node.isLeaf){
			return node.yClass;
		} else {
			if(x_value[node.attr-1] == node.value){
				return getDecision(node.children[0], x_value); //Send down true path
			} else {
				return getDecision(node.children[1], x_value); //Send down false path
			}
		}
	}
}
