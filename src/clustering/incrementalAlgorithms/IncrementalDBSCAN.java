package clustering.incrementalAlgorithms;

import java.util.ArrayList;

import clustering.algorithms.Cluster;

import datasets.DatasetPoint;

public class IncrementalDBSCAN {
	
	private ArrayList<DatasetPoint> dataset;

	public IncrementalDBSCAN(ArrayList<DatasetPoint> dataset) {
		this.dataset = dataset;
	}
	
	public void clusterPoint(DatasetPoint point){
		
	}
	
	public ArrayList<Integer> getUpdSeedSet(DatasetPoint point){
		ArrayList<Integer> updSeedIndex = new ArrayList<Integer>();
		
		return updSeedIndex;
	}
	
	public void mergeTwoClusters(DatasetPoint point,Cluster c1, Cluster c2){
		
	}
	
	public void joinCluster(DatasetPoint point, Cluster c){
		
	}
	
	public void markAsNoise(DatasetPoint point){
		
	}
	
	public void createCluster(DatasetPoint point, ArrayList<DatasetPoint> seedPoints){
		
	}
	
	
	

}
