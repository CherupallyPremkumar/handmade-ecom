package com.handmade.ecommerce.image.api.controller;

import com.handmade.ecommerce.image.api.service.ImageService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/images")

public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CompletableFuture<ResponseEntity<String>> uploadImage(
            @RequestParam("bucket") String bucketName,
            @RequestParam("key") String imageKey,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "sizes", required = false) String[] sizes) {
        
        try {
            if (sizes != null && sizes.length > 0) {
                return imageService.uploadImageWithResize(bucketName, imageKey, file.getInputStream(),
                        file.getContentType(), sizes, Map.of("originalName", file.getOriginalFilename()))
                        .thenApply(urls -> ResponseEntity.ok(urls.toString()));
            } else {
                return imageService.uploadImage(bucketName, imageKey, file.getInputStream(),
                        file.getContentType(), Map.of("originalName", file.getOriginalFilename()))
                        .thenApply(url -> ResponseEntity.ok(url));
            }
        } catch (Exception e) {
            return CompletableFuture.completedFuture(
                    ResponseEntity.badRequest().body("Failed to upload image: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{bucket}/{key}")
    public CompletableFuture<ResponseEntity<Object>> deleteImage(
            @PathVariable("bucket") String bucketName,
            @PathVariable("key") String imageKey) {
        
        return imageService.deleteImage(bucketName, imageKey)
                .thenApply(v -> ResponseEntity.ok().build())
                .exceptionally(e -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/{bucket}/{key}")
    public ResponseEntity<String> getImageUrl(
            @PathVariable("bucket") String bucketName,
            @PathVariable("key") String imageKey,
            @RequestParam(value = "size", required = false) String size) {
        
        return ResponseEntity.ok(imageService.getImageUrl(bucketName, imageKey, size));
    }

    @GetMapping("/{bucket}/{key}/exists")
    public CompletableFuture<ResponseEntity<Boolean>> imageExists(
            @PathVariable("bucket") String bucketName,
            @PathVariable("key") String imageKey) {
        
        return imageService.imageExists(bucketName, imageKey)
                .thenApply(ResponseEntity::ok)
                .exceptionally(e -> ResponseEntity.badRequest().body(false));
    }
} 