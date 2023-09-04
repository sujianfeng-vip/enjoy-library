package vip.sujianfeng.email;

import vip.sujianfeng.email.model.SmtpConfig;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

public class SMTPSendMailUtils {

    /**
     * 发送邮件
     * @param email
     * @param subject
     * @param body
     */
    public static void sendEmail(SmtpConfig smtpConfig, String email, String subject, String body) throws MessagingException, UnsupportedEncodingException {
        final Properties props = new Properties();
        props.put("mail.smtp.host", smtpConfig.getHost());
        props.put("mail.user", smtpConfig.getUser());
        props.put("mail.password", smtpConfig.getPassword());
        props.put("mail.smtp.port", smtpConfig.getPort());
        props.put("mail.transport.protocol", "smtp");// 发送邮件的协议
        props.put("mail.smtp.auth", "true");
        Transport transport = null;
        // 构建授权信息，用于进行SMTP进行身份验证
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
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
        //MimeMultipart类是一个容器类，包含MimeBodyPart类型的对象
        Multipart mainPart = new MimeMultipart();
        MimeBodyPart messageBodyPart = new MimeBodyPart();//创建一个包含HTML内容的MimeBodyPart
        //设置HTML内容
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
        sendEmail(smtpConfig, receiver,"邮件发送测试","测试内容");//收件人
        System.out.println("ok");

    }
}
