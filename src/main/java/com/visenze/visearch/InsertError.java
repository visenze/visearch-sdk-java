package com.visenze.visearch;


public class InsertError {

    private final String imName;
    private final Integer index;
    private final Integer errorCode;
    private final String errorMessage;

    public InsertError(String imName, Integer index, Integer errorCode, String errorMessage) {
        this.imName = imName;
        this.index = index;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getImName() {
        return imName;
    }

    public Integer getIndex() {
        return index;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return "InsertError{" +
                "imName='" + imName + '\'' +
                ", index=" + index +
                ", errorCode=" + errorCode +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
