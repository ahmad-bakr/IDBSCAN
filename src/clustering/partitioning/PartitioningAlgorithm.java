package clustering.partitioning;

import java.io.IOException;
import java.util.ArrayList;

import org.jfree.ui.RefineryUtilities;

import plot.PlotPartitioning;

import datasets.ChameleonData;
import datasets.DatasetPoint;

public class PartitioningAlgorithm {
 	private ArrayList<DatasetPoint> dataset;
	private Centroid[] centroids;
	public PartitioningAlgorithm(ArrayList<DatasetPoint> dataset, int k) {
		this.dataset= dataset;
		this.centroids = new Centroid[k];
		for (int i = 0; i < k; i++) {
			int randomIndex = 0 + (int)(Math.random() * (((200-1) - 0) + 1));
			DatasetPoint p = this.dataset.get(randomIndex);
			this.centroids[i] = new Centroid(i, p.getX(), p.getY());
		}
	}
	
	public Centroid[] getCentroids() {
		return centroids;
	}
	
	public void run(){
		for (int i = 0; i < this.dataset.size(); i++) {
			DatasetPoint point = this.dataset.get(i);
			double distance = Double.MAX_VALUE;
			Centroid cen = null;
			for (int j = 0; j < this.centroids.length; j++) {
				double d = calculateDistanceBtwTwoPoints(this.centroids[j].getX(), this.centroids[j].getY(), point);
				if(d<distance){
					cen = this.centroids[j];
					distance = d;
				}
			}
			point.setAssignedCentroidID(cen.getID());
			cen.updateCentroid(point);
		}
	}
	
	private double calculateDistanceBtwTwoPoints(double x, double y, DatasetPoint p2){
		double xDiff = x - p2.getX();
		double yDiff = y - p2.getY();
		return Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
	}

	
	public static void main(String[] args) throws IOException {
		int k =3;
		ChameleonData datasetLoader = new ChameleonData();
		ArrayList<DatasetPoint> dataset = datasetLoader.loadArrayList("/media/disk/master/Courses/Machine_Learning/datasets/chameleon-data/t5.8k.dat");
		PartitioningAlgorithm p = new PartitioningAlgorithm(dataset, k);
		p.run();
		PlotPartitioning plotter = new PlotPartitioning("partitions");
		plotter.plot(dataset, p.getCentroids());
		plotter.pack();
		RefineryUtilities.centerFrameOnScreen(plotter);
		plotter.setVisible(true); 

	}
	
}
