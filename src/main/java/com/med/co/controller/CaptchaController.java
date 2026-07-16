package com.med.co.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.med.co.dto.response.CaptchaResponse;
import com.med.co.service.CaptchaService;

@RestController
@RequestMapping("/api/auth")
public class CaptchaController {

    @Autowired
    private CaptchaService captchaService;

//     * Generate New Captcha

    @GetMapping("/captcha")
    public ResponseEntity<CaptchaResponse> generateCaptcha() {

        CaptchaResponse response = captchaService.generateCaptcha();

        return ResponseEntity.ok(response);
    }

}