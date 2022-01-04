package com.br.back02.utils;

import com.br.back02.exception.EmailException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

@Component
public class EmailUtils {

    @Value("${zerotwo.mail.user}")
    private String user;

    @Value("${zerotwo.mail.pass}")
    private String pass;

    public Session getSession() {
        Properties properties = System.getProperties();

        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, pass);
            }
        };

        return Session.getInstance(properties, authenticator);
    }

    public void sendEmail(Session session, String email, String subject, String body) throws EmailException {
        try {
            MimeMessage message = new MimeMessage(session);

            //Set headers
            message.addHeader("Content-type", "text/HTML; charset=UTF-8");

            //Set informations in e-mail
            message.setFrom(new InternetAddress(user, "ZeroTwo-NoReply"));
            message.setReplyTo(InternetAddress.parse(user, false));
            message.setSubject(subject);
            message.setText(body);
            message.setSentDate(new Date());
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));

            Transport.send(message);
        } catch (Exception e) {
            throw new EmailException("Ocorreu um erro ao enviar o e-mail");
        }
    }
}
