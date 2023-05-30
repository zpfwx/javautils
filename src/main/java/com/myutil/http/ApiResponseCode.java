package com.myutil.http;

/**
 * @author pfzhao
 * @title: ApiResponseCode
 * @projectName myUtil
 * @description: TODO
 * @date 2023/5/30 11:08
 */
public enum ApiResponseCode {
    OK(0, "ok"),
    UNKNOWN_EXCEPTION(3000, "Unknown exception"),
    REQUIRED_PARAMETER_IS_ERROR(3001, "Required parameter is error"),
    BUSINESS_EXCEPTION(3002, "This is a default business exception message"),
    REQUIRED_HEADER_IS_MISS(3003, "Required header is missed"),
    HAS_NO_PERMISSIONS(3004, "You haven't any permissions, please contract to admin"),
    TOKEN_HAS_EXPIRED(3005, "Token is invalid or expired"),
    USER_IS_NOT_REGISTERED(3006, "The email is not registered"),
    USER_PASSWORD_IS_INCORRECT(3007, "Incorrect password"),
    USER_ALREADY_EXISTS(3008, "User already exists"),
    APP_HAS_NO_PERMISSION(3009, "You haven't any permissions of this app"),
    ACCOUNT_ALREADY_EXISTS(3010, "Account already exists"),
    USER_IS_FORBIDDEN(3011, "User has been forbidden"),
    DATA_HAS_CHANGED(3012, "The Data has changed.");

    private int code;
    private String message;

    private ApiResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}

