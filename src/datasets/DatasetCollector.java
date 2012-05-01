package datasets;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class DatasetCollector {

	public void collectDatasets(String [] paths) throws IOException{
	 FileWriter fstreamWrite = new FileWriter("all.txt");
	 BufferedWriter out = new BufferedWriter(fstreamWrite);
		for (int i = 0; i < paths.length; i++) {
			String path = paths[i];
			 FileInputStream fstream = new FileInputStream(path);
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
			  while ((strLine = br.readLine()) != null)   {
				  out.write(strLine+"\n");
			  }
			  in.close();
		}
		out.close();
	}
	
	public static void main(String[] args) throws Exception {
		String [] paths = new String[4];
		paths[0] = "/media/disk/master/Courses/Machine_Learning/datasets/chameleon-data/t7.10k.dat";
		paths[1] = "/media/disk/master/Courses/Machine_Learning/datasets/chameleon-data/t4.8k.dat" ;
		paths[2] = "/media/disk/master/Courses/Machine_Learning/datasets/chameleon-data/t5.8k.dat" ;
		paths[3] = "/media/disk/master/Courses/Machine_Learning/datasets/chameleon-data/t8.8k.dat" ;
		
		
		DatasetCollector collecter = new DatasetCollector();
		collecter.collectDatasets(paths);

	}
}
