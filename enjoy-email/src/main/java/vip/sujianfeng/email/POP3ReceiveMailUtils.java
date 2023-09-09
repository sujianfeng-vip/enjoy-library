package vip.sujianfeng.email;

import vip.sujianfeng.email.model.EmailItem;
import vip.sujianfeng.email.model.EmailPerson;
import vip.sujianfeng.email.model.EmailPriority;
import vip.sujianfeng.email.model.Pop3Config;
import com.sun.mail.util.MailSSLSocketFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class POP3ReceiveMailUtils {

    public static void main(String[] args) throws Exception {
//        List<EmailItem> list = receive(Pop3Config.get163mailConfig("jiajiazupu@163.com", "ZUSFNKQMHGKJSDQY"), 1, 2);
//        System.out.println(list.size());

        Pop3Config config = Pop3Config.get163mailConfig("liumeng@richinfo.com.cn", "UmLrqSj2Ra7KjQb3");
        config.setHost("pop.exmail.qq.com");
        config.setPort("995");
        config.setUseSsl(true);
        List<EmailItem> list = receive(config, 10);
        System.out.println(list.size());
    }


    public static List<EmailItem> receive(Pop3Config config, int limit) throws Exception {
        List<EmailItem> result = new ArrayList<>();

        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "pop3");
        props.setProperty("mail.pop3.port", config.getPort());
        props.setProperty("mail.pop3.host", config.getHost());
        if (config.isUseSsl()){
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            props.put("mail.pop3.ssl.enable", true);
            props.put("mail.pop3.ssl.socketFactory", sf);
        }

        Session session = Session.getInstance(props);
        Store store = session.getStore("pop3");
        Folder folder = null;
        try{
            store.connect(config.getUser(), config.getPassword());

            folder = store.getFolder("INBOX");
            /* Folder.READ_ONLY
             * Folder.READ_WRITE
             */
            folder.open(Folder.READ_WRITE);

            int end = folder.getMessageCount();
            int start = end - limit;
            if (start < 1) {
                start = 1;
            }
            Message[] messages = folder.getMessages(start, end);
            for (Message message : messages) {
                result.add(0, parseMessage(message));
            }
        }finally {
            if (folder != null && folder.isOpen()){
                folder.close(true);
            }
            if (store.isConnected()){
                store.close();
            }
        }
        return result;
    }

    public static EmailItem parseMessage(Message messages) throws MessagingException, IOException {
        EmailItem result = new EmailItem();
        MimeMessage msg = (MimeMessage) messages;
        result.setMimeMessage(msg);
        result.setSubject(getSubject(msg));
        result.setSender(getFrom(msg));
        result.setTo(getReceiveAddress(msg, Message.RecipientType.TO));
        result.setCc(getReceiveAddress(msg, Message.RecipientType.CC));
        result.setBcc(getReceiveAddress(msg, Message.RecipientType.BCC));
        result.setMessageNumber(msg.getMessageNumber());
        result.setSendDate(msg.getSentDate());
        result.setSeen(isSeen(msg));
        result.setEmailPriority(getPriority(msg));
        result.setReplySign(isReplySign(msg));
        result.setSize(msg.getSize());
        boolean isContainerAttachment = isContainAttachment(msg);
        result.setContainAttachment(isContainerAttachment);
        StringBuffer content = new StringBuffer(30);
        getMailTextContent(msg, content);
        result.setContent(content);
        return result;
    }

    public static String getSubject(MimeMessage msg) throws UnsupportedEncodingException, MessagingException {
        return MimeUtility.decodeText(msg.getSubject());
    }

    public static EmailPerson getFrom(MimeMessage msg) throws MessagingException, UnsupportedEncodingException {
        EmailPerson result = new EmailPerson();
        Address[] froms = msg.getFrom();
        if (froms.length < 1)
            throw new MessagingException("not sender!");

        InternetAddress address = (InternetAddress) froms[0];
        String person = address.getPersonal();
        if (person != null) {
            person = MimeUtility.decodeText(person) + " ";
        } else {
            person = "";
        }
        result.setName(person);
        result.setAddress(address.getAddress());
        return result;
    }

    public static List<EmailPerson> getReceiveAddress(MimeMessage msg, Message.RecipientType type) throws MessagingException, UnsupportedEncodingException {
        List<EmailPerson> result = new ArrayList<>();

        Address[] addresss = null;
        if (type == null) {
            addresss = msg.getAllRecipients();
        } else {
            addresss = msg.getRecipients(type);
        }
        if (addresss != null) {
            for (Address address : addresss) {
                EmailPerson emailPerson = new EmailPerson();
                InternetAddress internetAddress = (InternetAddress)address;
                emailPerson.setName(internetAddress.getPersonal() != null ? MimeUtility.decodeText(internetAddress.getPersonal()) : "");
                emailPerson.setAddress(internetAddress.getAddress());
                emailPerson.setType(internetAddress.getType());
                result.add(emailPerson);
            }
        }
        return result;
    }

    public static String getSentDate(MimeMessage msg, String pattern) throws MessagingException {
        Date receivedDate = msg.getSentDate();
        if (receivedDate == null)
            return "";

        if (pattern == null || "".equals(pattern))
            pattern = "yyyy-MM-dd E HH:mm ";

        return new SimpleDateFormat(pattern).format(receivedDate);
    }

    public static boolean isContainAttachment(Part part) throws MessagingException, IOException {
        boolean flag = false;
        if (part.isMimeType("multipart/*")) {
            MimeMultipart multipart = (MimeMultipart) part.getContent();
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                String disp = bodyPart.getDisposition();
                if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE))) {
                    flag = true;
                } else if (bodyPart.isMimeType("multipart/*")) {
                    flag = isContainAttachment(bodyPart);
                } else {
                    String contentType = bodyPart.getContentType();
                    if (contentType.indexOf("application") != -1) {
                        flag = true;
                    }

                    if (contentType.indexOf("name") != -1) {
                        flag = true;
                    }
                }

                if (flag) break;
            }
        } else if (part.isMimeType("message/rfc822")) {
            flag = isContainAttachment((Part)part.getContent());
        }
        return flag;
    }

    public static boolean isSeen(MimeMessage msg) throws MessagingException {
        return msg.getFlags().contains(Flags.Flag.SEEN);
    }


    public static boolean isReplySign(MimeMessage msg) throws MessagingException {
        boolean replySign = false;
        String[] headers = msg.getHeader("Disposition-Notification-To");
        if (headers != null)
            replySign = true;
        return replySign;
    }


    public static EmailPriority getPriority(MimeMessage msg) throws MessagingException {
        EmailPriority priority = EmailPriority.Normal;
        String[] headers = msg.getHeader("X-Priority");
        if (headers != null) {
            String headerPriority = headers[0];
            if (headerPriority.contains("1") || headerPriority.contains("High"))
                priority = EmailPriority.High;
            else if (headerPriority.contains("5") || headerPriority.contains("Low"))
                priority = EmailPriority.Low;
        }
        return priority;
    }


    public static void getMailTextContent(Part part, StringBuffer content) throws MessagingException, IOException {
        boolean isContainTextAttach = part.getContentType().indexOf("name") > 0;
        if (part.isMimeType("text/*") && !isContainTextAttach) {
            content.append(part.getContent().toString());
        } else if (part.isMimeType("message/rfc822")) {
            getMailTextContent((Part)part.getContent(),content);
        } else if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                getMailTextContent(bodyPart,content);
            }
        }
    }


    public static void saveAttachment(Part part, String destDir) throws UnsupportedEncodingException, MessagingException,
            FileNotFoundException, IOException {
        if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                String disp = bodyPart.getDisposition();
                if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE))) {
                    InputStream is = bodyPart.getInputStream();
                    saveFile(is, destDir, decodeText(bodyPart.getFileName()));
                } else if (bodyPart.isMimeType("multipart/*")) {
                    saveAttachment(bodyPart,destDir);
                } else {
                    String contentType = bodyPart.getContentType();
                    if (contentType.indexOf("name") != -1 || contentType.indexOf("application") != -1) {
                        saveFile(bodyPart.getInputStream(), destDir, decodeText(bodyPart.getFileName()));
                    }
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            saveAttachment((Part) part.getContent(),destDir);
        }
    }


    private static void saveFile(InputStream is, String destDir, String fileName)
            throws FileNotFoundException, IOException {
        BufferedInputStream bis = new BufferedInputStream(is);
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(new File(destDir + fileName)));
        int len = -1;
        while ((len = bis.read()) != -1) {
            bos.write(len);
            bos.flush();
        }
        bos.close();
        bis.close();
    }


    public static String decodeText(String encodeText) throws UnsupportedEncodingException {
        if (encodeText == null || "".equals(encodeText)) {
            return "";
        } else {
            return MimeUtility.decodeText(encodeText);
        }
    }
}
