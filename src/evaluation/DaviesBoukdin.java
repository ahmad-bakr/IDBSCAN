package evaluation;

import java.util.ArrayList;

import clustering.algorithms.Cluster;
import clustering.algorithms.DenseRegion;
import datasets.DatasetPoint;

public class DaviesBoukdin {
	private ArrayList<DatasetPoint> dataset;
	private ArrayList<Cluster> clusters;

	public DaviesBoukdin(ArrayList<Cluster> clusters, ArrayList<DatasetPoint> dataset) {
		this.dataset = dataset;
		this.clusters = clusters;
	}
	
	public DaviesBoukdin(ArrayList<Cluster> clusters, ArrayList<DenseRegion> denseRegions ,ArrayList<DatasetPoint> dataset) {
		this.dataset = dataset;
		this.clusters = clusters;
		for (int i = 0; i < clusters.size(); i++) {
			Cluster c = this.clusters.get(i);
			if(!c.getIsActive()) continue;
   		ArrayList<DenseRegion> clusterDenseRegions = c.getRegions();
   		for (int j = 0; j < clusterDenseRegions.size(); j++) {
				DenseRegion d = clusterDenseRegions.get(j);
				c.addPointsList(d.getPoints());
   		}
		}
	}
	
	public double calculateDaviesMeasure(){
		double daviseMeasure = 0;
		for (int i = 0; i < this.clusters.size(); i++) {
			Cluster ci = this.clusters.get(i);
			if(!ci.getIsActive()) continue;
			double maxDistaceForCi = Double.MIN_VALUE;
			double avgDistanceForCi = calculateAverageDistanceInCluster(ci);
			for (int j = 0; j < this.clusters.size(); j++) {
				Cluster cj = this.clusters.get(j);
				if(ci.getID() == cj.getID() || !cj.getIsActive()) continue;
				double distance = calculateDaviesMeasureBetweenTwoCluster(avgDistanceForCi, ci, cj);
				if(distance > maxDistaceForCi){
					maxDistaceForCi = distance;
				}
			}
			daviseMeasure += maxDistaceForCi;
		}
		return daviseMeasure/this.clusters.size();
	}
	
	private double calculateDaviesMeasureBetweenTwoCluster(double avgDistanceForCi,Cluster ci ,Cluster cj){
		double davies = 0;
		davies = (avgDistanceForCi+calculateAverageDistanceInCluster(cj))/calculateDistanceBetweenTwoClusters(ci, cj);
		return davies;
	}
	
	/**
	 * calculate the minimum distance between two clusters
	 * @param ci cluster ci
	 * @param cj cluster cj
	 * @return min distance between ci and cj
	 */
	private double calculateDistanceBetweenTwoClusters(Cluster ci, Cluster cj){
		double distance = 0;
		ArrayList<Integer> ciPoints =	ci.getPointsIDs();
		ArrayList<Integer> cjPoints = cj.getPointsIDs();
		for (int i = 0; i < ciPoints.size(); i++) {
			DatasetPoint ciPoint = this.dataset.get(ciPoints.get(i));
			for (int j = 0; j < cjPoints.size(); j++) {
				DatasetPoint cjPoint = this.dataset.get(cjPoints.get(j));
				distance += calculateDistanceBtwTwoPoints(ciPoint, cjPoint);
				}
			}
		
		return distance/(ciPoints.size()*cjPoints.size());
	}
	
	
	/**
	 * Calculate the cluster size or diameter
	 * @param c cluster
	 * @return cluster size or diameter
	 */
	private double calculateAverageDistanceInCluster(Cluster c){
		double size = 0;
		ArrayList<Integer> clusterPoints = c.getPointsIDs();
		for (int i = 0; i < clusterPoints.size(); i++) {
			DatasetPoint point1 = this.dataset.get(clusterPoints.get(i));
			for (int j = 0; j < clusterPoints.size(); j++) {
				if(i==j) continue;
				DatasetPoint point2 = this.dataset.get(clusterPoints.get(j));
				size += calculateDistanceBtwTwoPoints(point1, point2);
			}
		}
		return size/(clusterPoints.size()*(clusterPoints.size()-1));
	}

	
	/**
	 * calculate the Euclidean Distance between two points
	 * @param p1 point 1
	 * @param p2 point 2
	 * @return Distance
	 */
	private double calculateDistanceBtwTwoPoints(DatasetPoint p1, DatasetPoint p2){
		double xDiff = p1.getX() - p2.getX();
		double yDiff = p1.getY() - p2.getY();
		return Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
	}

	
	
}
