package keywords;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeDriverService;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import reports.ExtentManager;

public class GenericKeywords {
	
	public WebDriver driver;
	public Properties prop;
	public Properties env_prop;
	public ExtentTest test;
	public SoftAssert softAssert;
	
	public void openBrowser (String browserName) {
		log("Opening Browser: "+browserName);
		System.out.println(prop.get("grid_run"));
		if(prop.get("grid_run").equals("Y")) {
			//Run on GRID
			DesiredCapabilities cap = new DesiredCapabilities();
			if(browserName.equals("Mozilla")) {
				cap.setBrowserName("firefox");
				cap.setPlatform(org.openqa.selenium.Platform.WINDOWS);
			}
			else if(browserName.equals("Chrome")) {
				cap.setBrowserName("chrome");
				cap.setPlatform(org.openqa.selenium.Platform.WINDOWS);
			}
			
			try {
				driver = new RemoteWebDriver(new URL("https://localhost:4444"), cap);
				
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		else {
			//Local machine
			if(browserName.equals("Mozilla")) {
				FirefoxOptions op = new FirefoxOptions();
				op.setPageLoadStrategy(PageLoadStrategy.EAGER);
				FirefoxProfile prof = new FirefoxProfile(); //allProf.getProfile(""); //Can get your profile of choice
				prof.setPreference("dom.webnotifications.enabled", false); //Manage notifications
				
				op.setProfile(prof);
				driver = new FirefoxDriver(op);
			}
			else if(browserName.equals("Chrome")) {
				//System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "C:\\Users\\rvarm\\Selenium Course\\SeleniumWebDriverMaven\\logs\\chrome.log");
				System.setProperty(ChromeDriverService.CHROME_DRIVER_SILENT_OUTPUT_PROPERTY, "true");

				ChromeOptions options = new ChromeOptions();
				options.setPageLoadStrategy(PageLoadStrategy.EAGER);
				options.addArguments("--disable-notifications"); //Handles Notifications
				options.addArguments("--start-maximized"); //Opens the browser maximized
				options.addArguments("ignore-certificate-errors"); //Handles Certificate errors

				driver = new ChromeDriver(options);
			}
			else if(browserName.equals("Edge")) {
				System.setProperty(EdgeDriverService.EDGE_DRIVER_SILENT_OUTPUT_PROPERTY, "true");

				EdgeOptions options = new EdgeOptions();
				options.setPageLoadStrategy(PageLoadStrategy.EAGER);
				options.addArguments("--disable-notifications"); //Handles Notifications
				options.addArguments("--start-maximized"); //Opens the browser maximized
				//options.addArguments("ignore-certificate-errors"); //Handles Certificate errors

				driver = new EdgeDriver(options);
			}
					
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS); //Dynamic wait. It uses 20s to find the element. If it's not found in 10s, it then throws an exception

		}
		
	}
	
	public void navigate (String url) {
		log("Navigating to URL: "+env_prop.getProperty(url));
		driver.get(env_prop.getProperty(url));
	}
	
	public void click (String locatorKey) {
		String locator = prop.getProperty(locatorKey);
		log("Clicking on the locator: "+locator);
		getElement(locatorKey).click();
	}
	
	public void clear (String locatorKey) {
		String locator = prop.getProperty(locatorKey);
		log("Clearing the text for the locator: "+locator);
		getElement(locatorKey).clear();
	}
	
	public void type (String locatorKey, String data) {
		String locator = prop.getProperty(locatorKey);
		log("Typing at locator: "+locator); //+", with data: \"+data
		getElement(locatorKey).sendKeys(data);
	}
	
	public void clickEnterBut(String locatorKey) {
		getElement(locatorKey).sendKeys(Keys.ENTER);
	}
	
	public void getText (String locator) {
		
	}
	
	public int getRowNumWithCellData(String tableLocator, String data) {
		
		WebElement table = getElement(tableLocator);
		List<WebElement> rows = table.findElements(By.tagName("tr"));
		for(int rNum=0;rNum<rows.size();rNum++) {
			WebElement row = rows.get(rNum);
			List<WebElement> cells = row.findElements(By.tagName("td"));
			for(int cNum=0;cNum<cells.size();cNum++) {
				WebElement cell = cells.get(cNum);
				System.out.println("Text "+ cell.getText());
				if(!cell.getText().trim().equals(""))
					if(data.startsWith(cell.getText()))
						return(rNum); //rNum+1
			}
		}
		
		return -1; // data is not found
	}

	
	public void selectByVisibleText(String locatorKey, String data) {
		Select s = new Select(getElement(locatorKey));
		s.selectByVisibleText(data);
	}
	
	public void acceptAlert() {
		Alert alert = driver.switchTo().alert();
		alert.accept();
	}
	
	public WebElement getElement (String locatorKey) {
		//Check the presence of the element
		//Check the visibility of the element
		
		if(!isElementPresent(locatorKey)) {
			//Report the failure
			test.log(Status.ERROR, "Element Not present with the locator: "+prop.getProperty(locatorKey));
		}
		
		if(!isElementVisible(locatorKey)) {
			//Report the failure
			test.log(Status.ERROR, "Element Not visible with the locator: "+prop.getProperty(locatorKey));
		}
		
		WebElement e = driver.findElement(getLocator(locatorKey));
		
		return e;
	}
	
	public boolean isElementPresent (String locatorKey) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		try {
			
			wait.until(ExpectedConditions.presenceOfElementLocated(getLocator(locatorKey)));
			
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean isElementVisible (String locatorKey) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(getLocator(locatorKey)));
			
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			//test.log(Status.FAIL, locatorKey+" Element is not visible");
			return false;
		}	
	}
	
	public By getLocator (String locatorKey) {
		By by = null;
		
		if(locatorKey.endsWith("id")) {
			by = By.id(prop.getProperty(locatorKey));
		}
		else if(locatorKey.endsWith("xpath")) {
			by = By.xpath(prop.getProperty(locatorKey));
		}
		else if(locatorKey.endsWith("css")) {
			by = By.cssSelector(prop.getProperty(locatorKey));
		}
		else if(locatorKey.endsWith("_name")) {
			by = By.name(prop.getProperty(locatorKey));
		}
		
		return by;
	}
	
	public void log (String message) {
		System.out.println(message);
		test.log(Status.INFO, message);
	}
	
	public void reportFailure(String failureMsg, boolean stopOnFailure) {
		System.out.println(failureMsg);
		takeScreenshot();
		test.log(Status.FAIL, failureMsg); //Failure in Extent Reports
		softAssert.fail(failureMsg); //Failure in TestNG
		
		if(stopOnFailure) {
			Reporter.getCurrentTestResult().getTestContext().setAttribute("criticalFailure", "yes");
			assertAll();
		}
	}
	
	public void assertAll() {
		softAssert.assertAll();
	}
	
	public void takeScreenshot() {
		Date d = new Date();
		String screenshotFile = d.toString().replace(":", "_").replace(" ", "_")+".png";
		File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		//System.out.println(ExtentManager.screenshotFolderPath);
		//System.out.println(screenshotFile);
		try {
			FileUtils.copyFile(src, new File(ExtentManager.screenshotFolderPath+screenshotFile));
			//test.addScreenCaptureFromPath("path of image", "");
			test.log(Status.INFO, "Screenshot -> "+test.addScreenCaptureFromPath(ExtentManager.screenshotFolderPath+screenshotFile));
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void waitForPageToLoad(){
		JavascriptExecutor js = (JavascriptExecutor)driver;
		int i=0;
		
		while(i!=10){
		String state = (String)js.executeScript("return document.readyState;");
		System.out.println(state);

		if(state.equals("complete"))
			break;
		else
			wait(2);

		i++;
		}
		// check for jquery status
		i=0;
		while(i!=10){
	
			Long d= (Long) js.executeScript("return jQuery.active;");
			System.out.println(d);
			if(d.longValue() == 0 )
			 	break;
			else
				 wait(2);
			 i++;
				
			}
		
	}
	
	public void wait(int time) {
		try {
			Thread.sleep(time*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void quit() {
		driver.quit();
	}
}
