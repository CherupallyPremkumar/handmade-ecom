package com.handmade.ecommerce.image.config;

import com.handmade.ecommerce.image.api.service.ImageService;
import com.handmade.ecommerce.image.service.impl.ImageServiceImpl;
import com.handmade.ecommerce.image.storage.StorageProvider;
import com.handmade.ecommerce.image.storage.composite.CompositeStorageProvider;
import com.handmade.ecommerce.image.storage.factory.StorageProviderFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {

    @Value("${storage.default-provider:aws}")
    private String defaultProvider;

    @Value("${storage.enabled-providers:aws}")
    private String[] enabledProviders;

    @Bean(name = "imageService")
    ImageService imageService(StorageProvider storageProvider)
    {
        return new ImageServiceImpl(storageProvider);
    }

    @Bean
    public StorageProviderFactory storageProviderFactory() {
        return new StorageProviderFactory();
    }

    @Bean
    public CompositeStorageProvider compositeStorageProvider(StorageProviderFactory factory) {
        CompositeStorageProvider compositeProvider = new CompositeStorageProvider(defaultProvider);
        
        // Add all enabled providers
        for (String providerType : enabledProviders) {
            try {
                StorageProvider provider = factory.getProvider(providerType);
                compositeProvider.addProvider(providerType, provider);
            } catch (Exception e) {
                // Log error but continue with other providers
                System.err.println("Failed to initialize provider: " + providerType + " - " + e.getMessage());
            }
        }
        
        return compositeProvider;
    }
} 