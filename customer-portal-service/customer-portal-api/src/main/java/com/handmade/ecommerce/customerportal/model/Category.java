package com.handmade.ecommerce.customerportal.model;

/**
 * Model class for Category in the customer portal.
 * Contains category information and product count.
 */
public class Category {
    private String id;
    private String name;
    private String description;
    private String imageUrl;
    private Integer productCount;
    
    // Default constructor
    public Category() {
    }
    
    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }
} 