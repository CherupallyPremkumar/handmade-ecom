package com.handmade.ecommerce.artisan.model;

import com.handmade.ecommerce.core.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.chenile.jpautils.entity.BaseJpaEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "hm_artisan_portfolio")

public class ArtisanPortfolio extends BaseJpaEntity {


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artisan_id", nullable = false)
    private Artisan artisan;
    
    @Column(name = "title", nullable = false, length = 255)
    private String title;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "category", nullable = false)
    private String category;
    
    @Column(name = "subcategory")
    private String subcategory;
    
    @ElementCollection
    @CollectionTable(
        name = "artisan_portfolio_images",
        joinColumns = @JoinColumn(name = "portfolio_id")
    )
    @Column(name = "image_url")
    @OrderColumn(name = "image_order")
    private Set<String> imageUrls = new HashSet<>();
    
    @ElementCollection
    @CollectionTable(
        name = "artisan_portfolio_tags",
        joinColumns = @JoinColumn(name = "portfolio_id")
    )
    @Column(name = "tag")
    private Set<String> tags = new HashSet<>();
    
    @Column(name = "price_range_start")
    private BigDecimal priceRangeStart;
    
    @Column(name = "price_range_end")
    private BigDecimal priceRangeEnd;
    
    @Column(name = "estimated_duration_hours")
    private Integer estimatedDurationHours;
    
    @Column(name = "materials_used", columnDefinition = "TEXT")
    private String materialsUsed;
    
    @Column(name = "completion_date")
    private java.time.LocalDate completionDate;
    
    @Column(name = "client_name")
    private String clientName;
    
    @Column(name = "client_testimonial", columnDefinition = "TEXT")
    private String clientTestimonial;
    
    @Column(name = "is_featured", nullable = false)
    private boolean isFeatured = false;
    
    @Column(name = "view_count", nullable = false)
    private Long viewCount = 0L;
    
    @Column(name = "like_count", nullable = false)
    private Long likeCount = 0L;

    
    public void incrementViewCount() {
        this.viewCount++;
    }
    
    public void incrementLikeCount() {
        this.likeCount++;
    }
    
    public void decrementLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }
} 