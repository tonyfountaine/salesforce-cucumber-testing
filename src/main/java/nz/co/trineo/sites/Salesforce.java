package nz.co.trineo.sites;

import static org.openqa.selenium.support.ui.ExpectedConditions.urlToBe;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import nz.co.trineo.pages.salesforce.CasesHomePage;
import nz.co.trineo.pages.salesforce.EditCasePage;
import nz.co.trineo.pages.salesforce.HomePage;
import nz.co.trineo.pages.salesforce.LoginPage;
import nz.co.trineo.pages.salesforce.ViewCasePage;

public class Salesforce implements Site {

	public static enum Environment {
		PRODUCTION, DEVELOPER, SANDBOX, CUSTOM
	}

	private final String siteURL;
	private final String loginURL;
	private final WebDriver driver;

	public static LoginPage loginPage;
	public static HomePage homePage;
	public static CasesHomePage casesHomePage;
	public static EditCasePage editCasePage;
	public static ViewCasePage viewCasePage;

	public Salesforce(final Environment env, final WebDriver driver) {
		this(env, null, driver);
	}

	public Salesforce(final Environment env, final String url, final WebDriver driver) {
		switch (env) {
		case PRODUCTION:
		case DEVELOPER:
			loginURL = "https://login.salesforce.com/";
			break;
		case SANDBOX:
			loginURL = "https://test.salesforce.com/";
			break;
		case CUSTOM:
		default:
			loginURL = url;
		}
		siteURL = url;
		this.driver = driver;
		loginPage = new LoginPage(loginURL, driver);
		homePage = new HomePage(siteURL, driver);
		casesHomePage = new CasesHomePage(siteURL, driver);
		editCasePage = new EditCasePage(siteURL, driver);
		viewCasePage = new ViewCasePage(siteURL, driver);
	}

	@Override
	public String getSiteURL() {
		return siteURL;
	}

	@Override
	public void open() {
		driver.navigate().to(loginURL);
	}

	@Override
	public void close() {
		driver.quit();
	}

	public void waitForHomePage() {
		final WebDriverWait wait = new WebDriverWait(driver, 60);
		wait.until(urlToBe(homePage.getPageURL()));
	}
}
