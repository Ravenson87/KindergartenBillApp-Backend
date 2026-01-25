package com.example.KindergartenBillApp.sharedTools.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;


    public void sendUplatnicaMail(String to, String subject, String text,
                                  String uplatilac, String svrha, String primalac, String racunPrimaoca,
                                  String iznos, String model, String poziv) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);


        mailSender.send(message);


    }
}





