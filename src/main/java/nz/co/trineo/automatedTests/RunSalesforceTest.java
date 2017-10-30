package nz.co.trineo.automatedTests;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = { "pretty", "junit:target/salesforce.xml", "html:target/salesforce",
		"json:target/salesforce.json" }, tags = { "~@ignored" }, glue = {
				"nz.co.trineo.steps.salesforce" }, features = { "classpath:salesforce.feature" })
public class RunSalesforceTest {

}
