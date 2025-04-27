package com.handmade.ecommerce.image.storage.azure;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.handmade.ecommerce.image.storage.StorageProvider;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class AzureBlobStorageProvider implements StorageProvider {

    private final BlobServiceClient blobServiceClient;

    public AzureBlobStorageProvider(String connectionString) {
        this.blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
    }

    @Override
    public CompletableFuture<String> uploadFile(String bucketName, String objectKey,
                                               InputStream inputStream, String contentType,
                                               Map<String, String> metadata) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(bucketName);
                BlobClient blobClient = containerClient.getBlobClient(objectKey);
                
                // Set content type
                BlobHttpHeaders headers = new BlobHttpHeaders().setContentType(contentType);
                
                // Upload the file
                blobClient.upload(inputStream, inputStream.available(), true);
                
                // Set metadata if provided
                if (metadata != null && !metadata.isEmpty()) {
                    blobClient.setMetadata(metadata);
                }
                
                // Set headers
                blobClient.setHttpHeaders(headers);
                
                return getPublicUrl(bucketName, objectKey);
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload file to Azure Blob Storage", e);
            }
        });
    }

    @Override
    public CompletableFuture<InputStream> downloadFile(String bucketName, String objectKey) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(bucketName);
                BlobClient blobClient = containerClient.getBlobClient(objectKey);
                
                if (!blobClient.exists()) {
                    throw new RuntimeException("File not found: " + objectKey);
                }
                
                return blobClient.openInputStream();
            } catch (Exception e) {
                throw new RuntimeException("Failed to download file from Azure Blob Storage", e);
            }
        });
    }

    @Override
    public CompletableFuture<Void> deleteFile(String bucketName, String objectKey) {
        return CompletableFuture.runAsync(() -> {
            try {
                BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(bucketName);
                BlobClient blobClient = containerClient.getBlobClient(objectKey);
                
                if (!blobClient.exists()) {
                    log.warn("File not found for deletion: {}", objectKey);
                    return;
                }
                
                blobClient.delete();
            } catch (Exception e) {
                throw new RuntimeException("Failed to delete file from Azure Blob Storage", e);
            }
        });
    }

    @Override
    public String getPublicUrl(String bucketName, String objectKey) {
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(bucketName);
        BlobClient blobClient = containerClient.getBlobClient(objectKey);
        return blobClient.getBlobUrl();
    }

    @Override
    public CompletableFuture<Boolean> fileExists(String bucketName, String objectKey) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(bucketName);
                BlobClient blobClient = containerClient.getBlobClient(objectKey);
                return blobClient.exists();
            } catch (Exception e) {
                log.warn("Failed to check if file exists in Azure Blob Storage", e);
                return false;
            }
        });
    }

    @Override
    public CompletableFuture<Map<String, String>> getFileMetadata(String bucketName, String objectKey) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(bucketName);
                BlobClient blobClient = containerClient.getBlobClient(objectKey);
                
                if (!blobClient.exists()) {
                    throw new RuntimeException("File not found: " + objectKey);
                }
                
                Map<String, String> metadata = blobClient.getProperties().getMetadata();
                return metadata != null ? metadata : new HashMap<>();
            } catch (Exception e) {
                throw new RuntimeException("Failed to get file metadata from Azure Blob Storage", e);
            }
        });
    }
} 