package hw5;

import java.util.Random;
import java.util.ArrayList;

public class Kmeans {
	public double [][] data;
	public int k;
	//public double [][] clusters;
	public double [] centroids;
	public java.util.ArrayList<Integer>[] clusters;
	
	@SuppressWarnings("unchecked")
	public Kmeans(double [][] source, int num){
		this.data = source;
		this.k = num;
		this.centroids = new double[this.k];
		 java.util.ArrayList<Integer>[] arr = new java.util.ArrayList[this.k];
		 for(int m = 0; m < this.k; m++){
			  arr[m] = new java.util.ArrayList<Integer>();		 
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
		
		//assign to cluster
		assignCluster();
	}
	
	public void printCluster(){
		for(int n = 0; n < this.clusters.length; n++){
			for(int j=0; j < this.clusters[n].size(); j++){
				int instance = this.clusters[n].get(j);
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
		for(int n = 0; n < this.k; n++){
			clusterCentroid(this.clusters[n], n);
		}
	}
	
	//update centers for clusters
	private void clusterCentroid(ArrayList<Integer> p, int clustNum){
		double c=0;
		System.out.printf("p size = %d\n", p.size());
		System.out.printf("data size = %d\n", this.data[0].length);
		double thing = data[1399][0];
		double thing2 = data[1399][6];
		System.out.printf("data[1399][0] = %f\n", thing);
		System.out.printf("data[1399][6] = %f\n", thing2);
		for(int j=0; j < p.size(); j++){
			for(int b = 0; b < this.data[0].length; b++){
				int index = p.get(j);
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
