package com.handmade.ecommerce.artisan.service.impl;

import com.handmade.ecommerce.artisan.configuration.dao.ArtisanRepository;
import com.handmade.ecommerce.artisan.configuration.dao.ArtisanReviewRepository;
import com.handmade.ecommerce.artisan.model.Artisan;
import com.handmade.ecommerce.artisan.model.ArtisanReview;
import com.handmade.ecommerce.artisan.model.service.ArtisanReviewService;

import com.handmade.ecommerce.common.exception.ResourceNotFoundException;
import com.handmade.ecommerce.common.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArtisanReviewServiceImpl implements ArtisanReviewService {

    private final ArtisanReviewRepository reviewRepository;
    private final ArtisanRepository artisanRepository;

    @Override
    @Transactional
    public ArtisanReview createReview(String artisanId, ArtisanReview review) {
        log.info("Creating review for artisan: {}", artisanId);
        
        Artisan artisan = artisanRepository.findById(artisanId)
            .orElseThrow(() -> new ResourceNotFoundException("Artisan not found with id: " , artisanId));
        
        validateReview(review);
        
        // Check if reviewer has already reviewed this artisan
        Optional<ArtisanReview> existingReview = reviewRepository.findByArtisanIdAndReviewerId(
            artisanId, review.getReviewerId());
        
        if (existingReview.isPresent()) {
            throw new ValidationException("Reviewer has already submitted a review for this artisan");
        }
        
        review.setArtisan(artisan);
        review.setReviewDate(LocalDateTime.now());
        review.setStatus("PENDING");
        review.setHelpfulVotes(0);
        review.setUnhelpfulVotes(0);
        
        return reviewRepository.save(review);
    }

    @Override
    @Transactional
    public ArtisanReview updateReview(String reviewId, ArtisanReview review) {
        log.info("Updating review with id: {}", reviewId);
        
        ArtisanReview existingReview = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " , reviewId));
        
        validateReview(review);
        
        // Only allow updating certain fields
        existingReview.setRating(review.getRating());
        existingReview.setComment(review.getComment());
        existingReview.setPublic(review.isPublic());
        
        return reviewRepository.save(existingReview);
    }

    @Override
    @Transactional
    public void deleteReview(String reviewId) {
        log.info("Deleting review with id: {}", reviewId);
        
        if (!reviewRepository.existsById(reviewId)) {
            throw new ResourceNotFoundException("Review not found with id: " , reviewId);
        }
        
        reviewRepository.deleteById(reviewId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ArtisanReview> getReviewsByArtisan(String artisanId, Pageable pageable) {
        log.info("Getting reviews for artisan: {}", artisanId);
        
        if (!artisanRepository.existsById(artisanId)) {
            throw new ResourceNotFoundException("Artisan not found with id: " , artisanId);
        }
        
        return reviewRepository.findByArtisanIdAndPublicAndApproved(artisanId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArtisanReview> getReviewsByReviewer(String reviewerId) {
        log.info("Getting reviews by reviewer: {}", reviewerId);
        return reviewRepository.findByReviewerId(reviewerId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ArtisanReview> getReviewByOrder(String artisanId, String orderId) {
        log.info("Getting review for artisan: {} and order: {}", artisanId, orderId);
        return reviewRepository.findByArtisanIdAndOrderId(artisanId, orderId);
    }

    @Override
    @Transactional
    public ArtisanReview respondToReview(String reviewId, String response) {
        log.info("Adding response to review: {}", reviewId);
        
        ArtisanReview review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " , reviewId));
        
        review.addArtisanResponse(response);
        return reviewRepository.save(review);
    }

    @Override
    @Transactional
    public void markReviewAsHelpful(String reviewId) {
        log.info("Marking review as helpful: {}", reviewId);
        
        ArtisanReview review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " , reviewId));
        
        review.incrementHelpfulVotes();
        reviewRepository.save(review);
    }

    @Override
    @Transactional
    public void markReviewAsUnhelpful(String reviewId) {
        log.info("Marking review as unhelpful: {}", reviewId);
        
        ArtisanReview review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " , reviewId));
        
        review.incrementUnhelpfulVotes();
        reviewRepository.save(review);
    }

    @Override
    @Transactional
    public void updateReviewStatus(String reviewId, String status) {
        log.info("Updating review status: {} to: {}", reviewId, status);
        
        ArtisanReview review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " ,reviewId));
        
        if (!isValidStatus(status)) {
            throw new ValidationException("Invalid status: " + status);
        }
        
        review.setStatus(status);
        reviewRepository.save(review);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArtisanReview> getPendingReviews() {
        log.info("Getting all pending reviews");
        return reviewRepository.findByStatus("PENDING");
    }

    @Override
    @Transactional(readOnly = true)
    public Double getAverageRating(String artisanId) {
        log.info("Calculating average rating for artisan: {}", artisanId);
        
        if (!artisanRepository.existsById(artisanId)) {
            throw new ResourceNotFoundException("Artisan not found with id: " , artisanId);
        }
        
        return reviewRepository.calculateAverageRating(artisanId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getReviewCount(String artisanId) {
        log.info("Getting review count for artisan: {}", artisanId);
        
        if (!artisanRepository.existsById(artisanId)) {
            throw new ResourceNotFoundException("Artisan not found with id: " , artisanId);
        }
        
        return reviewRepository.countReviewsByArtisanId(artisanId);
    }

    private void validateReview(ArtisanReview review) {
        if (review.getReviewerId() == null || review.getReviewerId().trim().isEmpty()) {
            throw new ValidationException("Reviewer ID is required");
        }
        
        if (review.getReviewerName() == null || review.getReviewerName().trim().isEmpty()) {
            throw new ValidationException("Reviewer name is required");
        }
        
        if (review.getRating() == null) {
            throw new ValidationException("Rating is required");
        }
        
        if (review.getRating() < 1 || review.getRating() > 5) {
            throw new ValidationException("Rating must be between 1 and 5");
        }
        
        if (review.getComment() != null && review.getComment().length() > 1000) {
            throw new ValidationException("Comment must not exceed 1000 characters");
        }
    }

    private boolean isValidStatus(String status) {
        return "PENDING".equals(status) || 
               "APPROVED".equals(status) || 
               "REJECTED".equals(status);
    }
} 