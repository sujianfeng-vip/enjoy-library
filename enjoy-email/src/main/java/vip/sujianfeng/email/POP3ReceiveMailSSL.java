package vip.sujianfeng.email;

import com.sun.mail.pop3.POP3Folder;
import com.sun.mail.pop3.POP3Store;
import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import java.security.GeneralSecurityException;
import java.util.Properties;

public class POP3ReceiveMailSSL {

    public static void main(String[] args) {
        try {
            POP3ReceiveMailSSL.receive();
        } catch (GeneralSecurityException | MessagingException e) {
            e.printStackTrace();
        }

    }


    public static void receive() throws GeneralSecurityException, MessagingException {
        Properties props = new Properties();
        props.setProperty("mail.popStore.protocol", "pop3"); // 使用pop3协议
        props.setProperty("mail.pop3.port", "995"); // 端口
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        props.put("mail.pop3.ssl.enable",true);
        props.put("mail.pop3.ssl.socketFactory",sf);
        //props.setProperty("mail.debug", "true");
        props.setProperty("mail.pop3.host", "pop.exmail.qq.com");
        Session session = Session.getInstance(props);
        POP3Store pop3Store = (POP3Store) session.getStore("pop3");
        pop3Store.connect("pop.exmail.qq.com", 995, "liumeng@richinfo.com.cn", "UmLrqSj2Ra7KjQb3");
        POP3Folder pop3Folder = (POP3Folder) pop3Store.getFolder("INBOX");
        /* Folder.READ_ONLY：只读权限
         * Folder.READ_WRITE：可读可写(可以修改邮件的状态)
         */
        pop3Folder.open(Folder.READ_WRITE); //打开收件箱
        Message[] messages = pop3Folder.getMessages(1, 2);
        System.out.println(messages.length);
    }
}
