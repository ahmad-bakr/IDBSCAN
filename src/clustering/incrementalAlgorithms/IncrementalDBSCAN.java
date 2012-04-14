package clustering.incrementalAlgorithms;

import java.util.ArrayList;
import java.util.Hashtable;

import clustering.algorithms.Cluster;

import datasets.DatasetPoint;

public class IncrementalDBSCAN {
	
	private ArrayList<DatasetPoint> dataset;
	private ArrayList<Cluster> clustersList;
	private int minPts;
	private double eps;
	private int clustersCount;
	
	public IncrementalDBSCAN(ArrayList<DatasetPoint> dataset, int minpts, double eps) {
		this.dataset = dataset;
		this.minPts = minpts;
		this.eps = eps;
		this.clustersList = new ArrayList<Cluster>();
		this.clustersCount = 0;
	}
	
	public void clusterPoint(DatasetPoint point){
		ArrayList<Integer> updSeedPointIndexs = getUpdSeedSet(point);
		if(updSeedPointIndexs.size()==0){
			 markAsNoise(point);
		}
		else if(updSeedContainsCorePointsWithNoCluster(updSeedPointIndexs)){
			createCluster(point, updSeedPointIndexs);
		}
		else if(updSeedContainsCorePointsFromOneCluster(updSeedPointIndexs)){
			joinCluster(point, updSeedPointIndexs);
		}
		else{
			mergeClusters(point, updSeedPointIndexs);
		}
		point.setVisited(true);
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

	/**
	 * Check if all the core points has no assigned cluster
	 * @param indexs updSeet set
	 * @return true if no cluster is assigned to all points
	 */
	public boolean updSeedContainsCorePointsWithNoCluster(ArrayList<Integer> indexs){
		for (int i = 0; i < indexs.size(); i++) {
			DatasetPoint p = this.dataset.get(indexs.get(i));
			if(!p.getAssignedCluster().equalsIgnoreCase("")) return false;
		}
		return true;
	}
	
	/**
	 * Check if all points has the same cluster
	 * @param indexs updSeed 
	 * @return true if all points have the same clusters
	 */
	public boolean updSeedContainsCorePointsFromOneCluster(ArrayList<Integer> indexs){
		String clusterID = this.dataset.get(indexs.get(0)).getAssignedCluster();
		for (int i = 1; i < indexs.size(); i++) {
			DatasetPoint p = this.dataset.get(indexs.get(i));
			if(!clusterID.equalsIgnoreCase(p.getAssignedCluster())) return false;
		}
		return true;
	}

	/**
	 * Collect the clusters ids of a updSeed
	 * @param pointsIDs points of updSeed
	 * @return list of clusters ids
	 */
	public ArrayList<String> getClusterOfPoints(ArrayList<Integer> pointsIDs){
		ArrayList<String> clusterIDs = new ArrayList<String>();
		Hashtable<String, Boolean> idsSeen = new Hashtable<String, Boolean>();
		for (int i = 0; i < pointsIDs.size(); i++) {
			DatasetPoint p = this.dataset.get(pointsIDs.get(i));
			if(!idsSeen.containsKey(p.getAssignedCluster())){
				clusterIDs.add(p.getAssignedCluster());
				idsSeen.put(p.getAssignedCluster(), true);
			}
		}
		return clusterIDs;
	}
	
	
	public void mergeClusters(DatasetPoint point,ArrayList<Integer> indexs){
		
	}
	
	/**
	 * Add point to cluster (Given that all points at indexs come from only one cluster)
	 * @param point the point
	 * @param indexs indexes point
	 */
	public void joinCluster(DatasetPoint point, ArrayList<Integer> indexs){
		String clusterID = this.dataset.get(indexs.get(0)).getAssignedCluster();
		Cluster c = this.clustersList.get(Integer.parseInt(clusterID));
		c.addPoint(point.getID());
		point.setAssignedCluster(clusterID);
	}
	
	/**
	 * Mark point as noise
	 * @param point point
	 */
	public void markAsNoise(DatasetPoint point){
		point.setNoise(true);
	}
	
	/**
	 * create new cluster with the points
	 * @param point datapoint
	 * @param seedPointsIDs updSeed points
	 */
	public void createCluster(DatasetPoint point, ArrayList<Integer> seedPointsIDs){
		Cluster c = new Cluster(this.clustersCount);
		String clusterID = String.valueOf(c.getID());
		this.clustersCount++;
		point.setAssignedCluster(clusterID);
		c.addPoint(point.getID());
		for (int i = 0; i < seedPointsIDs.size(); i++) {
			DatasetPoint p = this.dataset.get(seedPointsIDs.get(i));
			p.setAssignedCluster(clusterID);
			c.addPoint(p.getID());
		}
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
