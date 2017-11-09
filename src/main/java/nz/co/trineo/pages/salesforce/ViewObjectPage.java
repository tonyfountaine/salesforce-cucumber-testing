package nz.co.trineo.pages.salesforce;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.joining;
import static nz.co.trineo.utils.ModelUtils.STATIC_PREDICATE;
import static nz.co.trineo.utils.ModelUtils.fromJSON;
import static nz.co.trineo.utils.SalesforceUtils.getFieldMapFor;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.support.PageFactory.initElements;

import java.io.IOException;
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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import nz.co.trineo.pages.Page;

public abstract class ViewObjectPage<T> implements Page {
	private static final String CHECKED_STRING = "Checked";
	private static final String TITLE_ATTRIBUTE = "title";

	private final WebDriver driver;
	private String id;
	private final Map<String, String> fieldToPageField = new HashMap<>();
	private final Class<T> clazz;
	private final String baseURL;

	@FindBy(css = ".efpDetailsView")
	private WebElement detailsTab;

	@SuppressWarnings("unchecked")
	public ViewObjectPage(final WebDriver driver, final String baseURL) {
		this.driver = driver;
		this.baseURL = baseURL;
		this.clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		this.fieldToPageField.putAll(getFieldMapFor(clazz));
		initElements(driver, this);
	}

	protected WebElement getElementFor(final String id) {
		try {
			final WebElement fieldElement = driver.findElement(id(id));
			return fieldElement;
		} catch (NoSuchElementException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public URI getPageURI() {
		try {
			return new URIBuilder(baseURL).setPath(id).build();
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private WebElement getDivElementFor(final String name) {
		final String fieldName = fieldToPageField.get(name);
		final WebElement fieldElement = getElementFor(fieldName + "_ileinner");
		return fieldElement;
	}

	private WebElement getCheckboxElementFor(final String name) {
		final String fieldName = fieldToPageField.get(name);
		final WebElement fieldElement = getElementFor(fieldName + "_chkbox");
		return fieldElement;
	}

	private String getElementValue(final String name) {
		final WebElement fieldElement = getDivElementFor(name);
		if (fieldElement != null) {
			final WebElement chkboxField = getCheckboxElementFor(name);
			if (chkboxField != null) {
				// is a check box so check if the title attribute equals 'Checked'
				final String title = chkboxField.getAttribute(TITLE_ATTRIBUTE);
				final boolean checked = CHECKED_STRING.equals(title);
				return Boolean.toString(checked);
			}
			return fieldElement.getText();
		}
		return null;
	}

	public T readPage() throws JsonParseException, JsonMappingException, IOException {
		try {
			// if there is a details tab switch to it, so that we can read the page fields
			detailsTab.click();
		} catch (NoSuchElementException e) {
			// details tab not found, so we should be able to continue
			e.printStackTrace();
		}

		final Map<String, String> fieldValues = new HashMap<>();
		Stream.of(clazz.getDeclaredFields()).filter(STATIC_PREDICATE).forEach(f -> {
			final String name = f.getName();
			final String stringValue = getElementValue(name);
			fieldValues.put(name, stringValue);
		});
		final String modelString = fieldValues.entrySet().stream().filter(e -> isNotBlank(e.getValue()))
				.map(e -> "\"" + e.getKey() + "\":\"" + e.getValue() + "\"")
				.collect(collectingAndThen(joining(","), (String a) -> "{" + a + "}"));
		final T model = fromJSON(modelString, clazz);
		return model;
	}
}