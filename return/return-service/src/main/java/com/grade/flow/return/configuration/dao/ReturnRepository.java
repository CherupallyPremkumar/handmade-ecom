package com.handmade.ecommerce.return.configuration.dao;

import com.handmade.ecommerce.return.model.Return;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository  public interface ReturnRepository extends JpaRepository<Return,String> {}
