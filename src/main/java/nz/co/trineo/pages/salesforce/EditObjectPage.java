package nz.co.trineo.pages.salesforce;

import static java.util.Arrays.asList;
import static nz.co.trineo.utils.ModelUtils.STATIC_PREDICATE;
import static nz.co.trineo.utils.SalesforceUtils.getFieldMapFor;
import static nz.co.trineo.utils.SalesforceUtils.getPrefixFor;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.split;
import static org.openqa.selenium.By.name;
import static org.openqa.selenium.support.PageFactory.initElements;
import static org.openqa.selenium.support.ui.ExpectedConditions.urlContains;

import java.lang.reflect.ParameterizedType;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.http.client.utils.URIBuilder;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import nz.co.trineo.pages.Page;

public abstract class EditObjectPage<T> implements Page {
	private static final String CHECKBOX_TYPE = "checkbox";
	private static final String VALUE_ATTRIBUTE = "value";
	private static final String SELECT_TAG = "select";
	private static final String TEXTAREA_TAG = "textarea";
	private static final String INPUT_TAG = "input";
	private static final String TYPE_ATTRIBUTE = "type";

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

	protected List<WebElement> getFields(final String name) {
		try {
			final List<WebElement> fieldElements = driver.findElements(name(name));
			return fieldElements;
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

	protected List<WebElement> getElementsFor(final String name) {
		final String fieldName = fieldToPageField.get(name);
		final List<WebElement> fieldElements = getFields(fieldName);
		return fieldElements;
	}

	/**
	 * Method for setting the value of the found input element. Only handles plain text. May not work correctly for
	 * select, check boxes, radio buttons and multiselect picklists
	 * 
	 * @param name
	 * @param value
	 */
	protected void setElementValue(final String name, final Object value) {
		if (value == null) {
			return;
		}
		final List<WebElement> fieldElements = getElementsFor(name);
		if (fieldElements == null || fieldElements.isEmpty()) {
			return;
		}
		final String stringValue = value.toString();
		if (isBlank(stringValue)) {
			return;
		}
		if (fieldElements.size() == 1) {
			// only one element so assume input, select textArea
			final WebElement fieldElement = fieldElements.get(0);
			final String tagName = fieldElement.getTagName().toLowerCase();
			final String type = fieldElement.getAttribute(TYPE_ATTRIBUTE);
			if (INPUT_TAG.equals(tagName) && CHECKBOX_TYPE.equals(type)) {
				// is a check box
				final boolean booleanValue = Boolean.valueOf(stringValue);
				final boolean selected = fieldElement.isSelected();
				if (selected != booleanValue) {
					// the current value of the checkbox is not the same as the specified value, so click it
					fieldElement.click();
				}
			} else if (INPUT_TAG.equals(tagName) || TEXTAREA_TAG.equals(tagName)) {
				// is a text type of input
				fieldElement.clear();
				fieldElement.sendKeys(stringValue);
			} else if (SELECT_TAG.equals(tagName)) {
				// is a select
				final Select select = new Select(fieldElement);
				if (select.isMultiple()) {
					// allows multiple
					select.deselectAll();
					Stream.of(split(stringValue, "; ")).forEach(o -> {
						select.selectByValue(o);
					});
				} else {
					select.selectByValue(stringValue);
				}
			}
		} else {
			// more then one element so assume check boxes or radio buttons
			final List<String> listValue = asList(split(stringValue, "; "));
			fieldElements.forEach(e -> {
				final String elementValue = e.getAttribute(VALUE_ATTRIBUTE);
				if (listValue.contains(elementValue)) {
					e.click();
				}
			});
		}
	}

	public void updatePage(final T model) {
		Stream.of(model.getClass().getDeclaredFields()).filter(STATIC_PREDICATE).forEach(f -> {
			f.setAccessible(true);
			final String name = f.getName();
			try {
				final Object value = f.get(model);
				setElementValue(name, value);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		});
	}
}