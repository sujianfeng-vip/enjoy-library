package vip.sujianfeng.email.model;

import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.List;

/**
 * 一封邮件
 */
public class EmailItem {

    /**
     * 邮件数据
     */
    private MimeMessage mimeMessage;

    /**
     * 主题
     */
    private String subject;
    /**
     * 邮件内容
     */
    private StringBuffer content;

    /**
     * 发件人
     */
    private EmailPerson sender;
    /**
     * 收件人
     */
    private List<EmailPerson> to;
    /**
     * 抄送
     */
    private List<EmailPerson> cc;
    /**
     * 密送
     */
    private List<EmailPerson> bcc;

    /**
     * 发送时间
     */
    private Date sendDate;
    /**
     * 优先级
     */
    private EmailPriority emailPriority;
    /**
     * 是否需要回执
     */
    private boolean replySign;
    /**
     * 邮件大小
     */
    private int size;
    /**
     * 是否包含附件
     */
    private boolean containAttachment;
    /**
     * 序号
     */
    private int messageNumber;
    /**
     * 消息是已读
     */
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
