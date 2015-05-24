package hw5;

import java.io.PrintWriter;
import java.util.Random;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

@SuppressWarnings("unused")
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
			//System.out.printf("random points are %d\n", point);
			startingCentroid(point, i);
		}
		
		while(this.convergence == false){
			int z = 0;
			System.out.printf("loop %d\n", z);
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
			z++;
		}
	}
	
	public void printCluster()  throws Exception, FileNotFoundException, UnsupportedEncodingException{
		PrintWriter writer = new PrintWriter("kmeans.txt", "UTF-8");
		PrintWriter writer2 = new PrintWriter("kmeans.tsv", "UTF-8");
		for(int n = 0; n < this.clusters.length; n++){
			Iterator<Integer> iterator = this.clusters[n].iterator();
			while(iterator.hasNext()){	
			//for(int j=0; j < this.clusters[n].size(); j++){
				int instance = iterator.next();
				//int instance = this.clusters[n].get(j);
				writer.printf("Instance %d\tCluster %d\n", instance, n);
				writer2.printf("%d\tCluster %d\n", instance, n);
			}				
		}
		writer.close();
		writer2.close();
	}
	
	private void assignCluster(){
		//clear clusters from before to reassign
		//otherwise will have to remove points by checking if they are already in a cluster.
		for(int i = 0; i < this.k; i++){
			this.clusters[i].clear();
		}
		for(int s=0; s < this.data.length; s++){
			double temp = tempCentroid(s);
			double best = 1000000;
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
				//System.out.printf("index b = %d\n", index);
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
