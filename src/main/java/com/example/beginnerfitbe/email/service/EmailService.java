package com.example.beginnerfitbe.email.service;

import java.util.Random;

import com.example.beginnerfitbe.email.EmailDto;
import com.example.beginnerfitbe.error.StateResponse;
import com.example.beginnerfitbe.redis.service.RedisService;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;
    private final RedisService redisService;

    @Value("${spring.mail.username}")
    private String fromEmail;

    private static final String ePw = createKey();

    private MimeMessage createMessage(String to) throws Exception {
        System.out.println("보내는 대상 : " + to);
        System.out.println("인증 번호 : " + ePw);
        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, to); // 보내는 대상
        message.setSubject("beginner fit sign-up"); // 제목

        String msgg = "";
        msgg += "<div style='margin:20px;'>";
        msgg += "<h1> !BEGINNERFIT! </h1>";
        msgg += "<br>";
        msgg += "<br>";
        msgg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg += "<h3 style='color:black;'>회원가입 인증 코드입니다.</h3>";
        msgg += "<div style='font-size:130%'>";
        msgg += "CODE : <strong>";
        msgg += ePw + "</strong><div><br/> ";
        msgg += "</div>";
        message.setText(msgg, "utf-8", "html"); // 내용
        message.setFrom(new InternetAddress(fromEmail, "beginnerfit")); // 보내는 사람

        return message;
    }

    public static String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) { // 인증코드 8자리
            int index = rnd.nextInt(3); // 0~2 까지 랜덤

            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    break;
            }
        }
        return key.toString();
    }

    public String sendEmail(String to) throws Exception {
        MimeMessage message = createMessage(to);
        try {
            emailSender.send(message);
            redisService.setDataExpire(ePw, to, 60 * 5L);
        } catch (MailException es) {
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
        return ePw;
    }

    public StateResponse codeVerification(EmailDto emailDto) {
        String email = emailDto.getEmail();
        String code = emailDto.getCode();

        if (redisService.getData(code) != null && redisService.getData(code).equals(email)) {
            return StateResponse.builder()
                    .code("SUCCESS")
                    .message("이메일 인증에 성공했습니다.")
                    .build();
        }
        return StateResponse.builder()
                .code("FAIL")
                .message("이메일 인증에 실패했습니다.")
                .build();
    }
}
