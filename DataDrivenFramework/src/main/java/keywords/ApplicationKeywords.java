package keywords;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.openqa.selenium.By;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentTest;

public class ApplicationKeywords extends ValidationKeywords{

	public ApplicationKeywords() throws IOException {
		String path = System.getProperty("user.dir")+"//src//test//resources//env.properties";
		System.out.println(path);
		
		prop = new Properties();
		FileInputStream fs = new FileInputStream(path);
		prop.load(fs);
		
		String propval = prop.getProperty("env")+".properties";
		env_prop = new Properties();
		
		path = System.getProperty("user.dir")+"//src//test//resources//"+propval;
		
		fs = new FileInputStream(path);
		env_prop.load(fs);
		
		softAssert = new SoftAssert();
		
		System.out.println(env_prop.get("url"));
	}
	
	public void login() {
		
	}
	
	public void defaultLogin() {
		navigate("url");
		type("username_css", "rvarma77@outlook.com");
		type("password_xpath", "Rvarma#7");
		//click("login_id");
		waitForPageToLoad();
		wait(5);
	}

	
	public void selectDateFromCalendar(String locator, String date) {
		getElement(locator).sendKeys(date);
	}
	
	public void verifyCalendar() {
		
	}
	
	public void setReport(ExtentTest test) {
		//Extent Report
		this.test = test;
	}
	
	public void goToBuySell(String companyName) {
		log("Selecting the company row "+companyName );
		int row = getRowNumWithCellData("stock_table_css", companyName);
		if(row==-1) {
			log("Stock not present in list");
		}
		driver.findElement(By.cssSelector(prop.getProperty("stock_table_css")+" tr:nth-child("+row+") td:nth-child(1)")).click();
		driver.findElement(By.cssSelector(prop.getProperty("stock_table_css")+"  tr:nth-child("+row+") input.buySell" )).click();
		
	}
	
	public int findCurrentStockQuantity(String companyName) {
		log("Finding current stock quantity for "+ companyName);
		int row = getRowNumWithCellData("stock_table_css", companyName);
		if(row==-1) {
			log("Current Stock Quantity is 0 as Stock not present in list");
			return 0;
		}
		// table#stock > tbody > tr:nth-child(2) >td:nth-child(4)
		String quantity = driver.findElement(By.cssSelector(prop.getProperty("stock_table_css")+"  tr:nth-child("+row+") td:nth-child(4)")).getText();
		log("Current stock Quantity "+quantity);
		return Integer.parseInt(quantity);
	}
	
	/*
	public void selectDateFromCalendar(String date) {
		log("Selecting Date "+date);
		
		try {
			Date currentDate = new Date();
			Date dateToSel=new SimpleDateFormat("d-MM-yyyy").parse(date);
			String day=new SimpleDateFormat("d").format(dateToSel);
			String month=new SimpleDateFormat("MMMM").format(dateToSel);
			String year=new SimpleDateFormat("yyyy").format(dateToSel);
			String monthYearToBeSelected=month+" "+year;
			String monthYearDisplayed=getElement("monthyear_css").getText();
			
			while(!monthYearToBeSelected.equals(monthYearDisplayed)) {
				click("datebackButoon_xpath");
				monthYearDisplayed=getElement("monthyear_css").getText();
			}
			driver.findElement(By.xpath("//td[text()='"+day+"']")).click();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/


}
