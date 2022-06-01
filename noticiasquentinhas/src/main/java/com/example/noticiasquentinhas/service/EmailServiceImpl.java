package com.example.noticiasquentinhas.service;


import java.io.File;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.example.noticiasquentinhas.forms.EmailDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
public class EmailServiceImpl implements EmailService {

    @Autowired private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}") private String sender;

    /**
     * Send a mail without attachment
     * @param details the email details
     * @return if the mail was sent successfully or not
     */
    public String sendSimpleMail(EmailDetails details) {

        try {
            SimpleMailMessage mailMessage
                    = new SimpleMailMessage();

            // Setting up necessary details
            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getMsgBody());
            mailMessage.setSubject(details.getSubject());

            // Sending the mail
            javaMailSender.send(mailMessage);
            return "Mail Sent Successfully...";
        } catch (Exception e) {
            return "Error while Sending Mail";
        }
    }


    /**
     * Send a mail with attachment
     * @param details the email details
     * @return if the mail was sent successfully or not
     */
    public String sendMailWithAttachment(EmailDetails details) {

        MimeMessage mimeMessage
                = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {
            String path = "/news-thumbnail/0/noticiasquentinhas.png";
            FileSystemResource file
                    = new FileSystemResource(
                    new File("."+path));
            mimeMessageHelper
                    = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(details.getRecipient());
            mimeMessageHelper.setText(details.getMsgBody(),true);
            mimeMessageHelper.setSubject(
                    details.getSubject());
            mimeMessageHelper.addInline("image",file);
            // Sending the mail
            javaMailSender.send(mimeMessage);
            return "Mail sent Successfully";
        } catch (MessagingException e) {
            return "Error while sending mail!!!";
        }
    }
}