package clustering.partitioning;

import java.util.ArrayList;

import datasets.DatasetPoint;

public class Node {
	
	private Medoid [] medoids;
	private double cost;
	private boolean costCalculated;
	private ArrayList<ArrayList<Integer>> medoidsAssignedPoints ;
	
	/**
	 * Constructor
	 * @param numberOfMedoids number of medoids
	 */
	public Node(int numberOfMedoids) {
		this.medoids = new Medoid [numberOfMedoids];
		this.cost = 0;
		this.costCalculated = false;
		this.medoidsAssignedPoints = new ArrayList<ArrayList<Integer>>(numberOfMedoids);
		for (int i = 0; i < numberOfMedoids; i++) {
			this.medoidsAssignedPoints.add(new ArrayList<Integer>());
		}
	}
	
	
	/**
	 * calculate the cost if the node solution
	 * @param dataset dataset
	 */
	public void calculateCost(ArrayList<DatasetPoint> dataset){
		if (!this.costCalculated){
			double cost = 0;		
			for (int i = 0; i < dataset.size(); i++) {
				DatasetPoint p = dataset.get(i);
				double minDistance = Double.MAX_VALUE;
				int selectedMedoid = 0;
				for (int j = 0; j < this.medoids.length; j++) {
					double distance = calculateDistanceToMedoid(this.medoids[j], p);
					if(distance < minDistance){
						minDistance = distance;
						selectedMedoid = j;
					}
				}// end calculating distances from point p to all medoids 
				this.medoidsAssignedPoints.get(selectedMedoid).add(i);
				cost += minDistance;
			}// end looping for all points
			this.setCost(cost);
			this.costCalculated = true;		
		}
	}
	
	/**
	 * calculate distance to medoid
	 * @param m medoid m
	 * @param p dataset point p
	 * @return distance between m and p
	 */
	public double calculateDistanceToMedoid(Medoid m, DatasetPoint p){
		double xDiff = m.getX() - p.getX();
		double yDiff = m.getY() - p.getY();
		return Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
	}


	
	/**
	 * Add medoid to the node
	 * @param index index of the medoid in the node list
	 * @param x medoid's x
	 * @param y medoid's y
	 */
	public void addMedoid(int index, double x, double y){
		Medoid m = new Medoid(x, y);
		this.medoids[index] = m;
	}
	
	/**
	 * Update medoid at certain index
	 * @param index index
	 * @param x new x
	 * @param y new y
	 */
	public void updateMedoid(int index, double x, double y){
		Medoid m= this.medoids[index];
		m.setX(x);
		m.setY(y);
	}
	

	/**
	 * Check if the node has the medoid
	 * @param x medoid's x
	 * @param y medoid's y
	 * @return true if the node has the medoid
	 */
	public boolean hasMedoid(double x, double y){
		for (int i = 0; i < this.medoids.length; i++) {
			Medoid m = this.medoids[i];
			if (m==null) continue;
			if(m.getX() == x || m.getY() == y) return true;
		}
		return false;
	}
	
	/**
	 * get the medoids of the node
	 * @return return the medoids of the node
	 */
	public Medoid[] getMedoids() {
		return medoids;
	}

	/**
	 * Set the cost of the node
	 * @param cost cost of the node
	 */
	public void setCost(double cost) {
		this.cost = cost;
	}
	
	/**
	 * get the cost of the node
	 * @return cost of the node
	 */
	public double getCost() {
		return cost;
	}
	

}
