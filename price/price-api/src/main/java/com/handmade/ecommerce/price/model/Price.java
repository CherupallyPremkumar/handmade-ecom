package com.handmade.ecommerce.price.model;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.BaseJpaEntity;


@Entity
@Table(name = "hm_price")
@AttributeOverrides({
        @AttributeOverride(name = "createdTime", column = @Column(name = "created_time")),
        @AttributeOverride(name = "createdBy", column = @Column(name = "created_by")),
        @AttributeOverride(name = "lastModifiedTime", column = @Column(name = "last_modified_time")),
        @AttributeOverride(name = "lastModifiedBy", column = @Column(name = "last_modified_by")),
})
public class Price extends BaseJpaEntity  {

    @Column(name = "product_id")
    private String productId;
    @Column(name = "location_code")// USA , IN,
    private String locationCode;
    @Column(name = "currency_code")//DLR,INR
    private String currencyCode;
    private String price;
}
