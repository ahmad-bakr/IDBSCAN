package datasets;

public class DatasetPoint {
	private double x;
	private double y;
	private String originalCluster;
	private String assignedCluster;
	private boolean isVisited;
	private boolean isNoise;
	private int ID;
	
	public DatasetPoint(String cluster, double x, double y, int id) {
		this.x = x;
		this.y = y;
		this.originalCluster = cluster;
		this.isVisited = false;
		this.isNoise = false;
		this.assignedCluster="";
		this.ID = id;
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

}
