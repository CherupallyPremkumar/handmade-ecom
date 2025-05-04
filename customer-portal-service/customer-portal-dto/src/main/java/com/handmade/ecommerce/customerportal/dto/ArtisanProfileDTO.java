package com.handmade.ecommerce.customerportal.dto;

import java.io.Serializable;

/**
 * Data Transfer Object for Artisan Profile in the customer portal.
 */
public class ArtisanProfileDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String name;
    private String bio;
    private String profileImageUrl;
    private Float averageRating;
    private Integer completedOrderCount;
    private String specialties;
    
    // Default constructor
    public ArtisanProfileDTO() {
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

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public Float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Float averageRating) {
        this.averageRating = averageRating;
    }

    public Integer getCompletedOrderCount() {
        return completedOrderCount;
    }

    public void setCompletedOrderCount(Integer completedOrderCount) {
        this.completedOrderCount = completedOrderCount;
    }

    public String getSpecialties() {
        return specialties;
    }

    public void setSpecialties(String specialties) {
        this.specialties = specialties;
    }
} 