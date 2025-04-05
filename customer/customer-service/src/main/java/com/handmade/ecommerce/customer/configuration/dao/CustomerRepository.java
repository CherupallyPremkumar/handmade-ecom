package com.handmade.ecommerce.customer.configuration.dao;

import com.handmade.ecommerce.customer.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository  public interface CustomerRepository extends JpaRepository<Customer,String> {}
