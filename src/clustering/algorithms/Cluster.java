package clustering.algorithms;

import java.util.ArrayList;

public class Cluster {

	private int ID;
	private ArrayList<DenseRegion> regions;
	private boolean isActive;
	
	public Cluster(int id) {
		this.regions = new ArrayList<DenseRegion>();
		this.ID = id;
		this.isActive = true;
	}
	
	
	
	public int getID() {
		return ID;
	}
	
	public ArrayList<DenseRegion> getRegions() {
		return this.regions;
	}
	
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	public boolean getIsActive(){
		return this.isActive;
	}
	
	public void addDenseRegion(DenseRegion region){
		this.regions.add(region);
	}
	
}
