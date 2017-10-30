package nz.co.trineo.sites;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Wikipedia implements Site {

	public static class MainPage extends WikipediaPage {
		public MainPage(final WebDriver driver, final String baseURL) {
			super(driver, baseURL + "/wiki/Main_Page");
		}

		public void enterSearch(final String searchTerm) {
			final WebElement searchField = getSearchField();
			searchField.sendKeys(searchTerm);
		}

		public void clickSearch() {
			final WebElement searchButton = getSearchButton();
			searchButton.click();
		}
	}

	public static abstract class WikipediaPage implements Page {
		private final String pageURL;
		private final WebDriver driver;

		public WikipediaPage(final WebDriver driver, final String pageURL) {
			this.pageURL = pageURL;
			this.driver = driver;
		}

		@Override
		public String getPageURL() {
			return pageURL;
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

	public static class CucumberPage extends WikipediaPage {
		public CucumberPage(final WebDriver driver, final String baseURL) {
			super(driver, baseURL + "/wiki/Cucumber");
		}
	}

	private static String siteURL = "https://en.wikipedia.org";
	public static MainPage mainPage;
	private final WebDriver driver;
	public static CucumberPage cucumberPage;

	public Wikipedia(final WebDriver driver) {
		mainPage = new MainPage(driver, siteURL);
		cucumberPage = new CucumberPage(driver, siteURL);
		this.driver = driver;
	}

	@Override
	public String getSiteURL() {
		return siteURL;
	}

	@Override
	public void open() {
		driver.navigate().to(siteURL);
	}

	@Override
	public void close() {
		driver.quit();
	}
}
