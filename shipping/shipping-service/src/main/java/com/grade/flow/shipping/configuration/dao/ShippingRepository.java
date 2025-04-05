package com.handmade.ecommerce.shipping.configuration.dao;

import com.handmade.ecommerce.shipping.model.Shipping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository  public interface ShippingRepository extends JpaRepository<Shipping,String> {}
