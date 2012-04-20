package datasets;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import org.jfree.ui.RefineryUtilities;

import plot.DatasetPlotter;

public class ChameleonData implements DatasetsIF{

	/**
	 * Load dataset as hashtable
	 */
	@Override
	public Hashtable<String, ArrayList<DatasetPoint>> load(String path) throws IOException {
		Hashtable<String, ArrayList<DatasetPoint>> clustersHash = new Hashtable<String, ArrayList<DatasetPoint>>();
	  String fileName = "1";
    clustersHash.put(fileName, new ArrayList<DatasetPoint>());
	  FileInputStream fstream = new FileInputStream(path);
	  DataInputStream in = new DataInputStream(fstream);
	  BufferedReader br = new BufferedReader(new InputStreamReader(in));
	  String strLine;
	  int id =0;
	  while ((strLine = br.readLine()) != null)   {
	   String [] tokens = strLine.split(" ");
	   String clusterID = fileName;
	   DatasetPoint p = new DatasetPoint(clusterID, Double.parseDouble(tokens[0]), Double.parseDouble(tokens[1]), id);
	   id++;
	   clustersHash.get(clusterID).add(p);
    }
		return clustersHash;
	}
	
	/**
	 * load dataset as array list
	 */
	@Override
	public ArrayList<DatasetPoint> loadArrayList(String path) throws IOException {
		ArrayList<DatasetPoint> dataset = new ArrayList<DatasetPoint>();
	  FileInputStream fstream = new FileInputStream(path);
	  DataInputStream in = new DataInputStream(fstream);
	  BufferedReader br = new BufferedReader(new InputStreamReader(in));
	  String strLine;
	  int id=0;
	  while ((strLine = br.readLine()) != null)   {
	   String [] tokens = strLine.split(" ");
	   String clusterID = "1";
	   DatasetPoint p = new DatasetPoint(clusterID, Double.parseDouble(tokens[0]), Double.parseDouble(tokens[1]), id);
	   id++;
	   dataset.add(p);
	  }
		return dataset;
	}

	public static void main(String[] args) throws IOException {
		ChameleonData dataset = new ChameleonData();
		Hashtable<String, ArrayList<DatasetPoint>> clustersHash = dataset.load("/media/disk/master/Courses/Machine_Learning/datasets/chameleon-data/t7.10k.dat");
		Enumeration keys = clustersHash.keys();
		while (keys.hasMoreElements()) {
			String clusterID = (String) keys.nextElement();
			System.out.println("Cluster "+ clusterID + " has "+ clustersHash.get(clusterID).size() +" points");
		}
		DatasetPlotter plotter = new DatasetPlotter("Clusters");
		plotter.plot(clustersHash);
		plotter.pack();
		RefineryUtilities.centerFrameOnScreen(plotter);
		plotter.setVisible(true); 

	}

}
