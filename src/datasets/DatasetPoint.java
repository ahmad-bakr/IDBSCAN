package datasets;

import java.util.ArrayList;

public class DatasetPoint {
	private double x;
	private double y;
	private String originalCluster;
	private String assignedCluster;
	private boolean isVisited;
	private boolean isNoise;
	private boolean isBoarder;
	private int ID;
	private int indexInPartition;
	private int pointCausedToBeCore;
	private ArrayList<Integer> pointsAtEpsIndexs;
	private int assignedCentroidID;
	
	public DatasetPoint(String cluster, double x, double y, int id) {
		this.x = x;
		this.y = y;
		this.pointCausedToBeCore =-1;
		this.originalCluster = cluster;
		this.isVisited = false;
		this.isNoise = false;
		this.isBoarder = false;
		this.assignedCluster="";
		this.ID = id;
		this.pointsAtEpsIndexs = new ArrayList<Integer>(); 
	}
	
	public int getPointCausedToBeCore() {
		return pointCausedToBeCore;
	}
	
	public void setAssignedCentroidID(int assignedCentroidID) {
		this.assignedCentroidID = assignedCentroidID;
	}
	
	public int getAssignedCentroidID() {
		return assignedCentroidID;
	}
	
	
	public void setPointCausedToBeCore(int pointCausedToBeCore) {
		this.pointCausedToBeCore = pointCausedToBeCore;
	}
	
	public ArrayList<Integer> getPointsAtEpsIndexs() {
		return pointsAtEpsIndexs;
	}
	
	public void addToNeighborhoodPoints(int i){
		this.pointsAtEpsIndexs.add(i);
	}
	
	public void setBoarder(boolean isBoarder) {
		this.isBoarder = isBoarder;
	}
	
	public boolean getIsCore(int minPts){
		if(this.pointsAtEpsIndexs.size()>= minPts) return true;
		return false;
	}
	
	public boolean getIsBoarder(){
		return this.isBoarder;
	}
	
	public void setVisited(boolean isVisited) {
		this.isVisited = isVisited;
	}
	
	public void setNoise(boolean isNoise) {
		this.isNoise = isNoise;
	}
	
	public void setAssignedCluster(String assignedCluster) {
		this.assignedCluster = assignedCluster;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public void setOriginalCluster(String originalCluster) {
		this.originalCluster = originalCluster;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public String getOriginalCluster() {
		return originalCluster;
	}
	
	public String getAssignedCluster() {
		return assignedCluster;
	}
	
	public boolean getIsNoise(){
		return this.isNoise;
	}
	
	public boolean getIsVisited(){
		return this.isVisited;
	}
	
	public int getID() {
		return ID;
	}
	
	public void setID(int iD) {
		ID = iD;
	}
	
	public void setIndexInPartition(int indexInPartition) {
		this.indexInPartition = indexInPartition;
	}
	
	public int getIndexInPartition() {
		return indexInPartition;
	}
	
	

}
