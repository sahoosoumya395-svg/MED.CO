package com.med.co.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private int statusCode;

    private String message;

    private T data;
}