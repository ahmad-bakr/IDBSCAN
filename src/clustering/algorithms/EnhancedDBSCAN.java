package clustering.algorithms;

import java.lang.reflect.Array;
import java.util.ArrayList;

import clustering.partitioning.Clarans;
import clustering.partitioning.Medoid;
import clustering.partitioning.Node;

import datasets.DatasetPoint;

public class EnhancedDBSCAN {
	
	private ArrayList<DatasetPoint> dataset;
	private ArrayList<DenseRegion> denseRegions;
	private double eps;
	
	public EnhancedDBSCAN(ArrayList<DatasetPoint> dataset) {
		this.dataset = dataset;
		this.denseRegions = new ArrayList<DenseRegion>();
	}
	
	public void run(int numLocals, int maxNeighbors, int numPartitions, double eps, int minPts, double alpha){
		this.eps = eps;
		Clarans clarans = new Clarans();
		Node  bestRanSolution = clarans.perform(dataset, numLocals, maxNeighbors, numPartitions);
		for (int i = 0; i < bestRanSolution.getMedoids().length; i++) {
			DBSCANPartitioner dbscanpart = new DBSCANPartitioner(dataset, bestRanSolution.getMedoids()[i], bestRanSolution.getMedoidsAssignedPoints().get(i));
			dbscanpart.run(eps, minPts);			
		}
		collectDenseRegions(bestRanSolution.getMedoids());
		
		mergeRegions(bestRanSolution,alpha);
	}
	
	private void mergeRegions(Node clusteringSolution,double alpha){
		
	}
	
	private void mergeTwoRegions(DenseRegion r1, DenseRegion r2){
		
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
	
}
