package com.handmade.ecommerce.read.query.service.bdd;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;


@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/rest/features",
		glue = {"classpath:org/chenile/cucumber/rest", "classpath:com/handmade/ecommerce/read/query/service/bdd","classpath:org/chenile/cucumber/security/rest"},
        plugin = {"pretty"}
        )
@ActiveProfiles("unittest")
public class CukesRestTest {

}
