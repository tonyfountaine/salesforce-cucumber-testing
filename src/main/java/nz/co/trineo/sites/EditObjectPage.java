package nz.co.trineo.sites;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.openqa.selenium.By.name;
import static org.openqa.selenium.support.ui.ExpectedConditions.urlContains;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class EditObjectPage<T> implements Page {
	protected final WebDriver driver;
	private final String prefix;
	private final String baseURL;
	private final Map<String, String> fieldToPageField = new HashMap<>();

	public EditObjectPage(final WebDriver driver, final String baseURL, final String prefix,
			final Map<String, String> fieldToPageField) {
		this.driver = driver;
		this.baseURL = baseURL;
		this.prefix = prefix;
		this.fieldToPageField.putAll(fieldToPageField);
	}

	protected WebElement getField(final String name) {
		final WebElement fieldElement = driver.findElement(name(name));
		return fieldElement;
	}

	@Override
	public String getPageURL() {
		return baseURL + prefix + "/e";
	}

	public void open() {
		driver.navigate().to(getPageURL());
	}

	public void clickSave() {
		final WebElement saveButton = getField("save");
		saveButton.click();
	}

	public void waitForViewPage() {
		final WebDriverWait wait = new WebDriverWait(driver, 6000);
		wait.until(urlContains(prefix));
	}

	public String getNewId() {
		waitForViewPage();
		String currentUrl = driver.getCurrentUrl();
		int indexOf = currentUrl.indexOf(prefix);
		String id = currentUrl.substring(indexOf);
		return id;
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