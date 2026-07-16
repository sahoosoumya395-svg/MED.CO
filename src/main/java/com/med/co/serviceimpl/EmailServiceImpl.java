package com.med.co.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.med.co.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendOtpEmail(String toEmail, String otp) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(fromEmail);      // <-- Add this line
        message.setTo(toEmail);

        message.setSubject("MED.CO Password Reset OTP");

        message.setText(
                "Dear User,\n\n"
                + "Your OTP for resetting your MED.CO account password is: "
                + otp
                + "\n\n"
                + "This OTP is valid for 5 minutes."
                + "\n\n"
                + "Please do not share this OTP with anyone."
                + "\n\n"
                + "Regards,\nMED.CO Team");

        mailSender.send(message);
    }
}