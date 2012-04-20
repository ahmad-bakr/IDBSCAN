package clustering.algorithms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import org.jfree.ui.RefineryUtilities;

import plot.DatasetPlotter;

import datasets.ChameleonData;
import datasets.ChameleonModified;
import datasets.DatasetPoint;
import datasets.DatasetsIF;

public class DBSCAN {
	
	private ArrayList<DatasetPoint> dataset;
	private ArrayList<Double[]> triangularDistanceMatrix;
	private Hashtable<DatasetPoint, Integer> pointsIndex;
	
	public DBSCAN(ArrayList<DatasetPoint> dataset) {
		this.pointsIndex = new Hashtable<DatasetPoint, Integer>();
		this.dataset = dataset;
		System.out.println("Calculating the Distance Matrix, Number of points = " + dataset.size());
		this.triangularDistanceMatrix = new ArrayList<Double[]>(this.dataset.size());		
		calculateTriangularDistanceMatrxi();
		
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
			ArrayList<DatasetPoint> regionQuery = getRegionQueryUsingTriangularMatrix(point,i, eps);
			if(regionQuery.size() < minPts){
				point.setNoise(true);
			}else{
				expandCluster(point, regionQuery, clusterLabel, eps, minPts);
				clusterLabel++;
			}
		}
	}
	


	private ArrayList<DatasetPoint> getRegionQueryUsingTriangularMatrix(DatasetPoint point, int pointIndex ,double eps){
		ArrayList<DatasetPoint> list = new ArrayList<DatasetPoint>();
		Double [] rowRecord = this.triangularDistanceMatrix.get(pointIndex);
		for (int i = 0; i < pointIndex; i++) {
			if(rowRecord[i] <= eps){
				DatasetPoint p = this.dataset.get(i);
				list.add(p);
			}
		}
		
		for (int i = pointIndex+1; i < this.dataset.size(); i++) {
			if(this.triangularDistanceMatrix.get(i)[pointIndex] <= eps){
				DatasetPoint p = this.dataset.get(i);
				list.add(p);
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
				ArrayList<DatasetPoint> regionQueryOfNeighborPoint = getRegionQueryUsingTriangularMatrix(neighborPoint,this.pointsIndex.get(neighborPoint), eps);
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

	public void calculateTriangularDistanceMatrxi(){
		for (int i = 0; i < this.dataset.size(); i++) {
			System.out.println(i);
			DatasetPoint currentPoint = dataset.get(i);
			this.pointsIndex.put(currentPoint, i);
			Double [] distanceRecord = new Double [i];
			for (int j = 0; j < i; j++) {
				distanceRecord[j] = calculateDistanceBtwTwoPoints(currentPoint, dataset.get(j));
			}
			this.triangularDistanceMatrix.add(distanceRecord);
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
		double eps = 5;
		int minPts= 12;
		ChameleonData datasetLoader = new ChameleonData();
		ArrayList<DatasetPoint> dataset = datasetLoader.loadArrayList("/media/disk/master/Courses/Machine_Learning/datasets/chameleon-data/t5.8k.dat");
		DBSCAN dbscan = new DBSCAN(dataset);
		dbscan.run(eps, minPts);
		System.gc();
		DatasetPlotter plotter = new DatasetPlotter("Clusters");
		plotter.plotList(dataset);
		plotter.pack();
		RefineryUtilities.centerFrameOnScreen(plotter);
		plotter.setVisible(true); 

	}

	
}
