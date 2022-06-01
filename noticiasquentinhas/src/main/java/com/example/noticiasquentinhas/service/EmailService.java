package com.example.noticiasquentinhas.service;


import com.example.noticiasquentinhas.forms.EmailDetails;


public interface EmailService {

    // Method
    // To send a simple email
    String sendSimpleMail(EmailDetails details);

    // Method
    // To send an email with attachment
    String sendMailWithAttachment(EmailDetails details);
}