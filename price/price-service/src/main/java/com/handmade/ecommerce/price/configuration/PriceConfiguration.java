package com.handmade.ecommerce.price.configuration;

import com.handmade.ecommerce.price.configuration.dao.PriceRepository;
import com.handmade.ecommerce.price.service.store.PriceEntityCoreStore;
import org.springframework.beans.factory.annotation.Autowired;
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
	@Bean
	@Autowired
	public PriceService _priceService_(PriceEntityCoreStore priceEntityCoreStore) {
		return new PriceServiceImpl(priceEntityCoreStore);
	}

	@Bean PriceHealthChecker priceHealthChecker(){
    	return new PriceHealthChecker();
    }
	@Bean
	PriceEntityCoreStore priceEntityCoreStore(PriceRepository priceRepository)
	{
		return new PriceEntityCoreStore(priceRepository);
	}
}
