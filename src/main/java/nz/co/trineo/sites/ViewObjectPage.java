package nz.co.trineo.sites;

import static nz.co.trineo.utils.ModelUtils.fromJSON;
import static nz.co.trineo.utils.SalesforceUtils.getFieldMapFor;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.openqa.selenium.By.id;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public abstract class ViewObjectPage<T> implements Page {
	private final WebDriver driver;
	private String id;
	private final Map<String, String> fieldToPageField = new HashMap<>();
	private final Class<T> clazz;
	private final String baseURL;

	@SuppressWarnings("unchecked")
	public ViewObjectPage(final WebDriver driver, final String baseURL) {
		this.driver = driver;
		this.baseURL = baseURL;
		this.clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		this.fieldToPageField.putAll(getFieldMapFor(clazz));
	}

	protected WebElement getField(final String name) {
		final WebElement fieldElement = driver.findElement(id(name + "_ileinner"));
		return fieldElement;
	}

	@Override
	public String getPageURL() {
		return baseURL + "/" + id;
	}

	public void open() {
		driver.navigate().to(getPageURL());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public T readPage() throws JsonParseException, JsonMappingException, IOException {
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		Stream.of(clazz.getDeclaredFields()).filter(f -> (f.getModifiers() & Modifier.STATIC) != Modifier.STATIC)
				.forEach(f -> {
					f.setAccessible(true);
					final String name = f.getName();
					final String fieldName = fieldToPageField.get(name);
					final WebElement fieldElement = getField(fieldName);
					if (fieldElement != null) {
						String stringValue = fieldElement.getText();
						if (isNotBlank(stringValue)) {
							builder.append("\"" + name + "\":\"" + stringValue + "\"");
						}
					}
				});
		builder.append("}");
		final T model = fromJSON(builder.toString(), clazz);
		return model;
	}
}