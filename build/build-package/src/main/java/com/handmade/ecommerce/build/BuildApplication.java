package com.handmade.ecommerce.build;

import com.handmade.ecommerce.cart.configuration.dao.CartRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = { "com.handmade.ecommerce.**",
		"org.chenile.configuration" })
@EnableJpaRepositories(basePackages = {"com.handmade.ecommerce.cart.configuration.dao","com.handmade.ecommerce.product.configuration.dao","com.handmade.ecommerce.artisan.configuration.dao"})
@EntityScan(basePackages = {"com.handmade.ecommerce.cart.model","com.handmade.ecommerce.product.model","com.handmade.ecommerce.artisan.model"})
public class BuildApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(BuildApplication.class, args);
	}
}
