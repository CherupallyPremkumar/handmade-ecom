package com.handmade.ecommerce.order.configuration.dao;

import com.handmade.ecommerce.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository  public interface OrderRepository extends JpaRepository<Order,String> {}
