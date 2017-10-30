package nz.co.trineo.steps.salesforce;

import static nz.co.trineo.sites.Salesforce.homePage;
import static nz.co.trineo.sites.Salesforce.loginPage;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import nz.co.trineo.sites.Salesforce;
import nz.co.trineo.sites.Salesforce.Environment;

public class SalesforceSteps {
	private String username;
	private String password;
	private WebDriver driver;
	private Salesforce site;

	@Before
	public void before() {
		// System.setProperty("webdriver.gecko.driver", "/Users/tonyfountaine/Downloads/geckodriver");
		// driver = new FirefoxDriver();
		// System.setProperty("webdriver.chrome.driver", "/Users/tonyfountaine/Downloads/chromedriver");
		driver = new ChromeDriver();
		site = new Salesforce(Environment.DEVELOPER, driver);
		site.open();
		assertThat(driver.getCurrentUrl(), equalTo(loginPage.getPageURL()));
	}

	@After
	public void after() {
		// site.close();
	}

	@Given("^A username and password$")
	public void setUsernameAndPassword() {
		username = System.getProperty("sf.username");
		password = System.getProperty("sf.password");
	}

	@When("^Enter username and password$")
	public void enterUsernameAndPassword() {
		loginPage.setPassword(password);
		loginPage.setUsername(username);
		loginPage.clickLogin();
	}

	@Then("^I should see the Home page$")
	public void verifyHomePage() {
		final WebDriverWait wait = new WebDriverWait(driver, 6000);
		wait.until(ExpectedConditions.urlToBe(homePage.getPageURL()));
		assertThat(driver.getCurrentUrl(), equalTo(homePage.getPageURL()));
	}
}
