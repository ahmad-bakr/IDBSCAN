package plot;

import java.awt.Color;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class Plotter extends ApplicationFrame{
	
	public Plotter(String title) {
		super(title);
		
	}
	
	
	public void plot(){
	// create a dataset...
		XYSeries series1 = new XYSeries("Planned");
		series1.add(1.0, 1.0);
		series1.add(2.0, 4.0);
		series1.add(3.0, -3.0);
		series1.add(4.0, 5.0);
		series1.add(5.0, 5.0);
		series1.add(6.0, 7.0);
		series1.add(7.0, 7.0);
		series1.add(8.0, 8.0);

		XYSeries series2 = new XYSeries("Delivered");
		series2.add(1.0, 5.0);
		series2.add(2.0, 7.0);
		series2.add(3.0, 6.0);
		series2.add(4.0, 8.0);
		series2.add(5.0, -4.0);
		series2.add(6.0, 4.0);
		series2.add(7.0, 2.0);
		series2.add(8.0, 1.0);

		XYSeries series3 = new XYSeries("Third");
		series3.add(3.0, 4.0);
		series3.add(4.0, 3.0);
		series3.add(5.0, 2.0);
		series3.add(6.0, -3.0);
		series3.add(7.0, 6.0);
		series3.add(8.0, 3.0);
		series3.add(9.0, -4.0);
		series3.add(10.0, 3.0);

		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series1);
		dataset.addSeries(series2);
		dataset.addSeries(series3);

		JFreeChart chart = ChartFactory.createScatterPlot("Line Chart Demo", "X", "Y", dataset, PlotOrientation.VERTICAL, true, true, false);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		setContentPane(chartPanel); 
	}

	public static void main(String[] args) {
		Plotter demo = new Plotter("Scatter Plot Demo");
		demo.plot();
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true); 

	}
}
