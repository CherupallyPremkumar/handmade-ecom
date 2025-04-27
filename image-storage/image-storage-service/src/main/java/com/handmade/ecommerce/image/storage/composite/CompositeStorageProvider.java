package com.handmade.ecommerce.image.storage.composite;

import com.handmade.ecommerce.image.storage.StorageProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class CompositeStorageProvider implements StorageProvider {

    private final Map<String, StorageProvider> providers = new ConcurrentHashMap<>();
    private final String defaultProvider;

    public CompositeStorageProvider(String defaultProvider) {
        this.defaultProvider = defaultProvider;
    }

    public void addProvider(String name, StorageProvider provider) {
        providers.put(name, provider);
        log.info("Added storage provider: {}", name);
    }

    public void removeProvider(String name) {
        providers.remove(name);
        log.info("Removed storage provider: {}", name);
    }

    public StorageProvider getProvider(String name) {
        return providers.getOrDefault(name, providers.get(defaultProvider));
    }

    @Override
    public CompletableFuture<String> uploadFile(String bucketName, String objectKey,
                                               InputStream inputStream, String contentType,
                                               Map<String, String> metadata) {
        // Use the default provider for upload
        return getProvider(defaultProvider).uploadFile(bucketName, objectKey, inputStream, contentType, metadata);
    }

    @Override
    public CompletableFuture<InputStream> downloadFile(String bucketName, String objectKey) {
        // Try each provider until we find the file
        for (StorageProvider provider : providers.values()) {
            try {
                CompletableFuture<Boolean> existsFuture = provider.fileExists(bucketName, objectKey);
                if (existsFuture.get()) {
                    return provider.downloadFile(bucketName, objectKey);
                }
            } catch (Exception e) {
                log.warn("Failed to check if file exists in provider: {}", provider.getClass().getSimpleName(), e);
            }
        }
        
        // If we get here, the file wasn't found in any provider
        return CompletableFuture.failedFuture(new RuntimeException("File not found in any storage provider"));
    }

    @Override
    public CompletableFuture<Void> deleteFile(String bucketName, String objectKey) {
        // Delete from all providers
        CompletableFuture<Void>[] futures = providers.values().stream()
                .map(provider -> provider.deleteFile(bucketName, objectKey)
                        .exceptionally(e -> {
                            log.warn("Failed to delete file from provider: {}", provider.getClass().getSimpleName(), e);
                            return null;
                        }))
                .toArray(CompletableFuture[]::new);
        
        return CompletableFuture.allOf(futures);
    }

    @Override
    public String getPublicUrl(String bucketName, String objectKey) {
        // Try each provider until we find the file
        for (StorageProvider provider : providers.values()) {
            try {
                if (provider.fileExists(bucketName, objectKey).get()) {
                    return provider.getPublicUrl(bucketName, objectKey);
                }
            } catch (Exception e) {
                log.warn("Failed to check if file exists in provider: {}", provider.getClass().getSimpleName(), e);
            }
        }
        
        // If we get here, the file wasn't found in any provider
        throw new RuntimeException("File not found in any storage provider");
    }

    @Override
    public CompletableFuture<Boolean> fileExists(String bucketName, String objectKey) {
        // Check all providers
        CompletableFuture<Boolean>[] futures = providers.values().stream()
                .map(provider -> provider.fileExists(bucketName, objectKey)
                        .exceptionally(e -> {
                            log.warn("Failed to check if file exists in provider: {}", provider.getClass().getSimpleName(), e);
                            return false;
                        }))
                .toArray(CompletableFuture[]::new);
        
        return CompletableFuture.allOf(futures)
                .thenApply(v -> {
                    for (CompletableFuture<Boolean> future : futures) {
                        try {
                            if (future.get()) {
                                return true;
                            }
                        } catch (Exception e) {
                            log.warn("Failed to get result from provider", e);
                        }
                    }
                    return false;
                });
    }

    @Override
    public CompletableFuture<Map<String, String>> getFileMetadata(String bucketName, String objectKey) {
        // Try each provider until we find the file
        for (StorageProvider provider : providers.values()) {
            try {
                if (provider.fileExists(bucketName, objectKey).get()) {
                    return provider.getFileMetadata(bucketName, objectKey);
                }
            } catch (Exception e) {
                log.warn("Failed to check if file exists in provider: {}", provider.getClass().getSimpleName(), e);
            }
        }
        
        // If we get here, the file wasn't found in any provider
        return CompletableFuture.failedFuture(new RuntimeException("File not found in any storage provider"));
    }
} 