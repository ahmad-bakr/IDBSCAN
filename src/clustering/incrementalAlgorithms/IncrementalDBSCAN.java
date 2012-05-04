package clustering.incrementalAlgorithms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import org.jfree.ui.RefineryUtilities;

import plot.PlotIncrementalDBSCAN;

import clustering.algorithms.Cluster;

import datasets.ChameleonData;
import datasets.DatasetPoint;
import evaluation.DaviesBouldin;
import evaluation.DunnIndex;

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
	
	public void run(){
		for (int i = 0; i < this.dataset.size(); i++) {
			System.out.println(i);
			DatasetPoint p = this.dataset.get(i);
			clusterPoint(p);
		}
		noiseLabel();
	}
	
	/**
	 * Cluster a new point
	 * @param point new point
	 */
	private void clusterPoint(DatasetPoint point){
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
	private ArrayList<Integer> getUpdSeedSet(DatasetPoint point){
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
				 updSeedIndex.add(p.getID());
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
	private boolean updSeedContainsCorePointsWithNoCluster(ArrayList<Integer> indexs){
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
	private boolean updSeedContainsCorePointsFromOneCluster(ArrayList<Integer> indexs){
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
	private ArrayList<Cluster> getClusterOfPoints(ArrayList<Integer> pointsIDs){
		ArrayList<Cluster> clusters = new ArrayList<Cluster>();
		Hashtable<String, Boolean> idsSeen = new Hashtable<String, Boolean>();
		for (int i = 0; i < pointsIDs.size(); i++) {
			DatasetPoint p = this.dataset.get(pointsIDs.get(i));
			if(p.getAssignedCluster().equalsIgnoreCase("")) continue;
			if(!idsSeen.containsKey(p.getAssignedCluster())){
				clusters.add(this.clustersList.get(Integer.parseInt(p.getAssignedCluster())));
				idsSeen.put(p.getAssignedCluster(), true);
			}
		}
		return clusters;
	}
	
	
	/**
	 * Merge the clusters into the first cluster
	 * @param point point
	 * @param indexs updSeed points
	 */
	private void mergeClusters(DatasetPoint point,ArrayList<Integer> indexs){
		ArrayList<Cluster> clusters = getClusterOfPoints(indexs);
		Cluster masterCluster = clusters.get(0);
		String masterClusterID = String.valueOf(masterCluster.getID());
		point.setAssignedCluster(masterClusterID);
		masterCluster.addPoint(point.getID());
		for (int i = 1; i < clusters.size(); i++) {
			Cluster c = clusters.get(i);
			c.setActive(false);
			ArrayList<Integer> cPoints = c.getPointsIDs();
			for (int j = 0; j < cPoints.size(); j++) {
				DatasetPoint p = this.dataset.get(cPoints.get(j));
				p.setAssignedCluster(masterClusterID);
				masterCluster.addPoint(p.getID());
			}
		}
		//handleInsertionEffect(point, indexs);
	}
	
	/**
	 * Add point to cluster (Given that all points at indexs come from only one cluster)
	 * @param point the point
	 * @param indexs indexes point
	 */
	private void joinCluster(DatasetPoint point, ArrayList<Integer> indexs){
		String clusterID = this.dataset.get(indexs.get(0)).getAssignedCluster();
		Cluster c = this.clustersList.get(Integer.parseInt(clusterID));
		c.addPoint(point.getID());
		point.setAssignedCluster(clusterID);
	//	handleInsertionEffect(point, indexs);
	}
	
	/**
	 * Mark point as noise
	 * @param point point
	 */
	private void markAsNoise(DatasetPoint point){
		point.setNoise(true);
	}
	
	/**
	 * create new cluster with the points
	 * @param point datapoint
	 * @param seedPointsIDs updSeed points
	 */
	private void createCluster(DatasetPoint point, ArrayList<Integer> seedPointsIDs){
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
		this.clustersList.add(c);
	//	handleInsertionEffect(point, seedPointsIDs);
	}
	
	private void handleInsertionEffect(DatasetPoint point , ArrayList<Integer> seedPointsIDs){
		seedPointsIDs.add(point.getID());
		String clusterID = point.getAssignedCluster();
		System.out.println("undone");
		for (int i = 0; i < seedPointsIDs.size(); i++) {
			DatasetPoint p = this.dataset.get(seedPointsIDs.get(i));
			if(!p.getIsCore(this.minPts)) continue;
			ArrayList<Integer> pointsAtEPS = p.getPointsAtEpsIndexs();
			for (int j = 0; j < pointsAtEPS.size(); j++) {
				DatasetPoint pp = this.dataset.get(pointsAtEPS.get(j));
				if(pp.getAssignedCluster().equalsIgnoreCase("")) pp.setAssignedCluster(clusterID);
			}
			
		}
		System.out.println("done");
	}
	
	/**
	 * calculate the Euclidean Distance between two points
	 * @param p1 point 1
	 * @param p2 point 2
	 * @return Distance
	 */
	private double calculateDistanceBtwTwoPoints(DatasetPoint p1, DatasetPoint p2){
		double xDiff = p1.getX() - p2.getX();
		double yDiff = p1.getY() - p2.getY();
		return Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
	}
	
	public ArrayList<Cluster> getClustersList() {
		return clustersList;
	}

	
	private void noiseLabel(){
		for (int i = 0; i < this.dataset.size(); i++) {
			DatasetPoint p = this.dataset.get(i);
			if(!p.getIsNoise()) continue;
			ArrayList<Integer> neighbors = p.getPointsAtEpsIndexs();
			for (int j = 0; j < neighbors.size(); j++) {
				DatasetPoint neighbor = this.dataset.get(neighbors.get(j));
				if(neighbor.getAssignedCluster().equalsIgnoreCase("") || !neighbor.getIsCore(this.minPts)) continue;
		//		if(neighbor.getIsNoise()) continue;	
				p.setAssignedCluster(neighbor.getAssignedCluster());
				Cluster c = this.clustersList.get(Integer.parseInt(p.getAssignedCluster()));
				c.addPoint(p.getID());
				break;
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		double eps = 10;
		int minPts= 20;
		ChameleonData datasetLoader = new ChameleonData();
		ArrayList<DatasetPoint> dataset = datasetLoader.loadArrayList("/media/disk/master/Courses/Machine_Learning/datasets/chameleon-data/all.txt");
		long startTime = System.currentTimeMillis();
		IncrementalDBSCAN incDBSCAN = new IncrementalDBSCAN(dataset, minPts, eps);
		incDBSCAN.run();
		long endTime = System.currentTimeMillis();
		System.out.println("Runtime = " + (endTime-startTime));
		
//		ArrayList<Cluster> clustersList = incDBSCAN.getClustersList();
//		
//		DunnIndex dunn = new DunnIndex(clustersList, dataset);
//		System.out.println("Dunn Index = " + dunn.calculateDunnIndex());
//		
//		DaviesBouldin davies = new DaviesBouldin(clustersList, dataset);
//		System.out.println("Davies Measure = " + davies.calculateDaviesMeasure());
//		
//		PlotIncrementalDBSCAN plotter = new PlotIncrementalDBSCAN("Clusters");
//		plotter.plot(dataset, clustersList);
//		plotter.pack();
//		RefineryUtilities.centerFrameOnScreen(plotter);
//		plotter.setVisible(true); 

		
	}
	
	

}
