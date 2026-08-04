package com.med.co.serviceimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.med.co.exception.BadRequestException;
import com.med.co.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendOtpEmail(String toEmail, String otp) {

        log.info("=================================================");
        log.info(" [MED.CO OTP GENERATED] Target: {} | OTP: {}", toEmail, otp);
        log.info("=================================================");

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
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
            log.info("OTP Email sent successfully to {}", toEmail);
        } catch (Exception e) {
            log.error("SMTP Delivery Failed for {}: {}", toEmail, e.getMessage(), e);
            throw new BadRequestException("Mail sending failed: " + e.getMessage());
        }
    }
}