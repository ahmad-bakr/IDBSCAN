package clustering.partitioning;

import java.util.ArrayList;

import datasets.DatasetPoint;

public class Clarans {
	
	/**
	 * Perform the CLARANS algorithm
	 * @param dataset dataset 
	 * @param numLocals number of locals
	 * @param maxNeighbors max number of neighbors
	 * @param numPartitions number of partitions (medoids)
	 * @return the best random solution
	 */
	public Node perform(ArrayList<DatasetPoint> dataset, int numLocals, int maxNeighbors, int numPartitions){
		int i=0;
		double minCost = Double.MAX_VALUE;
		Node bestNode =null;
		while (i <= numLocals){
			Node current = null;
			Node randomNeighbor = null;
			NodeFactory graph = new NodeFactory(dataset, numPartitions);
			int j=1;	
			current = graph.getNode();
			while (j <= maxNeighbors){		
				randomNeighbor = graph.getNeighbor(current);
				current.calculateCost();
				randomNeighbor.calculateCost();		
				j++;
				if(randomNeighbor.getCost() < current.getCost()){
					 j=1;
					 current = randomNeighbor;
				}
			}
			
			if(current.getCost() < minCost){
				minCost = current.getCost();
				bestNode = current;
			}
			
			i++;	
		}
		
		
		return bestNode;
	}
	

}
