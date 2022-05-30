package com.example.noticiasquentinhas.service;


import com.example.noticiasquentinhas.smtp.EmailDetails;


public interface EmailService {

    // Method
    // To send a simple email
    String sendSimpleMail(EmailDetails details);

    // Method
    // To send an email with attachment
    String sendMailWithAttachment(EmailDetails details);
}