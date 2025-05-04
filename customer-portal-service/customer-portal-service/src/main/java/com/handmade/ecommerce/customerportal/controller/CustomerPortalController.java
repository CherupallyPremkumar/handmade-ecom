package com.handmade.ecommerce.customerportal.controller;

import com.handmade.ecommerce.customerportal.dto.*;
import com.handmade.ecommerce.customerportal.model.*;
import com.handmade.ecommerce.customerportal.service.CustomerPortalService;
import com.handmade.ecommerce.customerportal.service.DtoMapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * REST controller for customer portal operations.
 */
@RestController
@RequestMapping("/api/v1/customer-portal")
public class CustomerPortalController {

    private final CustomerPortalService customerPortalService;
    private final DtoMapperService dtoMapperService;

    @Autowired
    public CustomerPortalController(CustomerPortalService customerPortalService, DtoMapperService dtoMapperService) {
        this.customerPortalService = customerPortalService;
        this.dtoMapperService = dtoMapperService;
    }

    /**
     * Search for products with filters
     */
    @GetMapping("/products/search")
    public ResponseEntity<List<ProductViewDTO>> searchProducts(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false, defaultValue = "newest") String sortBy,
            @RequestParam(required = false, defaultValue = "20") int limit,
            @RequestParam(required = false, defaultValue = "0") int offset) {
        
        try {
            List<ProductView> products = customerPortalService.searchProducts(
                    tenantId, searchTerm, minPrice, maxPrice, sortBy, limit, offset);
            
            List<ProductViewDTO> dtos = dtoMapperService.toDtoList(
                    products, dtoMapperService::toProductViewDto);
            
            return ResponseEntity.ok(dtos != null ? dtos : Collections.emptyList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error searching products: " + e.getMessage(), e);
        }
    }

    /**
     * Get products by category
     */
    @GetMapping("/products/category/{categoryId}")
    public ResponseEntity<List<ProductViewDTO>> getProductsByCategory(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @PathVariable String categoryId,
            @RequestParam(required = false, defaultValue = "newest") String sortBy,
            @RequestParam(required = false, defaultValue = "20") int limit,
            @RequestParam(required = false, defaultValue = "0") int offset) {
        
        try {
            List<ProductView> products = customerPortalService.getProductsByCategory(
                    tenantId, categoryId, sortBy, limit, offset);
            
            List<ProductViewDTO> dtos = dtoMapperService.toDtoList(
                    products, dtoMapperService::toProductViewDto);
            
            return ResponseEntity.ok(dtos != null ? dtos : Collections.emptyList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error getting products by category: " + e.getMessage(), e);
        }
    }

    /**
     * Get products by artisan
     */
    @GetMapping("/products/artisan/{artisanId}")
    public ResponseEntity<List<ProductViewDTO>> getProductsByArtisan(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @PathVariable String artisanId,
            @RequestParam(required = false, defaultValue = "20") int limit,
            @RequestParam(required = false, defaultValue = "0") int offset) {
        
        try {
            List<ProductView> products = customerPortalService.getProductsByArtisan(
                    tenantId, artisanId, limit, offset);
            
            List<ProductViewDTO> dtos = dtoMapperService.toDtoList(
                    products, dtoMapperService::toProductViewDto);
            
            return ResponseEntity.ok(dtos != null ? dtos : Collections.emptyList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error getting products by artisan: " + e.getMessage(), e);
        }
    }

    /**
     * Get detailed product information
     */
    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductDetailDTO> getProductDetails(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @PathVariable String productId) {
        
        try {
            ProductDetail product = customerPortalService.getProductDetails(tenantId, productId);
            
            if (product == null) {
                return ResponseEntity.notFound().build();
            }
            
            ProductDetailDTO dto = dtoMapperService.toProductDetailDto(product);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error getting product details: " + e.getMessage(), e);
        }
    }

    /**
     * Get artisan profile information
     */
    @GetMapping("/artisans/{artisanId}")
    public ResponseEntity<ArtisanProfileDTO> getArtisanProfile(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @PathVariable String artisanId) {
        
        try {
            ArtisanProfile artisan = customerPortalService.getArtisanProfile(tenantId, artisanId);
            
            if (artisan == null) {
                return ResponseEntity.notFound().build();
            }
            
            ArtisanProfileDTO dto = dtoMapperService.toArtisanProfileDto(artisan);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error getting artisan profile: " + e.getMessage(), e);
        }
    }

    /**
     * Get customer order history
     */
    @GetMapping("/orders")
    public ResponseEntity<List<CustomerOrderDTO>> getCustomerOrders(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @RequestParam String customerId,
            @RequestParam(required = false, defaultValue = "20") int limit,
            @RequestParam(required = false, defaultValue = "0") int offset) {
        
        try {
            List<CustomerOrder> orders = customerPortalService.getCustomerOrders(
                    tenantId, customerId, limit, offset);
            
            List<CustomerOrderDTO> dtos = dtoMapperService.toDtoList(
                    orders, dtoMapperService::toCustomerOrderDto);
            
            return ResponseEntity.ok(dtos != null ? dtos : Collections.emptyList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error getting customer orders: " + e.getMessage(), e);
        }
    }

    /**
     * Get featured products
     */
    @GetMapping("/products/featured")
    public ResponseEntity<List<ProductViewDTO>> getFeaturedProducts(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @RequestParam(required = false, defaultValue = "10") int limit) {
        
        try {
            List<ProductView> products = customerPortalService.getFeaturedProducts(tenantId, limit);
            
            List<ProductViewDTO> dtos = dtoMapperService.toDtoList(
                    products, dtoMapperService::toProductViewDto);
            
            return ResponseEntity.ok(dtos != null ? dtos : Collections.emptyList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error getting featured products: " + e.getMessage(), e);
        }
    }

    /**
     * Get categories
     */
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDTO>> getCategories(
            @RequestHeader("X-Tenant-ID") String tenantId) {
        
        try {
            List<Category> categories = customerPortalService.getCategories(tenantId);
            
            List<CategoryDTO> dtos = dtoMapperService.toDtoList(
                    categories, dtoMapperService::toCategoryDto);
            
            return ResponseEntity.ok(dtos != null ? dtos : Collections.emptyList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error getting categories: " + e.getMessage(), e);
        }
    }

    /**
     * Get related products
     */
    @GetMapping("/products/{productId}/related")
    public ResponseEntity<List<ProductViewDTO>> getRelatedProducts(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @PathVariable String productId,
            @RequestParam(required = false, defaultValue = "5") int limit) {
        
        try {
            List<ProductView> products = customerPortalService.getRelatedProducts(
                    tenantId, productId, limit);
            
            List<ProductViewDTO> dtos = dtoMapperService.toDtoList(
                    products, dtoMapperService::toProductViewDto);
            
            return ResponseEntity.ok(dtos != null ? dtos : Collections.emptyList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error getting related products: " + e.getMessage(), e);
        }
    }
} 