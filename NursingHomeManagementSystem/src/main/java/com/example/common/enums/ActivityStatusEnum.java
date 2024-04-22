package com.example.common.enums;

public enum ActivityStatusEnum {
    NOT_START("Not Started"),
    IS_END("Ended"),
    NOT_SIGN("Not Registered"),
    IS_SIGN("Signed Up");

    private String value;

    public String getValue() {
        return value;
    }

    ActivityStatusEnum(String value) {
        this.value = value;
    }
}
