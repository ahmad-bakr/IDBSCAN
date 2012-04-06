package clustering.algorithms;

import java.util.ArrayList;

import clustering.partitioning.Clarans;
import clustering.partitioning.Node;

import datasets.DatasetPoint;

public class EnhancedDBSCAN {
	
	private ArrayList<DatasetPoint> dataset;
	
	public EnhancedDBSCAN(ArrayList<DatasetPoint> dataset) {
		this.dataset = dataset;
	}
	
	public void run(int numLocals, int maxNeighbors, int numPartitions, double eps, int minPts, double alpha){
		Clarans clarans = new Clarans();
		Node  bestRanSolution = clarans.perform(dataset, numLocals, maxNeighbors, numPartitions);
		for (int i = 0; i < bestRanSolution.getMedoids().length; i++) {
			DBSCANPartitioner dbscanpart = new DBSCANPartitioner(dataset, bestRanSolution.getMedoids()[i], bestRanSolution.getMedoidsAssignedPoints().get(i));
			dbscanpart.run(eps, minPts);			
		}
		mergeRegions(bestRanSolution,alpha);
	}
	
	public void mergeRegions(Node clusteringSolution,double alpha){
		
	}
	
	
}
