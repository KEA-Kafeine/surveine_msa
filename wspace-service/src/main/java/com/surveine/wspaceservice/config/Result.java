package com.surveine.wspaceservice.config;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Result {
    private Boolean isSuccess;
    private String message;
    private Object result;

    @Builder
    public Result(Boolean isSuccess, String message, Object result) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.result = result;
    }
}