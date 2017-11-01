package nz.co.trineo.pages.salesforce;

import org.openqa.selenium.WebDriver;

import nz.co.trineo.model.Case;

public class ViewCasePage extends ViewObjectPage<Case> {
	public ViewCasePage(final String baseURL, final WebDriver driver) {
		super(driver, baseURL);
	}
}