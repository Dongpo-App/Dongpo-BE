package com.dongyang.dongpo.dto.apiresponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private T data;
    private String message;

    public ApiResponse(T data) {
        this.data = data;
    }

    public ApiResponse(String message) {
        this.message = message;
    }
}
