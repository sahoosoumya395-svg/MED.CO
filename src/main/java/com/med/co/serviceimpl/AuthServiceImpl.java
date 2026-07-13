package com.med.co.serviceimpl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.med.co.dto.request.LoginRequest;
import com.med.co.dto.response.ApiResponse;
import com.med.co.dto.response.LoginResponse;
import com.med.co.entity.UserRole;
import com.med.co.exception.ResourceNotFoundException;
import com.med.co.repository.UserRepository;
import com.med.co.service.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ApiResponse<?> login(LoginRequest request) {

        UserRole user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid Email"));

        if (!Boolean.TRUE.equals(user.getEnabled())) {
            return new ApiResponse<>(
                    403,
                    "User Account Disabled",
                    null);
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new ApiResponse<>(
                    401,
                    "Invalid Password",
                    null);
        }

        LoginResponse response = new LoginResponse(
                user.getId(),
                user.getEmail(),
                user.getRole().getRoleName().name());

        return new ApiResponse<>(
                200,
                "Login Successful",
                response);
    }
}