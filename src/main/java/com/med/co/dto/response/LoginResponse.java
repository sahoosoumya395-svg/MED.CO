package com.med.co.dto.response;

import lombok.AllArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnore;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private String role;

    @JsonIgnore
    private String tokenType = "Bearer";

    private String token;

    @JsonIgnore
    private long expiresInMs;
    
    @JsonIgnore
    private long expiresInMinutes;

}