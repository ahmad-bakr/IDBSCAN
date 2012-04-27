package clustering.incrementalAlgorithms;

import java.io.IOException;
import java.util.ArrayList;

import org.jfree.ui.RefineryUtilities;

import plot.PlotIncrementalEnhancedDenseRegions;

import clustering.algorithms.Cluster;
import clustering.algorithms.DenseRegion;
import clustering.partitioning.Centroid;
import clustering.partitioning.PartitioningAlgorithm;
import datasets.ChameleonData;
import datasets.DatasetPoint;

public class IncrementalEnhancedDBSCAN {
	private Centroid [] partitions;
	private ArrayList<Cluster> clusters;
	private ArrayList<DatasetPoint> dataset;
	private PartitioningAlgorithm partitioner;
	private ArrayList<DenseRegion> denseRegions;
	private IncrementalDBSCANPartitioner [] incrementalDBSCANs;
	private int minPts;
	private double eps;

	public IncrementalEnhancedDBSCAN(ArrayList<DatasetPoint> dataset, int numberOfPartitions, int minpts, double eps) {
		this.dataset = dataset;
		this.minPts = minpts;
		this.eps = eps;
		this.clusters = new ArrayList<Cluster>();
		this.denseRegions = new ArrayList<DenseRegion>();
		this.partitioner = new PartitioningAlgorithm(dataset, numberOfPartitions);
		this.partitions = partitioner.getCentroids();
		this.incrementalDBSCANs = new IncrementalDBSCANPartitioner[numberOfPartitions];
		for (int i = 0; i < numberOfPartitions; i++) {
			this.incrementalDBSCANs[i] = new IncrementalDBSCANPartitioner(i, this.minPts, this.eps, this.dataset);
		}
	}
	
	public void run(){
		for (int i = 0; i < this.dataset.size(); i++) {
			DatasetPoint point = this.dataset.get(i);
			int partitionIndex = this.partitioner.partitionPoint(point);
			IncrementalDBSCANPartitioner incrementalDBSCAN = this.incrementalDBSCANs[partitionIndex];
			incrementalDBSCAN.addPointToPartition(point);
		}
		
		for (int i = 0; i < this.incrementalDBSCANs.length; i++) {
			IncrementalDBSCANPartitioner p = incrementalDBSCANs[i];
			ArrayList<DenseRegion> regions = p.getDenseRegions();
			for (int j = 0; j < regions.size(); j++) {
				DenseRegion d = regions.get(j);
				if(d.getActive()) this.denseRegions.add(d);
			}
		}
	}
	
	public ArrayList<DenseRegion> getDenseRegions() {
		return denseRegions;
	}

	
	public static void main(String[] args) throws IOException {
		int numPartitions =70;
		double eps = 10;
		int minPts= 20;
		ChameleonData datasetLoader = new ChameleonData();
		ArrayList<DatasetPoint> dataset = datasetLoader.loadArrayList("/media/disk/master/Courses/Machine_Learning/datasets/chameleon-data/t4.8k.dat");	
		IncrementalEnhancedDBSCAN algorithm = new IncrementalEnhancedDBSCAN(dataset, numPartitions, minPts, eps);
		algorithm.run();
		ArrayList<DenseRegion> denseRegions = algorithm.getDenseRegions();		
		System.out.println("done");
		PlotIncrementalEnhancedDenseRegions plotter = new PlotIncrementalEnhancedDenseRegions("Dense Regions");
		plotter.plot(denseRegions, dataset);
		plotter.pack();
		RefineryUtilities.centerFrameOnScreen(plotter);
		plotter.setVisible(true); 

	}
}
