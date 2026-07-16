package com.med.co.service;

import com.med.co.dto.response.CaptchaResponse;

public interface CaptchaService {

    
//      *Generate a new captcha
      
    CaptchaResponse generateCaptcha();

//     * Validate captcha entered by user
    
    boolean validateCaptcha(String captchaId, String userCaptcha);

}