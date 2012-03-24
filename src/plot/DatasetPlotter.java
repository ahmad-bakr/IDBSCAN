package plot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import datasets.ChameleonModified;
import datasets.DatasetPoint;
import datasets.DatasetsIF;

public class DatasetPlotter extends ApplicationFrame{

	public DatasetPlotter(String title) {
		super(title);
	}
	
	/**
	 * Draw the dataset
	 * @param clustersHash clusters hash
	 */
	public void plot(Hashtable<String, ArrayList<DatasetPoint>> clustersHash){
		XYSeriesCollection datasetCollection = new XYSeriesCollection();
		Enumeration keys = clustersHash.keys();
		while (keys.hasMoreElements()) {
			String clusterID = (String) keys.nextElement();
			XYSeries series = new XYSeries(clusterID);
			ArrayList<DatasetPoint> pointsList = clustersHash.get(clusterID);
			for (int i = 0; i < pointsList.size(); i++) {
				DatasetPoint p = pointsList.get(i);
				series.add(p.getX(), p.getY());
			}// end looping for points
			datasetCollection.addSeries(series);
		}// end looping for clusters
		JFreeChart chart = ChartFactory.createScatterPlot("Clusters", "X", "Y", datasetCollection, PlotOrientation.VERTICAL, true, true, false);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		setContentPane(chartPanel); 

	}
	
	public static void main(String[] args) throws IOException {
		DatasetsIF dataset = new ChameleonModified();
		Hashtable<String, ArrayList<DatasetPoint>> clustersHash = dataset.load("/media/disk/master/Courses/Machine_Learning/datasets/chameleon_modified.txt");
		DatasetPlotter plotter = new DatasetPlotter("Clusters");
		plotter.plot(clustersHash);
		plotter.pack();
		RefineryUtilities.centerFrameOnScreen(plotter);
		plotter.setVisible(true); 

	}
}
