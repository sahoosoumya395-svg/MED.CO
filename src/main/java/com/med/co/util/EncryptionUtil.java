package com.med.co.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class EncryptionUtil {

    private static final String SECRET_KEY = "HealthBridgeSecretKeyForAuth1234"; // 32 bytes
    private static final String INIT_VECTOR = "1234567890123456"; // 16 bytes

    public static String decrypt(String encryptedText) {
        if (encryptedText == null || encryptedText.trim().isEmpty()) {
            return encryptedText;
        }

        try {
            byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
            byte[] ivBytes = INIT_VECTOR.getBytes(StandardCharsets.UTF_8);

            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText.trim()));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            // Fallback: If text was not encrypted, return original text
            return encryptedText;
        }
    }

    public static String encrypt(String plainText) {
        if (plainText == null || plainText.trim().isEmpty()) {
            return plainText;
        }

        try {
            byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
            byte[] ivBytes = INIT_VECTOR.getBytes(StandardCharsets.UTF_8);

            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            return plainText;
        }
    }
}
