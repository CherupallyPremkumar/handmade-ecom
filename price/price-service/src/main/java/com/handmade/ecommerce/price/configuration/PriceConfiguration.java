package com.handmade.ecommerce.price.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.handmade.ecommerce.price.service.PriceService;
import com.handmade.ecommerce.price.service.impl.PriceServiceImpl;
import com.handmade.ecommerce.price.service.healthcheck.PriceHealthChecker;

/**
 This is where you will instantiate all the required classes in Spring

*/
@Configuration
public class PriceConfiguration {
	@Bean public PriceService _priceService_() {
		return new PriceServiceImpl();
	}

	@Bean PriceHealthChecker priceHealthChecker(){
    	return new PriceHealthChecker();
    }
}
