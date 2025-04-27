package com.handmade.ecommerce.image.api.service;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface ImageService {
    
    /**
     * Uploads an image to the storage provider
     *
     * @param bucketName The name of the bucket/container
     * @param imageKey The key/path of the image in the bucket
     * @param imageStream The input stream of the image
     * @param contentType The content type of the image
     * @param metadata Additional metadata to store with the image
     * @return A CompletableFuture that completes with the URL of the uploaded image
     */
    CompletableFuture<String> uploadImage(String bucketName, String imageKey,
                                        InputStream imageStream, String contentType,
                                        Map<String, String> metadata);
    
    /**
     * Uploads an image and creates multiple resized versions
     *
     * @param bucketName The name of the bucket/container
     * @param imageKey The key/path of the image in the bucket
     * @param imageStream The input stream of the image
     * @param contentType The content type of the image
     * @param sizes The sizes to create (e.g., "100x100", "200x200")
     * @param metadata Additional metadata to store with the image
     * @return A CompletableFuture that completes with a map of size to URL for all versions
     */
    CompletableFuture<Map<String, String>> uploadImageWithResize(String bucketName, String imageKey,
                                                               InputStream imageStream, String contentType,
                                                               String[] sizes, Map<String, String> metadata);
    
    /**
     * Deletes an image and all its resized versions
     *
     * @param bucketName The name of the bucket/container
     * @param imageKey The key/path of the image in the bucket
     * @return A CompletableFuture that completes when all versions are deleted
     */
    CompletableFuture<Void> deleteImage(String bucketName, String imageKey);
    
    /**
     * Gets the URL for an image
     *
     * @param bucketName The name of the bucket/container
     * @param imageKey The key/path of the image in the bucket
     * @param size The size of the image to get (null for original)
     * @return The URL of the image
     */
    String getImageUrl(String bucketName, String imageKey, String size);
    
    /**
     * Checks if an image exists
     *
     * @param bucketName The name of the bucket/container
     * @param imageKey The key/path of the image in the bucket
     * @return A CompletableFuture that completes with true if the image exists, false otherwise
     */
    CompletableFuture<Boolean> imageExists(String bucketName, String imageKey);
} 