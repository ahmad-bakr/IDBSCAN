package clustering.algorithms;

import java.io.IOException;
import java.util.ArrayList;

import org.jfree.ui.RefineryUtilities;

import plot.PlotDBSCANPartitioner;

import clustering.partitioning.Clarans;
import clustering.partitioning.Medoid;
import clustering.partitioning.Node;

import datasets.ChameleonData;
import datasets.ChameleonModified;
import datasets.DatasetPoint;

public class DBSCANPartitioner {
	private ArrayList<DatasetPoint> dataset;
	private ArrayList<Double[]> triangularDistanceMatrix;
	private int medoidID;
	private ArrayList<Integer> pointsID;
	
	public DBSCANPartitioner(ArrayList<DatasetPoint> dataset, int medoidID ,ArrayList<Integer> pointsIDS) {
		this.dataset = dataset;
		this.pointsID = pointsIDS;
		this.medoidID = medoidID;
		this.triangularDistanceMatrix = new ArrayList<Double[]>(this.pointsID.size());
		for (int i = 0; i < this.pointsID.size(); i++) {
			this.dataset.get(this.pointsID.get(i)).setIndexInPartition(i);
		}
		calculateTriangularDistanceMatrxi();
	}	
	

	/**
	 * Run DBSCAN algorithm
	 * @param eps eps value
	 * @param minPts min pts value
	 */
	public void run( double eps, int minPts){
		int clusterLabel = 0;
		for (int i = 0; i < this.pointsID.size(); i++) {
			System.out.println(i);
			DatasetPoint point = dataset.get(this.pointsID.get(i));
			if (point.getIsVisited()) continue;
			point.setVisited(true);
			ArrayList<DatasetPoint> regionQuery = getRegionQueryUsingTriangularMatrix(point, eps);
			if(regionQuery.size() < minPts){
				point.setNoise(true);				
			}else{
				expandCluster(point, regionQuery, clusterLabel, eps, minPts);
				clusterLabel++;
			}
		}
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
		point.setAssignedCluster(String.valueOf(this.medoidID)+"_"+String.valueOf(clusterLabel));
		for (int i = 0; i < regionQuery.size(); i++) {
			DatasetPoint neighborPoint = regionQuery.get(i);
			if (!neighborPoint.getIsVisited()){
				neighborPoint.setVisited(true);
				ArrayList<DatasetPoint> regionQueryOfNeighborPoint = getRegionQueryUsingTriangularMatrix(neighborPoint, eps);
				if(regionQueryOfNeighborPoint.size() >= minPts){
					regionQuery.addAll(regionQueryOfNeighborPoint);
				}else{
					neighborPoint.setBoarder(true);
				}
			}
			if (neighborPoint.getAssignedCluster().equalsIgnoreCase("")){
				neighborPoint.setAssignedCluster(String.valueOf(this.medoidID)+"_"+String.valueOf(clusterLabel));
			}
		}
	}


	/**
	 * Get the region query of a point
	 * @param point point 
	 * @param pointIndex point index at pointsIDs
	 * @param eps eps value
	 * @return list of points in the region
	 */
	private ArrayList<DatasetPoint> getRegionQueryUsingTriangularMatrix(DatasetPoint point ,double eps){
		int pointIndex = point.getIndexInPartition();
		ArrayList<DatasetPoint> list = new ArrayList<DatasetPoint>();
		Double [] rowRecord = this.triangularDistanceMatrix.get(pointIndex);
		for (int i = 0; i < pointIndex; i++) {
			if(rowRecord[i] <= eps){
				DatasetPoint p = this.dataset.get(this.pointsID.get(i));
				list.add(p);
			}
		}
		
		for (int i = pointIndex+1; i < this.pointsID.size(); i++) {
			if(this.triangularDistanceMatrix.get(i)[pointIndex] <= eps){
				DatasetPoint p = this.dataset.get(this.pointsID.get(i));
				list.add(p);
			}
		}
		
		return list;
	}

		
	/**
	 * calculate the distance matrix for the partition
	 */
	public void calculateTriangularDistanceMatrxi(){
		for (int i = 0; i < this.pointsID.size(); i++) {
			System.out.println(i);
			DatasetPoint currentPoint = dataset.get(this.pointsID.get(i));
			Double [] distanceRecord = new Double [i];
			for (int j = 0; j < i; j++) {
				distanceRecord[j] = calculateDistanceBtwTwoPoints(currentPoint, dataset.get(this.pointsID.get(j)));
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
		int numLocals = 9;
		int maxNeighbors = 7;
		int numPartitions =9;
		double eps = 10;
		int minPts= 15;
		ChameleonData datasetLoader = new ChameleonData();
		ArrayList<DatasetPoint> dataset = datasetLoader.loadArrayList("/media/disk/master/Courses/Machine_Learning/datasets/chameleon-data/t7.10k.dat");	
		Clarans clarans = new Clarans();
		Node  bestRanSolution = clarans.perform(dataset, numLocals, maxNeighbors, numPartitions);
		for (int i = 0; i < bestRanSolution.getMedoids().length; i++) {
			DBSCANPartitioner dbscanpart = new DBSCANPartitioner(dataset, i, bestRanSolution.getMedoidsAssignedPoints().get(i));
			dbscanpart.run(eps, minPts);			
		}
		
		PlotDBSCANPartitioner plotter = new PlotDBSCANPartitioner("regions");
		plotter.plot(dataset, bestRanSolution);
		plotter.pack();
		RefineryUtilities.centerFrameOnScreen(plotter);
		plotter.setVisible(true); 

	}

}
