package com.handmade.ecommerce.product.configuration.dao;

import com.handmade.ecommerce.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository  public interface ProductRepository extends JpaRepository<Product,String> {}
