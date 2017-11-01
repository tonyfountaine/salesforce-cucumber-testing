package nz.co.trineo.pages.wikipedia;

import org.openqa.selenium.WebDriver;

public class CucumberPage extends WikipediaPage {
	public CucumberPage(final WebDriver driver, final String baseURL) {
		super(driver, baseURL + "/wiki/Cucumber");
	}
}