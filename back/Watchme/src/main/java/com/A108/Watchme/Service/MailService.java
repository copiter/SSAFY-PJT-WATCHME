package com.A108.Watchme.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMail(String email, String subject, String context) {

        MimeMessagePreparator msg = new MimeMessagePreparator() {

            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                mimeMessageHelper.setTo(email);
                mimeMessageHelper.setSubject(subject);
                mimeMessageHelper.setText(context, true);
            }
        };
        javaMailSender.send(msg);
    }
}