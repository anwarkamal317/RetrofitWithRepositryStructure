package com.example.apc4.retrofitwithrepositrystructure.ResponseModels;

public class CommonResponse {


    private boolean success;
    private String message;
    private int code;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

}