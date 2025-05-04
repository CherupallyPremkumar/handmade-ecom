package com.handmade.ecommerce.customerportal.service;

import com.handmade.ecommerce.customerportal.dto.*;
import com.handmade.ecommerce.customerportal.model.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service to handle mapping between DTO and model objects.
 */
@Service
public class DtoMapperService {

    /**
     * Convert ProductView model to ProductViewDTO
     */
    public ProductViewDTO toProductViewDto(ProductView model) {
        if (model == null) {
            return null;
        }
        
        ProductViewDTO dto = new ProductViewDTO();
        dto.setId(model.getId());
        dto.setName(model.getName());
        dto.setDescription(model.getDescription());
        dto.setPrice(model.getPrice());
        dto.setCurrencyCode(model.getCurrencyCode());
        dto.setAvailableQuantity(model.getAvailableQuantity());
        dto.setStatus(model.getStatus());
        dto.setCreatedTime(model.getCreatedTime());
        dto.setAverageRating(model.getAverageRating());
        dto.setReviewCount(model.getReviewCount());
        dto.setArtisanId(model.getArtisanId());
        dto.setArtisanName(model.getArtisanName());
        dto.setArtisanProfileImage(model.getArtisanProfileImage());
        dto.setImageUrls(model.getImageUrls());
        
        return dto;
    }
    
    /**
     * Convert ProductDetail model to ProductDetailDTO
     */
    public ProductDetailDTO toProductDetailDto(ProductDetail model) {
        if (model == null) {
            return null;
        }
        
        ProductDetailDTO dto = new ProductDetailDTO();
        // Set base fields from ProductView
        dto.setId(model.getId());
        dto.setName(model.getName());
        dto.setDescription(model.getDescription());
        dto.setPrice(model.getPrice());
        dto.setCurrencyCode(model.getCurrencyCode());
        dto.setAvailableQuantity(model.getAvailableQuantity());
        dto.setStatus(model.getStatus());
        dto.setCreatedTime(model.getCreatedTime());
        dto.setAverageRating(model.getAverageRating());
        dto.setReviewCount(model.getReviewCount());
        dto.setArtisanId(model.getArtisanId());
        dto.setArtisanName(model.getArtisanName());
        dto.setArtisanProfileImage(model.getArtisanProfileImage());
        dto.setImageUrls(model.getImageUrls());
        
        // Set ProductDetail specific fields
        dto.setReviews(model.getReviews());
        dto.setImages(model.getImages());
        dto.setCategories(model.getCategories());
        dto.setAttributes(model.getAttributes());
        
        return dto;
    }
    
    /**
     * Convert ArtisanProfile model to ArtisanProfileDTO
     */
    public ArtisanProfileDTO toArtisanProfileDto(ArtisanProfile model) {
        if (model == null) {
            return null;
        }
        
        ArtisanProfileDTO dto = new ArtisanProfileDTO();
        dto.setId(model.getId());
        dto.setName(model.getName());
        dto.setBio(model.getBio());
        dto.setProfileImageUrl(model.getProfileImageUrl());
        dto.setAverageRating(model.getAverageRating());
        dto.setCompletedOrderCount(model.getCompletedOrderCount());
        dto.setSpecialties(model.getSpecialties());
        
        return dto;
    }
    
    /**
     * Convert CustomerOrder model to CustomerOrderDTO
     */
    public CustomerOrderDTO toCustomerOrderDto(CustomerOrder model) {
        if (model == null) {
            return null;
        }
        
        CustomerOrderDTO dto = new CustomerOrderDTO();
        dto.setId(model.getId());
        dto.setOrderNumber(model.getOrderNumber());
        dto.setStatus(model.getStatus());
        dto.setOrderDate(model.getOrderDate());
        dto.setEstimatedDeliveryDate(model.getEstimatedDeliveryDate());
        dto.setTotalAmount(model.getTotalAmount());
        dto.setCurrencyCode(model.getCurrencyCode());
        dto.setItemCount(model.getItemCount());
        dto.setTrackingNumber(model.getTrackingNumber());
        dto.setShippingStatus(model.getShippingStatus());
        dto.setPaymentStatus(model.getPaymentStatus());
        
        return dto;
    }
    
    /**
     * Convert Category model to CategoryDTO
     */
    public CategoryDTO toCategoryDto(Category model) {
        if (model == null) {
            return null;
        }
        
        CategoryDTO dto = new CategoryDTO();
        dto.setId(model.getId());
        dto.setName(model.getName());
        dto.setDescription(model.getDescription());
        dto.setImageUrl(model.getImageUrl());
        dto.setProductCount(model.getProductCount());
        
        return dto;
    }
    
    /**
     * Convert a list of models to a list of DTOs
     */
    public <T, U> List<U> toDtoList(List<T> models, java.util.function.Function<T, U> mapper) {
        if (models == null) {
            return null;
        }
        
        return models.stream()
                .map(mapper)
                .collect(Collectors.toList());
    }
    
    /**
     * Convert ProductViewDTO to ProductView model
     */
    public ProductView toProductView(ProductViewDTO dto) {
        if (dto == null) {
            return null;
        }
        
        ProductView model = new ProductView();
        model.setId(dto.getId());
        model.setName(dto.getName());
        model.setDescription(dto.getDescription());
        model.setPrice(dto.getPrice());
        model.setCurrencyCode(dto.getCurrencyCode());
        model.setAvailableQuantity(dto.getAvailableQuantity());
        model.setStatus(dto.getStatus());
        model.setCreatedTime(dto.getCreatedTime());
        model.setAverageRating(dto.getAverageRating());
        model.setReviewCount(dto.getReviewCount());
        model.setArtisanId(dto.getArtisanId());
        model.setArtisanName(dto.getArtisanName());
        model.setArtisanProfileImage(dto.getArtisanProfileImage());
        model.setImageUrls(dto.getImageUrls());
        
        return model;
    }
} 