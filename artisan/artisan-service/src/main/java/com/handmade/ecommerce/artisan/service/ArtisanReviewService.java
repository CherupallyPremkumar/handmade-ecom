package com.handmade.ecommerce.artisan.service;

import com.handmade.ecommerce.artisan.model.ArtisanReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing artisan reviews.
 * This service handles review creation, updates, responses, and analytics.
 */
public interface ArtisanReviewService {

    /**
     * Create a new review for an artisan
     * @param artisanId the ID of the artisan
     * @param review the review to create
     * @return the created review
     */
    ArtisanReview createReview(String artisanId, ArtisanReview review);

    /**
     * Update an existing review
     * @param reviewId the ID of the review to update
     * @param review the updated review
     * @return the updated review
     */
    ArtisanReview updateReview(String reviewId, ArtisanReview review);

    /**
     * Delete a review
     * @param reviewId the ID of the review to delete
     */
    void deleteReview(String reviewId);

    /**
     * Get all reviews for an artisan with pagination
     * @param artisanId the ID of the artisan
     * @param pageable pagination information
     * @return page of reviews
     */
    Page<ArtisanReview> getReviewsByArtisan(String artisanId, Pageable pageable);

    /**
     * Get all reviews by a specific reviewer
     * @param reviewerId the ID of the reviewer
     * @return list of reviews
     */
    List<ArtisanReview> getReviewsByReviewer(String reviewerId);

    /**
     * Get a review by artisan ID and order ID
     * @param artisanId the ID of the artisan
     * @param orderId the ID of the order
     * @return optional containing the review if found
     */
    Optional<ArtisanReview> getReviewByOrder(String artisanId, String orderId);

    /**
     * Add an artisan response to a review
     * @param reviewId the ID of the review
     * @param response the response text
     * @return the updated review
     */
    ArtisanReview respondToReview(String reviewId, String response);

    /**
     * Mark a review as helpful
     * @param reviewId the ID of the review
     */
    void markReviewAsHelpful(String reviewId);

    /**
     * Mark a review as unhelpful
     * @param reviewId the ID of the review
     */
    void markReviewAsUnhelpful(String reviewId);

    /**
     * Update the status of a review
     * @param reviewId the ID of the review
     * @param status the new status
     */
    void updateReviewStatus(String reviewId, String status);

    /**
     * Get all pending reviews
     * @return list of pending reviews
     */
    List<ArtisanReview> getPendingReviews();

    /**
     * Calculate the average rating for an artisan
     * @param artisanId the ID of the artisan
     * @return the average rating
     */
    Double getAverageRating(String artisanId);

    /**
     * Get the total number of reviews for an artisan
     * @param artisanId the ID of the artisan
     * @return the number of reviews
     */
    Long getReviewCount(String artisanId);
} 