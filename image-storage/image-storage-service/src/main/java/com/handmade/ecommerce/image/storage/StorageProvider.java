package com.handmade.ecommerce.image.storage;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface StorageProvider {
    
    /**
     * Uploads a file to the storage provider
     *
     * @param bucketName The name of the bucket/container
     * @param objectKey The key/path of the object in the bucket
     * @param inputStream The input stream of the file to upload
     * @param contentType The content type of the file
     * @param metadata Additional metadata to store with the file
     * @return A CompletableFuture that completes with the URL of the uploaded file
     */
    CompletableFuture<String> uploadFile(String bucketName, String objectKey, 
                                       InputStream inputStream, String contentType, 
                                       Map<String, String> metadata);
    
    /**
     * Downloads a file from the storage provider
     *
     * @param bucketName The name of the bucket/container
     * @param objectKey The key/path of the object in the bucket
     * @return A CompletableFuture that completes with the input stream of the downloaded file
     */
    CompletableFuture<InputStream> downloadFile(String bucketName, String objectKey);
    
    /**
     * Deletes a file from the storage provider
     *
     * @param bucketName The name of the bucket/container
     * @param objectKey The key/path of the object in the bucket
     * @return A CompletableFuture that completes when the file is deleted
     */
    CompletableFuture<Void> deleteFile(String bucketName, String objectKey);
    
    /**
     * Gets the public URL for a file
     *
     * @param bucketName The name of the bucket/container
     * @param objectKey The key/path of the object in the bucket
     * @return The public URL of the file
     */
    String getPublicUrl(String bucketName, String objectKey);
    
    /**
     * Checks if a file exists
     *
     * @param bucketName The name of the bucket/container
     * @param objectKey The key/path of the object in the bucket
     * @return A CompletableFuture that completes with true if the file exists, false otherwise
     */
    CompletableFuture<Boolean> fileExists(String bucketName, String objectKey);
    
    /**
     * Gets the metadata for a file
     *
     * @param bucketName The name of the bucket/container
     * @param objectKey The key/path of the object in the bucket
     * @return A CompletableFuture that completes with the metadata of the file
     */
    CompletableFuture<Map<String, String>> getFileMetadata(String bucketName, String objectKey);
} 