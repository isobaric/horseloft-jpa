package com.horseloft.jpa.exception;

import com.horseloft.jpa.enums.ResponseCode;

public class BusinessException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 5055324742442443508L;

    private String errorCode;

    public BusinessException(String code, Throwable cause) {
        super(cause);
        setErrorCode(code);
    }

    public BusinessException(String code, String message) {
    	super(message);
        setErrorCode(code);
    }
    
    public BusinessException(Throwable cause) {
        super(cause);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public BusinessException(ResponseCode responseCode) {
        this(responseCode.getCode(), responseCode.getMessage());
    }

    public static BusinessException ofErrorParameter() {
        return new BusinessException(ResponseCode.PARAMETER_ERROR);
    }

    public static BusinessException ofErrorParameter(String message) {
        return new BusinessException(ResponseCode.PARAMETER_ERROR.getCode(), message);
    }
}
