package com.handmade.ecommerce.build;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication(scanBasePackages = { "com.handmade.ecommerce.**",
		"org.chenile.configuration" })
@EnableJpaRepositories(basePackages ={"com.handmade.ecommerce.cart.configuration.dao",
		"com.handmade.ecommerce.artisan.configuration.dao",
		"com.handmade.ecommerce.product.configuration.dao",
		"com.handmade.ecommerce.tenant.configuration.dao",
		"com.handmade.ecommerce.price.configuration.dao",
		"com.handmade.ecommerce.category.configuration.dao"})
@EntityScan(basePackages = {"com.handmade.ecommerce.cart.model",
		"com.handmade.ecommerce.product.model",
		"com.handmade.ecommerce.artisan.model",
		"com.handmade.ecommerce.tenant.model",
		"com.handmade.ecommerce.price.model",
		"com.handmade.ecommerce.category.model"})

public class BuildApplication extends SpringBootServletInitializer {
	public static void main(String[] args) {
		SpringApplication.run(BuildApplication.class, args);
	}
}
