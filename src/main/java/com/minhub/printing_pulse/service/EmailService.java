package com.minhub.printing_pulse.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;
    private final ThingQueryService queryService;
    private final NewsletterRenderService renderService;

    public void sendHtml(String subject, String html, String textAlt) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            // TODO: from/to addresses from application.yml
            helper.setFrom("sdk926@gmail.com");
            helper.setTo("sdk926@gmail.com"); // TODO: 실제 이메일 주소로 변경
            helper.setSubject(subject);
            helper.setText(textAlt, html);
            
            mailSender.send(message);
            log.info("이메일 전송 완료: {}", subject);
        } catch (MessagingException e) {
            log.error("이메일 전송 실패: {}", e.getMessage(), e);
            // TODO: Implement multipart/alternative (text/plain + text/html) using JavaMailSender
            // TODO: from/to addresses from application.yml
        }
    }

}
