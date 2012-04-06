package clustering.algorithms;

import java.util.ArrayList;

public class DenseRegion {
	
	private ArrayList<Integer> points;
	private ArrayList<Integer> boarderPoints;
	private int clusterID;
	private boolean isInCluster;
	
	public DenseRegion() {
		this.points = new ArrayList<Integer>();
		this.boarderPoints = new ArrayList<Integer>();
		this.isInCluster = false;
	}
	
	public void setClusterID(int clusterID) {
		this.clusterID = clusterID;
		this.isInCluster = true;
	}
	
	public boolean getIsInCluster(){
		return this.isInCluster;
	}
	
	public int getClusterID() {
		return clusterID;
	}
	
	public ArrayList<Integer> getBoarderPoints() {
		return boarderPoints;
	}
	public ArrayList<Integer> getPoints() {
		return points;
	}
	
	public void addPoint(int pointIndex){
		this.points.add(pointIndex);
	}
	
	public void addBoarderPoint(int pointIndex){
		this.boarderPoints.add(pointIndex);
		this.points.add(pointIndex);
	}
	
}
