package testcases.rediff;

import org.json.simple.JSONObject;
import org.testng.ITestContext;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import testbase.BaseTest;

public class StockManagement extends BaseTest {

	@Test
	public void addFreshStock(ITestContext cont) {
		JSONObject data = (JSONObject) cont.getAttribute("data");
		
		String companyName = (String) data.get("stockname");//"Birla Corporation Ltd";
		String selectDate = (String) data.get("date"); //"10-10-2024";
		String quantity = (String) data.get("quantity"); //"100";
		String stockPrice = (String) data.get("price"); //"10";
		
		app.log("Adding Stock: "+companyName+", at price: "+stockPrice+", with quantity: "+quantity);
		app.click("add_stock_id");
		app.type("stock_name_id", companyName);
		app.wait(5);
		app.clickEnterBut("stock_name_id");
		app.wait(1);
		app.selectDateFromCalendar("stock_cal_date_id", selectDate);
		app.type("stock_quantity_id", quantity);
		app.type("stock_price_id", stockPrice);
		app.click("add_stock_but_id");
		
		app.waitForPageToLoad();
		
		app.log("Stocks added successfully");
	}
	
	@Parameters({"action"})
	@Test
	public void modifyStock(String action, ITestContext cont) {
		String companyName = "Birla Corporation Ltd";
		String selectDate = "11-10-2024";
		String quantity = "50";
		String stockPrice = "10";
		
		app.log("Selling "+quantity+" of company "+ companyName);
		int quatityBeforeModification = app.findCurrentStockQuantity(companyName);
		cont.setAttribute("quatityBeforeModification", quatityBeforeModification);
		
		app.goToBuySell(companyName);
		
		if(action.equals("sellstock"))
		   app.selectByVisibleText("equity_id", "Sell");
		else
			app.selectByVisibleText("equity_id", "Buy");
		
		//app.click("buySellCalendar_id");
		app.log("Selecting Date "+ selectDate);
		app.selectDateFromCalendar("buySellCalendar_id", selectDate);
		app.type("buySell_qut_id", quantity);
		app.type("buy_sell_price_id", stockPrice);
		app.click("buy_sell_but_id");
		app.waitForPageToLoad();
		app.log("Stock Sold");

	}
	
	@Test
	public void verifyStockInList() {
		
	}
	
	@Test
	public void verifyQuantity() {
		
	}
	
	@Test
	public void verifyTransactionHistory() {
		
	}

}
