package com.example.common.enums;

public enum ResultCodeEnum {
    SUCCESS("200", "Success"),

    PARAM_ERROR("400", "Parameter Error"),
    TOKEN_INVALID_ERROR("401", "Invalid Token"),
    TOKEN_CHECK_ERROR("401", "Token validation failed, please re-login"),
    PARAM_LOST_ERROR("4001", "Parameter Missing"),

    SYSTEM_ERROR("500", "System Error"),
    USER_EXIST_ERROR("5001", "Username Already Exists"),
    USER_NOT_LOGIN("5002", "User Not Logged In"),
    USER_ACCOUNT_ERROR("5003", "Account or Password Incorrect"),
    USER_NOT_EXIST_ERROR("5004", "User Does Not Exist"),
    PARAM_PASSWORD_ERROR("5005", "Incorrect Original Password"),
    SIGN_ERROR("5005", "Already Signed Up for the Activity"),
    ;

    public String code;
    public String msg;

    ResultCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
