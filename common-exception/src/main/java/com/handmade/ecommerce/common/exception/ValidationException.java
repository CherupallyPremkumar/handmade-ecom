package com.handmade.ecommerce.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when validation fails for input data or business rules.
 * Error messages are handled by Chenile's internationalization system.
 */
public class ValidationException extends BaseException {

    private static final int ERROR_CODE = 400;
    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;

    /**
     * Constructor with default error code
     */
    public ValidationException() {
        super(ERROR_CODE, HTTP_STATUS);
    }

    /**
     * Constructor with cause
     *
     * @param cause the cause of the exception
     */
    public ValidationException(Throwable cause) {
        super(ERROR_CODE, HTTP_STATUS, cause);
    }

    /**
     * Constructor with field validation details
     *
     * @param fieldName the name of the field that failed validation
     * @param value     the invalid value
     * @param reason    the reason for validation failure
     */
    public ValidationException(String fieldName, Object value, String reason) {
        super(ERROR_CODE, HTTP_STATUS, fieldName, value, reason);
    }

    /**
     * Constructor with multiple validation errors
     *
     * @param validationErrors array of validation errors, each containing field name, value, and reason
     */
    public ValidationException(Object... validationErrors) {
        super(ERROR_CODE, HTTP_STATUS, validationErrors);
    }

    /**
     * Constructor with entity validation details
     *
     * @param entityType the type of entity that failed validation
     * @param entityId   the ID of the entity (if applicable)
     * @param reason     the reason for validation failure
     */
    public ValidationException(String entityType, String entityId, String reason) {
        super(ERROR_CODE, HTTP_STATUS, entityType, entityId, reason);
    }

    /**
     * Constructor with custom error code for module-specific validation errors
     *
     * @param errorCode the module-specific error code
     * @param params    additional parameters for the error
     */
    public ValidationException(int errorCode, Object... params) {
        super(errorCode, HTTP_STATUS, params);
    }

    /**
     * Constructor with custom error code and cause for module-specific validation errors
     *
     * @param errorCode the module-specific error code
     * @param cause     the cause of the exception
     * @param params    additional parameters for the error
     */
    public ValidationException(int errorCode, Throwable cause, Object... params) {
        super(errorCode, HTTP_STATUS, cause, params);
    }
} 