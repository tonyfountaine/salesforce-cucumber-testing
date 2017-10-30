package nz.co.trineo.sites;

import static org.openqa.selenium.support.ui.ExpectedConditions.urlToBe;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import nz.co.trineo.model.Case;

public class Salesforce implements Site {

	public static enum Environment {
		PRODUCTION, DEVELOPER, SANDBOX, CUSTOM
	}

	public static class LoginPage implements Page {
		private final String siteURL;
		private final WebDriver driver;

		public LoginPage(final String siteURL, final WebDriver driver) {
			this.siteURL = siteURL;
			this.driver = driver;
		}

		@Override
		public String getPageURL() {
			return siteURL;
		}

		private WebElement getUsernameInput() {
			return driver.findElement(By.id("username"));
		}

		private WebElement getPasswordInput() {
			return driver.findElement(By.id("password"));
		}

		private WebElement getLoginButton() {
			return driver.findElement(By.id("Login"));
		}

		public void setUsername(final String username) {
			getUsernameInput().sendKeys(username);
		}

		public void setPassword(final String password) {
			getPasswordInput().sendKeys(password);
		}

		public void clickLogin() {
			getLoginButton().click();
		}

		public void login(final String username, final String password) {
			setPassword(password);
			setUsername(username);
			clickLogin();
		}
	}

	public static class HomePage implements Page {
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

	public static class CasesHomePage extends ObjectHomePage<Case> {
		public CasesHomePage(final String baseURL, final WebDriver driver) {
			super(driver, baseURL, Case.PREFIX);
		}
	}

	public static class EditCasePage extends EditObjectPage<Case> {
		public EditCasePage(final String baseURL, final WebDriver driver) {
			super(driver, baseURL);
		}
	}

	public static class ViewCasePage extends ViewObjectPage<Case> {
		public ViewCasePage(final String baseURL, final WebDriver driver) {
			super(driver, baseURL);
		}
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
		final WebDriverWait wait = new WebDriverWait(driver, 6000);
		wait.until(urlToBe(homePage.getPageURL()));
	}
}
