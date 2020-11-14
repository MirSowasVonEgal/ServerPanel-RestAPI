package me.mirsowasvonegal.serverpanel.RestAPI.manager;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * @Projekt: RestAPI
 * @Created: 13.11.2020
 * @By: MirSowasVonEgal | Timo
 */
public class MailManager {


    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

    public MailManager() throws MessagingException, IOException {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", "true");

        mailSender.setHost("91.200.103.154");
        mailSender.setUsername("Admin@ShadeMC.de");
        mailSender.setPassword("Timo0580!");

        mailSender.setProtocol("smtp");
        mailSender.setPort(25);
        mailSender.setJavaMailProperties(prop);
    }

    public MailManager sendMail(String from, String title, String to, String text) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(from);
        helper.setSubject(title);
        helper.setTo(to);
        helper.setText(text,true);

        try{
            new Thread(() -> {
                mailSender.send(message);
            }).start();
        }
        catch(MailException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
        return this;
    }

    public MailManager sendRegisterMail(String userid, String token, String to) throws IOException, MessagingException {
        String registerMail = new FileManager().readfile("/mail/register.html").replace("%token%", token).replace("%userid%", userid);
        sendMail("no-replay@shademc.de", "Account Bestätigen", to, registerMail);
        return this;
    }

}
