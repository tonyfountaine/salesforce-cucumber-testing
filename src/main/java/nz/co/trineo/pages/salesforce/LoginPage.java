package nz.co.trineo.pages.salesforce;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import nz.co.trineo.pages.Page;

public class LoginPage implements Page {
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