package testbase;

import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import keywords.ApplicationKeywords;
import reports.ExtentManager;
import runner.DataUtil;

//3 Types of Failures in Tests
	//1: Acceptable Failure
	//2: Critical Failure
	//3: Unexpected Failure


//How to configure and run in GRID
//How to run this test with JSON Config
//How to manage data from XLS or JSON
//Running from GIT and Jenkins

public class BaseTest {
	
	public ApplicationKeywords app;
	public ExtentReports rep;
	public ExtentTest test;
	
	@BeforeTest(alwaysRun = true)
	public void beforeTest(ITestContext cont) throws IOException, NumberFormatException, ParseException {
		System.out.println("----Before Test----");
		String datafilepath = cont.getCurrentXmlTest().getParameter("datafilepath");
		String dataflag = cont.getCurrentXmlTest().getParameter("dataflag");
		String iteration = cont.getCurrentXmlTest().getParameter("iteration");

		System.out.println(dataflag);
		System.out.println(datafilepath);
		System.out.println(iteration);
		
		//Read Test data
		JSONObject data = (JSONObject) new DataUtil().getTestData(datafilepath, dataflag, Integer.parseInt(iteration));
		
		//Initialize the Extent Reports
		rep = ExtentManager.getReports();
		test = rep.createTest(cont.getCurrentXmlTest().getName());
		test.log(Status.INFO, "Starting Test: "+cont.getCurrentXmlTest().getName());
		cont.setAttribute("report", rep);
		cont.setAttribute("test", test);
		
		cont.setAttribute("data", data);
		
		if(!data.get("runmode").equals("Y")) {
			test.log(Status.SKIP, "Data runmode is No");
			throw new SkipException("Data runmode is No");
		}
		
		//Initialize the Application Keywords
		app = new ApplicationKeywords();
		cont.setAttribute("app", app);
		app.setReport(test);

		app.openBrowser("Chrome");
		app.defaultLogin();
		
	}
	
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(ITestContext cont) {
		System.out.println("----Before Method----");
		test = (ExtentTest) cont.getAttribute("test");

		String critical_failure = (String) cont.getAttribute("criticalFailure");
		
		if(critical_failure != null && critical_failure.equals("yes")) {
			test.log(Status.SKIP, "Critical Failure in Previous Test");
			
			throw new SkipException("Critical Failure in Previous Test");
		}
		else {
			app = (ApplicationKeywords) cont.getAttribute("app");
			rep = (ExtentReports) cont.getAttribute("report");
		}
	}
	
	@AfterTest(alwaysRun = true)
	public void quit(ITestContext cont) {
		app = (ApplicationKeywords) cont.getAttribute("app");
		rep = (ExtentReports) cont.getAttribute("report");

		if(app != null) {
			app.quit();
		}
		if(rep != null) {
			rep.flush();
		}
	}

}
