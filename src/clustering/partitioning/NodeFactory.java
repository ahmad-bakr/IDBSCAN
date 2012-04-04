package clustering.partitioning;

import java.util.ArrayList;
import java.util.Hashtable;

import datasets.DatasetPoint;

public class NodeFactory {
	
	private ArrayList<DatasetPoint> dataset;
	private int numberOfMedoids;

	public NodeFactory(ArrayList<DatasetPoint> dataset, int numberOfMedoids) {
		this.dataset = dataset;
		this.numberOfMedoids = numberOfMedoids;
	}
	
	/**
	 * Get random node from the graph
	 * @return random node
	 */
	public Node getNode(){
		Node node = new Node(this.numberOfMedoids);
		int maxIndex = this.dataset.size()-1;
		int selectedMedoidsNumber = 0;
		while(selectedMedoidsNumber != this.numberOfMedoids){
			double medoidXValue =0;
			double medoidYValue =0;
			do {
				int randomIndex = 0 + (int)(Math.random() * ((maxIndex - 0) + 1));
				DatasetPoint p = this.dataset.get(randomIndex);
				medoidXValue = p.getX();
				medoidYValue = p.getY();
			} while (node.hasMedoid(medoidXValue, medoidYValue));
			node.addMedoid(selectedMedoidsNumber, medoidXValue, medoidYValue);
			selectedMedoidsNumber++;
		}
		return node;
	}
	
	/**
	 * Get neighbor of a node
	 * @param node node
	 * @return neighbor of a node
	 */
	public Node getNeighbor(Node node){
		Node neighbor = new Node(this.numberOfMedoids);
		Medoid [] medoids = node.getMedoids();
		int maxIndex = this.dataset.size()-1;
		//clone the node to neighbor
		for (int i = 0; i < medoids.length; i++) {
			Medoid m = medoids[i];
			neighbor.addMedoid(i, m.getX(), m.getY());
		}
		//find a new medoid to replace the current one
		int replacedMedoidIndex = 0 + (int)(Math.random() * (((this.numberOfMedoids-1) - 0) + 1));
		double newMedoidXValue = 0;
		double newMedoidYValue = 0;
		do {
			int randomIndex = 0 + (int)(Math.random() * ((maxIndex - 0) + 1));
			DatasetPoint p = this.dataset.get(randomIndex);
			newMedoidXValue = p.getX();
			newMedoidYValue = p.getY();			
		} while (neighbor.hasMedoid(newMedoidXValue, newMedoidYValue));
		neighbor.updateMedoid(replacedMedoidIndex, newMedoidXValue, newMedoidYValue);
		return neighbor;
	}
	
	
	public static void main(String[] args) {
	
	}

}
