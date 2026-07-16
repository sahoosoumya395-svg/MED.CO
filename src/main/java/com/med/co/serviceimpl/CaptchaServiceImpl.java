package com.med.co.serviceimpl;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.med.co.dto.response.CaptchaResponse;
import com.med.co.security.CaptchaCache;
import com.med.co.security.CaptchaGenerator;
import com.med.co.service.CaptchaService;

@Service
public class CaptchaServiceImpl implements CaptchaService {

    /**
     * captchaId -> CaptchaCache
     */
    private static final ConcurrentHashMap<String, CaptchaCache> CAPTCHA_STORE =
            new ConcurrentHashMap<>();

    /**
     * Expiry Time (5 Minutes)
     */
    private static final int CAPTCHA_EXPIRY_MINUTES = 5;

    @Override
    public CaptchaResponse generateCaptcha() {

        String captchaText =
                CaptchaGenerator.generateCaptchaText(5);

        String captchaImage =
                CaptchaGenerator.generateCaptchaImage(captchaText);

        String captchaId =
                UUID.randomUUID().toString();

        CAPTCHA_STORE.put(
                captchaId,
                new CaptchaCache(
                        captchaText,
                        LocalDateTime.now().plusMinutes(CAPTCHA_EXPIRY_MINUTES)
                )
        );

        return new CaptchaResponse(
                captchaId,
                captchaImage
        );

    }

    @Override
    public boolean validateCaptcha(
            String captchaId,
            String userCaptcha) {

        if (captchaId == null || userCaptcha == null) {

            return false;

        }

        CaptchaCache cache =
                CAPTCHA_STORE.get(captchaId);

        if (cache == null) {

            return false;

        }

        if (LocalDateTime.now().isAfter(cache.getExpiryTime())) {

            CAPTCHA_STORE.remove(captchaId);

            return false;

        }

        boolean valid =
                cache.getCaptcha().equalsIgnoreCase(userCaptcha);

        /**
         * Remove after one use
         */
        CAPTCHA_STORE.remove(captchaId);

        return valid;

    }

}