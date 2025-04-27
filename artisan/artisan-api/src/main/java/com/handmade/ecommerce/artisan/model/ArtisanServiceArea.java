package com.handmade.ecommerce.artisan.model;


import jakarta.persistence.*;
import org.chenile.jpautils.entity.BaseJpaEntity;


@Entity
@Table(name = "hm_artisan_service_area")
public class ArtisanServiceArea extends BaseJpaEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artisan_id", nullable = false)
    private Artisan artisan;
    
    @Column(name = "country", nullable = false)
    private String country;
    
    @Column(name = "state")
    private String state;
    
    @Column(name = "city")
    private String city;
    
    @Column(name = "postal_code")
    private String postalCode;
    
    @Column(name = "service_radius")
    private Integer serviceRadius;
    
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;
} 