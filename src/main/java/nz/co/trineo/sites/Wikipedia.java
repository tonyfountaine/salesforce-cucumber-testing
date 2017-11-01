package nz.co.trineo.sites;

import org.openqa.selenium.WebDriver;

import nz.co.trineo.pages.wikipedia.CucumberPage;
import nz.co.trineo.pages.wikipedia.MainPage;

public class Wikipedia implements Site {

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
