package nz.co.trineo.pages.wikipedia;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.utils.URIBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import nz.co.trineo.pages.Page;

public abstract class WikipediaPage implements Page {
	private final String pageURL;
	private final WebDriver driver;

	public WikipediaPage(final WebDriver driver, final String pageURL) {
		this.pageURL = pageURL;
		this.driver = driver;
	}

	@Override
	public URI getPageURI() {
		try {
			return new URIBuilder(pageURL).build();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getPageResults() {
		final WebElement results = driver.findElement(By.cssSelector("div#mw-content-text.mw-content-ltr p"));
		return results.getText();
	}

	protected WebElement getSearchButton() {
		return driver.findElement(By.id("searchButton"));
	}

	protected WebElement getSearchField() {
		return driver.findElement(By.id("searchInput"));
	}
}