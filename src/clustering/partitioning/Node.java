package clustering.partitioning;

public class Node {
	
	private Medoid [] medoids;
	
	/**
	 * Constructor
	 * @param numberOfMedoids number of medoids
	 */
	public Node(int numberOfMedoids) {
		this.medoids = new Medoid [numberOfMedoids];
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

}
