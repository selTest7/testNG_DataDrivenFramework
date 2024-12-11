package runner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONRunner {

	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
		// TODO Auto-generated method stub
		Map <String, String> class_methods = new DataUtil().load_class_methods();

		System.out.println(class_methods);
		
		String path = System.getProperty("user.dir")+"//src//test//resources//testconfig.json";
		
		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject) parser.parse(new FileReader(new File(path)));
		String parallel_exec = (String) json.get("parallelsuites");
		
		TestNGRunner runner = new TestNGRunner(Integer.parseInt(parallel_exec));
		
		JSONArray testsuites = (JSONArray) json.get("testsuites");
		
		for(int i=0; i<testsuites.size(); i++) {
			JSONObject cur_obj = (JSONObject) testsuites.get(i);
			String runmode = (String) cur_obj.get("runmode");
			
			if(runmode.equals("Y")) {
				String paralleltests = (String) cur_obj.get("paralleltests");
				String test_name = (String) cur_obj.get("name");
				String suitefile_name = (String) cur_obj.get("suitefilename");
				String testdata_json = (String) cur_obj.get("testdatajsonfile");
				System.out.println(runmode+"--"+test_name);
				
				boolean par_test = false;
				
				if(paralleltests.equals("Y")) {
					par_test = true;
				}
				
				runner.createSuite(test_name, par_test);
				runner.addListener("listener.myTestNGListener");
				
				path = System.getProperty("user.dir")+"//src//test//resources//"+suitefile_name;
				
				JSONParser suiteParser = new JSONParser();
				JSONObject suiteJson = (JSONObject) suiteParser.parse(new FileReader(new File(path)));
				JSONArray suite_testcases = (JSONArray) suiteJson.get("testcases");
				
				for(int j=0; j<suite_testcases.size(); j++) {
					JSONObject cur_testcase = (JSONObject) suite_testcases.get(j);
					JSONArray parameter_names = (JSONArray) cur_testcase.get("parameternames");
					String t_name = (String) cur_testcase.get("name");
							
					JSONArray executions = (JSONArray) cur_testcase.get("executions");
						
					for(int k=0; k<executions.size(); k++) {
						JSONObject testcase = (JSONObject) executions.get(k);
						String testcase_name = (String) testcase.get("executionname");
						JSONArray param_val = (JSONArray) testcase.get("parametervalues");
						String test_runmode = (String) testcase.get("runmode");
						
						if(test_runmode!=null && test_runmode.equals("Y")) {

							JSONArray testcase_methods = (JSONArray) testcase.get("methods");
							String data_flag = (String) testcase.get("dataflag");
							
							int test_data_it = new DataUtil().getTestDataIterations(testdata_json, data_flag);
							
							for(int dt=0; dt<test_data_it; dt++) {
								//System.out.println(data_flag);
								//System.out.println(parameter_names+"--"+param_val);
								//System.out.println(testcase_methods);
								
								runner.addTest(t_name+"-"+testcase_name+" It-"+(dt+1));
								
								for(int a=0; a<param_val.size(); a++) {
									runner.addTestParameter((String) parameter_names.get(a), (String) param_val.get(a));
								}
								runner.addTestParameter("datafilepath", testdata_json);
								runner.addTestParameter("dataflag", data_flag);
								runner.addTestParameter("iteration", String.valueOf(dt));
								
								List<String> incMethods = new ArrayList<>();
								
								for(int b=0; b<testcase_methods.size(); b++) {
									String mth = (String) testcase_methods.get(b);
									String methodclass = class_methods.get(mth);
									
									if(b==testcase_methods.size()-1 || !((String)class_methods.get((String)testcase_methods.get(b+1))).equals(methodclass)) {
										incMethods.add(mth);
										//System.out.println(incMethods);
										runner.addTestClass(methodclass, incMethods);
										incMethods = new ArrayList<>();
									}
									else {
										incMethods.add(mth);
									}
								}
							}
						}
					}
				}
				
			}

		}
		runner.run();
	}

}
