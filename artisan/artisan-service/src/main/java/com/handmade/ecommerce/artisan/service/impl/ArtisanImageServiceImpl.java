package com.handmade.ecommerce.artisan.service.impl;

import com.handmade.ecommerce.artisan.service.ArtisanImageService;
import com.handmade.ecommerce.image.api.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class ArtisanImageServiceImpl implements ArtisanImageService {

    private final ImageService imageService;

    @Value("${artisan.image.profile-bucket:artisan-profiles}")
    private String profileBucket;

    @Value("${artisan.image.portfolio-bucket:artisan-portfolios}")
    private String portfolioBucket;

    @Value("${artisan.image.sizes:thumbnail,small,medium,large}")
    private String[] imageSizes;

    @Autowired
    public ArtisanImageServiceImpl(@Qualifier("imageService") ImageService imageService) {
        this.imageService = imageService;
        log.info("Initialized ArtisanImageServiceImpl with ImageService: {}", imageService.getClass().getName());
    }

    @Override
    public CompletableFuture<String> uploadProfileImage(String artisanId, InputStream imageStream,
                                                      String contentType, Map<String, String> metadata) {
        String imageKey = generateProfileImageKey(artisanId);
        Map<String, String> enrichedMetadata = enrichMetadata(metadata, artisanId, "profile");
        
        return imageService.uploadImageWithResize(profileBucket, imageKey, imageStream, contentType, 
                                                imageSizes, enrichedMetadata)
                .thenApply(urls -> urls.get("original"));
    }

    @Override
    public CompletableFuture<Map<String, String>> uploadPortfolioImage(String artisanId, String portfolioId,
                                                                     InputStream imageStream, String contentType,
                                                                     Map<String, String> metadata) {
        String imageKey = generatePortfolioImageKey(artisanId, portfolioId);
        Map<String, String> enrichedMetadata = enrichMetadata(metadata, artisanId, "portfolio");
        
        return imageService.uploadImageWithResize(portfolioBucket, imageKey, imageStream, contentType,
                                                imageSizes, enrichedMetadata);
    }

    @Override
    public CompletableFuture<Void> deleteProfileImage(String artisanId) {
        String imageKey = generateProfileImageKey(artisanId);
        return imageService.deleteImage(profileBucket, imageKey);
    }

    @Override
    public CompletableFuture<Void> deletePortfolioImage(String artisanId, String portfolioId, String imageKey) {
        String fullImageKey = generatePortfolioImageKey(artisanId, portfolioId);
        return imageService.deleteImage(portfolioBucket, fullImageKey);
    }

    @Override
    public String getProfileImageUrl(String artisanId, String size) {
        String imageKey = generateProfileImageKey(artisanId);
        return imageService.getImageUrl(profileBucket, imageKey, size);
    }

    @Override
    public String getPortfolioImageUrl(String artisanId, String portfolioId, String imageKey, String size) {
        String fullImageKey = generatePortfolioImageKey(artisanId, portfolioId);
        return imageService.getImageUrl(portfolioBucket, fullImageKey, size);
    }

    @Override
    public CompletableFuture<Boolean> profileImageExists(String artisanId) {
        String imageKey = generateProfileImageKey(artisanId);
        return imageService.imageExists(profileBucket, imageKey);
    }

    @Override
    public CompletableFuture<Boolean> portfolioImageExists(String artisanId, String portfolioId, String imageKey) {
        String fullImageKey = generatePortfolioImageKey(artisanId, portfolioId);
        return imageService.imageExists(portfolioBucket, fullImageKey);
    }

    private String generateProfileImageKey(String artisanId) {
        return String.format("artisans/%s/profile", artisanId);
    }

    private String generatePortfolioImageKey(String artisanId, String portfolioId) {
        return String.format("artisans/%s/portfolios/%s", artisanId, portfolioId);
    }

    private Map<String, String> enrichMetadata(Map<String, String> metadata, String artisanId, String imageType) {
        Map<String, String> enrichedMetadata = new HashMap<>(metadata != null ? metadata : new HashMap<>());
        enrichedMetadata.put("artisanId", artisanId);
        enrichedMetadata.put("imageType", imageType);
        enrichedMetadata.put("uploadTimestamp", String.valueOf(System.currentTimeMillis()));
        return enrichedMetadata;
    }
} 