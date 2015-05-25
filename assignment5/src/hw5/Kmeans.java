package hw5;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class Kmeans {
	public double[][] data;
	public int k;
	public double[][] centroids;
	public ArrayList<ArrayList<ArrayList<Double>>> clusters;
	public Boolean convergence;

	@SuppressWarnings("unchecked")
	public Kmeans(double[][] source, int num) {
		this.data = source;
		this.k = num;
		this.centroids = new double[this.k][this.data[0].length];
		this.convergence = false;
		this.clusters = new ArrayList<ArrayList<ArrayList<Double>>>(this.k);
		
		Random rand = new Random();
		for (int i = 0; i < this.k; i++) {
			int point = rand.nextInt(this.data.length);
			// random xi's
			// calculate their center
			// System.out.printf("random points are %d\n", point);
			startingCentroid(point, i);
		}

		while (this.convergence == false) {
			int z = 0;
			System.out.printf("loop %d\n", z);
			// assign to cluster
			assignCluster();

			// save current centers
			double currentCenters[][] = this.centroids;
			// calculate new centers
			for (int n = 0; n < this.k; n++) {
				clusterCentroid(this.clusters.get(n), n);
			}
			// check if centers have changed, meaning no moving of points.
			int sameness = 0;
			for (int n = 0; n < this.k; n++) {
				if (currentCenters[n] == this.centroids[n]) {
					sameness++;
				}
			}
			if (sameness == this.k) {
				this.convergence = true;
			}
			z++;
		}
	}


	private void assignCluster() {
		// clear clusters from before to reassign
		// otherwise will have to remove points by checking if they are already
		// in a cluster.
		this.clusters = new ArrayList<ArrayList<ArrayList<Double>>>(this.k);
		for(int i = 0; i < this.k; i++){
			this.clusters.add(new ArrayList<ArrayList<Double>>());
		
		}
		
		for (int p = 0; p < this.data.length; p++) {
			double best = distance(this.centroids[0],this.data[p]);
			int clusterNum = 0;
			for (int m = 1; m < this.k; m++) {
				double dif = distance(this.centroids[m],this.data[p]);
				if (dif < best) {
					best = dif;
					clusterNum = m;
				}
			}
			
			this.clusters.get(clusterNum).add(pointToList(this.data[p]));
		}

	}
	
	private ArrayList<Double> pointToList(double[] point){
		ArrayList<Double> list = new ArrayList<Double>();
		
		for(int i = 0; i < point.length; i++){
			list.add(new Double(point[i]));
		}
		
		return list;
	}
	
	private double distance(double[] mu, double[] x){
		double dist = 0;
		
		for(int i = 0; i < mu.length; i++){
			dist += Math.pow(mu[i]-x[i], 2);
		}
		
		return Math.sqrt(dist);
		
	}

	// update centers for clusters
	private void clusterCentroid(ArrayList<ArrayList<Double>> p, int clustNum) {
		double[] sum = new double[this.data[0].length];
		for(ArrayList<Double> point : p){
			for(int i = 0; i < point.size(); i++){
				sum[i] += point.get(i).doubleValue();
			}
		}
		
		for(int i = 0; i < sum.length; i++){
			sum[i] /= (float)p.size();
		}
		
		this.centroids[clustNum] = sum;
	}

	// calculate center for beginning random "instances"
	private void startingCentroid(int p, int clusterNum) {
		this.centroids[clusterNum] = this.data[p];
	}

}
