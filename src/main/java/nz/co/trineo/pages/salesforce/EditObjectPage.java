package nz.co.trineo.pages.salesforce;

import static nz.co.trineo.utils.SalesforceUtils.getFieldMapFor;
import static nz.co.trineo.utils.SalesforceUtils.getPrefixFor;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.openqa.selenium.By.name;
import static org.openqa.selenium.support.PageFactory.initElements;
import static org.openqa.selenium.support.ui.ExpectedConditions.urlContains;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.http.client.utils.URIBuilder;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import nz.co.trineo.pages.Page;

public abstract class EditObjectPage<T> implements Page {
	protected final WebDriver driver;
	protected final String prefix;
	private final String baseURL;
	final Map<String, String> fieldToPageField = new HashMap<>();
	private String id;
	private final Class<T> clazz;

	@FindBy(name = "save")
	private WebElement saveButton;

	@SuppressWarnings("unchecked")
	public EditObjectPage(final WebDriver driver, final String baseURL) {
		this.clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		this.driver = driver;
		this.baseURL = baseURL;
		this.prefix = getPrefixFor(clazz);
		this.fieldToPageField.putAll(getFieldMapFor(clazz));
		initElements(driver, this);
	}

	protected WebElement getField(final String name) {
		try {
			final WebElement fieldElement = driver.findElement(name(name));
			return fieldElement;
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	@Override
	public URI getPageURI() {
		try {
			final URIBuilder builder = new URIBuilder(baseURL).setPath(prefix + "/e");
			if (isNotBlank(id)) {
				builder.addParameter("id", id);
			}
			return builder.build();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void open() {
		try {
			driver.navigate().to(getPageURI().toURL());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public void clickSave() {
		saveButton.click();
	}

	public void waitForViewPage() {
		final WebDriverWait wait = new WebDriverWait(driver, 6000);
		wait.until(urlContains(prefix));
	}

	public void updatePage(final T model) {
		Stream.of(model.getClass().getDeclaredFields())
				.filter(f -> (f.getModifiers() & Modifier.STATIC) != Modifier.STATIC).forEach(f -> {
					f.setAccessible(true);
					final String name = f.getName();
					final String fieldName = fieldToPageField.get(name);
					final WebElement fieldElement = getField(fieldName);
					if (fieldElement != null) {
						// fieldElement.clear();
						try {
							final Object value = f.get(model);
							if (value != null) {
								final String stringValue = value.toString();
								if (isNotBlank(stringValue)) {
									fieldElement.sendKeys(stringValue);
								}
							}
						} catch (IllegalArgumentException | IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				});
	}
}