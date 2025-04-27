package com.handmade.ecommerce.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a user is not authorized to perform an action.
 * Error messages are handled by Chenile's internationalization system.
 */
public class UnauthorizedException extends BaseException {

    private static final int ERROR_CODE = 401;
    private static final HttpStatus HTTP_STATUS = HttpStatus.UNAUTHORIZED;

    /**
     * Constructor with default error code
     */
    public UnauthorizedException() {
        super(ERROR_CODE, HTTP_STATUS);
    }

    /**
     * Constructor with cause
     *
     * @param cause the cause of the exception
     */
    public UnauthorizedException(Throwable cause) {
        super(ERROR_CODE, HTTP_STATUS, cause);
    }

    /**
     * Constructor with user and resource details
     *
     * @param userId       the ID of the unauthorized user
     * @param resourceType the type of resource being accessed
     * @param resourceId   the ID of the resource being accessed
     */
    public UnauthorizedException(String userId, String resourceType, String resourceId) {
        super(ERROR_CODE, HTTP_STATUS, userId, resourceType, resourceId);
    }
} 