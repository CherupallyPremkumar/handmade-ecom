package com.handmade.ecommerce.category;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;


@Configuration
@PropertySource("classpath:com/handmade/ecommerce/category/TestService.properties")
@SpringBootApplication(scanBasePackages = {  "org.chenile.configuration", "com.handmade.ecommerce.category.configuration" })
@ActiveProfiles("unittest")
public class SpringTestConfig extends SpringBootServletInitializer{

	
}

