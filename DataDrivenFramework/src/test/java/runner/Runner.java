package runner;

import java.util.ArrayList;
import java.util.List;

public class Runner {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestNGRunner runner = new TestNGRunner(1);
		
		runner.createSuite("Stock Management", false);
		runner.addListener("listener.myTestNGListener");
		runner.addTest("Add New Stock Test");
		runner.addTestParameter("action", "addstock");
		
		List<String> inclMethods = new ArrayList<>();
		inclMethods.add("selectPortfolio");
		runner.addTestClass("testcases.rediff.PortfolioManagement", inclMethods);
		
		inclMethods = new ArrayList<>();
		inclMethods.add("addNewStock");
		runner.addTestClass("testcases.rediff.StockManagement", inclMethods);
		
		runner.run();
	}

}
