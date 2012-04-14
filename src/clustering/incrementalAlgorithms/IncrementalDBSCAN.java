package clustering.incrementalAlgorithms;

import java.util.ArrayList;

import clustering.algorithms.Cluster;

import datasets.DatasetPoint;

public class IncrementalDBSCAN {
	
	private ArrayList<DatasetPoint> dataset;
	private ArrayList<Cluster> clustersList;
	private int minPts;
	private double eps;
	
	public IncrementalDBSCAN(ArrayList<DatasetPoint> dataset, int minpts, double eps) {
		this.dataset = dataset;
		this.minPts = minpts;
		this.eps = eps;
		this.clustersList = new ArrayList<Cluster>();
	}
	
	public void clusterPoint(DatasetPoint point){
		ArrayList<Integer> updSeedPointIndexs = getUpdSeedSet(point);
		if(updSeedPointIndexs.size()==0){
			 markAsNoise(point);
		}else if(updSeedContainsCorePointsWithNoCluster(updSeedPointIndexs)){
			createCluster(point, updSeedPointIndexs);
		}else if(updSeedContainsCorePointsFromOneCluster(updSeedPointIndexs)){
			joinCluster(point, updSeedPointIndexs);
		}else if(updSeedContainsCoreObjectsFromMultipleClusters(updSeedPointIndexs)){
			mergeClusters(point, updSeedPointIndexs);
		}
	}
	
	public ArrayList<Integer> getUpdSeedSet(DatasetPoint point){
		ArrayList<Integer> updSeedIndex = new ArrayList<Integer>();
		
		return updSeedIndex;
	}

	public boolean updSeedContainsCorePointsWithNoCluster(ArrayList<Integer> indexs){
		return false;
	}
	
	public boolean updSeedContainsCorePointsFromOneCluster(ArrayList<Integer> indexs){
		return false;
	}
	
	public boolean updSeedContainsCoreObjectsFromMultipleClusters(ArrayList<Integer> indexs){
		return false;
	}
	
	
	public void mergeClusters(DatasetPoint point,ArrayList<Integer> indexs){
		
	}
	
	public void joinCluster(DatasetPoint point, ArrayList<Integer> indexs){
		
	}
	
	public void markAsNoise(DatasetPoint point){
		
	}
	
	public void createCluster(DatasetPoint point, ArrayList<Integer> seedPoints){
		
	}
	
	
	

}
