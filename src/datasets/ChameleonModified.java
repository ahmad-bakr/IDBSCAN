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

public class ChameleonModified {
	
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
	  while ((strLine = br.readLine()) != null)   {
	  	String [] tokens = strLine.split("\t");
	  	String clusterID = tokens[0];
	  	DatasetPoint p = new DatasetPoint(clusterID, Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2]));
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
	
	public static void main(String[] args) throws IOException {
		ChameleonModified dataset = new ChameleonModified();
		Hashtable<String, ArrayList<DatasetPoint>> clustersHash = dataset.load("/media/disk/master/Courses/Machine_Learning/datasets/chameleon_modified.txt");
		Enumeration keys = clustersHash.keys();
		while (keys.hasMoreElements()) {
			String clusterID = (String) keys.nextElement();
			System.out.println("Cluster "+ clusterID + " has "+ clustersHash.get(clusterID).size() +" points");
		}
	}
}
