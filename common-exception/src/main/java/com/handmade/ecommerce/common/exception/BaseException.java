package com.handmade.ecommerce.common.exception;

import lombok.Getter;
import org.chenile.base.exception.ErrorNumException;
import org.chenile.base.response.ErrorType;
import org.chenile.base.response.ResponseMessage;
import org.springframework.http.HttpStatus;

/**
 * Base exception class for all custom exceptions in the Handmade E-commerce platform.
 * This class extends ErrorNumException to provide standardized error handling.
 * Messages are handled by Chenile's internationalization system.
 */
@Getter
public abstract class BaseException extends ErrorNumException {

    private final HttpStatus httpStatus;

    /**
     * Constructor with error code and HTTP status
     *
     * @param errorNum   the numeric error code
     * @param httpStatus the HTTP status
     */
    protected BaseException(int errorNum, HttpStatus httpStatus) {
        super(errorNum, 0, new Object[]{});
        this.httpStatus = httpStatus;
        getResponseMessage().setSeverity(ErrorType.ERROR);
    }

    /**
     * Constructor with error code, HTTP status, and parameters
     *
     * @param errorNum   the numeric error code
     * @param httpStatus the HTTP status
     * @param params     additional parameters for the error
     */
    protected BaseException(int errorNum, HttpStatus httpStatus, Object... params) {
        super(errorNum, 0, params);
        this.httpStatus = httpStatus;
        getResponseMessage().setSeverity(ErrorType.ERROR);
    }

    /**
     * Constructor with error code, HTTP status, and cause
     *
     * @param errorNum   the numeric error code
     * @param httpStatus the HTTP status
     * @param cause      the cause of the exception
     */
    protected BaseException(int errorNum, HttpStatus httpStatus, Throwable cause) {
        super(errorNum, 0, new Object[]{}, cause);
        this.httpStatus = httpStatus;
        getResponseMessage().setSeverity(ErrorType.ERROR);
    }

    /**
     * Constructor with error code, HTTP status, cause, and parameters
     *
     * @param errorNum   the numeric error code
     * @param httpStatus the HTTP status
     * @param cause      the cause of the exception
     * @param params     additional parameters for the error
     */
    protected BaseException(int errorNum, HttpStatus httpStatus, Throwable cause, Object... params) {
        super(errorNum, 0, params, cause);
        this.httpStatus = httpStatus;
        getResponseMessage().setSeverity(ErrorType.ERROR);
    }

    /**
     * Add a sub-error to this exception
     *
     * @param subErrorNum the sub-error number
     * @param params      parameters for the sub-error
     */
    protected void addSubError(int subErrorNum, Object... params) {
        ResponseMessage subError = new ResponseMessage();
        subError.setCode(getErrorNum());
        subError.setSubErrorCode(subErrorNum);
        subError.setParams(params);
        subError.setSeverity(ErrorType.ERROR);
        addError(subError);
    }
} 