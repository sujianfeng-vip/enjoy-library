package vip.sujianfeng.email;

import vip.sujianfeng.email.model.SmtpConfig;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

public class SMTPSendMailUtils {

    public static void sendEmail(SmtpConfig smtpConfig, String email, String subject, String body) throws MessagingException, UnsupportedEncodingException {
        final Properties props = new Properties();
        props.put("mail.smtp.host", smtpConfig.getHost());
        props.put("mail.user", smtpConfig.getUser());
        props.put("mail.password", smtpConfig.getPassword());
        props.put("mail.smtp.port", smtpConfig.getPort());
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        Transport transport = null;
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };
        Session session = Session.getInstance(props, authenticator);

        MimeMessage msg = new MimeMessage(session);
        msg.setSentDate(new Date());
        InternetAddress fromAddress = new InternetAddress(smtpConfig.getFromAddress(), smtpConfig.getFromName());
        msg.setFrom(fromAddress);
        InternetAddress[] toAddress = new InternetAddress[1];
        toAddress[0] = new InternetAddress(email);
        msg.setRecipients(Message.RecipientType.TO, toAddress);
        Multipart mainPart = new MimeMultipart();
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(body,"text/html; charset=utf-8");
        mainPart.addBodyPart(messageBodyPart);
        msg.setSubject(subject, "UTF-8");
        msg.setContent(mainPart);
        msg.saveChanges();
        Transport.send(msg, msg.getAllRecipients());

    }
    public static void main(String args[]) throws  Exception {
        SmtpConfig smtpConfig = new SmtpConfig();
        smtpConfig.setUser("xmpilot@163.com");
        smtpConfig.setPassword("960720163mail");
        String receiver = "360482507@qq.com";
        sendEmail(smtpConfig, receiver,"test1","test2");
        System.out.println("ok");

    }
}
