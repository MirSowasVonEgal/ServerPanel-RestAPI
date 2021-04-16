package me.mirsowasvonegal.serverpanel.RestAPI.manager;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
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


        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(text, "text/html; charset=utf-8" ); // <---

        Multipart multipart = new MimeMultipart();

// add the message body to the mime message
        multipart.addBodyPart( messageBodyPart );

// Put all message parts in the message
        message.setContent( multipart );



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
        sendStandardMail("http://localhost:8080/auth/verify/" + token + "/" + userid, "Account Bestätigen", "Du hast es fast geschafft! Best&auml;tige einfach deine E-Mail-Adresse", "Du hast erfolgreich ein ShadeHost-Konto erstellt. Um es zu aktivieren, klicke bitte unten, um deine E-Mail-Adresse zu verifizieren.", to);
        return this;
    }

    public MailManager sendResetPasswordMail(String userid, String passwordtoken, String to) throws IOException, MessagingException {
        sendStandardMail("http://localhost:8080/auth/password_reset/" + passwordtoken + "/" + userid, "Passwort zurücksetzen", "Du hast ein neues Passwort für ShadeHost-Konto angefordert.", "Klicke bitte unten, um dein Passwort zurückzusetzen.", to);
        return this;
    }

    public MailManager sendStandardMail(String link, String title, String titlemesage, String message, String to) throws IOException, MessagingException {
        String mail = new FileManager().readfile("/mail/mail.html")
                .replace("%link%", link)
                .replace("%titlemessage%", titlemesage)
                .replace("%title%", title)
                .replace("%message%", message)
                .replace("%hostname%", "http://localhost:8080/");
        sendMail("no-replay@shademc.de", title, to, mail);
        return this;
    }
}
