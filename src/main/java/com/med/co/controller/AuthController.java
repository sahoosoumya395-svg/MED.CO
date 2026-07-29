package com.med.co.controller;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import com.med.co.dto.request.ForgotPasswordRequest;
import com.med.co.dto.request.LoginRequest;
import com.med.co.dto.request.ResetPasswordRequest;
import com.med.co.dto.request.VerifyOtpRequest;
import com.med.co.dto.response.ApiResponse;
import com.med.co.dto.response.CaptchaResponse;
import com.med.co.service.AuthService;
import com.med.co.service.CaptchaService;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CaptchaService captchaService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    // Generate captcha - moved here from CaptchaController
    @GetMapping("/create-captcha")
    public ResponseEntity<CaptchaResponse> generateCaptcha() {
        CaptchaResponse response = captchaService.generateCaptcha();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ApiResponse<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        return authService.forgotPassword(request);
    }
  
    @PostMapping("/verify-otp")
    public ApiResponse<?> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        return authService.verifyOtp(request);
    }

    // reset password
    @PostMapping("/reset-password")
    public ApiResponse<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        return authService.resetPassword(request);
    }

    @PostMapping("/logout")
    public ApiResponse<?> logout(HttpServletRequest request) {
        return authService.logout(request);
    }
    
}