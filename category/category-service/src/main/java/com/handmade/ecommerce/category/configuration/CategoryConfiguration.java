package com.handmade.ecommerce.category.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.handmade.ecommerce.category.service.CategoryService;
import com.handmade.ecommerce.category.service.impl.CategoryServiceImpl;
import com.handmade.ecommerce.category.service.healthcheck.CategoryHealthChecker;

/**
 This is where you will instantiate all the required classes in Spring

*/
@Configuration
public class CategoryConfiguration {
	@Bean public CategoryService _categoryService_() {
		return new CategoryServiceImpl();
	}

	@Bean CategoryHealthChecker categoryHealthChecker(){
    	return new CategoryHealthChecker();
    }
}
