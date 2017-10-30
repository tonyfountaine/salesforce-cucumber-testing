package nz.co.trineo.automatedTests;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = { "pretty", "junit:target/case.xml", "html:target/case", "json:target/case.json" }, tags = {
		"~@ignored" }, glue = {
				"nz.co.trineo.steps.salesforcecase" }, features = { "classpath:salesforcecase.feature" })
public class RunSalesforceCaseTest {

}
