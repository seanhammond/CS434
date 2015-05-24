package hw5;

import java.util.Random;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Iterator;

public class Kmeans {
	public double [][] data;
	public int k;
	public double [] centroids;
	public java.util.Set<Integer>[] clusters;
	public Boolean convergence;
	
	@SuppressWarnings("unchecked")
	public Kmeans(double [][] source, int num){
		this.data = source;
		this.k = num;
		this.centroids = new double[this.k];
		this.convergence = false;
		 java.util.Set<Integer>[] arr = new java.util.HashSet[this.k];
		 for(int m = 0; m < this.k; m++){
			  arr[m] = new java.util.HashSet<Integer>();		 
		  }
		this.clusters = arr;
		Random rand = new Random();
		for(int i = 0; i < this.k; i++){
			int point = rand.nextInt(1400);
			//random xi's 
			//calculate their center
			System.out.printf("random points are %d\n", point);
			startingCentroid(point, i);
		}
		
		while(this.convergence == false){
			//assign to cluster
			assignCluster();
		
			//save current centers 
			double currentCenters[] = this.centroids;
			//calculate new centers
			for(int n = 0; n < this.k; n++){
				clusterCentroid(this.clusters[n], n);
			}
			//check if centers have changed, meaning no moving of points.
			int sameness = 0;
			for(int n = 0; n < this.k; n++){
				if(currentCenters[n] == this.centroids[n]){
				sameness++;
			}
			}
			if(sameness==this.k){
				this.convergence = true;
			}
		}
	}
	
	public void printCluster(){
		
		for(int n = 0; n < this.clusters.length; n++){
			Iterator<Integer> iterator = this.clusters[n].iterator();
			while(iterator.hasNext()){	
			//for(int j=0; j < this.clusters[n].size(); j++){
				int instance = iterator.next();
				//int instance = this.clusters[n].get(j);
				System.out.printf("Instance %d -> Cluster %d\n", instance, n);
			}
			
					
		}
	}
	
	private void assignCluster(){
		
		for(int s=0; s < this.data.length; s++){
			double temp = tempCentroid(s);
			double best = 10000;
			int clusterNum = this.k+1;
			for(int m = 0; m < this.k; m++){
				double dif = Math.abs(this.centroids[m] - temp);
				if(dif < best){
					best = dif;
					clusterNum = m;
				}
			}
			this.clusters[clusterNum].add(s);
		}
		
	}
	
	//update centers for clusters
	private void clusterCentroid(java.util.Set<Integer> p, int clustNum){
		double c=0;
		for(Integer ind : p){
			for(int b = 0; b < this.data[0].length; b++){
				int index = (Integer) ind;
				System.out.printf("index b = %d\n", index);
				c += this.data[index][b];
			}
		}
		c = c/this.k;
		this.centroids[clustNum] = c;
	}
	
	//calculate center for beginning random "instances"
	private void startingCentroid(int p, int clusterNum){
			double c=0;
			for(int j=0; j < this.data[0].length; j++){
				c += this.data[p][j];
			}
			c = c/this.k;
			this.centroids[clusterNum] = c;
		}
	
	//calculate center for "instances" when determining cluster
	private double tempCentroid(int p){
		double c=0;
		for(int j=0; j < this.data[0].length; j++){
			 c += this.data[p][j];
		}
		c = c/this.k;
		return c;
	}
}
