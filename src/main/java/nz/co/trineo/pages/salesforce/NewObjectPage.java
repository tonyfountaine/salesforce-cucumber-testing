package nz.co.trineo.pages.salesforce;

import static org.openqa.selenium.support.PageFactory.initElements;

import java.lang.reflect.Field;

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
				final Object value = recordTypeField.get(model);
				setElementValue(name, value);
				clickContinue();
				waitForViewPage();
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
				return;
			}
		}
		super.updatePage(model);
	}

	public String getNewId() {
		waitForViewPage();
		String currentUrl = driver.getCurrentUrl();
		int indexOf = currentUrl.indexOf(prefix);
		String id = currentUrl.substring(indexOf);
		return id;
	}
}