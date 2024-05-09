package com.dongyang.dongpo.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Response<T> {
    private String message;
    private T data;

    public Response(String message) {
        this.message = message;
    }

    public Response(T data) {
        this.data = data;
    }
}
