package hw5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import Jama.Matrix;

@SuppressWarnings("unused")
public class Kmeans {
	public double[][] data;
	public int k;
	public double[][] centroids;
	public ArrayList<ArrayList<Integer>> clusters;
	public Boolean convergence;
	
	private static final double TOL = 0.00001;

	public Kmeans(double[][] source, int num) {
		data = source;
		k = num;
		centroids = new double[k][data[0].length];
		convergence = false;
		clusters = new ArrayList<ArrayList<Integer>>(k);
	}
	
	public void compute(){
		
		setStartingCentroids();
		int z = 0;
		while (!convergence) {
		
			//System.out.printf("loop " + z + "\n");
			
			// save current centers
			double currentCenters[][] = new double[k][data[0].length];
			
			for(int i = 0; i < centroids.length; i++){
				currentCenters[i] = centroids[i].clone();
			}
			
			// assign to cluster
			assignCluster();

			
			// calculate new centers
			for (int n = 0; n < k; n++) {
				clusterCentroid(clusters.get(n), n);
			}
			
			//assignCluster();
			// check if centers have changed, meaning no moving of points.
			int sameness = 0;
			for (int n = 0; n < k; n++) {
				boolean good = true;
				for(int i = 0; i < centroids[n].length; i++){
					if (!(currentCenters[n][i] <= (centroids[n][i] + TOL) && currentCenters[n][i] >= (centroids[n][i] - TOL))) {
						good = false;
					}
				}
				
				if(good)
					sameness++;
				
			}
			if (sameness == k) {
				convergence = true;
			}
			z++;
			if(z > 200){
				break;
			}
		}
	}

	private void setStartingCentroids() {
		Random rand = new Random();
		int point = rand.nextInt(data.length);
		startingCentroid(point, 0);
		
		int[] points = new int[k];
		points[0] = point;
		for (int i = 1; i < k; i++) {
			int[] subPoints = new int[i+1];
			for(int j = 0; j < i; j++){
				subPoints[j] = points[j];
			}
			
			point = rand.nextInt(data.length);
			points[i] = point;
			startingCentroid(point, i);
		}
	}
	
	private int getIndexOfFarthestPoint(int[] point){
		double largestDistance = 0;
		int farthestPoint = 0;
		for(int i = 0; i < point.length; i++){
			largestDistance += Math.pow(distance(data[point[i]],data[0]), 2.0);
		}
		
		
		for(int i = 1; i < data.length; i++){
			double d = 0;
			for(int j = 0; j < point.length; j++){
				d = Math.pow(distance(data[i],data[point[j]]),2.0);
			}
			
			if(d > largestDistance){
				farthestPoint = i;
				largestDistance = d;
			}
		}
		return farthestPoint;
	}

	public double clusterSSE(){
		double sse = 0;
		for(int i = 0; i < clusters.size(); i++){
			for(Integer pointIndex : clusters.get(i)){
				double[] pointArray = data[pointIndex.intValue()];
				sse += Math.pow(distance(pointArray, centroids[i]),2.0);
				
			}
			
		}
		return sse;
	}
	
	private void assignCluster() {
		// clear clusters from before to reassign
		// otherwise will have to remove points by checking if they are already
		// in a cluster.
		clusters = new ArrayList<ArrayList<Integer>>(k);
		for(int i = 0; i < k; i++){
			clusters.add(new ArrayList<Integer>());
		
		}
		
		for (int p = 0; p < data.length; p++) {
			double best = distance(centroids[0],data[p]);
			int clusterNum = 0;
			for (int m = 1; m < k; m++) {
				double dif = distance(centroids[m],data[p]);
				if (dif < best) {
					best = dif;
					clusterNum = m;
				}
			}
			
			clusters.get(clusterNum).add(new Integer(p));
		}

	}
	
	private double distance(double[] mu, double[] x){
		double dist = 0;
		
		for(int i = 0; i < mu.length; i++){
			dist += Math.pow(mu[i]-x[i], 2);
		}
		
		return Math.sqrt(dist);
		
	}

	// update centers for clusters
	private void clusterCentroid(ArrayList<Integer> p, int clustNum) {
		double[] sum = new double[data[0].length];
		for(Integer pointIndex : p){
			for(int i = 0; i < data[pointIndex.intValue()].length; i++){
				sum[i] += data[pointIndex][i];
			}
		}
		
		for(int i = 0; i < sum.length; i++){
			sum[i] /= (float)p.size();
		}
		
		centroids[clustNum] = sum;
	}

	// calculate center for beginning random "instances"
	private void startingCentroid(int p, int clusterNum) {
		centroids[clusterNum] = data[p];
	}
	
	//Confusion matrix is a Kx2 matrix
	public int[][] confusionMatrix(double[] yValues){
		int[][] matrix = new int[k][2];
		
		for(int i = 0; i < k; i++){
			for(int j = 0; j < 2; j++){
				matrix[i][j] = 0;
			}
		}
		
		for(int i = 0; i < clusters.size(); i++){
			for(Integer pointIndex : clusters.get(i)){
				if(yValues[pointIndex.intValue()] == 0){
					matrix[i][0]++;
				} else {
					matrix[i][1]++;
				}
			}
		}
		return matrix;
	}
	
	public double clusterPurity(double[] yValues){
		double totalCorrect = 0;
		
		for(int i = 0; i < clusters.size(); i++){
			int class0 = 0;
			int class1 = 0;
			for(Integer pointIndex : clusters.get(i)){
				if(yValues[pointIndex.intValue()] == 0){
					class0++;
				} else {
					class1++;
				}
				
			}
			totalCorrect += (class0 > class1) ? class0 : class1;
		}
		
		return (1.0/((float)yValues.length)) * totalCorrect;
		
		
	}
	
	public static class Eigen implements Comparable<Eigen>{
		public double value;
		public double[] vector;
		public int featureValue;
		public Eigen(double value, double[] vector,int featureValue){
			this.value = value;
			this.vector = vector;
			this.featureValue = featureValue;
		}
		
		@Override
		public int compareTo(Eigen e) {
			if(e.value > this.value){
				return 1;
			}
			else if(e.value < this.value){
				return -1;
			} 
			return 0;
		}
	}
	
	private void printDim(Matrix m)throws Exception, FileNotFoundException, UnsupportedEncodingException{
		PrintWriter writer = new PrintWriter("kmeans_dim.txt", "UTF-8");
		writer.println(m.getRowDimension() +"x"+ m.getColumnDimension());
		writer.close();
		System.out.println(m.getRowDimension() +"x"+ m.getColumnDimension());
		
	}
	
	public static double[][] reducePCAData(double[][] data){
		double[] center = new double[data[0].length];
		
		for(int i = 0; i < data[0].length; i++){
			center[i] = 0;
		}
		for(int i = 0; i < data.length; i++){
			for(int j = 0; j < data[0].length; j++){
				center[j] += data[i][j];
			}
		}
		for(int i = 0; i < data[0].length; i++){
			center[i] /= (float)data.length;
		}
		
		Matrix covarianceMatrix = new Matrix(center.length,center.length);
		Matrix centerMatrix = new Matrix(center,1);
		for(int i = 0; i < data.length; i++){
			Matrix e = (new Matrix(data[i],1)).minus(centerMatrix);
			Matrix squared = e.transpose().times(e);
			covarianceMatrix = covarianceMatrix.plus(squared);
		}
		covarianceMatrix = covarianceMatrix.times(1.0/((float)data.length));
		
		double[] eigenValues = covarianceMatrix.eig().getRealEigenvalues();
		double[][] eigenVectors = covarianceMatrix.eig().getV().getArray();
		
		Eigen[] eigens = new Eigen[eigenValues.length];
		for(int i = 0; i < eigenValues.length; i++){
			eigens[i] = new Eigen(eigenValues[i],eigenVectors[i],i);
		}
		
		Arrays.sort(eigens);

		int d = data[0].length;
		int newD = d;
		
		for(int i = d; i > 0; i--){
			if(infoRetained(eigens,d,newD) > 0.8){
				newD = i;
			} else {
				break;
			}
		}

		for(int i = 0; i < (d-newD);i++){
			data = removeFeature(eigens[i], data);
		}
		System.out.println("Removed " + (d-newD) + " dims");
		System.out.println("DONE!");	
		return data;
	}
	
	private static ArrayList<ArrayList<Double>> pointsToArrayList(double[][] data){
		ArrayList<ArrayList<Double>> lists = new ArrayList<ArrayList<Double>>();
		for(int i = 0; i < data.length; i++){
			ArrayList<Double> value = new ArrayList<Double>();
			for(int j = 0; j < data[i].length; j++){
				value.add(new Double(data[i][j]));
				
			}
			lists.add(value);
		}
		return lists;
	}
	
	private static double[][] arrayListToPoints(ArrayList<ArrayList<Double>> lists){
		double[][] points = new double[lists.size()][lists.get(0).size()];
		for(int i = 0; i < lists.size(); i++){
			for(int j = 0; j < lists.get(i).size(); j++){
				points[i][j] = lists.get(i).get(j).doubleValue();
			}
		}
		return points;
	}
	
	private static double[][] removeFeature(Eigen e, double[][] data){
		ArrayList<ArrayList<Double>> dataList = pointsToArrayList(data);
		for(ArrayList<Double> value : dataList){
			value.remove(e.featureValue);
		}
		return arrayListToPoints(dataList);
	}
	
	private static double infoRetained(Eigen[] eigens, int d, int newD){
		double sumD = 0;
		double sumNewD = 0;
		
		for(int i = 0; i < d; i++){
			sumD += eigens[i].value;
		}
		
		for(int i = 0; i < newD; i++){
			sumNewD += eigens[i].value;
		}
		return (sumNewD/sumD);
	}

}
