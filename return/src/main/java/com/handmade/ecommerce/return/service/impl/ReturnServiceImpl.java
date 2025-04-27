package com.handmade.ecommerce.return.service.impl;

import com.handmade.ecommerce.return.model.Return;
import com.handmade.ecommerce.return.model.ReturnStatus;
import com.handmade.ecommerce.return.repository.ReturnRepository;
import com.handmade.ecommerce.return.service.ReturnService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReturnServiceImpl implements ReturnService {

    private final ReturnRepository returnRepository;

    @Override
    public Return createReturn(Return returnRequest) {
        return returnRepository.save(returnRequest);
    }

    @Override
    public Optional<Return> getReturnById(String id) {
        return returnRepository.findById(id);
    }

    @Override
    public List<Return> getReturnsByCustomerId(String customerId) {
        return returnRepository.findByCustomerId(customerId);
    }

    @Override
    public List<Return> getReturnsByOrderId(String orderId) {
        return returnRepository.findByOrderId(orderId);
    }

    @Override
    public List<Return> getReturnsByStatus(ReturnStatus status) {
        return returnRepository.findByStatus(status.name());
    }

    @Override
    @Transactional
    public Return updateReturnStatus(String id, ReturnStatus status) {
        Return returnEntity = returnRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Return not found with id: " + id));
        returnEntity.setStatus(status);
        return returnRepository.save(returnEntity);
    }

    @Override
    @Transactional
    public Return updateReturnTrackingNumber(String id, String trackingNumber) {
        Return returnEntity = returnRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Return not found with id: " + id));
        returnEntity.setReturnTrackingNumber(trackingNumber);
        return returnRepository.save(returnEntity);
    }

    @Override
    @Transactional
    public void deleteReturn(String id) {
        returnRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Return processRefund(String id, Double refundAmount) {
        Return returnEntity = returnRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Return not found with id: " + id));
        returnEntity.setRefundAmount(refundAmount);
        returnEntity.setStatus(ReturnStatus.REFUNDED);
        return returnRepository.save(returnEntity);
    }
} 