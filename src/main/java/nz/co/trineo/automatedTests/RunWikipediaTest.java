package nz.co.trineo.automatedTests;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = { "pretty", "junit:target/wikipedia.xml", "html:target/wikipedia",
		"json:target/wikipedia.json" }, tags = {
				"~@ignored" }, glue = { "nz.co.trineo.steps.wikipedia" }, features = { "classpath:wikipedia.feature" })
public class RunWikipediaTest {

}
