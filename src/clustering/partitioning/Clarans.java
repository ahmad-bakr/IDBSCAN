package clustering.partitioning;

import java.io.IOException;
import java.util.ArrayList;

import org.jfree.ui.RefineryUtilities;

import plot.PlotClarans;

import datasets.ChameleonModified;
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
		NodeFactory graph = new NodeFactory(dataset, numPartitions);
		int j=1;
		while (i <= numLocals){
			Node current = null;
			Node randomNeighbor = null;
		  j=1;	
			current = graph.getNode();
			while (j <= maxNeighbors){		
				randomNeighbor = graph.getNeighbor(current);
				current.calculateCost(dataset);
				randomNeighbor.calculateCost(dataset);		
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
	
	public static void main(String[] args) throws IOException {
		int numLocals = 4;
		int maxNeighbors = 5;
		int numPartitions =4;
		ChameleonModified datasetLoader = new ChameleonModified();
		ArrayList<DatasetPoint> dataset = datasetLoader.loadArrayList("/media/disk/master/Courses/Machine_Learning/datasets/gaussian2clusters.txt");
		Clarans clarans = new Clarans();
		Node  bestRanSolution = clarans.perform(dataset, numLocals, maxNeighbors, numPartitions);
		PlotClarans plotter = new PlotClarans("Partitions");
		plotter.plotNode(dataset, bestRanSolution);
		plotter.pack();
		RefineryUtilities.centerFrameOnScreen(plotter);
		plotter.setVisible(true); 

	}
	

}
