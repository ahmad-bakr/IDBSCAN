package clustering.algorithms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import org.jfree.ui.RefineryUtilities;

import plot.DatasetPlotter;

import datasets.ChameleonData;
import datasets.DatasetPoint;
import datasets.DatasetsIF;

public class DBSCAN {
	
	private Hashtable<DatasetPoint, ArrayList<Double>> distanceMatrix;
	private ArrayList<DatasetPoint> dataset;
	
	public DBSCAN(ArrayList<DatasetPoint> dataset) {
		this.distanceMatrix = new Hashtable<DatasetPoint, ArrayList<Double>>();
		this.dataset = dataset;
		System.out.println("Calculating the Distance Matrix, Number of points = " + dataset.size());
		calculateDistanceMatrix(dataset);
	}
	
	
	/**
	 * Run DBSCAN algorithm
	 * @param eps eps value
	 * @param minPts min pts value
	 */
	public void run( double eps, int minPts){
		int clusterLabel = 0;
		for (int i = 0; i < dataset.size(); i++) {
			System.out.println(i);
			DatasetPoint point = dataset.get(i);
			if (point.getIsVisited()) continue;
			point.setVisited(true);
			ArrayList<DatasetPoint> regionQuery = getRegionQuery(point, eps);
			if(regionQuery.size() < minPts){
				point.setNoise(true);
			}else{
				expandCluster(point, regionQuery, clusterLabel, eps, minPts);
				clusterLabel++;
			}
		}
	}
	
	/**
	 * Get the region query of a specific point
	 * @param point point 
	 * @param eps eps
	 * @return region query of points
	 */
	private ArrayList<DatasetPoint> getRegionQuery(DatasetPoint point, double eps){
		ArrayList<DatasetPoint> list = new ArrayList<DatasetPoint>();
		ArrayList<Double> distanceRecord = this.distanceMatrix.get(point);
		for (int i = 0; i < distanceRecord.size(); i++) {
			if(distanceRecord.get(i) <= eps){
				DatasetPoint p = this.dataset.get(i);
				if(p != point){
					list.add(p);
				}
			}
		}
		return list;
	}
	
	/**
	 * Expand cluster
	 * @param point current point
	 * @param regionQuery region query of the point
	 * @param clusterLabel cluster label
	 * @param eps eps value
	 * @param minPts min pts value
	 */
	private void expandCluster(DatasetPoint point, ArrayList<DatasetPoint> regionQuery, int clusterLabel, double eps, int minPts ){
		point.setAssignedCluster(String.valueOf(clusterLabel));
		for (int i = 0; i < regionQuery.size(); i++) {
			DatasetPoint neighborPoint = regionQuery.get(i);
			if (!neighborPoint.getIsVisited()){
				neighborPoint.setVisited(true);
				ArrayList<DatasetPoint> regionQueryOfNeighborPoint = getRegionQuery(neighborPoint, eps);
				if(regionQueryOfNeighborPoint.size() >= minPts){
					// add regionQueryOfNeighborPoint to regionQuery
					regionQuery.addAll(regionQueryOfNeighborPoint);
				}
			}
			if (neighborPoint.getAssignedCluster().equalsIgnoreCase("")){
				neighborPoint.setAssignedCluster(String.valueOf(clusterLabel));
			}
		}
	}

	/**
	 * calculate the distance matrix between all the points
	 * @param dataset dataset
	 */
	public void calculateDistanceMatrix(ArrayList<DatasetPoint> dataset){
		for (int i = 0; i < dataset.size(); i++) {
			System.out.println(i);
			DatasetPoint currentPoint = dataset.get(i);
			ArrayList<Double> distanceRecord = new ArrayList<Double>();
			for (int j = 0; j < dataset.size(); j++) {
				distanceRecord.add(calculateDistanceBtwTwoPoints(currentPoint, dataset.get(j)));
			}
			this.distanceMatrix.put(currentPoint, distanceRecord);
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
	
	public static void main(String[] args) throws IOException {
		double eps = 15;
		int minPts= 25;
		ChameleonData datasetLoader = new ChameleonData();
		ArrayList<DatasetPoint> dataset = datasetLoader.loadArrayList("/media/disk/master/Courses/Machine_Learning/datasets/chameleon-data/t4.8k.dat");
		DBSCAN dbscan = new DBSCAN(dataset);
		dbscan.run(eps, minPts);

		DatasetPlotter plotter = new DatasetPlotter("Clusters");
		plotter.plotList(dataset);
		plotter.pack();
		RefineryUtilities.centerFrameOnScreen(plotter);
		plotter.setVisible(true); 

	}

	
}
