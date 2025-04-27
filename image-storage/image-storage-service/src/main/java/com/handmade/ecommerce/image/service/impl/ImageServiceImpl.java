package com.handmade.ecommerce.image.service.impl;

import com.handmade.ecommerce.image.api.service.ImageService;
import com.handmade.ecommerce.image.storage.StorageProvider;
import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


@Service
public class ImageServiceImpl implements ImageService {

    Logger logger=LoggerFactory.getLogger(ImageServiceImpl.class);

    private final StorageProvider storageProvider;
    private static final String SIZE_SEPARATOR = "x";

    public ImageServiceImpl(StorageProvider storageProvider) {
        this.storageProvider = storageProvider;
    }

    @Override
    public CompletableFuture<String> uploadImage(String bucketName, String imageKey,
                                               InputStream imageStream, String contentType,
                                               Map<String, String> metadata) {
        return storageProvider.uploadFile(bucketName, imageKey, imageStream, contentType, metadata);
    }

    @Override
    public CompletableFuture<Map<String, String>> uploadImageWithResize(String bucketName, String imageKey,
                                                                      InputStream imageStream, String contentType,
                                                                      String[] sizes, Map<String, String> metadata) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Read the original image
                BufferedImage originalImage = ImageIO.read(imageStream);
                Map<String, String> urls = new HashMap<>();

                // Upload original image
                ByteArrayOutputStream originalOutputStream = new ByteArrayOutputStream();
                ImageIO.write(originalImage, getImageFormat(contentType), originalOutputStream);
                String originalUrl = storageProvider.uploadFile(bucketName, imageKey,
                        new ByteArrayInputStream(originalOutputStream.toByteArray()),
                        contentType, metadata).get();
                urls.put("original", originalUrl);

                // Create and upload resized versions
                for (String size : sizes) {
                    String[] dimensions = size.split(SIZE_SEPARATOR);
                    int width = Integer.parseInt(dimensions[0]);
                    int height = Integer.parseInt(dimensions[1]);

                    BufferedImage resizedImage = Scalr.resize(originalImage, width, height);
                    ByteArrayOutputStream resizedOutputStream = new ByteArrayOutputStream();
                    ImageIO.write(resizedImage, getImageFormat(contentType), resizedOutputStream);

                    String resizedKey = getResizedImageKey(imageKey, size);
                    String resizedUrl = storageProvider.uploadFile(bucketName, resizedKey,
                            new ByteArrayInputStream(resizedOutputStream.toByteArray()),
                            contentType, metadata).get();
                    urls.put(size, resizedUrl);
                }

                return urls;
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload and resize image", e);
            }
        });
    }

    @Override
    public CompletableFuture<Void> deleteImage(String bucketName, String imageKey) {
        return CompletableFuture.runAsync(() -> {
            try {
                // Delete original image
                storageProvider.deleteFile(bucketName, imageKey).get();

                // Delete all resized versions
                Map<String, String> metadata = storageProvider.getFileMetadata(bucketName, imageKey).get();
                if (metadata != null && metadata.containsKey("sizes")) {
                    String[] sizes = metadata.get("sizes").split(",");
                    for (String size : sizes) {
                        String resizedKey = getResizedImageKey(imageKey, size);
                        storageProvider.deleteFile(bucketName, resizedKey).get();
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to delete image and its resized versions", e);
            }
        });
    }

    @Override
    public String getImageUrl(String bucketName, String imageKey, String size) {
        if (size == null || size.equals("original")) {
            return storageProvider.getPublicUrl(bucketName, imageKey);
        }
        return storageProvider.getPublicUrl(bucketName, getResizedImageKey(imageKey, size));
    }

    @Override
    public CompletableFuture<Boolean> imageExists(String bucketName, String imageKey) {
        return storageProvider.fileExists(bucketName, imageKey);
    }

    private String getResizedImageKey(String originalKey, String size) {
        int lastDotIndex = originalKey.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return originalKey + "_" + size;
        }
        return originalKey.substring(0, lastDotIndex) + "_" + size + originalKey.substring(lastDotIndex);
    }

    private String getImageFormat(String contentType) {
        return contentType.substring(contentType.lastIndexOf('/') + 1);
    }
} 