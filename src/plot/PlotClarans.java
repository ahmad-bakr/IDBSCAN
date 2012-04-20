package plot;

import java.util.ArrayList;
import java.util.Hashtable;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

import clustering.partitioning.Medoid;
import clustering.partitioning.Node;

import datasets.DatasetPoint;

public class PlotClarans extends ApplicationFrame{

	public PlotClarans(String title) {
		super(title);
	}

	public void plotNode (ArrayList<DatasetPoint> dataset, Node node){
		XYSeriesCollection datasetCollection = new XYSeriesCollection();
		Medoid [] medoids = node.getMedoids();
		ArrayList<ArrayList<Integer>> medoidsAssingedPoints = node.getMedoidsAssignedPoints();
		for (int i = 0; i < medoids.length; i++) {
			XYSeries series = new XYSeries(i);
			Medoid m = medoids[i];
			ArrayList<Integer> medoidsPoints = medoidsAssingedPoints.get(i);
			for (int j = 0; j < medoidsPoints.size(); j++) {
				DatasetPoint p = dataset.get(medoidsPoints.get(j));
				if(p.getX() == m.getX() && p.getY() == m.getY()) continue;
				series.add(p.getX(), p.getY());
			}
				datasetCollection.addSeries(series);
				XYSeries seriesMed = new XYSeries("m"+String.valueOf(i));
				seriesMed.add(m.getX(), m.getY());
				datasetCollection.addSeries(seriesMed);
		}
		
		JFreeChart chart = ChartFactory.createScatterPlot("Clusters", "X", "Y", datasetCollection, PlotOrientation.VERTICAL, true, true, false);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		setContentPane(chartPanel); 

	}

}
