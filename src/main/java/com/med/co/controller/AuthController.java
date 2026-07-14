package com.med.co.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.med.co.dto.request.LoginRequest;
import com.med.co.dto.response.ApiResponse;
import com.med.co.service.AuthService;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    
    

    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody LoginRequest request) {

        return authService.login(request);
    }
   

}