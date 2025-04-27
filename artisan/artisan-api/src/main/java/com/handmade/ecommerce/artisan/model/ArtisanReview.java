package com.handmade.ecommerce.artisan.model;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.BaseJpaEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "hm_artisan_review")
public class ArtisanReview extends BaseJpaEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artisan_id", nullable = false)
    private Artisan artisan;
    
    @Column(name = "reviewer_id", nullable = false)
    private String reviewerId;
    
    @Column(name = "reviewer_name", nullable = false)
    private String reviewerName;
    
    @Column(name = "rating", nullable = false)
    private Integer rating;
    
    @Column(name = "comment", length = 1000)
    private String comment;
    
    @Column(name = "review_date", nullable = false)
    private LocalDateTime reviewDate;
    
    @Column(name = "order_id")
    private String orderId;
    
    @Column(name = "is_verified_purchase")
    private boolean isVerifiedPurchase;
    
    @Column(name = "is_public")
    private boolean isPublic = true;
    
    @Column(name = "artisan_response")
    private String artisanResponse;
    
    @Column(name = "response_date")
    private LocalDateTime responseDate;
    
    @Column(name = "helpful_votes")
    private Integer helpfulVotes = 0;
    
    @Column(name = "unhelpful_votes")
    private Integer unhelpfulVotes = 0;
    
    @Column(name = "status", nullable = false)
    private String status = "PENDING"; // PENDING, APPROVED, REJECTED
    
    public Artisan getArtisan() {
        return artisan;
    }
    
    public void setArtisan(Artisan artisan) {
        this.artisan = artisan;
    }
    
    public String getReviewerId() {
        return reviewerId;
    }
    
    public void setReviewerId(String reviewerId) {
        this.reviewerId = reviewerId;
    }
    
    public String getReviewerName() {
        return reviewerName;
    }
    
    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }
    
    public Integer getRating() {
        return rating;
    }
    
    public void setRating(Integer rating) {
        this.rating = rating;
    }
    
    public String getComment() {
        return comment;
    }
    
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public LocalDateTime getReviewDate() {
        return reviewDate;
    }
    
    public void setReviewDate(LocalDateTime reviewDate) {
        this.reviewDate = reviewDate;
    }
    
    public String getOrderId() {
        return orderId;
    }
    
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    
    public boolean isVerifiedPurchase() {
        return isVerifiedPurchase;
    }
    
    public void setVerifiedPurchase(boolean verifiedPurchase) {
        isVerifiedPurchase = verifiedPurchase;
    }
    
    public boolean isPublic() {
        return isPublic;
    }
    
    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }
    
    public String getArtisanResponse() {
        return artisanResponse;
    }
    
    public void setArtisanResponse(String artisanResponse) {
        this.artisanResponse = artisanResponse;
    }
    
    public LocalDateTime getResponseDate() {
        return responseDate;
    }
    
    public void setResponseDate(LocalDateTime responseDate) {
        this.responseDate = responseDate;
    }
    
    public Integer getHelpfulVotes() {
        return helpfulVotes;
    }
    
    public void setHelpfulVotes(Integer helpfulVotes) {
        this.helpfulVotes = helpfulVotes;
    }
    
    public Integer getUnhelpfulVotes() {
        return unhelpfulVotes;
    }
    
    public void setUnhelpfulVotes(Integer unhelpfulVotes) {
        this.unhelpfulVotes = unhelpfulVotes;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    /**
     * Increment the helpful votes count
     */
    public void incrementHelpfulVotes() {
        this.helpfulVotes = this.helpfulVotes + 1;
    }
    
    /**
     * Increment the unhelpful votes count
     */
    public void incrementUnhelpfulVotes() {
        this.unhelpfulVotes = this.unhelpfulVotes + 1;
    }
    
    /**
     * Add an artisan response to the review
     * @param response the response text
     */
    public void addArtisanResponse(String response) {
        this.artisanResponse = response;
        this.responseDate = LocalDateTime.now();
    }
} 