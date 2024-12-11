package temp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ReadingProperties {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String path = System.getProperty("user.dir")+"//src//test//resources//project.properties";
		System.out.println(path);
		
		Properties prop = new Properties();
		FileInputStream fs = new FileInputStream(path);
		prop.load(fs);
		
		System.out.println(prop.get("url"));
	}

}
