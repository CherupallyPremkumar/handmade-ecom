package com.handmade.ecommerce.payment.service.store;

import org.chenile.utils.entity.service.EntityStore;
import com.handmade.ecommerce.payment.model.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.chenile.base.exception.NotFoundException;
import com.handmade.ecommerce.payment.configuration.dao.PaymentRepository;
import java.util.Optional;

public class PaymentEntityStore implements EntityStore<Payment>{
    @Autowired private PaymentRepository paymentRepository;

	@Override
	public void store(Payment entity) {
        paymentRepository.save(entity);
	}

	@Override
	public Payment retrieve(String id) {
        Optional<Payment> entity = paymentRepository.findById(id);
        if (entity.isPresent()) return entity.get();
        throw new NotFoundException(1500,"Unable to find Payment with ID " + id);
	}

}
