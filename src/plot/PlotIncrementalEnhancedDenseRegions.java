package plot;

import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

import clustering.algorithms.DenseRegion;
import datasets.DatasetPoint;

public class PlotIncrementalEnhancedDenseRegions extends ApplicationFrame{
	
	public PlotIncrementalEnhancedDenseRegions(String title) {
		super(title);
	}
	
	public void plot(ArrayList<DenseRegion> regions, ArrayList<DatasetPoint> dataset){
		XYSeriesCollection datasetCollection = new XYSeriesCollection();
		for (int i = 0; i < regions.size(); i++) {
			DenseRegion d = regions.get(i);
			XYSeries series = new XYSeries(i);
			ArrayList<Integer> pointsIDs = d.getPoints();
			for (int j = 0; j < pointsIDs.size(); j++) {
				DatasetPoint p = dataset.get(pointsIDs.get(j));
				series.add(p.getX(), p.getY());
			}
			datasetCollection.addSeries(series);
		}
		JFreeChart chart = ChartFactory.createScatterPlot("Clusters", "X", "Y", datasetCollection, PlotOrientation.VERTICAL, true, true, false);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		setContentPane(chartPanel); 
	}

}
