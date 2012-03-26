package clustering.algorithms;

import java.util.ArrayList;
import java.util.Hashtable;

import datasets.DatasetPoint;
import datasets.DatasetsIF;

public class DBSCAN {

	/**
	 * Run DBSCAN algorithm
	 * @param dataset datasets of points
	 * @param eps eps value
	 * @param minPts min pts value
	 */
	public void run(ArrayList<DatasetPoint> dataset, double eps, int minPts){
		int clusterLabel = 0;
		for (int i = 0; i < dataset.size(); i++) {
			DatasetPoint point = dataset.get(i);
			point.setVisited(true);
			ArrayList<DatasetPoint> regionQuery = getRegionQuery(point, eps);
			if(regionQuery.size() < minPts){
				point.setNoise(true);
			}else{
				exandCluster(point, regionQuery, clusterLabel, eps, minPts);
				clusterLabel++;
			}
		}
	}
	
	/**
	 * Get the region query of a specific point
	 * @param point point 
	 * @param eps eps
	 * @return region query of points
	 */
	private ArrayList<DatasetPoint> getRegionQuery(DatasetPoint point, double eps){
		ArrayList<DatasetPoint> list = new ArrayList<DatasetPoint>();
		return list;
	}
	
	/**
	 * Expand cluster
	 * @param point current point
	 * @param regionQuery region query of the point
	 * @param clusterLabel cluster label
	 * @param eps eps value
	 * @param minPts min pts value
	 */
	private void exandCluster(DatasetPoint point, ArrayList<DatasetPoint> regionQuery, int clusterLabel, double eps, int minPts ){
		point.setAssignedCluster(String.valueOf(clusterLabel));
		for (int i = 0; i < regionQuery.size(); i++) {
			DatasetPoint neighborPoint = regionQuery.get(i);
			if (!neighborPoint.getIsVisited()){
				neighborPoint.setVisited(true);
				ArrayList<DatasetPoint> regionQueryOfNeighborPoint = getRegionQuery(neighborPoint, eps);
				if(regionQueryOfNeighborPoint.size() >= minPts){
					// add regionQueryOfNeighborPoint to regionQuery
				}
			}
			if (neighborPoint.getAssignedCluster().equalsIgnoreCase("")){
				neighborPoint.setAssignedCluster(String.valueOf(clusterLabel));
			}
		}
	}
	
	
}
