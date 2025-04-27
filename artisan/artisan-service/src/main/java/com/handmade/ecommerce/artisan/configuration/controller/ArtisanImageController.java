package com.handmade.ecommerce.artisan.configuration.controller;

import com.handmade.ecommerce.artisan.service.ArtisanImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/api/v1/artisans")
@RequiredArgsConstructor
public class ArtisanImageController {

    private final ArtisanImageService artisanImageService;

    @PostMapping(value = "/{artisanId}/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CompletableFuture<ResponseEntity<String>> uploadProfileImage(
            @PathVariable String artisanId,
            @RequestParam("file") MultipartFile file) throws IOException {
        
        return artisanImageService.uploadProfileImage(
                artisanId,
                file.getInputStream(),
                file.getContentType(),
                Map.of("originalFilename", Objects.requireNonNull(file.getOriginalFilename()))
        ).thenApply(ResponseEntity::ok);
    }

    @PostMapping(value = "/{artisanId}/portfolios/{portfolioId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CompletableFuture<ResponseEntity<Map<String, String>>> uploadPortfolioImage(
            @PathVariable String artisanId,
            @PathVariable String portfolioId,
            @RequestParam("file") MultipartFile file) throws IOException {
        
        return artisanImageService.uploadPortfolioImage(
                artisanId,
                portfolioId,
                file.getInputStream(),
                file.getContentType(),
                Map.of("originalFilename", Objects.requireNonNull(file.getOriginalFilename()))
        ).thenApply(ResponseEntity::ok);
    }

    @DeleteMapping("/{artisanId}/profile-image")
    public CompletableFuture<ResponseEntity<Void>> deleteProfileImage(
            @PathVariable String artisanId) {
        
        return artisanImageService.deleteProfileImage(artisanId)
                .thenApply(v -> ResponseEntity.ok().build());
    }

    @DeleteMapping("/{artisanId}/portfolios/{portfolioId}/images/{imageKey}")
    public CompletableFuture<ResponseEntity<Void>> deletePortfolioImage(
            @PathVariable String artisanId,
            @PathVariable String portfolioId,
            @PathVariable String imageKey) {
        
        return artisanImageService.deletePortfolioImage(artisanId, portfolioId, imageKey)
                .thenApply(v -> ResponseEntity.ok().build());
    }

    @GetMapping("/{artisanId}/profile-image")
    public ResponseEntity<String> getProfileImageUrl(
            @PathVariable String artisanId,
            @RequestParam(required = false) String size) {
        
        String url = artisanImageService.getProfileImageUrl(artisanId, size);
        return ResponseEntity.ok(url);
    }

    @GetMapping("/{artisanId}/portfolios/{portfolioId}/images/{imageKey}")
    public ResponseEntity<String> getPortfolioImageUrl(
            @PathVariable String artisanId,
            @PathVariable String portfolioId,
            @PathVariable String imageKey,
            @RequestParam(required = false) String size) {
        
        String url = artisanImageService.getPortfolioImageUrl(artisanId, portfolioId, imageKey, size);
        return ResponseEntity.ok(url);
    }

    @GetMapping("/{artisanId}/profile-image/exists")
    public CompletableFuture<ResponseEntity<Boolean>> profileImageExists(
            @PathVariable String artisanId) {
        
        return artisanImageService.profileImageExists(artisanId)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{artisanId}/portfolios/{portfolioId}/images/{imageKey}/exists")
    public CompletableFuture<ResponseEntity<Boolean>> portfolioImageExists(
            @PathVariable String artisanId,
            @PathVariable String portfolioId,
            @PathVariable String imageKey) {
        
        return artisanImageService.portfolioImageExists(artisanId, portfolioId, imageKey)
                .thenApply(ResponseEntity::ok);
    }
} 