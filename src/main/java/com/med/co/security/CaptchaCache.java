package com.med.co.security;

import java.time.LocalDateTime;

public class CaptchaCache {

    private String captcha;

    private LocalDateTime expiryTime;

    public CaptchaCache() {
    }

    public CaptchaCache(String captcha, LocalDateTime expiryTime) {
        this.captcha = captcha;
        this.expiryTime = expiryTime;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(LocalDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }

}