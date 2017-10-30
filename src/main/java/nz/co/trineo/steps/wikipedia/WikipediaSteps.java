package nz.co.trineo.steps.wikipedia;

import static nz.co.trineo.sites.Wikipedia.cucumberPage;
import static nz.co.trineo.sites.Wikipedia.mainPage;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java8.En;
import nz.co.trineo.sites.Wikipedia;

public class WikipediaSteps implements En {
	private WebDriver driver;
	private Wikipedia site;

	@Before
	public void before() {
		// System.setProperty("webdriver.gecko.driver", "/Users/tonyfountaine/Downloads/geckodriver");
		// driver = new FirefoxDriver();
		System.setProperty("webdriver.chrome.driver", "/Users/tonyfountaine/Downloads/chromedriver");
		driver = new ChromeDriver();
		site = new Wikipedia(driver);
		site.open();
		assertThat(driver.getCurrentUrl(), equalTo(mainPage.getPageURL()));
	}

	@After
	public void after() {
		site.close();
	}

	@Given("^Enter search term '(.*?)'$")
	public void searchFor(String searchTerm) {
		mainPage.enterSearch(searchTerm);
	}

	@When("^Do search$")
	public void clickSearchButton() {
		mainPage.clickSearch();
	}

	@Then("^Single result is shown for '(.*?)'$")
	public void assertSingleResult(String searchResult) {
		assertThat(driver.getCurrentUrl(), equalTo(cucumberPage.getPageURL()));
		final String pageResults = cucumberPage.getPageResults();
		assertFalse(pageResults.contains(searchResult + " may refer to:"));
		assertTrue(pageResults.startsWith(searchResult));
	}
}
