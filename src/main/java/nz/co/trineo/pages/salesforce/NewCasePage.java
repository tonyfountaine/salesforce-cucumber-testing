package nz.co.trineo.pages.salesforce;

import org.openqa.selenium.WebDriver;

import nz.co.trineo.model.Case;

public class NewCasePage extends NewObjectPage<Case> {
	public NewCasePage(final String baseURL, final WebDriver driver) {
		super(driver, baseURL);
	}
}