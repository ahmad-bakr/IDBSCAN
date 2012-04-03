package clustering.partitioning;

public class Medoid {
	
	private double x;
	private double y;
	
	/**
	 * Medoid Constructor
	 * @param x medoid X
	 * @param y mdeoid Y
	 */
	public Medoid(double x, double y) {
		this.x = x;
		this.y =y;
	}
	
	/**
	 * Get X value
	 * @return X
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * Get Y value 
	 * @return Y
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * Set X value
	 * @param x X
	 */
	public void setX(double x) {
		this.x = x;
	}
	
	/**
	 * Set Y value
	 * @param y Y
	 */
	public void setY(double y) {
		this.y = y;
	}
	
}
