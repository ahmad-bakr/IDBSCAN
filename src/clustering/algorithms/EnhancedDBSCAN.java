package clustering.algorithms;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import org.jfree.ui.RefineryUtilities;

import plot.PlotEhancedDBSCAN;

import clustering.partitioning.Clarans;
import clustering.partitioning.Medoid;
import clustering.partitioning.Node;

import datasets.ChameleonData;
import datasets.DatasetPoint;

public class EnhancedDBSCAN {
	
	private ArrayList<DatasetPoint> dataset;
	private ArrayList<DenseRegion> denseRegions;
	private ArrayList<Cluster> clusters;
	private double eps;
	private int clustersCount;

	public EnhancedDBSCAN(ArrayList<DatasetPoint> dataset) {
		this.dataset = dataset;
		this.denseRegions = new ArrayList<DenseRegion>();
		this.clusters = new ArrayList<Cluster>();
		this.clustersCount =0;
	}
	
	/**
	 * Run enhanced DBSCAN
	 * @param numLocals number of locals
	 * @param maxNeighbors max number of neighbors
	 * @param numPartitions number of partition
	 * @param eps eps value
	 * @param minPts min pts value
	 * @param alpha alpha value
	 */
	public void run(int numLocals, int maxNeighbors, int numPartitions, double eps, int minPts, double alpha){
		this.eps = eps;
		Clarans clarans = new Clarans();
		Node  bestRanSolution = clarans.perform(dataset, numLocals, maxNeighbors, numPartitions);
		for (int i = 0; i < bestRanSolution.getMedoids().length; i++) {
			DBSCANPartitioner dbscanpart = new DBSCANPartitioner(dataset, bestRanSolution.getMedoids()[i], bestRanSolution.getMedoidsAssignedPoints().get(i));
			dbscanpart.run(eps, minPts);			
		}
		collectDenseRegions(bestRanSolution.getMedoids());
		
		mergeRegions(alpha);
	}
	
	/**
	 * Merge regions
	 * @param alpha alpha merge value
	 */
	private void mergeRegions(double alpha){
		for (int i = 0; i < this.denseRegions.size(); i++) {
			DenseRegion r1 = this.denseRegions.get(i);
			if(r1.getIsInCluster()) continue;
			for (int j = 0; j < i; j++) {
				DenseRegion r2 = this.denseRegions.get(j);
				if(r2.getIsInCluster()) continue;
				double connectivity = calculateConnectivityBetweenTwoRegions(r1, r2);
				System.out.println(connectivity);
				if(connectivity >= alpha) mergeTwoRegions(r1, r2);
			}
		}
		
		for (int i = 0; i < this.denseRegions.size(); i++) {
			DenseRegion r = denseRegions.get(i);
			if (r.getIsInCluster()) continue;
			Cluster c = new Cluster();
			c.addToPointsList(r.getPoints());
			this.clusters.add(c);
		}
		
	}
	
	/**
	 * Merge to regions in clusters
	 * @param r1 region 1
	 * @param r2 region 2
	 */
	private void mergeTwoRegions(DenseRegion r1, DenseRegion r2){
		int clusterID =0;
		Cluster c = null;
		if(r1.getIsInCluster()){
			clusterID= r1.getClusterID();
			c = this.clusters.get(clusterID);
			r2.setClusterID(clusterID);
		}else if (r2.getIsInCluster()){
			clusterID = r2.getClusterID();
			c = this.clusters.get(clusterID);
			r1.setClusterID(clusterID);
		}else{
			clusterID = this.clustersCount;
			c = new Cluster();
			c.addToPointsList(r1.getPoints());
			c.addToPointsList(r2.getPoints());
			r1.setClusterID(clusterID);
			r2.setClusterID(clusterID);
			this.clusters.add(c);
			this.clustersCount++;
		}
	}
	
	private double calculateConnectivityBetweenTwoRegions(DenseRegion r1, DenseRegion r2){
		int r1EdgesCount = calculateNumberOfEdgesInRegion(r1);
		int r2EdgesCount = calculateNumberOfEdgesInRegion(r2);
		int connectingEdgesCount = calculateNumOfConnectingEdgesBetTwoRegions(r1, r2);
		return (connectingEdgesCount*1.0)/((r1EdgesCount+r2EdgesCount)/2);
	}
	
	/**
	 * calculate the number of edges in a dense region
	 * @param r dense region
	 * @return number of edges in the dense region
	 */
	private int calculateNumberOfEdgesInRegion(DenseRegion r){
		ArrayList<Integer> regionBoarderPoints = r.getBoarderPoints();
		int numberOfEdges =0;
		for (int i = 0; i < regionBoarderPoints.size(); i++) {
			DatasetPoint p1 = this.dataset.get(regionBoarderPoints.get(i));
			for (int j = 0; j < i; j++) {
				DatasetPoint p2 = this.dataset.get(regionBoarderPoints.get(j));
				double distance = calculateDistanceBtwTwoPoints(p1, p2);
				if (distance <=eps) numberOfEdges++;
			}
		}
		return numberOfEdges;
	}
	
	/**
	 * Number of connectivity edges between two regions
	 * @param r1 region 1
	 * @param r2 region 2
	 * @return number of connectivity edges between r1 and r2
	 */
	private int calculateNumOfConnectingEdgesBetTwoRegions(DenseRegion r1, DenseRegion r2){
		ArrayList<Integer> r1BoarderPoints = r1.getBoarderPoints();
		ArrayList<Integer> r2BoarderPoints = r2.getBoarderPoints();
		int numberOfConnectivityEdges =0;
		for (int i = 0; i < r1BoarderPoints.size(); i++) {
			DatasetPoint p1 = this.dataset.get(r1BoarderPoints.get(i));
			for (int j = 0; j < r2BoarderPoints.size(); j++) {
				DatasetPoint p2 = this.dataset.get(r2BoarderPoints.get(j));
				double distance = calculateDistanceBtwTwoPoints(p1, p2);
				if(distance<=this.eps) numberOfConnectivityEdges++;
			}
		}
		return numberOfConnectivityEdges;
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

	/**
	 * 	Collection the dense regions from all medoids
	 * @param medoids medoids
	 */
	private void collectDenseRegions(Medoid [] medoids){
		for (int i = 0; i < medoids.length; i++) {
			Medoid m = medoids[i];
			denseRegions.addAll(m.getRegions());
		}
	}
	
	public ArrayList<Cluster> getClusters() {
		return this.clusters;
	}
	
	public static void main(String[] args) throws IOException {
		int numLocals = 9;
		int maxNeighbors = 7;
		int numPartitions =10;
		double eps = 10;
		int minPts= 15;
		double alpha = 0.01;
		ChameleonData datasetLoader = new ChameleonData();
		ArrayList<DatasetPoint> dataset = datasetLoader.loadArrayList("/media/disk/master/Courses/Machine_Learning/datasets/chameleon-data/t7.10k.dat");	
		EnhancedDBSCAN eDBSCAN = new EnhancedDBSCAN(dataset);
		eDBSCAN.run(numLocals, maxNeighbors, numPartitions, eps, minPts, alpha);
		ArrayList<Cluster> clusters = eDBSCAN.getClusters();
		System.out.println("***********************");
		System.out.println(clusters.size());
		System.out.println("***********************");
		PlotEhancedDBSCAN plotter = new PlotEhancedDBSCAN("Clusters");
		plotter.plot(dataset, clusters);
		plotter.pack();
		RefineryUtilities.centerFrameOnScreen(plotter);
		plotter.setVisible(true); 

	}
}
