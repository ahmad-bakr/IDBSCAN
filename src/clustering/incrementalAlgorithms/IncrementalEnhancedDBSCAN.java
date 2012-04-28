package clustering.incrementalAlgorithms;

import java.io.IOException;
import java.util.ArrayList;

import org.jfree.ui.RefineryUtilities;

import plot.PlotEhancedDBSCAN;
import plot.PlotIncrementalDBSCAN;
import plot.PlotIncrementalEnhancedDenseRegions;

import clustering.algorithms.Cluster;
import clustering.algorithms.DenseRegion;
import clustering.partitioning.Centroid;
import clustering.partitioning.PartitioningAlgorithm;
import datasets.ChameleonData;
import datasets.DatasetPoint;

public class IncrementalEnhancedDBSCAN {
	private Centroid [] partitions;
	private ArrayList<Cluster> clusters;
	private ArrayList<DatasetPoint> dataset;
	private PartitioningAlgorithm partitioner;
	private ArrayList<DenseRegion> denseRegions;
	private IncrementalDBSCANPartitioner [] incrementalDBSCANs;
	private int minPts;
	private double eps;
	private int clustersCount;
	private double alpha;
	
	public IncrementalEnhancedDBSCAN(ArrayList<DatasetPoint> dataset, int numberOfPartitions, int minpts, double eps, double alpha) {
		this.dataset = dataset;
		this.minPts = minpts;
		this.eps = eps;
		this.clusters = new ArrayList<Cluster>();
		this.denseRegions = new ArrayList<DenseRegion>();
		this.partitioner = new PartitioningAlgorithm(dataset, numberOfPartitions);
		this.partitions = partitioner.getCentroids();
		this.incrementalDBSCANs = new IncrementalDBSCANPartitioner[numberOfPartitions];
		for (int i = 0; i < numberOfPartitions; i++) {
			this.incrementalDBSCANs[i] = new IncrementalDBSCANPartitioner(i, this.minPts, this.eps, this.dataset);
		}
		this.clustersCount =0;
		this.alpha = alpha;
	}
	
	public void run(){
		for (int i = 0; i < this.dataset.size(); i++) {
			DatasetPoint point = this.dataset.get(i);
			int partitionIndex = this.partitioner.partitionPoint(point);
			IncrementalDBSCANPartitioner incrementalDBSCAN = this.incrementalDBSCANs[partitionIndex];
			incrementalDBSCAN.addPointToPartition(point);
		}
		
		for (int i = 0; i < this.incrementalDBSCANs.length; i++) {
			IncrementalDBSCANPartitioner p = incrementalDBSCANs[i];
			ArrayList<DenseRegion> regions = p.getDenseRegions();
			for (int j = 0; j < regions.size(); j++) {
				DenseRegion d = regions.get(j);
				if(d.getActive()) this.denseRegions.add(d);
			}
		}
		
		removeNoiseAndLabelOutliers();
		mergeRegions(alpha);

	}

	private void removeNoiseAndLabelOutliers(){
		for (int i = 0; i < this.dataset.size(); i++) {
			DatasetPoint p = this.dataset.get(i);
			if (p.getAssignedCluster().equalsIgnoreCase("")){
				DenseRegion d = getNearestRegion(p);
				
				if(d!=null)d.addPoint(p.getID());
			}
		}
	}

	
	private DenseRegion getNearestRegion(DatasetPoint point){
		DenseRegion denseRegion = null;
		Double minDistance = Double.MAX_VALUE;
		for (int i = 0; i < this.denseRegions.size(); i++) {
			DenseRegion d = this.denseRegions.get(i);
			ArrayList<Integer> points = d.getPoints();
			for (int j = 0; j < points.size(); j++) {
				DatasetPoint p2 = this.dataset.get(points.get(j));
				if (p2.getIsNoise()) continue;
				double distance = calculateDistanceBtwTwoPoints(point, p2);
				if(distance < minDistance){
					denseRegion = d;
					minDistance = distance;
				}
			}
		}
		
		if(minDistance > this.eps) return null;
		
		return denseRegion;
	}

	public ArrayList<DenseRegion> getDenseRegions() {
		return denseRegions;
	}
	
	/**
	 * Merge regions
	 * @param alpha alpha merge value
	 */
	private void mergeRegions(double alpha){
		for (int i = 0; i < this.denseRegions.size(); i++) {
			DenseRegion r1 = this.denseRegions.get(i);
			for (int j = 0; j < i; j++) {
				
				DenseRegion r2 = this.denseRegions.get(j);
				if(r1.getIsInCluster() && r2.getIsInCluster() && (r1.getClusterID() == r2.getClusterID())) continue;
				double connectivity = calculateConnectivityBetweenTwoRegions(r1, r2);
				System.out.println(connectivity);
				if(connectivity >= alpha) mergeTwoRegions(r1, r2);
			}
		}
		
		for (int i = 0; i < this.denseRegions.size(); i++) {
			DenseRegion r = denseRegions.get(i);
			if (r.getIsInCluster()) continue;
			Cluster c = new Cluster(this.clustersCount);
			r.setClusterID(c.getID());
			c.addDenseRegion(r);
			this.clusters.add(c);
			clustersCount++;
		}
		
	}

	/**
	 * Merge to regions in clusters
	 * @param r1 region 1
	 * @param r2 region 2
	 */
	private void mergeTwoRegions(DenseRegion r1, DenseRegion r2){
		Cluster c = null;
		int clusterID =0;
		if(!r1.getIsInCluster() && !r2.getIsInCluster()){
			c = new Cluster(this.clustersCount);
			c.addDenseRegion(r1);
			c.addDenseRegion(r2);
			r1.setClusterID(c.getID());
			r2.setClusterID(c.getID());
			this.clusters.add(c);
			this.clustersCount++;
			
		}
		else if(r1.getIsInCluster() && r2.getIsInCluster()){
			mergeCluster(this.clusters.get(r1.getClusterID()), this.clusters.get(r2.getClusterID()));
		}
		else if(r1.getIsInCluster()){
			clusterID= r1.getClusterID();
			c = this.clusters.get(clusterID);
			r2.setClusterID(clusterID);	
			c.addDenseRegion(r2);
		}
		else if (r2.getIsInCluster()){
			clusterID = r2.getClusterID();
			c = this.clusters.get(clusterID);
			r1.setClusterID(clusterID);
			c.addDenseRegion(r1);
		}
		
		
	}
	
	/**
	 * Merge two clusters
	 * @param c1 cluster 1
	 * @param c2 cluster 2
	 */
	private void mergeCluster(Cluster c1, Cluster c2){
		c2.setActive(false);
		System.out.println("Mergeing cluster " + c1.getID() + "with cluster "+ c2.getID());
		ArrayList<DenseRegion> c2Regions = c2.getRegions();
		for (int i = 0; i < c2Regions.size(); i++) {
			DenseRegion r = c2Regions.get(i);
			r.setClusterID(c1.getID());
			c1.addDenseRegion(r);
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
		ArrayList<Integer> regionPoints = r.getPoints();
		int numberOfEdges =0;
		for (int i = 0; i < regionPoints.size(); i++) {
			DatasetPoint p1 = this.dataset.get(regionPoints.get(i));
			if(p1.getIsCore(this.minPts)) continue;
			for (int j = 0; j < i; j++) {
				DatasetPoint p2 = this.dataset.get(regionPoints.get(j));
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
		ArrayList<Integer> r1BoarderPoints = r1.getPoints();
		ArrayList<Integer> r2BoarderPoints = r2.getPoints();
		int numberOfConnectivityEdges =0;
		for (int i = 0; i < r1BoarderPoints.size(); i++) {
			DatasetPoint p1 = this.dataset.get(r1BoarderPoints.get(i));
			if(p1.getIsCore(this.minPts)) continue;
			for (int j = 0; j < r2BoarderPoints.size(); j++) {
				DatasetPoint p2 = this.dataset.get(r2BoarderPoints.get(j));
				if(p2.getIsCore(this.minPts)) continue;
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

	public ArrayList<Cluster> getClusters() {
		return clusters;
	}
	
	public static void main(String[] args) throws IOException {
		int numPartitions =90;
		double eps = 5;
		int minPts= 12;
		double alpha = 0.01;
		ChameleonData datasetLoader = new ChameleonData();
		ArrayList<DatasetPoint> dataset = datasetLoader.loadArrayList("/media/disk/master/Courses/Machine_Learning/datasets/chameleon-data/t5.8k.dat");	
		IncrementalEnhancedDBSCAN algorithm = new IncrementalEnhancedDBSCAN(dataset, numPartitions, minPts, eps, alpha);
		algorithm.run();
		ArrayList<Cluster> clusters = algorithm.getClusters();
		System.out.println("done");
		System.out.println(clusters.size());
		PlotEhancedDBSCAN plotter = new PlotEhancedDBSCAN("Clusters");
		plotter.plot(dataset, clusters);
		plotter.pack();
		RefineryUtilities.centerFrameOnScreen(plotter);
		plotter.setVisible(true); 

	}
}
