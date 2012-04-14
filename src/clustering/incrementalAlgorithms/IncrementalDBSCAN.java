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
	
	/**
	 * Get the updSeed set of a datapoint
	 * @param point point
	 * @return updSeed set
	 */
	public ArrayList<Integer> getUpdSeedSet(DatasetPoint point){
		ArrayList<Integer> updSeedIndex = new ArrayList<Integer>();
		for (int i = 0; i < this.dataset.size(); i++) {
			DatasetPoint p = this.dataset.get(i);
			if(!p.getIsVisited()) break;
			double distance = calculateDistanceBtwTwoPoints(point, p);
			if(distance > this.eps) continue;
			point.addToNeighborhoodPoints(p.getID());
			p.addToNeighborhoodPoints(point.getID());
			if(p.getPointsAtEpsIndexs().size() == this.minPts){
				 p.setPointCausedToBeCore(point.getID());
				 if(!p.getAssignedCluster().equalsIgnoreCase("")) updSeedIndex.add(p.getID());
				 continue;
			}
			if(p.getIsCore(this.minPts)) updSeedIndex.add(p.getID());
		}
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
	
	/**
	 * calculate the Euclidean Distance between two points
	 * @param p1 point 1
	 * @param p2 point 2
	 * @return Distance
	 */
	public double calculateDistanceBtwTwoPoints(DatasetPoint p1, DatasetPoint p2){
		double xDiff = p1.getX() - p2.getX();
		double yDiff = p1.getY() - p2.getY();
		return Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
	}

	
	

}
