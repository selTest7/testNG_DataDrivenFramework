package testcases.rediff;

import org.testng.ITestContext;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import keywords.ApplicationKeywords;
import testbase.BaseTest;

public class Session extends BaseTest {
	
	@Test
	public void doLogin(ITestContext cont) {
		app.log("Logging In");
		
		app.openBrowser("Chrome");
		app.navigate("url");
		app.type("username_css", "rvarma77@outlook.com");
		app.type("password_xpath", "Rvarma#7");
		app.reportFailure("Text is incorrect", false);
		//app.validateElementPresent("loginsubmit");
		app.click("login_id");

	}
	
	public void doLogout() { 
		app.log("Logging out");
	}
}
