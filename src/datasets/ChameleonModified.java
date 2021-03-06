package datasets;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.xml.crypto.Data;

import org.jfree.ui.RefineryUtilities;

import plot.DatasetPlotter;

public class ChameleonModified implements DatasetsIF{
	
	/**
	 * Load dataset points from the file
	 * @param datasetPath dataset path
	 * @return
	 * @throws IOException
	 */
	public Hashtable<String, ArrayList<DatasetPoint>> load(String datasetPath) throws IOException{
		Hashtable<String, ArrayList<DatasetPoint>> clustersHash = new Hashtable<String, ArrayList<DatasetPoint>>();
		FileInputStream fstream = new FileInputStream(datasetPath);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		int id =0;
	  while ((strLine = br.readLine()) != null)   {
	  	String [] tokens = strLine.split("\t");
	  	String clusterID = tokens[0];
	  	DatasetPoint p = new DatasetPoint(clusterID, Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2]), id);
	  	id++;
	  	if (clustersHash.containsKey(clusterID)){
	  		 clustersHash.get(clusterID).add(p);
	  	}else{
	  		ArrayList<DatasetPoint> list = new ArrayList<DatasetPoint>();
	  		list.add(p);
	  		clustersHash.put(clusterID, list);
	  	}
	  }
	  in.close();
		return clustersHash;
	}


	@Override
	public ArrayList<DatasetPoint> loadArrayList(String path) throws IOException {
		ArrayList<DatasetPoint> list = new ArrayList<DatasetPoint>();
		FileInputStream fstream = new FileInputStream(path);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		int id=0;
	  while ((strLine = br.readLine()) != null)   {
	  	String [] tokens = strLine.split("\t");
	  	String clusterID = tokens[0];
	  	DatasetPoint p = new DatasetPoint(clusterID, Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2]),id);
	  	id++;
	  	list.add(p);
	  }
	  in.close();

		return list;
	}

	public static void main(String[] args) throws IOException {
		ChameleonModified datasetLoader = new ChameleonModified();
	//	Hashtable<String, ArrayList<DatasetPoint>> clustersHash = dataset.load("/media/disk/master/Courses/Machine_Learning/datasets/chameleon_modified.txt");
		ArrayList<DatasetPoint> dataset = datasetLoader.loadArrayList("/media/disk/master/Courses/Machine_Learning/datasets/gaussian2clusters.txt");

		DatasetPlotter plotter = new DatasetPlotter("Clusters");
		plotter.plotList(dataset);
		plotter.pack();
		RefineryUtilities.centerFrameOnScreen(plotter);
		plotter.setVisible(true); 

	}
}
