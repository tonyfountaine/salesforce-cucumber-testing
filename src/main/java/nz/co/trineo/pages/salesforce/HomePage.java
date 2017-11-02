package nz.co.trineo.pages.salesforce;

import static org.openqa.selenium.support.PageFactory.initElements;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import nz.co.trineo.pages.Page;

public class HomePage implements Page {
	private final String baseURL;

	@FindBy(css = "li#AllTab_Tab a")
	private WebElement showAllTabs;

	public HomePage(final String baseURL, final WebDriver driver) {
		this.baseURL = baseURL;
		initElements(driver, this);
	}

	@Override
	public String getPageURL() {
		return baseURL + "/setup/forcecomHomepage.apexp?setupid=ForceCom";
	}

	public void showAllTabs() {
		showAllTabs.click();
	}
}