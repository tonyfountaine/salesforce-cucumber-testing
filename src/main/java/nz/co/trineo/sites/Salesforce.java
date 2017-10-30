package nz.co.trineo.sites;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.openqa.selenium.support.ui.ExpectedConditions.urlToBe;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import nz.co.trineo.model.Case;
import nz.co.trineo.utils.ModelUtils;

public class Salesforce implements Site {

	public static enum Environment {
		PRODUCTION, DEVELOPER, SANDBOX, CUSTOM
	}

	public static class LoginPage implements Page {
		private final String siteURL;
		private final WebDriver driver;

		public LoginPage(final String siteURL, final WebDriver driver) {
			this.siteURL = siteURL;
			this.driver = driver;
		}

		@Override
		public String getPageURL() {
			return siteURL;
		}

		private WebElement getUsernameInput() {
			return driver.findElement(By.id("username"));
		}

		private WebElement getPasswordInput() {
			return driver.findElement(By.id("password"));
		}

		private WebElement getLoginButton() {
			return driver.findElement(By.id("Login"));
		}

		public void setUsername(final String username) {
			getUsernameInput().sendKeys(username);
		}

		public void setPassword(final String password) {
			getPasswordInput().sendKeys(password);
		}

		public void clickLogin() {
			getLoginButton().click();
		}

		public void login(final String username, final String password) {
			setPassword(password);
			setUsername(username);
			clickLogin();
		}
	}

	public static class HomePage implements Page {
		private final WebDriver driver;

		public HomePage(WebDriver driver) {
			this.driver = driver;
		}

		@Override
		public String getPageURL() {
			return "https://ap1.salesforce.com/setup/forcecomHomepage.apexp?setupid=ForceCom";
		}

		private WebElement getShowAllTabs() {
			return driver.findElement(By.cssSelector("li#AllTab_Tab a"));
		}

		public void showAllTabs() {
			WebElement showAllTabs = getShowAllTabs();
			showAllTabs.click();
		}
	}

	public static abstract class ObjectHomePage implements Page {
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
			return driver.findElement(By.name("new"));
		}

		@Override
		public String getPageURL() {
			return baseURL + prefix + "/o";
		}

		public void open() {
			driver.navigate().to(getPageURL());
		}
	}

	public static class CasesHomePage extends ObjectHomePage {
		public CasesHomePage(final WebDriver driver) {
			super(driver, "https://ap1.salesforce.com/", Case.PREFIX);
		}
	}

	public static abstract class EditObjectPage<T> implements Page {
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
			final WebElement fieldElement = driver.findElement(By.name(name));
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
			wait.until(ExpectedConditions.urlContains(prefix));
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

	public static class EditCasePage extends EditObjectPage<Case> {
		public EditCasePage(WebDriver driver) {
			super(driver, "https://ap1.salesforce.com/", Case.PREFIX, Case.fieldToPageField);
		}
	}

	public static abstract class ViewObjectPage<T> implements Page {
		private final WebDriver driver;
		private String id;
		private final Map<String, String> fieldToPageField = new HashMap<>();
		private final Class<T> clazz;
		private final String baseURL;

		public ViewObjectPage(final WebDriver driver, final String baseURL, final Class<T> clazz,
				final Map<String, String> fieldToPageField) {
			this.driver = driver;
			this.baseURL = baseURL;
			this.clazz = clazz;
			this.fieldToPageField.putAll(fieldToPageField);
		}

		protected WebElement getField(final String name) {
			final WebElement fieldElement = driver.findElement(By.id(name + "_ileinner"));
			return fieldElement;
		}

		@Override
		public String getPageURL() {
			return baseURL + id;
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
			final T model = ModelUtils.fromJSON(builder.toString(), clazz);
			return model;
		}
	}

	public static class ViewCasePage extends ViewObjectPage<Case> {
		public ViewCasePage(WebDriver driver) {
			super(driver, "https://ap1.salesforce.com/", Case.class, Case.fieldToPageField);
		}
	}

	private final String siteURL;
	private final WebDriver driver;

	public static LoginPage loginPage;
	public static HomePage homePage;
	public static CasesHomePage casesHomePage;
	public static EditCasePage editCasePage;
	public static ViewCasePage viewCasePage;

	public Salesforce(final Environment env, final WebDriver driver) {
		this(env, null, driver);
	}

	public Salesforce(final Environment env, final String url, final WebDriver driver) {
		switch (env) {
		case PRODUCTION:
		case DEVELOPER:
			siteURL = "https://login.salesforce.com/";
			break;
		case SANDBOX:
			siteURL = "https://test.salesforce.com/";
			break;
		case CUSTOM:
		default:
			siteURL = url;
		}
		this.driver = driver;
		loginPage = new LoginPage(siteURL, driver);
		homePage = new HomePage(driver);
		casesHomePage = new CasesHomePage(driver);
		editCasePage = new EditCasePage(driver);
		viewCasePage = new ViewCasePage(driver);
	}

	@Override
	public String getSiteURL() {
		return siteURL;
	}

	@Override
	public void open() {
		driver.navigate().to(siteURL);
	}

	@Override
	public void close() {
		driver.quit();
	}

	public void waitForHomePage() {
		final WebDriverWait wait = new WebDriverWait(driver, 6000);
		wait.until(urlToBe(homePage.getPageURL()));
	}
}
