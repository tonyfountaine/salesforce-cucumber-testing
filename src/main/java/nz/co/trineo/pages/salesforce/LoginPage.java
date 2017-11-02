package nz.co.trineo.pages.salesforce;

import static org.openqa.selenium.support.PageFactory.initElements;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import nz.co.trineo.pages.Page;

public class LoginPage implements Page {
	private final String siteURL;

	@FindBy(id = "username")
	private WebElement usernameInput;
	@FindBy(id = "password")
	private WebElement passwordInput;
	@FindBy(id = "Login")
	private WebElement loginButton;

	public LoginPage(final String siteURL, final WebDriver driver) {
		this.siteURL = siteURL;
		initElements(driver, this);
	}

	@Override
	public String getPageURL() {
		return siteURL;
	}

	public void setUsername(final String username) {
		usernameInput.sendKeys(username);
	}

	public void setPassword(final String password) {
		passwordInput.sendKeys(password);
	}

	public void clickLogin() {
		loginButton.click();
	}

	public void login(final String username, final String password) {
		setPassword(password);
		setUsername(username);
		clickLogin();
	}
}