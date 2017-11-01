package nz.co.trineo.pages.salesforce;

import org.openqa.selenium.WebDriver;

import nz.co.trineo.model.Case;

public class EditCasePage extends EditObjectPage<Case> {
	public EditCasePage(final String baseURL, final WebDriver driver) {
		super(driver, baseURL);
	}
}