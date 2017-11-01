package nz.co.trineo.pages.salesforce;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import nz.co.trineo.pages.Page;

public class HomePage implements Page {
	private final WebDriver driver;
	private final String baseURL;

	public HomePage(final String baseURL, final WebDriver driver) {
		this.driver = driver;
		this.baseURL = baseURL;
	}

	@Override
	public String getPageURL() {
		return baseURL + "/setup/forcecomHomepage.apexp?setupid=ForceCom";
	}

	private WebElement getShowAllTabs() {
		return driver.findElement(By.cssSelector("li#AllTab_Tab a"));
	}

	public void showAllTabs() {
		WebElement showAllTabs = getShowAllTabs();
		showAllTabs.click();
	}
}