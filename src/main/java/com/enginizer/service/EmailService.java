package com.enginizer.service;

import com.enginizer.model.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Created by sorinavasiliu on 1/20/17.
 */
@Service
public class EmailService {

//    @Autowired
//    private ContentBuilderService contentBuilderService;

//    @Autowired
//    private JavaMailSender mailSender;
//
//    @Value("${mail.from}")
//    private String from;
//
//    @Value("${mail.subject}")
//    private String subject;

    public void prepareAndSend(final User user, final String token) throws MessagingException {

//        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
//        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
//        message.setSubject(subject);
//        message.setTo(user.getEmail());
//
////        String htmlContent = contentBuilderService.build(user,token);
////        message.setText(htmlContent, true);
//
//        // Send email
//        this.mailSender.send(mimeMessage);
    }


}
