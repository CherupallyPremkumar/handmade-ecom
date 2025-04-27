package com.handmade.ecommerce.artisan.configuration.dao;

import com.handmade.ecommerce.artisan.model.ArtisanReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArtisanReviewRepository extends JpaRepository<ArtisanReview, String> {
    
    /**
     * Find all reviews for a specific artisan
     * @param artisanId the ID of the artisan
     * @return list of reviews
     */
    @Query("SELECT r FROM ArtisanReview r WHERE r.artisan.id = :artisanId")
    List<ArtisanReview> findByArtisanId(@Param("artisanId") String artisanId);
    
    /**
     * Find all reviews for a specific artisan with pagination
     * @param artisanId the ID of the artisan
     * @param pageable pagination information
     * @return page of reviews
     */
    @Query("SELECT r FROM ArtisanReview r WHERE r.artisan.id = :artisanId AND r.isPublic = true AND r.status = 'APPROVED'")
    Page<ArtisanReview> findByArtisanIdAndPublicAndApproved(@Param("artisanId") String artisanId, Pageable pageable);
    
    /**
     * Find all reviews by a specific reviewer
     * @param reviewerId the ID of the reviewer
     * @return list of reviews
     */
    @Query("SELECT r FROM ArtisanReview r WHERE r.reviewerId = :reviewerId")
    List<ArtisanReview> findByReviewerId(@Param("reviewerId") String reviewerId);
    
    /**
     * Find a review by artisan ID and order ID
     * @param artisanId the ID of the artisan
     * @param orderId the ID of the order
     * @return optional containing the review if found
     */
    @Query("SELECT r FROM ArtisanReview r WHERE r.artisan.id = :artisanId AND r.orderId = :orderId")
    Optional<ArtisanReview> findByArtisanIdAndOrderId(@Param("artisanId") String artisanId, @Param("orderId") String orderId);
    
    /**
     * Find all reviews with a specific status
     * @param status the status to search for
     * @return list of reviews
     */
    @Query("SELECT r FROM ArtisanReview r WHERE r.status = :status")
    List<ArtisanReview> findByStatus(@Param("status") String status);
    
    /**
     * Calculate the average rating for an artisan
     * @param artisanId the ID of the artisan
     * @return the average rating
     */
    @Query("SELECT AVG(r.rating) FROM ArtisanReview r WHERE r.artisan.id = :artisanId AND r.status = 'APPROVED'")
    Double calculateAverageRating(@Param("artisanId") String artisanId);
    
    /**
     * Count the number of reviews for an artisan
     * @param artisanId the ID of the artisan
     * @return the number of reviews
     */
    @Query("SELECT COUNT(r) FROM ArtisanReview r WHERE r.artisan.id = :artisanId AND r.status = 'APPROVED'")
    Long countReviewsByArtisanId(@Param("artisanId") String artisanId);
}
