package clustering.partitioning;

import java.util.ArrayList;

import datasets.DatasetPoint;

public class Clarans {
	
	public void perform(ArrayList<DatasetPoint> dataset, int numLocals, int maxNeighbors, int numPartitions){
		int i=0;
		int j=1;
		double minCost = Double.MAX_VALUE;
		NodeFactory graph = new NodeFactory(dataset, numPartitions);
		Node current = graph.getNode();
		Node randomNeighbor = graph.getNeighbor(current);
		double currentCost = calculateCost(current);
		double neighborCost = calculateCost(randomNeighbor);
		
	}
	
	public double calculateCost(Node node){
		return 0;
	}

}
