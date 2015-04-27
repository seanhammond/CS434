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
	MatrixPac posBranch;
	MatrixPac negBranch;
	List<Integer> usedAttr = new ArrayList<Integer>();
	
	public class Node {
		public Node(int[] i) {
			this.attr = i[0];
			this.value = i[1];
			this.children = new Node[2];
		}
		boolean isLeaf; //used for when selecting root
		int attr; 
		int value;
		Node[] children;
		
	}
	
	//takes set of training examples
	public int[] chooseRoot(MatrixPac p, List<Integer> usedAttr) throws FileNotFoundException, UnsupportedEncodingException{
		int bestAttr = -1;
		int bestValue = 1;
		double bestGain = -1;
		double[][] count = new double[p.x_values[0].length][LARGEST_ATTRIBUTE]; //[number of attr][3]
		double[] ent = new double[p.x_values[0].length];
		double gain = 0;
		
		double positive = 0;
		double negative = 0;
		for(int i = 0; i < p.y_values.length; i++){
			if(p.y_values[i] > 0){
				positive++;
			} else {
				negative++;
			}
		}
		PrintWriter writer = new PrintWriter("testGain.txt", "UTF-8");
		double baseEntropy = calcEntropy(positive,negative);
		
		System.out.println("Base Entropy H(S) = " + baseEntropy);
		
		//loop to test each feature
		for(int i = 0;i < p.x_values[0].length;i++){
			if(usedAttr.contains(i+1)){
				continue;
			}
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
			
			
			//ent[i] = calcEntropy(count[i][0],count[i][1]);
			
			//gain equation is correct, need to change to summation of remaining attr
			//H(S) - (p1 H(S1) + p2 H(S2))
			
			//For each possible choice for the attribute
			for(int k = 0; k < LARGEST_ATTRIBUTE; k ++){
				if(count[i][k] == 0 ){ //This is not a valid option, thus a binary attribute probably
					continue;
				}
				
				//Get count for True
				double trueCount = count[i][k];
				
				//Get count for False
				double falseCount = p.x_values.length - trueCount;

				double p1 = trueCount/p.x_values.length;
				double p2 = falseCount/p.x_values.length;
				
				positive = 0;
				negative = 0;
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
				
				gain =  baseEntropy - (p1*hs1 + p2*hs2 );
				writer.println("Gain for test x" + (i+1) + ": " + gain + " with value: " + (k+1));
				if (gain > bestGain){
					bestGain = gain;
					bestAttr = i+1;
					bestValue = k+1; //k is from 0..2, we want actual value 1..3
				}
				
			}
			
			//gain = ent[i] - ((count[i][0] + count[i][1]) / p.x_values[0].length) * calcEntropy(count[i][0], count[i][1]);
			//if (gain > bestGain){
			//	bestGain = gain;
			//	bestAttr = i;
			//}
		
		}
		writer.close();
		System.out.println("Best attribute: x" + bestAttr + " with value: " + bestValue + " with information gain: " + bestGain);
		int[] best = new int[2];
		best[0] = bestAttr;
		best[1] = bestValue;
		return best;
	}
	
	public Node expandTree(MatrixPac p) throws FileNotFoundException, UnsupportedEncodingException{
		Node n = new Node(chooseRoot(p, usedAttr));
		usedAttr.add(n.attr);
		split(p, n.attr, n.value);
		n.children[0] = expandTree(posBranch);
		n.children[1] = expandTree(negBranch);
		//need to add stopping condition
		//if(n.gain == 0 || n.gain < 0.000
		return n;
		
	}
	
	//Builds DT starting with the root node
	public void growTree(MatrixPac p) throws FileNotFoundException, UnsupportedEncodingException{
		root = expandTree(p);
		
	}
	
	//Splits the MatrixPac into two branches, positive and negative
	public void split(MatrixPac p, int attr, int val){
		List<Integer> posRows = new ArrayList<Integer>();
		List<Integer> negRows = new ArrayList<Integer>();
		posBranch = new MatrixPac();
		negBranch = new MatrixPac();

		
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
	}
	
	
	/*
	public static double calcEntropy(double a, double b){
		double entropy = 0.0;
		
		if (a == 0 || b == 0)
			return entropy;
		else
			entropy = -1.0 * ( ((a/(a+b)) * log2(a/(a+b))) + ( (b/(a+b)) * log2(b/(a+b)))) / log2(2);
		
		return entropy;
	}*/
	
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
}
