package com.handmade.ecommerce.customer.service.store;

import org.chenile.utils.entity.service.EntityStore;
import com.handmade.ecommerce.customer.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.chenile.base.exception.NotFoundException;
import com.handmade.ecommerce.customer.configuration.dao.CustomerRepository;
import java.util.Optional;

public class CustomerEntityStore implements EntityStore<Customer>{
    @Autowired private CustomerRepository customerRepository;

	@Override
	public void store(Customer entity) {
        customerRepository.save(entity);
	}

	@Override
	public Customer retrieve(String id) {
        Optional<Customer> entity = customerRepository.findById(id);
        if (entity.isPresent()) return entity.get();
        throw new NotFoundException(1500,"Unable to find Customer with ID " + id);
	}

}
