package plot;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Set;

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

public class PlotDBSCANPartitioner extends ApplicationFrame{

		public PlotDBSCANPartitioner(String title) {
			super(title);
		}
		
		public void plot(ArrayList<DatasetPoint> dataset, Node solution){
			XYSeriesCollection datasetCollection = new XYSeriesCollection();
			Hashtable<String, XYSeries> seensRegons = new Hashtable<String, XYSeries>();
			Medoid [] medoids = solution.getMedoids();
			for (int i = 0; i < medoids.length; i++) {
				Medoid m = medoids[i];
				ArrayList<Integer> assignedPointsToM = solution.getMedoidsAssignedPoints().get(i);
				for (int j = 0; j < assignedPointsToM.size(); j++) {
					DatasetPoint p = dataset.get(assignedPointsToM.get(j));
					if (!p.getIsBoarder()) continue; 
					if(seensRegons.containsKey(p.getAssignedCluster())){
						seensRegons.get(p.getAssignedCluster()).add(p.getX(), p.getY());
					}else{
						XYSeries seriesMed = new XYSeries(p.getAssignedCluster());
						seriesMed.add(p.getX(), p.getY());
						seensRegons.put(p.getAssignedCluster(), seriesMed);
					}
				}//end looping for medoid's points
			}// end looping for medoids
			
			Enumeration keys = seensRegons.keys();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				datasetCollection.addSeries(seensRegons.get(key));
			}
			JFreeChart chart = ChartFactory.createScatterPlot("Clusters", "X", "Y", datasetCollection, PlotOrientation.VERTICAL, true, true, false);
			ChartPanel chartPanel = new ChartPanel(chart);
			chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
			setContentPane(chartPanel); 

		}
}
