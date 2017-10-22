package com.hyb.backstage.base;

public class ValidateFailedException extends Exception {
    private final static String MSG = "验证失败";

    private String field;

    public ValidateFailedException() {
        super(MSG);
    }

    public ValidateFailedException(String field, String message) {
        super(MSG + ":" + message);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
