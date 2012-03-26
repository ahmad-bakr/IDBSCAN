package datasets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

public interface DatasetsIF {
	Hashtable<String, ArrayList<DatasetPoint>> load (String path) throws IOException;
	ArrayList<DatasetPoint> loadArrayList (String path)throws IOException;
}
