package clustering.partitioning;

import java.util.ArrayList;

import clustering.algorithms.DenseRegion;

public class Medoid {
	
	private double x;
	private double y;
	int id;
	private ArrayList<DenseRegion> regions;
	
	/**
	 * Medoid Constructor
	 * @param x medoid X
	 * @param y mdeoid Y
	 */
	public Medoid(int id,double x, double y) {
		this.x = x;
		this.y =y;
		this.id = id;
		this.regions = new ArrayList<DenseRegion>();
	}
	
	public int getId() {
		return id;
	}
	public void addRegion(DenseRegion r){
		this.regions.add(r);
	}
	
	public ArrayList<DenseRegion> getRegions() {
		return regions;
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
