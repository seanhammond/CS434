package assignment3;

public class DecisionTree {
	
	Node root;
	
	public class Node {
		boolean isLeaf; //used for when selecting root
		int feat; //x1, x2, ..., x6 
		int attr; 
		Node[] children;
		
	}
	
	//takes set of training examples
	public int chooseRoot(MatrixPac p){
		int bestAttr = -1;
		double bestGain = -1;
		double[][] count = new double[p.x_values[0].length][2];
		double[] ent = new double[p.x_values[0].length];
		double gain = 0;
		
		//loop to test each feature
		for(int i = 0;i < p.x_values[0].length;i++){

			//loop to test each example
			for(int j = 0; j < p.x_values.length;j++){
				if(p.x_values[i][j] == 1)
					count[i][0]++;
				if(p.x_values[i][j] == 2)
					count[i][1]++;
			}
			ent[i] = calcEntropy(count[i][0],count[i][1]);
			//gain equation is correct, need to change to summation of remaining attr
			gain = ent[i] - ((count[i][0] + count[i][1]) / p.x_values[0].length) * calcEntropy(count[i][0], count[i][1]);
			if (gain > bestGain){
				bestGain = gain;
				bestAttr = i;
			}
			
		}
		
		return bestAttr;
	}
	
	public static double calcEntropy(double a, double b){
		double entropy = 0.0;
		
		if (a == 0 || b == 0)
			return entropy;
		else
			entropy = -1.0 * ( ((a/(a+b)) * Math.log(a/(a+b))) + ( (b/(a+b)) * Math.log(b/(a+b)))) / Math.log(2);
		
		return entropy;
	}
	
}
