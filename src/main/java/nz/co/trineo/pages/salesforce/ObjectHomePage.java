package nz.co.trineo.pages.salesforce;

import static nz.co.trineo.utils.SalesforceUtils.getPrefixFor;
import static org.openqa.selenium.support.PageFactory.initElements;

import java.lang.reflect.ParameterizedType;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import nz.co.trineo.pages.Page;

public abstract class ObjectHomePage<T> implements Page {
	protected final WebDriver driver;
	private final String prefix;
	private final String baseURL;

	@FindBy(name = "new")
	private WebElement newButton;

	@SuppressWarnings("unchecked")
	public ObjectHomePage(final WebDriver driver, final String baseURL) {
		final Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
		this.driver = driver;
		this.prefix = getPrefixFor(clazz);
		this.baseURL = baseURL;
		initElements(driver, this);
	}

	public void clickNewButton() {
		newButton.click();
	}

	@Override
	public String getPageURL() {
		return baseURL + "/" + prefix + "/o";
	}

	public void open() {
		driver.navigate().to(getPageURL());
	}
}