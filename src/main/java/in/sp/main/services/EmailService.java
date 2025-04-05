package in.sp.main.services;

import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service 
public class EmailService {
    public boolean sendEmail(String subject, String message, String to) {

        boolean flag = false;
        String from = "kakaqwer143@gmail.com";
        String password = "eqsc ldos jeku lats";
        String host = "smtp.gmail.com";

        Properties properties = System.getProperties();
        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        session.setDebug(true);
        try {
            Message m = new MimeMessage(session);
            m.setFrom(new InternetAddress(from));
            m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            m.setSubject(subject);
           // m.setText(message);
            m.setContent(message,"text/html");
            Transport.send(m);
            System.out.println("Email sent successfully.");
            flag = true;
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return flag;

    }
}
