package com.handmade.ecommerce.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a user attempts to access a resource that is forbidden.
 * This is typically used when a user has authenticated but does not have sufficient permissions.
 */
public class ForbiddenException extends BaseException {

    private static final String ERROR_CODE = "FORBIDDEN";
    private static final HttpStatus HTTP_STATUS = HttpStatus.FORBIDDEN;

    /**
     * Constructor with a message
     *
     * @param message the error message
     */
    public ForbiddenException(String message) {
        super(message, ERROR_CODE, HTTP_STATUS);
    }

    /**
     * Constructor with a message and cause
     *
     * @param message the error message
     * @param cause   the cause of the exception
     */
    public ForbiddenException(String message, Throwable cause) {
        super(message, ERROR_CODE, HTTP_STATUS, cause);
    }

    /**
     * Constructor with user ID and resource name
     *
     * @param userId       the ID of the user
     * @param resourceName the name of the resource
     */
    public ForbiddenException(String userId, String resourceName) {
        super(
                String.format("User '%s' is forbidden from accessing resource '%s'", userId, resourceName),
                ERROR_CODE,
                HTTP_STATUS,
                userId, resourceName
        );
    }

    /**
     * Constructor with user ID, resource name, and cause
     *
     * @param userId       the ID of the user
     * @param resourceName the name of the resource
     * @param cause        the cause of the exception
     */
    public ForbiddenException(String userId, String resourceName, Throwable cause) {
        super(
                String.format("User '%s' is forbidden from accessing resource '%s'", userId, resourceName),
                ERROR_CODE,
                HTTP_STATUS,
                cause,
                userId, resourceName
        );
    }
}