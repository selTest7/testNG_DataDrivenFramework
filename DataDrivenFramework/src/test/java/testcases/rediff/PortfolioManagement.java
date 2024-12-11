package testcases.rediff;

import org.testng.ITestContext;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import testbase.BaseTest;

public class PortfolioManagement extends BaseTest {

	@Test
	public void createPortfolio(ITestContext cont) {
		app.log("Creating Portfolio");
		app.click("portf_create_id");
		app.clear("portf_name_id");
		app.type("portf_name_id", "Temp Portfolio1");
		app.click("portf_create_button_id");
		app.waitForPageToLoad();
		app.validateInDropdown("portf_dropdown_id", "Temp Portfolio1");
		app.assertAll();

	}
	
	@Test
	public void deletePortfolio() {
		app.log("Deleting Portfolio");
		app.selectByVisibleText("portf_dropdown_id", "Temp Portfolio1");
		app.click("del_portf_id");
		app.acceptAlert();
		app.waitForPageToLoad();
		app.validateNotInDropdown("portf_dropdown_id", "Temp Portfolio1");
		app.assertAll();
	}
	
	@Test
	public void selectPortfolio() {
		app.log("Selecting Portfolio");
		app.selectByVisibleText("portf_dropdown_id", "My Portfolio1");
		app.waitForPageToLoad();
	}
}
