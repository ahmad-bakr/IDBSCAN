package clustering.incrementalAlgorithms;

import java.io.IOException;
import java.util.ArrayList;

import clustering.algorithms.Cluster;
import clustering.partitioning.Centroid;
import clustering.partitioning.PartitioningAlgorithm;
import datasets.ChameleonData;
import datasets.DatasetPoint;

public class IncrementalEnhancedDBSCAN {
	private Centroid [] partitions;
	private ArrayList<Cluster> clusters;
	private ArrayList<DatasetPoint> dataset;
	private PartitioningAlgorithm partitioner;
	private IncrementalDBSCANPartitioner [] incrementalDBSCANs;
	private int minPts;
	private double eps;

	public IncrementalEnhancedDBSCAN(ArrayList<DatasetPoint> dataset, int numberOfPartitions, int minpts, double eps) {
		this.dataset = dataset;
		this.minPts = minpts;
		this.eps = eps;
		this.clusters = new ArrayList<Cluster>();
		this.partitioner = new PartitioningAlgorithm(dataset, numberOfPartitions);
		this.partitions = partitioner.getCentroids();
		this.incrementalDBSCANs = new IncrementalDBSCANPartitioner[numberOfPartitions];
		for (int i = 0; i < numberOfPartitions; i++) {
			this.incrementalDBSCANs[i] = new IncrementalDBSCANPartitioner(i, this.minPts, this.eps);
		}
	}
	
	public void run(){
		for (int i = 0; i < this.dataset.size(); i++) {
			DatasetPoint point = this.dataset.get(i);
			int partitionIndex = this.partitioner.partitionPoint(point);
			IncrementalDBSCANPartitioner incrementalDBSCAN = this.incrementalDBSCANs[partitionIndex];
			incrementalDBSCAN.addPointToPartition(point);
		}
	}

	
	public static void main(String[] args) throws IOException {
		int numPartitions =9;
		double eps = 10;
		int minPts= 4;
		ChameleonData datasetLoader = new ChameleonData();
		ArrayList<DatasetPoint> dataset = datasetLoader.loadArrayList("/media/disk/master/Courses/Machine_Learning/datasets/chameleon-data/t8.8k.dat");	
		IncrementalEnhancedDBSCAN algorithm = new IncrementalEnhancedDBSCAN(dataset, numPartitions, minPts, eps);
		algorithm.run();
		System.out.println("done");
	}
}
