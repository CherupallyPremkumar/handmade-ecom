package com.handmade.ecommerce.cart.service.exception;

import org.chenile.base.exception.ErrorNumException;

/**
 * Custom exception for cart service errors with internationalization support
 */
public class CartException extends ErrorNumException {


    public CartException(int errorNum, int subErrorNum, Object[] params) {
        super(errorNum, subErrorNum, params);
    }
}