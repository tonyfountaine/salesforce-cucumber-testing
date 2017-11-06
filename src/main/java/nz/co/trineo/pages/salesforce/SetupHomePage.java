package nz.co.trineo.pages.salesforce;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.utils.URIBuilder;
import org.openqa.selenium.WebDriver;

public class SetupHomePage extends HomePage {
	public SetupHomePage(String baseURL, WebDriver driver) {
		super(baseURL, driver);
	}

	public URI getPageURI() {
		try {
			return new URIBuilder(baseURL).setPath("/setup/forcecomHomepage.apexp").addParameter("setupid", "ForceCom")
					.build();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}
}
