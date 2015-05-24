package hw5;

import java.util.Random;
import java.util.ArrayList;

public class Kmeans {
	public double [][] data;
	public int k;
	//public double [][] clusters;
	public double [] centroids;
	public ArrayList<Integer> clusters[];
	//public ArrayList<Integer> clusters[];
	
	public Kmeans(double [][] source, int num){
		this.data = source;
		this.k = num;	
		//ArrayList<Integer> clusters[] = new ArrayList[this.k];
		Random rand = new Random();
		for(int i = 0; i < this.k; i++){
			//this.clusters[i] = new ArrayList<Integer>();
			int point = rand.nextInt(1400);
			//random xi's 
			//calculate their center
			currentCentroid(point);
		}
		
		//assign to cluster
		assignCluster();
	}
	
	public void printCluster(){
		for(int n = 0; n < this.k; n++){
			for(int j=0; j < this.clusters[n].size(); j++){
				int instance = this.clusters[n].get(j);
				System.out.printf("Instance %d -> Cluster %d", instance, n);
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
		for(int j=0; j < p.size(); j++){
			c += this.data[p.get(j)][j];
		}
		c = c/this.k;
		this.centroids[clustNum] = c;
	}
	
	//calculate center for beginning random "instances"
	private void currentCentroid(int p){
			double c=0;
			for(int j=0; j < this.data[0].length; j++){
				c += this.data[p][j];
			}
			c = c/this.k;
			this.centroids[p] = c;
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
