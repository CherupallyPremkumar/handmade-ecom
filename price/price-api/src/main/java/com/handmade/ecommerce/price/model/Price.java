package com.handmade.ecommerce.price.model;

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

public class Price extends BaseJpaEntity {

    @Column(name = "product_id")
    private String productId;
    @Column(name = "location_code")
    private String locationCode;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    private BigDecimal amount;

    // Default constructor
    public Price() {
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



    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
