package com.handmade.ecommerce.artisan.service;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Service for managing artisan-related images using the image storage module.
 */
public interface ArtisanImageService {

    /**
     * Upload an artisan profile image.
     *
     * @param artisanId The ID of the artisan
     * @param imageStream The image input stream
     * @param contentType The content type of the image
     * @param metadata Additional metadata for the image
     * @return A CompletableFuture that completes with the public URL of the uploaded image
     */
    CompletableFuture<String> uploadProfileImage(String artisanId, InputStream imageStream, 
                                               String contentType, Map<String, String> metadata);

    /**
     * Upload a portfolio image with multiple sizes.
     *
     * @param artisanId The ID of the artisan
     * @param portfolioId The ID of the portfolio item
     * @param imageStream The image input stream
     * @param contentType The content type of the image
     * @param metadata Additional metadata for the image
     * @return A CompletableFuture that completes with a map of size keys to their respective URLs
     */
    CompletableFuture<Map<String, String>> uploadPortfolioImage(String artisanId, String portfolioId,
                                                              InputStream imageStream, String contentType,
                                                              Map<String, String> metadata);

    /**
     * Delete an artisan profile image.
     *
     * @param artisanId The ID of the artisan
     * @return A CompletableFuture that completes when the deletion is done
     */
    CompletableFuture<Void> deleteProfileImage(String artisanId);

    /**
     * Delete a portfolio image and all its resized versions.
     *
     * @param artisanId The ID of the artisan
     * @param portfolioId The ID of the portfolio item
     * @param imageKey The key of the image to delete
     * @return A CompletableFuture that completes when the deletion is done
     */
    CompletableFuture<Void> deletePortfolioImage(String artisanId, String portfolioId, String imageKey);

    /**
     * Get the URL of an artisan's profile image.
     *
     * @param artisanId The ID of the artisan
     * @param size Optional size parameter for resized versions
     * @return The public URL of the image
     */
    String getProfileImageUrl(String artisanId, String size);

    /**
     * Get the URL of a portfolio image.
     *
     * @param artisanId The ID of the artisan
     * @param portfolioId The ID of the portfolio item
     * @param imageKey The key of the image
     * @param size Optional size parameter for resized versions
     * @return The public URL of the image
     */
    String getPortfolioImageUrl(String artisanId, String portfolioId, String imageKey, String size);

    /**
     * Check if an artisan's profile image exists.
     *
     * @param artisanId The ID of the artisan
     * @return A CompletableFuture that completes with true if the image exists
     */
    CompletableFuture<Boolean> profileImageExists(String artisanId);

    /**
     * Check if a portfolio image exists.
     *
     * @param artisanId The ID of the artisan
     * @param portfolioId The ID of the portfolio item
     * @param imageKey The key of the image
     * @return A CompletableFuture that completes with true if the image exists
     */
    CompletableFuture<Boolean> portfolioImageExists(String artisanId, String portfolioId, String imageKey);
} 