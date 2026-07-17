package com.med.co.serviceimpl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.med.co.dto.request.ForgotPasswordRequest;
import com.med.co.dto.request.LoginRequest;
import com.med.co.dto.request.ResetPasswordRequest;
import com.med.co.dto.request.VerifyOtpRequest;
import com.med.co.dto.response.ApiResponse;
import com.med.co.dto.response.LoginResponse;
import com.med.co.entity.PasswordResetOtp;
import com.med.co.entity.UserRole;
import com.med.co.exception.BadRequestException;
import com.med.co.repository.PasswordResetOtpRepository;
import com.med.co.repository.UserRepository;
import com.med.co.security.JwtUtils;
import com.med.co.service.AuthService;
import com.med.co.service.CaptchaService;
import com.med.co.service.EmailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final UserRepository userRepository;

    private final PasswordResetOtpRepository otpRepository;

    private final EmailService emailService;

    private final PasswordEncoder passwordEncoder;

    private final CaptchaService captchaService;

    @Override
    public ApiResponse<?> login(LoginRequest request) {

        // ==========================
        // Validate Captcha First
        // ==========================

        boolean validCaptcha = captchaService.validateCaptcha(
                request.getCaptchaId(),
                request.getCaptcha());

        if (!validCaptcha) {
            throw new BadRequestException("Invalid or Expired Captcha");
        }

        // ==========================
        // Authenticate User
        // ==========================

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()));

        // ==========================
        // Generate JWT Token
        // ==========================

        String token = jwtUtils.generateJwtToken(authentication);

        UserRole user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("User Not Found"));

        LoginResponse response = new LoginResponse(
                user.getRole().getRoleName().name(),
                "Bearer",
                token,
                jwtUtils.getJwtExpirationMs(),
                jwtUtils.getJwtExpirationMinutes()
        );

        return new ApiResponse<>(
                200,
                "Login Successful",
                response);
    }

    private String generateOtp() {

        Random random = new Random();

        return String.format("%06d", random.nextInt(999999));

    }

    @Transactional
    @Override
    public ApiResponse<?> forgotPassword(ForgotPasswordRequest request) {

        Optional<UserRole> optionalUser =
                userRepository.findByEmail(request.getEmail());

        if (optionalUser.isEmpty()) {

            return new ApiResponse<>(
                    404,
                    "Email not registered",
                    null);
        }

        otpRepository.deleteByEmail(request.getEmail());

        String otp = generateOtp();

        PasswordResetOtp passwordResetOtp =
                PasswordResetOtp.builder()
                        .email(request.getEmail())
                        .otp(otp)
                        .verified(false)
                        .createdAt(LocalDateTime.now())
                        .expiryTime(LocalDateTime.now().plusMinutes(5))
                        .build();

        otpRepository.save(passwordResetOtp);

        emailService.sendOtpEmail(request.getEmail(), otp);

        return new ApiResponse<>(
                200,
                "OTP Sent Successfully",
                null);

    }

    @Override
    public ApiResponse<?> verifyOtp(VerifyOtpRequest request) {

        Optional<PasswordResetOtp> optionalOtp =
                otpRepository.findTopByEmailOrderByCreatedAtDesc(
                        request.getEmail());

        if (optionalOtp.isEmpty()) {

            return new ApiResponse<>(
                    404,
                    "OTP Not Found",
                    null);
        }

        PasswordResetOtp otpEntity = optionalOtp.get();

        if (!otpEntity.getOtp().equals(request.getOtp())) {

            return new ApiResponse<>(
                    400,
                    "Invalid OTP",
                    null);
        }

        if (otpEntity.getExpiryTime().isBefore(LocalDateTime.now())) {

            return new ApiResponse<>(
                    400,
                    "OTP Expired",
                    null);
        }

        otpEntity.setVerified(true);

        otpRepository.save(otpEntity);

        return new ApiResponse<>(
                200,
                "OTP Verified Successfully",
                null);

    }

    @Transactional
    @Override
    public ApiResponse<?> resetPassword(ResetPasswordRequest request) {

        PasswordResetOtp otp = otpRepository
                .findTopByEmailOrderByCreatedAtDesc(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("OTP not found"));

        if (LocalDateTime.now().isAfter(otp.getExpiryTime())) {

            return new ApiResponse<>(
                    400,
                    "OTP Expired",
                    null
            );
        }

        if (!otp.isVerified()) {

            return new ApiResponse<>(
                    400,
                    "OTP Not Verified",
                    null
            );
        }

        if (!otp.getOtp().equals(request.getOtp())) {

            return new ApiResponse<>(
                    400,
                    "Invalid OTP",
                    null
            );
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {

            return new ApiResponse<>(
                    400,
                    "Password and Confirm Password do not match",
                    null
            );
        }

        UserRole user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);

        otpRepository.deleteByEmail(request.getEmail());

        return new ApiResponse<>(
                200,
                "Password Updated Successfully",
                null
        );
    }
}