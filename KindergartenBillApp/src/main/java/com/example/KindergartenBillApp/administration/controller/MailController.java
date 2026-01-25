package com.example.KindergartenBillApp.administration.controller;

import com.example.KindergartenBillApp.sharedTools.services.MailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailController {


        private final MailService mailService;

        @PostMapping("/send-uplatnica")
        public ResponseEntity<String> sendUplatnica(@RequestParam String to) throws MessagingException
        {
            mailService.sendUplatnicaMail(
                    to,
                    "Vaša uplatnica",
                    "U prilogu se nalazi PDF uplatnica.",
                    "Siniša Gavrić",              // uplatilac
                    "Uplata za vrtić",            // svrha
                    "Vrtić Sunce",                // primalac
                    "123-4567890123456-78",       // račun primaoca
                    "5000",                       // iznos
                    "97",                         // model
                    "2026-01"                     // poziv na broj
            );

            return ResponseEntity.ok("Mail sa uplatnicom poslat!");
        }
    }




