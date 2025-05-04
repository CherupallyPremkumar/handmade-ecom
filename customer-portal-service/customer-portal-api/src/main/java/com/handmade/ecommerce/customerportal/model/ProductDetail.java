package com.handmade.ecommerce.customerportal.model;

import java.util.List;
import java.util.Map;

/**
 * Model class for Product Detail in the customer portal.
 * Contains detailed product information including reviews, images, categories, and attributes.
 */
public class ProductDetail extends ProductView {
    private List<Map<String, Object>> reviews;
    private List<Map<String, Object>> images;
    private List<Map<String, Object>> categories;
    private List<Map<String, Object>> attributes;
    
    // Default constructor
    public ProductDetail() {
        super();
    }
    
    // Getters and setters
    public List<Map<String, Object>> getReviews() {
        return reviews;
    }

    public void setReviews(List<Map<String, Object>> reviews) {
        this.reviews = reviews;
    }

    public List<Map<String, Object>> getImages() {
        return images;
    }

    public void setImages(List<Map<String, Object>> images) {
        this.images = images;
    }

    public List<Map<String, Object>> getCategories() {
        return categories;
    }

    public void setCategories(List<Map<String, Object>> categories) {
        this.categories = categories;
    }

    public List<Map<String, Object>> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Map<String, Object>> attributes) {
        this.attributes = attributes;
    }
} 