package keywords;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class ValidationKeywords extends GenericKeywords {
	
	public void validateTitle () {
		log("Validating Title");
	}
	
	public void validateText () {
		
	}
	
	public void validateElementPresent (String locator) {
		
	}
	
	public void validateLogin() {
		
	}
	
	public void validateInDropdown(String locatorKey, String option) {
		//String locator = prop.getProperty(locatorKey);
		//log("Validating Dropdown");
		//List<WebElement> el = driver.findElements(getLocator(locatorKey));
		//System.out.println(el);
		
		Select s = new Select(getElement(locatorKey));
		String text = s.getFirstSelectedOption().getText();
		System.out.println("Current first value in dropdown: "+text);
		
		if(!text.equals(option)) {
			reportFailure("Select option: "+option+" not present in dropdown for: "+locatorKey, true);
		}
		
		log("Dropdown value is as expected");
		
	}
	
	public void validateNotInDropdown(String locatorKey, String option) {
		//String locator = prop.getProperty(locatorKey);
		//log("Validating Dropdown");
		//List<WebElement> el = driver.findElements(getLocator(locatorKey));
		//System.out.println(el);
		
		Select s = new Select(getElement(locatorKey));
		String text = s.getFirstSelectedOption().getText();
		System.out.println("Current first value in dropdown: "+text);
		
		if(text.equals(option)) {
			reportFailure("Select option: "+option+" present in the dropdown for: "+locatorKey, true);
		}
		
		log("Dropdown value is as expected");
		
	}
}
