package nz.co.trineo.pages.salesforce;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.openqa.selenium.support.PageFactory.initElements;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.stream.Stream;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

public abstract class NewObjectPage<T> extends EditObjectPage<T> {
	@FindBys({ @FindBy(css = "#bottomButtonRow"), @FindBy(name = "save") })
	private WebElement continueButton;

	public NewObjectPage(final WebDriver driver, final String baseURL) {
		super(driver, baseURL);
		initElements(driver, this);
	}

	public void clickContinue() {
		continueButton.click();
	}

	@Override
	public void updatePage(final T model) {
		if (driver.getCurrentUrl().contains("recordtypeselect")) {
			// is asking for a record type
			try {
				final String name = "recordTypeId";
				final Field recordTypeField = model.getClass().getDeclaredField(name);
				recordTypeField.setAccessible(true);
				final String fieldName = fieldToPageField.get(name);
				final WebElement fieldElement = getField(fieldName);
				if (fieldElement != null) {
					// fieldElement.clear();
					final Object value = recordTypeField.get(model);
					if (value != null) {
						final String stringValue = value.toString();
						if (isNotBlank(stringValue)) {
							fieldElement.sendKeys(stringValue);
						}
					}
				}
				clickContinue();
				waitForViewPage();
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
				return;
			}
		}
		super.updatePage(model);
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

	public String getNewId() {
		waitForViewPage();
		String currentUrl = driver.getCurrentUrl();
		int indexOf = currentUrl.indexOf(prefix);
		String id = currentUrl.substring(indexOf);
		return id;
	}
}