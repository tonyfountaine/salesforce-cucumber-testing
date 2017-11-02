package nz.co.trineo.pages.salesforce;

import org.openqa.selenium.WebDriver;

import nz.co.trineo.model.Case;

public class CasesHomePage extends ObjectHomePage<Case> {
	public CasesHomePage(final String baseURL, final WebDriver driver) {
		super(driver, baseURL);
	}
}