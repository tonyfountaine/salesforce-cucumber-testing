package nz.co.trineo.sites;

import static nz.co.trineo.utils.SalesforceUtils.getPrefixFor;
import static org.openqa.selenium.By.name;

import java.lang.reflect.ParameterizedType;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public abstract class ObjectHomePage<T> implements Page {
	protected final WebDriver driver;
	private final String prefix;
	private final String baseURL;

	@SuppressWarnings("unchecked")
	public ObjectHomePage(final WebDriver driver, final String baseURL, final String prefix) {
		final Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
		this.driver = driver;
		this.prefix = getPrefixFor(clazz);
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
		return baseURL + "/" + prefix + "/o";
	}

	public void open() {
		driver.navigate().to(getPageURL());
	}
}