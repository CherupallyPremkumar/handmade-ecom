package com.handmade.ecommerce.product.model;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.BaseJpaEntity;

import java.math.BigDecimal;

@Entity
@Table(name = "hm_product_price")
@AttributeOverrides({
    @AttributeOverride(name = "createdTime", column = @Column(name = "created_time")),
    @AttributeOverride(name = "createdBy", column = @Column(name = "created_by")),
    @AttributeOverride(name = "lastModifiedTime", column = @Column(name = "last_modified_time")),
    @AttributeOverride(name = "lastModifiedBy", column = @Column(name = "last_modified_by")),
})
public class ProductPrice extends BaseJpaEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
    
    @Column(name = "location_code")
    private String locationCode;
    
    @Column(name = "currency_code")
    private String currencyCode;
    
    private BigDecimal price;
    
    // Default constructor
    public ProductPrice() {
    }
    
    // Parameterized constructor
    public ProductPrice(Product product, String locationCode, String currencyCode, BigDecimal price) {
        this.product = product;
        this.locationCode = locationCode;
        this.currencyCode = currencyCode;
        this.price = price;
    }
    
    public Product getProduct() {
        return product;
    }
    
    public void setProduct(Product product) {
        this.product = product;
    }
    
    public String getLocationCode() {
        return locationCode;
    }
    
    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }
    
    public String getCurrencyCode() {
        return currencyCode;
    }
    
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
} 