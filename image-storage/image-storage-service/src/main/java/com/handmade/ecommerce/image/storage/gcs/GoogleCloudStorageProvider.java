package com.handmade.ecommerce.image.storage.gcs;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.handmade.ecommerce.image.storage.StorageProvider;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class GoogleCloudStorageProvider implements StorageProvider {

    private final Storage storage;
    private final String projectId;

    public GoogleCloudStorageProvider(String projectId, String credentialsFilePath) {
        this.projectId = projectId;
        this.storage = StorageOptions.newBuilder()
                .setProjectId(projectId)
                .build()
                .getService();
    }

    @Override
    public CompletableFuture<String> uploadFile(String bucketName, String objectKey,
                                               InputStream inputStream, String contentType,
                                               Map<String, String> metadata) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                BlobId blobId = BlobId.of(bucketName, objectKey);
                BlobInfo.Builder blobInfoBuilder = BlobInfo.newBuilder(blobId)
                        .setContentType(contentType);

                // Add metadata if provided
                if (metadata != null && !metadata.isEmpty()) {
                    blobInfoBuilder.setMetadata(metadata);
                }

                BlobInfo blobInfo = blobInfoBuilder.build();
                Blob blob = storage.create(blobInfo, inputStream.readAllBytes());

                return getPublicUrl(bucketName, objectKey);
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload file to Google Cloud Storage", e);
            }
        });
    }

    @Override
    public CompletableFuture<InputStream> downloadFile(String bucketName, String objectKey) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                BlobId blobId = BlobId.of(bucketName, objectKey);
                Blob blob = storage.get(blobId);
                
                if (blob == null) {
                    throw new RuntimeException("File not found: " + objectKey);
                }
                
                return blob.getContent(Blob.BlobSourceOption.generationMatch());
            } catch (Exception e) {
                throw new RuntimeException("Failed to download file from Google Cloud Storage", e);
            }
        });
    }

    @Override
    public CompletableFuture<Void> deleteFile(String bucketName, String objectKey) {
        return CompletableFuture.runAsync(() -> {
            try {
                BlobId blobId = BlobId.of(bucketName, objectKey);
                boolean deleted = storage.delete(blobId);
                
                if (!deleted) {
                    log.warn("File not found for deletion: {}", objectKey);
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to delete file from Google Cloud Storage", e);
            }
        });
    }

    @Override
    public String getPublicUrl(String bucketName, String objectKey) {
        return String.format("https://storage.googleapis.com/%s/%s", bucketName, objectKey);
    }

    @Override
    public CompletableFuture<Boolean> fileExists(String bucketName, String objectKey) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                BlobId blobId = BlobId.of(bucketName, objectKey);
                Blob blob = storage.get(blobId);
                return blob != null;
            } catch (Exception e) {
                log.warn("Failed to check if file exists in Google Cloud Storage", e);
                return false;
            }
        });
    }

    @Override
    public CompletableFuture<Map<String, String>> getFileMetadata(String bucketName, String objectKey) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                BlobId blobId = BlobId.of(bucketName, objectKey);
                Blob blob = storage.get(blobId);
                
                if (blob == null) {
                    throw new RuntimeException("File not found: " + objectKey);
                }
                
                Map<String, String> metadata = blob.getMetadata();
                return metadata != null ? metadata : new HashMap<>();
            } catch (Exception e) {
                throw new RuntimeException("Failed to get file metadata from Google Cloud Storage", e);
            }
        });
    }
} 