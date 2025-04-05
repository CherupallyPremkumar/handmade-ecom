package com.handmade.ecommerce.cart.configuration.dao;

import com.handmade.ecommerce.cart.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository  public interface CartRepository extends JpaRepository<Cart,String> {}
