package com.med.co.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private String role;

    private String tokenType = "Bearer";

    private String token;

    private long expiresInMs;

    private long expiresInMinutes;

}