package com.med.co.service;

import com.med.co.dto.request.LoginRequest;
import com.med.co.dto.response.ApiResponse;

public interface AuthService {

    ApiResponse<?> login(LoginRequest request);

}