package vip.sujianfeng.email.model;

import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.List;

public class EmailItem {


    private MimeMessage mimeMessage;

    private String subject;

    private StringBuffer content;

    private EmailPerson sender;

    private List<EmailPerson> to;

    private List<EmailPerson> cc;

    private List<EmailPerson> bcc;

    private Date sendDate;

    private EmailPriority emailPriority;

    private boolean replySign;

    private int size;

    private boolean containAttachment;

    private int messageNumber;

    private boolean seen;

    public MimeMessage getMimeMessage() {
        return mimeMessage;
    }

    public void setMimeMessage(MimeMessage mimeMessage) {
        this.mimeMessage = mimeMessage;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public StringBuffer getContent() {
        return content;
    }

    public void setContent(StringBuffer content) {
        this.content = content;
    }

    public EmailPerson getSender() {
        return sender;
    }

    public void setSender(EmailPerson sender) {
        this.sender = sender;
    }

    public List<EmailPerson> getTo() {
        return to;
    }

    public void setTo(List<EmailPerson> to) {
        this.to = to;
    }

    public List<EmailPerson> getCc() {
        return cc;
    }

    public void setCc(List<EmailPerson> cc) {
        this.cc = cc;
    }

    public List<EmailPerson> getBcc() {
        return bcc;
    }

    public void setBcc(List<EmailPerson> bcc) {
        this.bcc = bcc;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public EmailPriority getEmailPriority() {
        return emailPriority;
    }

    public void setEmailPriority(EmailPriority emailPriority) {
        this.emailPriority = emailPriority;
    }

    public boolean isReplySign() {
        return replySign;
    }

    public void setReplySign(boolean replySign) {
        this.replySign = replySign;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isContainAttachment() {
        return containAttachment;
    }

    public void setContainAttachment(boolean containAttachment) {
        this.containAttachment = containAttachment;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public void setMessageNumber(int messageNumber) {
        this.messageNumber = messageNumber;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
}
