package clustering.incrementalAlgorithms;

import java.util.ArrayList;

import clustering.algorithms.Cluster;
import clustering.partitioning.Centroid;
import datasets.DatasetPoint;

public class IncrementalEnhancedDBSCAN {
	private ArrayList<Centroid> partitions;
	private ArrayList<Cluster> clusters;
	private ArrayList<DatasetPoint> dataset;

	public IncrementalEnhancedDBSCAN(ArrayList<DatasetPoint> dataset) {
		this.dataset = dataset;
		this.partitions = new ArrayList<Centroid>();
		this.clusters = new ArrayList<Cluster>();
	}

}
