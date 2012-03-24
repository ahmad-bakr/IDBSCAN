package datasets;

public class DatasetPoint {
	private double x;
	private double y;
	private String originalCluster;
	
	public DatasetPoint(String cluster, double x, double y) {
		this.x = x;
		this.y = y;
		this.originalCluster = cluster;
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

}
