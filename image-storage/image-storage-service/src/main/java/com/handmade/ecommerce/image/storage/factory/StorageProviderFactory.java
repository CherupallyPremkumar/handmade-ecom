package com.handmade.ecommerce.image.storage.factory;

import com.handmade.ecommerce.image.storage.StorageProvider;
import com.handmade.ecommerce.image.storage.aws.S3StorageProvider;
import com.handmade.ecommerce.image.storage.azure.AzureBlobStorageProvider;
import com.handmade.ecommerce.image.storage.gcs.GoogleCloudStorageProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class StorageProviderFactory {

    @Value("${storage.aws.access-key:}")
    private String awsAccessKey;

    @Value("${storage.aws.secret-key:}")
    private String awsSecretKey;

    @Value("${storage.aws.region:}")
    private String awsRegion;

    @Value("${storage.gcs.project-id:}")
    private String gcsProjectId;

    @Value("${storage.gcs.credentials-file:}")
    private String gcsCredentialsFile;

    @Value("${storage.azure.connection-string:}")
    private String azureConnectionString;

    private final Map<String, StorageProvider> providerCache = new HashMap<>();

    public StorageProvider getProvider(String providerType) {
        return providerCache.computeIfAbsent(providerType, this::createProvider);
    }

    private StorageProvider createProvider(String providerType) {
        return switch (providerType.toLowerCase()) {
            case "aws" -> createS3StorageProvider();
            case "gcs" -> createGoogleCloudStorageProvider();
            case "azure" -> createAzureBlobStorageProvider();
            default -> throw new IllegalArgumentException("Unsupported storage provider: " + providerType);
        };
    }

    private StorageProvider createS3StorageProvider() {
        if (awsAccessKey.isEmpty() || awsSecretKey.isEmpty() || awsRegion.isEmpty()) {
            throw new IllegalStateException("AWS credentials not properly configured");
        }
        return new S3StorageProvider(awsAccessKey, awsSecretKey, awsRegion);
    }

    private StorageProvider createGoogleCloudStorageProvider() {
        if (gcsProjectId.isEmpty() || gcsCredentialsFile.isEmpty()) {
            throw new IllegalStateException("Google Cloud Storage credentials not properly configured");
        }
        return new GoogleCloudStorageProvider(gcsProjectId, gcsCredentialsFile);
    }

    private StorageProvider createAzureBlobStorageProvider() {
        if (azureConnectionString.isEmpty()) {
            throw new IllegalStateException("Azure Storage connection string not properly configured");
        }
        return new AzureBlobStorageProvider(azureConnectionString);
    }
} 