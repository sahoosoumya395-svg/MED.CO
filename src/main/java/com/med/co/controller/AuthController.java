package com.med.co.controller;

import org.springframework.web.bind.annotation.*;
import com.med.co.dto.request.ForgotPasswordRequest;
import com.med.co.dto.request.LoginRequest;
import com.med.co.dto.request.ResetPasswordRequest;
import com.med.co.dto.request.VerifyOtpRequest;
import com.med.co.dto.response.ApiResponse;
import com.med.co.service.AuthService;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest request){

        return ResponseEntity.ok(authService.login(request));

    }

    @PostMapping("/forgot-password")
    public ApiResponse<?> forgotPassword(
            @RequestBody ForgotPasswordRequest request) {

        return authService.forgotPassword(request);
    }

    @PostMapping("/verify-otp")
    public ApiResponse<?> verifyOtp(
            @RequestBody VerifyOtpRequest request) {

        return authService.verifyOtp(request);
    }
    
    //reset password
    @PostMapping("/reset-password")
    public ApiResponse<?> resetPassword(
            @RequestBody ResetPasswordRequest request) {

        return authService.resetPassword(request);
    }
    
    @PostMapping("/logout")
    public ApiResponse<?> logout(HttpServletRequest request){

        return authService.logout(request);

    }
    
  

}