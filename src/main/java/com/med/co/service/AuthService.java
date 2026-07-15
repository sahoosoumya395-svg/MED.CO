package com.med.co.service;

import com.med.co.dto.request.ForgotPasswordRequest;
import com.med.co.dto.request.LoginRequest;
import com.med.co.dto.request.ResetPasswordRequest;
import com.med.co.dto.request.VerifyOtpRequest;
import com.med.co.dto.response.ApiResponse;

public interface AuthService {

    ApiResponse<?> login(LoginRequest request);

    ApiResponse<?> forgotPassword(ForgotPasswordRequest request);

    ApiResponse<?> verifyOtp(VerifyOtpRequest request);

    ApiResponse<?> resetPassword(ResetPasswordRequest request);

}