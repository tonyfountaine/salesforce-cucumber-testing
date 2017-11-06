package nz.co.trineo.pages.salesforce;

import static org.openqa.selenium.support.PageFactory.initElements;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.utils.URIBuilder;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import nz.co.trineo.pages.Page;

public class HomePage implements Page {
	final String baseURL;

	@FindBy(css = "li#AllTab_Tab a")
	private WebElement showAllTabs;

	public HomePage(final String baseURL, final WebDriver driver) {
		this.baseURL = baseURL;
		initElements(driver, this);
	}

	public void showAllTabs() {
		showAllTabs.click();
	}

	@Override
	public URI getPageURI() {
		try {
			return new URIBuilder(baseURL).setPath("/home/home.jsp").build();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}
}