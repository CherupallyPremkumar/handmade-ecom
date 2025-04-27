package com.handmade.ecommerce.image.storage.aws;

import com.handmade.ecommerce.image.storage.StorageProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.InputStream;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class S3StorageProvider implements StorageProvider {

    private final S3Client s3Client;
    private final S3Presigner presigner;
    private final String region;

    public S3StorageProvider(String accessKey, String secretKey, String region) {
        this.region = region;
        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(accessKey, secretKey))
                .build();
        this.presigner = S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(accessKey, secretKey))
                .build();
    }

    @Override
    public CompletableFuture<String> uploadFile(String bucketName, String objectKey,
                                               InputStream inputStream, String contentType,
                                               Map<String, String> metadata) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                PutObjectRequest.Builder requestBuilder = PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(objectKey)
                        .contentType(contentType);

                if (metadata != null && !metadata.isEmpty()) {
                    requestBuilder.metadata(metadata);
                }

                s3Client.putObject(requestBuilder.build(),
                        RequestBody.fromInputStream(inputStream, inputStream.available()));

                return getPublicUrl(bucketName, objectKey);
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload file to S3", e);
            }
        });
    }

    @Override
    public CompletableFuture<InputStream> downloadFile(String bucketName, String objectKey) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                GetObjectRequest request = GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(objectKey)
                        .build();
                return s3Client.getObject(request);
            } catch (Exception e) {
                throw new RuntimeException("Failed to download file from S3", e);
            }
        });
    }

    @Override
    public CompletableFuture<Void> deleteFile(String bucketName, String objectKey) {
        return CompletableFuture.runAsync(() -> {
            try {
                DeleteObjectRequest request = DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(objectKey)
                        .build();
                s3Client.deleteObject(request);
            } catch (Exception e) {
                throw new RuntimeException("Failed to delete file from S3", e);
            }
        });
    }

    @Override
    public String getPublicUrl(String bucketName, String objectKey) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofHours(1))
                    .getObjectRequest(getObjectRequest)
                    .build();

            return presigner.presignGetObject(presignRequest).url().toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate presigned URL", e);
        }
    }

    @Override
    public CompletableFuture<Boolean> fileExists(String bucketName, String objectKey) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                HeadObjectRequest request = HeadObjectRequest.builder()
                        .bucket(bucketName)
                        .key(objectKey)
                        .build();
                s3Client.headObject(request);
                return true;
            } catch (NoSuchKeyException e) {
                return false;
            } catch (Exception e) {
                throw new RuntimeException("Failed to check if file exists in S3", e);
            }
        });
    }

    @Override
    public CompletableFuture<Map<String, String>> getFileMetadata(String bucketName, String objectKey) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                HeadObjectRequest request = HeadObjectRequest.builder()
                        .bucket(bucketName)
                        .key(objectKey)
                        .build();
                HeadObjectResponse response = s3Client.headObject(request);
                return response.metadata();
            } catch (Exception e) {
                throw new RuntimeException("Failed to get file metadata from S3", e);
            }
        });
    }
} 