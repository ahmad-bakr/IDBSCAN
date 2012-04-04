package clustering.algorithms;

import java.util.ArrayList;

import clustering.partitioning.Medoid;

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
		//	ArrayList<DatasetPoint> regionQuery = getRegionQueryUsingTriangularMatrix(point,this.pointsID.get(i), eps);
			//if(regionQuery.size() < minPts){
		//		point.setNoise(true);
	//		}else{
			//	expandCluster(point, regionQuery, clusterLabel, eps, minPts);
		//		clusterLabel++;
		//	}
		}
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


}
