package nz.co.trineo.sites;

import static org.openqa.selenium.By.name;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public abstract class ObjectHomePage implements Page {
	protected final WebDriver driver;
	private final String prefix;
	private final String baseURL;

	public ObjectHomePage(final WebDriver driver, final String baseURL, final String prefix) {
		this.driver = driver;
		this.prefix = prefix;
		this.baseURL = baseURL;
	}

	public void clickNewButton() {
		WebElement newButton = getNewButton();
		newButton.click();
	}

	private WebElement getNewButton() {
		return driver.findElement(name("new"));
	}

	@Override
	public String getPageURL() {
		return baseURL + prefix + "/o";
	}

	public void open() {
		driver.navigate().to(getPageURL());
	}
}