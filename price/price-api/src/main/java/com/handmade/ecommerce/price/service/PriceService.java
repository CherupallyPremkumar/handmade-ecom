package com.handmade.ecommerce.price.service;

import com.handmade.ecommerce.price.model.Price;

public interface PriceService {
	// Define your interface here
    public Price save(Price price);
    public Price retrieve(String id);
}
