package com.handmade.ecommerce.read.query.service.bdd;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

@Configuration
@SpringBootApplication(scanBasePackages = { "org.chenile.configuration","com.handmade.ecommerce.**" })
@ActiveProfiles("unittest")
public class SpringTestConfig extends SpringBootServletInitializer{
}

