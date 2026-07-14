package com.med.co.serviceimpl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.med.co.dto.request.LoginRequest;
import com.med.co.dto.response.ApiResponse;
import com.med.co.dto.response.LoginResponse;
import com.med.co.entity.UserRole;
import com.med.co.repository.UserRepository;
import com.med.co.security.JwtUtils;
import com.med.co.service.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final UserRepository userRepository;

    @Override
    public ApiResponse<?> login(LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()));

        String token = jwtUtils.generateJwtToken(authentication);

        UserRole user = userRepository.findByEmail(request.getEmail()).get();
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

}