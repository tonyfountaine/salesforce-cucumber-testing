package nz.co.trineo.pages.wikipedia;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class MainPage extends WikipediaPage {
	public MainPage(final WebDriver driver, final String baseURL) {
		super(driver, baseURL + "/wiki/Main_Page");
	}

	public void enterSearch(final String searchTerm) {
		final WebElement searchField = getSearchField();
		searchField.sendKeys(searchTerm);
	}

	public void clickSearch() {
		final WebElement searchButton = getSearchButton();
		searchButton.click();
	}
}