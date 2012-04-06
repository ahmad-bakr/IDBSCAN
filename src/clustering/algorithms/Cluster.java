package clustering.algorithms;

import java.util.ArrayList;

public class Cluster {

	private ArrayList<Integer> pointsList;
	
	public Cluster() {
		this.pointsList = new ArrayList<Integer>();
	}
	
	public void addToPointsList(ArrayList<Integer> list){
		this.pointsList.addAll(list);
	}
	
	public ArrayList<Integer> getPointsList() {
		return pointsList;
	}
	
	
}
