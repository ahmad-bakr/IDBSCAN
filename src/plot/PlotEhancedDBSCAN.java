package plot;

import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

import clustering.algorithms.Cluster;
import clustering.algorithms.DenseRegion;
import datasets.DatasetPoint;

public class PlotEhancedDBSCAN extends ApplicationFrame{

	public PlotEhancedDBSCAN(String title) {
		super(title);
	}
	
	public void plot(ArrayList<DatasetPoint> dataset,ArrayList<Cluster> clusters){
		XYSeriesCollection datasetCollection = new XYSeriesCollection();

		for (int i = 0; i < clusters.size(); i++) {
			Cluster c = clusters.get(i);
			if(!c.getIsActive()) continue;
			ArrayList<DenseRegion> regions = c.getRegions();
			
			int totalNumberOfPoints = 0;
			for (int j = 0; j < regions.size(); j++) {
				totalNumberOfPoints+= regions.get(j).getPoints().size();
			}
			
			if(totalNumberOfPoints < 50) continue;
			
			
			XYSeries series = new XYSeries(i);

			for (int j = 0; j < regions.size(); j++) {
				DenseRegion r = regions.get(j);
				ArrayList<Integer> points = r.getPoints();
				for (int k = 0; k < points.size(); k++) {
					DatasetPoint p = dataset.get(points.get(k));
					series.add(p.getX(), p.getY());

				}
			}
			datasetCollection.addSeries(series);
		}
		JFreeChart chart = ChartFactory.createScatterPlot("Clusters", "X", "Y", datasetCollection, PlotOrientation.VERTICAL, true, true, false);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		setContentPane(chartPanel); 

	}
}
