package com.huntkey.rx.sceo.common.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lulx on 2017/12/25 0025 下午 14:41
 */
public class EmailVo {

    /**
     * 接收者
     * lulx@huntkey.net
     */
    private List<String> mailRecipient;

    /**
     * 抄送
     * lulx@huntkey.net
     */
    private List<String> mailCopyRecipient;

    /**
     * 发送主题
     */
    private String mailSubject;

    /**
     * 发送内容
     */
    private String mailContent;

    public EmailVo() {
        mailRecipient = new ArrayList<String>();
        mailCopyRecipient = new ArrayList<String>();
    }

    @Override
    public String toString() {
        return "EmailVo{" +
                "mailRecipient=" + mailRecipient +
                ", mailCopyRecipient=" + mailCopyRecipient +
                ", mailSubject='" + mailSubject + '\'' +
                ", mailContent='" + mailContent + '\'' +
                '}';
    }

    public List<String> getMailRecipient() {
        return mailRecipient;
    }

    public boolean addMailRecipient(String recipient) {
        return mailRecipient.add(recipient);
    }

    public void setMailRecipient(List<String> mailRecipient) {
        this.mailRecipient = mailRecipient;
    }

    public List<String> getMailCopyRecipient() {
        return mailCopyRecipient;
    }

    public void setMailCopyRecipient(List<String> mailCopyRecipient) {
        this.mailCopyRecipient = mailCopyRecipient;
    }

    public boolean addMailCopyRecipient(String copyRecipient) {
        return mailCopyRecipient.add(copyRecipient);
    }

    public String getMailSubject() {
        return mailSubject;
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    public String getMailContent() {
        return mailContent;
    }

    public void setMailContent(String mailContent) {
        this.mailContent = mailContent;
    }
}
