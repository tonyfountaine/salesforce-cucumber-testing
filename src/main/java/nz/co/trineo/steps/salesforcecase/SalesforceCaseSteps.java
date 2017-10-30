package nz.co.trineo.steps.salesforcecase;

import static nz.co.trineo.sites.Salesforce.casesHomePage;
import static nz.co.trineo.sites.Salesforce.editCasePage;
import static nz.co.trineo.sites.Salesforce.homePage;
import static nz.co.trineo.sites.Salesforce.loginPage;
import static nz.co.trineo.sites.Salesforce.viewCasePage;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStream;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import nz.co.trineo.model.Case;
import nz.co.trineo.sites.Salesforce;
import nz.co.trineo.sites.Salesforce.Environment;
import nz.co.trineo.utils.ModelUtils;

public class SalesforceCaseSteps {
	private WebDriver driver;
	private Salesforce site;
	private String id;

	@Before
	public void open() {
		driver = new ChromeDriver();
		site = new Salesforce(Environment.DEVELOPER, driver);
		site.open();
		assertThat(driver.getCurrentUrl(), equalTo(loginPage.getPageURL()));
	}

	@After
	public void close() {
		site.close();
	}

	@Given("^that I am logged in$")
	public void login() {
		final String username = System.getProperty("sf.username");
		final String password = System.getProperty("sf.password");
		loginPage.login(username, password);
	}

	@Given("^I can see the HomePage$")
	public void seeHomePage() {
		site.waitForHomePage();
		assertThat(driver.getCurrentUrl(), equalTo(homePage.getPageURL()));
	}

	@Given("^I click Cases tab$")
	public void clickCasesTab() {
		casesHomePage.open();
	}

	@When("^I click the new button$")
	public void newCase() {
		casesHomePage.clickNewButton();
	}

	@When("^I enter the case info$")
	public void createCase() throws JsonParseException, JsonMappingException, IOException {
		final InputStream caseResource = getClass().getResourceAsStream("/case.json");
		final Case c = ModelUtils.fromJSON(caseResource, Case.class);
		editCasePage.updatePage(c);
		editCasePage.clickSave();
		id = editCasePage.getNewId();
	}

	@Then("^I should see the new Case$")
	public void verifyNewCase() throws JsonParseException, JsonMappingException, IOException {
		assertThat(id, notNullValue());
		viewCasePage.setId(id);
		final Case actual = viewCasePage.readPage();
		assertThat(actual.getCaseOrigin(), equalTo("Web"));
	}
}
