package com.dongyang.dongpo.common.dto.apiresponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private T data;
    private String code;
    private String message;

    public ApiResponse() {}

    public ApiResponse(T data) {
        this.data = data;
    }

    public ApiResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ApiResponse(T data, String code, String message) {
        this.data = data;
        this.code = code;
        this.message = message;
    }
}
