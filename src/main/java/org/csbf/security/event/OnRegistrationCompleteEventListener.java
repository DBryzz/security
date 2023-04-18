package org.csbf.security.event;

import org.csbf.security.model.User;
import org.csbf.security.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.util.UUID;

public class OnRegistrationCompleteEventListener implements ApplicationListener<OnRegistrationCompleteEvent> {

        private AuthenticationService service;

        private MessageSource messages;

        private JavaMailSender mailSender;

        @Override
        public void onApplicationEvent(OnRegistrationCompleteEvent event) {
            this.confirmRegistration(event);
        }

        private void confirmRegistration(OnRegistrationCompleteEvent event) {
            User user = event.getUser();
            String token = UUID.randomUUID().toString();
            service.createEmailVerificationToken(user, token);

            String recipientAddress = user.getEmail();
            String subject = "Registration Confirmation";
            String confirmationUrl = event.getAppUrl() + "/regitrationConfirm?token=" + token;
            String message = messages.getMessage("message.regSucc", null, event.getLocale());

            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(recipientAddress);
            email.setSubject(subject);
            email.setText(message + "\r\n" + "http://localhost:8080" + confirmationUrl);
            mailSender.send(email);
        }
}
