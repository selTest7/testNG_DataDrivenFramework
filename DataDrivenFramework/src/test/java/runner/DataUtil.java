package runner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DataUtil {
	
	public Map<String, String> load_class_methods() throws FileNotFoundException, IOException, ParseException {
		Map <String, String> class_method_map = new HashMap<>();
		
		String path = System.getProperty("user.dir")+"//src//test//resources//classmethods.json";
		
		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject) parser.parse(new FileReader(new File(path)));
		//System.out.println(json.toString());
		
		JSONArray classMethods = (JSONArray) json.get("classdetails");
		//System.out.println(classMethods.toJSONString());
		
		for(int i=0; i<classMethods.size(); i++) {
			JSONObject class_det = (JSONObject) classMethods.get(i);
			JSONArray methods = (JSONArray) class_det.get("methods");
			String class_name = (String) class_det.get("class");
			
			//System.out.println(class_name);
			for(int j=0; j<methods.size(); j++) {
				String method_name = (String) methods.get(j);
				//System.out.println(method_name);
				class_method_map.put(method_name, class_name);
			}
			//System.out.println("------");
		}
		
		//System.out.println(class_method_map);
		
		return class_method_map;
	}
	
	public int getTestDataIterations(String pathfile, String dataflag) throws FileNotFoundException, IOException, ParseException {
		String path = System.getProperty("user.dir")+"//src//test//resources//"+pathfile;
		
		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject) parser.parse(new FileReader(new File(path)));
		JSONArray arr = (JSONArray) json.get("testdata");
		
		for(int i=0; i<arr.size(); i++) {
			JSONObject cur = (JSONObject) arr.get(i);
			String flag = (String) cur.get("flag");
			
			if(dataflag.equals(flag)) {
				JSONArray data = (JSONArray) cur.get("data");
				return data.size();
			}
		}
		
		return -1;
	}
	
	public Map<String, String> getTestData(String pathfile, String dataflag, int it) throws FileNotFoundException, IOException, ParseException{
		String path = System.getProperty("user.dir")+"//src//test//resources//"+pathfile;
		
		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject) parser.parse(new FileReader(new File(path)));
		JSONArray arr = (JSONArray) json.get("testdata");
		
		for(int i=0; i<arr.size(); i++) {
			JSONObject cur = (JSONObject) arr.get(i);
			String flag = (String) cur.get("flag");
			
			if(dataflag.equals(flag)) {
				JSONArray data = (JSONArray) cur.get("data");
				JSONObject obj = (JSONObject) data.get(it);
				
				return obj;
			}
		}
		
		return null;
	}
}
