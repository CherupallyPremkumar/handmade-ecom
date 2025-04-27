package com.handmade.ecommerce.artisan.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = {
    "com.handmade.ecommerce.artisan",
    "com.handmade.ecommerce.image"
})
public class ArtisanConfig {
    // This configuration class ensures that components from both artisan and image modules are scanned
} 