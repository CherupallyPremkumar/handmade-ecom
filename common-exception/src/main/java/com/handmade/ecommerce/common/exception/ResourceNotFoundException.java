package com.handmade.ecommerce.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a requested resource is not found.
 * Error messages are handled by Chenile's internationalization system.
 */
public class ResourceNotFoundException extends BaseException {

    private static final int ERROR_CODE = 404;
    private static final HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND;

    /**
     * Constructor with default error code
     */
    public ResourceNotFoundException() {
        super(ERROR_CODE, HTTP_STATUS);
    }

    /**
     * Constructor with cause
     *
     * @param cause the cause of the exception
     */
    public ResourceNotFoundException(Throwable cause) {
        super(ERROR_CODE, HTTP_STATUS, cause);
    }

    /**
     * Constructor with resource details
     *
     * @param resourceType the type of resource that was not found
     * @param resourceId   the ID of the resource that was not found
     */
    public ResourceNotFoundException(String resourceType, String resourceId) {
        super(ERROR_CODE, HTTP_STATUS, resourceType, resourceId);
    }

    /**
     * Constructor with resource details and additional context
     *
     * @param resourceType the type of resource that was not found
     * @param resourceId   the ID of the resource that was not found
     * @param context      additional context about the resource (e.g., parent resource)
     */
    public ResourceNotFoundException(String resourceType, String resourceId, String context) {
        super(ERROR_CODE, HTTP_STATUS, resourceType, resourceId, context);
    }

    /**
     * Constructor with custom error code for module-specific resource not found errors
     *
     * @param errorCode the module-specific error code
     * @param params    additional parameters for the error
     */
    public ResourceNotFoundException(int errorCode, Object... params) {
        super(errorCode, HTTP_STATUS, params);
    }

    /**
     * Constructor with custom error code and cause for module-specific resource not found errors
     *
     * @param errorCode the module-specific error code
     * @param cause     the cause of the exception
     * @param params    additional parameters for the error
     */
    public ResourceNotFoundException(int errorCode, Throwable cause, Object... params) {
        super(errorCode, HTTP_STATUS, cause, params);
    }
} 